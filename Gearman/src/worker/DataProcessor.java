

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
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gearman.client.GearmanJobResult;
import org.gearman.client.GearmanJobResultImpl;
import org.gearman.worker.AbstractGearmanFunction;

/**
 *
 * @author marshed
 */
public class DataProcessor extends AbstractGearmanFunction {

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

    @Override
    public GearmanJobResult executeFunction() {
        try {
            String request = new String((byte[]) this.data);
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
            String resultString = "  Number of NERs = " + nerCount + " NER :" + ners;

            GearmanJobResult gjr = new GearmanJobResultImpl(this.jobHandle,
                    true, resultString.getBytes(),
                    new byte[0], new byte[0], 0, 0);
            return gjr;
        } catch (Exception ex) {
            Logger.getLogger(DataProcessor.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
