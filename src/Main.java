import twitter4j.Status;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ling on 5/10/14.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        String authKeys = Paths.get("twitterkeys.txt").toAbsolutePath().toString();
        TwitterClient twitterClient = new TwitterClient(authKeys);
        ArrayList<String> dates = new ArrayList<String>();
        dates.add("04");
        dates.add("05");
        dates.add("06");
        dates.add("07");
        dates.add("08");
        dates.add("09");
        dates.add("10");
        dates.add("11");

        for (int i = 0; i < 7; i++) {
            String startDate = dates.get(i);
            String endDate = dates.get(i + 1);
            System.out.println("Working on date: " + startDate);
            List<Status> results = twitterClient.search("obama", startDate, endDate, "05", "2014");
            int positiveTweets = 0;
            int negativeTweets = 0;
            for (Status status : results) {
//            System.out.println(status.getText());
                String tweetText = SentimentAnalysis.cleanText(status.getText());
                int sentiment = SentimentAnalysis.findSentiment(tweetText);
                if (sentiment > 0) {
                    positiveTweets++;
//                System.out.println("Positive Tweet");
                }
                else if (sentiment < 0) {
                    negativeTweets++;
//                System.out.println("Negative Tweet");
                }
            }
            float proportionPositive = (float) positiveTweets / (positiveTweets + negativeTweets);
            System.out.println(proportionPositive);
        }


        String input;

//        while (scanner.hasNext()) {
//            input = SentimentAnalysis.cleanText(scanner.nextLine());

//            SentimentAnalysis.findSentiment(scanner.nextLine());
//        }
//        System.out.println("Done!");
    }
}
