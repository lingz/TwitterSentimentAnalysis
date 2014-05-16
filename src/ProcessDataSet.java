import java.nio.file.Path;

/**
 * Created by ling on 5/17/14.
 */
public class ProcessDataSet extends Thread {
    Path dataSetPath;
    public ProcessDataSet(Path dataSetPath) {
        this.dataSetPath = dataSetPath;
    }

    public void run() {
        try {
            long start = System.nanoTime();
            TwitterDataset analyzedData = new TwitterDataset(dataSetPath);
            analyzedData.writeoutData();
            long time = System.nanoTime() - start;
            if (Main.timed)
                System.out.println("Processing of " + dataSetPath.getFileName() + " finished in " + time / 1000000000 +
                        " seconds");
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
