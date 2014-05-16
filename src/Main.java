import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by ling on 5/10/14.
 */
public class Main {
    public static void main(String[] args) throws IOException, org.json.simple.parser.ParseException {
        String inputDataFile = "data/filtered_egypt_government_results.json";
        Path inputDataPath = Paths.get(inputDataFile);
        TwitterDataset analyzedData = new TwitterDataset(inputDataPath);
        analyzedData.writeoutData();
    }
}
