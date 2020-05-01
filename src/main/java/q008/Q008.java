package q008;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Q008 埋め込み文字列の抽出
 *
 * 一般に、定数定義の場合を除いて、プログラム中に埋め込み文字列を記述するのは良くないとされています。
 * そのような埋め込み文字列を見つけるために、埋め込み文字列や埋め込み文字（"test"や'a'など）が
 * 記述された行を出力するプログラムを作成してください。
 *
 * - 実行ディレクトリ配下（再帰的にチェック）に存在するすべてのjavaファイルをチェックする
 * - ファイル名はディレクトリ付きでも無しでも良い
 * - 行の内容を出力する際は、先頭のインデントは削除しても残しても良い

[出力イメージ]
Q001.java(12): return "test";  // テストデータ
Q002.java(10): private static final DATA = "test"
Q002.java(11): + "aaa";
Q002.java(20): if (test == 'x') {
Q004.java(13): Pattern pattern = Pattern.compile("(\".*\")|(\'.*\')");

 */
public class Q008 {

    private static final Pattern p = Pattern.compile("\".*\"|'.+'");

    /**
     * JavaファイルのStreamを作成する
     *
     * @return
     * @throws UncheckedIOException
     */
    private static Stream<Path> listJavaFiles() {
        try {
            return Files.walk(Paths.get(".")).filter(f -> f.toString().endsWith(".java"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void main(String[] args) {
        listJavaFiles().forEach(Q008::print);
    }

    private static void print(Path path) {
        try {
            int[] num = { 0 };
            Files.lines(path).peek(line -> num[0]++).filter(line -> p.matcher(line).find())
                    .map(line -> String.format("%s(%d): %s", path.getFileName(), num[0], line.trim()))
                    .forEach(System.out::println);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
// 完成までの時間: 00時間 18分
