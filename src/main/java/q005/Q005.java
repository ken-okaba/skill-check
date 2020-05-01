package q005;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Q005 データクラスと様々な集計
 *
 * 以下のファイルを読み込んで、WorkDataクラスのインスタンスを作成してください。
 * resources/q005/data.txt
 * (先頭行はタイトルなので読み取りをスキップする)
 *
 * 読み込んだデータを以下で集計して出力してください。
 * (1) 役職別の合計作業時間
 * (2) Pコード別の合計作業時間
 * (3) 社員番号別の合計作業時間
 * 上記項目内での出力順は問いません。
 *
 * 作業時間は "xx時間xx分" の形式にしてください。
 * また、WorkDataクラスは自由に修正してください。
 *
[出力イメージ]
部長: xx時間xx分
課長: xx時間xx分
一般: xx時間xx分
Z-7-31100: xx時間xx分
I-7-31100: xx時間xx分
T-7-30002: xx時間xx分
（省略）
194033: xx時間xx分
195052: xx時間xx分
195066: xx時間xx分
（省略）
 */
public class Q005 {
    public static void main(String[] args) {
        List<WorkData> list = toWorkDataList();
        print(list, WorkData::getPosition);
        print(list, WorkData::getpCode);
        print(list, WorkData::getNumber);
    }

    private static List<WorkData> toWorkDataList() {
        return new BufferedReader(new InputStreamReader(Q005.class.getResourceAsStream("data.txt")))
                .lines().skip(1).map(line -> line.split(","))
                .map(columns -> new WorkData(columns[0], columns[1], columns[2], columns[3],
                        Integer.parseInt(columns[4])))
                .collect(Collectors.toList());
    }

    private static void print(List<WorkData> list, Function<WorkData, String> func) {
        list.stream().collect(Collectors.groupingBy(func, Collectors.summingInt(WorkData::getWorkTime))).entrySet()
                .stream().map(entry -> String.format("%s: %d時間%02d分", entry.getKey(), entry.getValue() / 60,
                        entry.getValue() % 60))
                .forEach(System.out::println);
    }
}
// 完成までの時間: 00時間 17分