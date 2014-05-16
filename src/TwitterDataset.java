import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ling on 5/17/14.
 */
public class TwitterDataset {
    Map<Integer, ArrayList<Tweet>> tweetMap;
    Map<Integer, Integer> tweetCountMap;
    Map<Integer, Integer> tweetWeightMap;
    // index 0 - negative, 1 - neutral, 2 - positive
    Map<Integer, Integer[]> tweetSentimentMap;
    Map<Integer, Integer[]> tweetSentimentWeightedMap;
    Path originalFilePath;
    SentimentAnalysis SentimentAnalysis = new SentimentAnalysis();

    public TwitterDataset(Path filePath) throws IOException, org.json.simple.parser.ParseException {
        originalFilePath = filePath;
        tweetMap = new HashMap<Integer, ArrayList<Tweet>>();
        tweetCountMap = new HashMap<Integer, Integer>();
        tweetWeightMap = new HashMap<Integer, Integer>();
        tweetSentimentMap = new HashMap<Integer, Integer[]>();
        tweetSentimentWeightedMap = new HashMap<Integer, Integer[]>();

        FileReader fileReader = new FileReader(filePath.toAbsolutePath().toString());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String rawJson = bufferedReader.readLine();
        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(rawJson);
        for (int days_after_event = -30; days_after_event < 31; days_after_event++) {
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();
            JSONArray rawTweets = (JSONArray) data.get(Integer.toString(days_after_event));

            int tweetCount = rawTweets.size();
            tweetCountMap.put(days_after_event, tweetCount);

            for (int i=0; i < tweetCount; i++) {
                JSONObject rawTweet = (JSONObject) rawTweets.get(i);
                String content = (String) rawTweet.get("content");
                int retweets = ((Long) rawTweet.get("retweets")).intValue();

                Tweet tweet = new Tweet(content, retweets);
                tweets.add(tweet);
            }

            tweetMap.put(days_after_event, tweets);
        }
        process_tweet_sentiment();
    }

    private void process_tweet_sentiment() {
        for (int days_after_event = -30; days_after_event < -29; days_after_event++) {
            ArrayList<Tweet> tweets = tweetMap.get(days_after_event);
            Integer[] sentimentCounts = {0, 0, 0};
            Integer[] sentimentWeights = {0, 0, 0};
            for (Tweet tweet : tweets) {
                int sentiment = SentimentAnalysis.findSentiment(tweet.content);
                if (Main.verbose) {
                    System.out.println(tweet.content);
                    System.out.println(sentiment);
                }

                int sentimentArrayIndex;
                if (sentiment < 0) {
                    sentimentArrayIndex = 0;
                } else if (sentiment == 0) {
                    sentimentArrayIndex = 1;
                } else {
                    sentimentArrayIndex = 2;
                }
                sentimentCounts[sentimentArrayIndex] += 1;
                sentimentWeights[sentimentArrayIndex] += tweet.retweets + 1;
            }
            int totalWeight = sentimentWeights[0] + sentimentWeights[1] + sentimentWeights[2];
            tweetWeightMap.put(days_after_event, totalWeight);


            tweetSentimentMap.put(days_after_event, sentimentCounts);
            tweetSentimentWeightedMap.put(days_after_event, sentimentWeights);
        }
    }

    public void writeoutData() throws IOException {
        String newFileName = "processed_" + originalFilePath.getFileName().toString().replace(".json", ".csv");
        Path newFilePath = Paths.get("data/output/" + newFileName);
        writeoutData(newFilePath);
    }

    public void writeoutData(Path filepath) throws IOException {
        FileWriter fileWriter = new FileWriter(filepath.toAbsolutePath().toString());
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write("Days After Event,Number of Tweets,Negative Tweets, Positive Tweets,Negative,Positive Proportion," +
                "Total Weight,Negative Weight,Neutral Weight,Positive Weight,Positive Weight Proportion\n");
        for (int days_after_event = -30; days_after_event < -29; days_after_event++) {
            writer.write(days_after_event + ",");

            int numberOfTweets = tweetCountMap.get(days_after_event);
            writer.write(numberOfTweets + ",");

            Integer[] sentimentCounts = tweetSentimentMap.get(days_after_event);
            for (int i=0; i < 3; i++)
                writer.write(sentimentCounts[i] + ",");

            double positiveProportion = (1.0 * (sentimentCounts[2])) /
                    numberOfTweets;
            writer.write(positiveProportion + ",");

            int totalWeight = tweetWeightMap.get(days_after_event);
            writer.write(totalWeight + ",");

            Integer[] sentimentWeights = tweetSentimentWeightedMap.get(days_after_event);
            for (int i=0; i < 3; i++)
                writer.write(sentimentWeights[i] + ",");

            double positiveWeightProportion = (1.0 * (sentimentWeights[2])) /
                    totalWeight;

            writer.write(positiveWeightProportion + ",");

            writer.write("\n");
        }
        writer.close();

    }





}
