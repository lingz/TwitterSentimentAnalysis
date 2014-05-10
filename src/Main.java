import java.util.Scanner;

/**
 * Created by ling on 5/10/14.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            SentimentAnalysis.findSentiment(scanner.nextLine());
        }
        System.out.println("Done!");
    }
}
