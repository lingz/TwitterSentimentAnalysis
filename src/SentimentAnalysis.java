import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.logging.RedwoodConfiguration;

import java.util.List;
import java.util.Properties;


/**
 * Created by ling on 5/10/14.
 */


public class SentimentAnalysis {
    private static StanfordCoreNLP pipeline;

    public SentimentAnalysis() {
        if (!Main.verbose)
            RedwoodConfiguration.empty().capture(System.err).apply();
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
        pipeline = new StanfordCoreNLP(props);
        if (!Main.verbose)
            RedwoodConfiguration.current().clear().apply();
    }

    public int findSentiment(String rawText) {
        String text = cleanText(rawText);
        int overallSentiment = 0;


        if (text == null || text.length() == 0)
            return 0;

        Annotation annotation = pipeline.process(text);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {

            Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);

            int sentiment = RNNCoreAnnotations.getPredictedClass(tree) - 2;

            overallSentiment += sentiment;

        }


        return overallSentiment;

    }

    private static String cleanText(String text) {
        String output = "";

        for (int i = 0; i < text.length(); i++) {
            Character c = text.charAt(i);
            if (Character.isLetterOrDigit(c) || Character.isWhitespace(c) ||
                c == '"' || c == ',' || c == '\'') {
                output += c;
            }

        }
        return output;


    }
}
