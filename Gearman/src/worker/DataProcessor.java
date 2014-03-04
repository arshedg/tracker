

package worker;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.gearman.Gearman;
import org.gearman.GearmanFunction;
import org.gearman.GearmanFunctionCallback;
import org.gearman.GearmanWorker;

/**
 *
 * @author marshed
 */
public class DataProcessor implements GearmanFunction {

    private String article;
    private String articleId;
    List<String> result = new ArrayList<>();

    public List<String> doJob() throws Exception {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        String text = article;
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                String word = token.get(TextAnnotation.class);
                String ne = token.get(NamedEntityTagAnnotation.class);    
                if (ne != null && "ORGANIZATION".equals(ne)) {
                    result.add("\"" + word + "\"");
                }
            }
        }
        return result;
    }
    public static void main(String arg[]){
         Gearman gearman = Gearman.createGearman();
                
                /** Create a gearman worker */
                GearmanWorker worker = gearman.createGearmanWorker();
                
                /** Tell the worker how to perform the echo function */
                worker.addFunction("NLP", new DataProcessor());
                
                /** Add a server to communicate with to the worker */
                worker.addServer(gearman.createGearmanServer("localhost", 4567));
    }
    

    @Override
    public byte[] work(String function, byte[] data, GearmanFunctionCallback callback) throws Exception {
            String request = new String((byte[]) data);
            int border = request.indexOf(":");//first index of :
            this.article = request.substring(border + 1);
            this.articleId = request.substring(0, border);
            long startTime = System.currentTimeMillis();
            List<String> nerResult = this.doJob();
            long finishTime = System.currentTimeMillis();
            long timeTaken = finishTime - startTime;
            String logText = "Article ID:" + articleId + ",time taken:" + timeTaken;
            int nerCount = nerResult.size();
            String ners = nerResult.toString();
            String resultString =logText+ "  Number of NERs = " + nerCount + " NER :" + ners;
            System.out.println(resultString);
            return resultString.getBytes("UTF-8");
    }
}
