package q009;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Q009 重い処理を別スレッドで実行
 *
 * 標準入力から整数を受け取り、別スレッドで素因数分解して、その整数を構成する2以上の素数を求めるプログラムを記述してください。
 * - 素因数分解は重い処理であるため、別スレッドで実行してください
 * - 標準入力から整数を受け取った後は、再度標準入力に戻ります
 * - 空文字が入力された場合は、素因数分解を実行中の状態を表示します（「実行中」あるいは処理結果を表示）
 * - 素因数分解の効率的なアルゴリズムを求めるのが問題ではないため、あえて単純なアルゴリズムで良い（遅いほどよい）
 *   （例えば、2および3以上の奇数で割り切れるかを全部チェックする方法でも良い）
 * - BigIntegerなどを使って、大きな数値も扱えるようにしてください
[実行イメージ]
入力) 65536
入力)
65536: 実行中  <-- もし65536の素因数分解が実行中だった場合はこのように表示する
入力) 12345
入力)
65536: 2      <-- 実行が終わっていたら結果を表示する。2の16乗だが、16乗の部分の表示は不要（複数溜まっていた場合の順番は問わない）
12345: 実行中
入力)
12345: 3,5,823
 */
public class Q009 {

    private static final BigInteger TWO = new BigInteger("2");

    public static void main(String[] args) {

        Map<CompletableFuture<String>, BigInteger> map = new LinkedHashMap<>();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("入力) ");
                execute(map, scanner.nextLine());
            }
        } catch (NumberFormatException e) {
            // 空文字と数値以外の場合は、完了を待機して終了
            map.entrySet().stream().map(entry -> entry.getValue() + ": " + entry.getKey().join())
                    .forEach(System.out::println);
        }
    }

    private static void execute(Map<CompletableFuture<String>, BigInteger> map, String input) {

        if (!input.isEmpty()) {
            BigInteger number = new BigInteger(input);
            map.put(CompletableFuture.supplyAsync(() -> factorizePrime(number)), number);
            return;
        }

        Iterator<Map.Entry<CompletableFuture<String>, BigInteger>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<CompletableFuture<String>, BigInteger> entry = it.next();
            if (entry.getKey().isDone()) {
                System.out.println(entry.getValue() + ": " + entry.getKey().join());
                it.remove();
            } else {
                System.out.println(entry.getValue() + ": 実行中");
            }
        }
    }

    private static String factorizePrime(BigInteger number) {
        return factorizePrime2(number).stream().distinct().map(BigInteger::toString).collect(Collectors.joining(","));
    }

    private static List<BigInteger> factorizePrime2(final BigInteger number) {

        BigInteger target = number;
        List<BigInteger> list = new LinkedList<>();

        if (target.compareTo(TWO) <= 0) {
            list.add(target);
            return list;
        }

        BigInteger max = number.sqrt().add(BigInteger.ONE);
        for (BigInteger bi = TWO; bi.compareTo(max) <= 0; bi = bi.add(BigInteger.ONE)) {
            if (!bi.isProbablePrime(100)) {
                continue;
            }
            while (target.remainder(bi).equals(BigInteger.ZERO)) {
                list.add(bi);
                target = target.divide(bi);
            }
            if (target.equals(BigInteger.ONE)) {
                return list;
            }
        }
        list.add(target);
        return list;
    }
}
// 完成までの時間: 01時間 00分