package q007;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * q007 最短経路探索
 *
 * 壁を 'X' 通路を ' ' 開始を 'S' ゴールを 'E' で表現された迷路で、最短経路を通った場合に
 * 何歩でゴールまでたどり着くかを出力するプログラムを実装してください。
 * もし、ゴールまで辿り着くルートが無かった場合は -1 を出力してください。
 * なお、1歩は上下左右のいずれかにしか動くことはできません（斜めはNG）。
 *
 * 迷路データは MazeInputStream から取得してください。
 * 迷路の横幅と高さは毎回異なりますが、必ず長方形（あるいは正方形）となっており、外壁は全て'X'で埋まっています。
 * 空行が迷路データの終了です。
 *

[迷路の例]
XXXXXXXXX
XSX    EX
X XXX X X
X   X X X
X X XXX X
X X     X
XXXXXXXXX

[答え]
14
 */
public class Q007 {
    public static void main(String[] args) {

        MazeInputStream mis = new MazeInputStream();

        System.err.println(
                new BufferedReader(new InputStreamReader(mis)).lines()
                        .collect(Collectors.joining(System.lineSeparator())));

        mis.reset();

        String[][] ss = new BufferedReader(new InputStreamReader(mis)).lines()
                .map(line -> Stream.of(line.split("")).toArray(String[]::new)).toArray(String[][]::new);

        Coordinate start = xxxxxx(ss);

        List<List<Coordinate>> list = new ArrayList<>();
        List<Coordinate> listX = new ArrayList<>();
        listX.add(start);
        while (true) {
            listX = aaa(ss, list, listX);
            list.add(listX);
            System.err.println("list: " + list);
        }
    }

    private static Coordinate xxxxxx(String[][] ss) {
        Coordinate start = null;
        for (int y = 0; y < ss.length; y++) {
            String[] strings = ss[y];
            for (int x = 0; x < strings.length; x++) {
                if (strings[x].equals("S")) {
                    start = new Coordinate(x, y);
                }
            }
        }
        return start;
    }

    private static List<Coordinate> aaa(String[][] ss, List<List<Coordinate>> list, List<Coordinate> list0) {
        List<Coordinate> list1 = new ArrayList<>();
        Optional<Coordinate> xy = extracted(ss, list, list0, list1);
        xy.ifPresent(val -> {
            System.out.println(list.size() + 1);
            System.exit(0);
        });
        if (list1.isEmpty()) {
            System.out.println("-1");
            System.exit(-1);
        }
        return list1;
    }

    private static Optional<Coordinate> extracted(String[][] ss, List<List<Coordinate>> list, List<Coordinate> list0,
            List<Coordinate> list1) {
        Optional<Coordinate> xy = list0.stream()
                .map(coo -> aaa(ss, coo, list1, list))
                .filter(Optional::isPresent)
                .findFirst().flatMap(UnaryOperator.identity());
        return xy;
    }

    private static Optional<Coordinate> aaa(String[][] ss, Coordinate coo, List<Coordinate> list,
            List<List<Coordinate>> list0) {
        Stream<Coordinate> st = Stream.of(new Coordinate(coo.getX(), coo.getY() - 1),
                new Coordinate(coo.getX() + 1, coo.getY()),
                new Coordinate(coo.getX(), coo.getY() + 1), new Coordinate(coo.getX() - 1, coo.getY()));

        return st.filter(xy -> list0.stream().noneMatch(l -> l.contains(xy))).filter(xy -> extracted(ss, xy, list))
                .findFirst();
    }

    private static boolean extracted(String[][] ss, Coordinate coo, List<Coordinate> list) {
        String s = ss[coo.getY()][coo.getX()];
        if (s.equals("E")) {
            return true;
        }

        if (s.equals(" ")) {
            list.add(coo);
        }
        return false;
    }

    public static class Coordinate {
        private final int x;
        private final int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Coordinate) {
                Coordinate other = (Coordinate) obj;
                return other.x == x && other.y == y;
            }
            return false;
        }

        @Override
        public String toString() {
            return "{" + x + "," + y + "}";
        }
    }
}
// 完成までの時間: 02時間 14分