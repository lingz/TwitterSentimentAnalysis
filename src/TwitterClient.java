import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by ling on 5/12/14.
 */
public class TwitterClient {
    public String TWITTER_OAUTH_CONSUMER_KEY;
    public String TWITTER_OAUTH_CONSUMER_SECRET;
    public String TWITTER_OAUTH_ACCESS_TOKEN;
    public String TWITTER_OAUTH_ACCESS_TOKEN_SECRET;


    public TwitterClient(String filepath) throws IOException {
        FileReader input = new FileReader(filepath);
        BufferedReader bufferedReader = new BufferedReader(input);
        String[] keyValues;
        for (int i = 0; i < 4; i++) {
            keyValues = bufferedReader.readLine().split(",");
            switch (i) {
                case 0:
                    TWITTER_OAUTH_CONSUMER_KEY = keyValues[1];
                    break;
                case 1:
                    TWITTER_OAUTH_CONSUMER_SECRET = keyValues[1];
                    break;
                case 2:
                    TWITTER_OAUTH_ACCESS_TOKEN = keyValues[1];
                    break;
                case 3:
                    TWITTER_OAUTH_ACCESS_TOKEN_SECRET = keyValues[1];
                    break;
            }
        }

    }

    public List<Status> search(String keyword, String startDay, String endDay, String month, String year) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        String startDate = year + "-" + month + "-" + startDay;
        String endDate = year + "-" + month + "-" + endDay;
        System.out.println("Going for date");
        cb.setDebugEnabled(true).setOAuthConsumerKey(TWITTER_OAUTH_CONSUMER_KEY)
                .setOAuthConsumerSecret(TWITTER_OAUTH_CONSUMER_SECRET)
                .setOAuthAccessToken(TWITTER_OAUTH_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(TWITTER_OAUTH_ACCESS_TOKEN_SECRET);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Query query = new Query(keyword + " -filter:retweets -filter:links -filter:replies -filter:images");
        query.setCount(10);
        query.setLocale("en");
        query.setLang("en");
        query.setSince(startDate);
        query.setUntil(endDate);
        try {
            System.out.println("Trying query");
            QueryResult queryResult = twitter.search(query);
            return queryResult.getTweets();
        } catch (TwitterException e) {
            // ignore
            e.printStackTrace();
            System.out.println("error");
        }
        System.out.println("No results");
        return Collections.emptyList();
    }

}
