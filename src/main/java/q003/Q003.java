package q003;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Q003 集計と並べ替え
 *
 * 以下のデータファイルを読み込んで、出現する単語ごとに数をカウントし、アルファベット辞書順に並び変えて出力してください。
 * resources/q003/data.txt
 * 単語の条件は以下となります
 * - "I"以外は全て小文字で扱う（"My"と"my"は同じく"my"として扱う）
 * - 単数形と複数形のように少しでも文字列が異れば別単語として扱う（"dream"と"dreams"は別単語）
 * - アポストロフィーやハイフン付の単語は1単語として扱う（"isn't"や"dead-end"）
 *
 * 出力形式:単語=数
 *
[出力イメージ]
（省略）
highest=1
I=3
if=2
ignorance=1
（省略）

 * 参考
 * http://eikaiwa.dmm.com/blog/4690/
 */
public class Q003 {
    private static final Pattern p1 = Pattern.compile(" ");
    private static final Pattern p2 = Pattern.compile("[^A-Za-z'-]");
    private static final Pattern p3 = Pattern.compile("[A-Za-z]");

    /**
     * データファイルを開く
     * resources/q003/data.txt
     */
    private static InputStream openDataFile() {
        return Q003.class.getResourceAsStream("data.txt");
    }

    public static void main(String[] args) throws IOException {
        new BufferedReader(new InputStreamReader(openDataFile())).lines().flatMap(p1::splitAsStream)
                .map(str -> p2.matcher(str).replaceAll(""))
                .filter(str -> p3.matcher(str).find())
                .map(str -> str.equals("I") ? str : str.toLowerCase())
                .collect(Collectors.groupingBy(UnaryOperator.identity(), Collectors.counting())).entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(entry -> entry.getKey() + "=" + entry.getValue()).forEach(System.out::println);
    }
}
// 完成までの時間: 00時間 23分