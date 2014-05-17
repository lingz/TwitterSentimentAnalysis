import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by ling on 5/10/14.
 */
public class Main {
    public static boolean verbose = false;
    public static boolean timed = false;

    public static void main(String[] args) throws IOException, org.json.simple.parser.ParseException {

        ArrayList<Path> dataFiles = new ArrayList<Path>();
        for (String arg : args) {
            if (arg.equals("-v")) {
                verbose = true;
            } else if (arg.equals("-t")){
                timed = true;
            } else {
                Path inputDataPath = Paths.get(arg);
                dataFiles.add(inputDataPath);
                if (verbose)
                    System.out.println(inputDataPath.toString() + " added to analysis path");
            }
        }

        for (Path dataFile : dataFiles) {
            System.out.println("Starting thread");
            (new ProcessDataSet(dataFile)).start();
        }

    }
}
