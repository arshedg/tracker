/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

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

/**
 *
 * @author marshed
 */
public class DataProcessor {
    private final String data;
    List<String> result = new ArrayList<>();
    public DataProcessor(String text){
        this.data = text;
    }
    
    public List<String> doJob() throws Exception{
       
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
     
         String text =data;//"Arizona got snubbed by Apple Inc. (AAPL) in 2012 when the iPhone maker picked Texas to build a new operations hub. Scott Smith, the mayor of the Phoenix suburb of Mesa, was determined to keep history from repeating itself.  So last year, when Apple was searching for a place to house a factory that makes a stronger glass for its gadgets, Mesa pulled out the stops. The city, which was ravaged by the 2007 housing crash, offered tax breaks, built power lines, fast-tracked building permits and got the state to declare a vacant 1.3 million-square-foot facility that Apple was exploring a foreign trade zone. With unemployment high, such are the lengths that towns are willing to go to to lure the worlds most valuable company.  Any time you have a company like Apple come in and invest in your area, especially with this type of operation, its significant, said Smith, who triumphed late last year when Apple spent $114 million to buy the factory. The mayor celebrated by placing bowls of green and red apples in City Hall.  Yet even as Apple prepares to open the facility with 700 full-time jobs this month, along with another 1,300 temporary construction jobs, its arrival is no panacea for the region around Phoenix that refers to itself as Silicon Desert. According to Smith, the area lost about 300,000 jobs after the 2007 housing market crash, with only about half of those coming back. Photographer: Susana Gonzalez/BloombergOnce we reached the short list, the Apple people showed up and started talking, said... Read MoreOnce we reached the short list, the Apple people showed up and started talking, said Scott Smith, mayor of Mesa, Arizona. They had narrowed it down to one, two or three facilities.  CloseCloseOpenPhotographer: Susana Gonzalez/BloombergOnce we reached the short list, the Apple people showed up and started talking, said Scott Smith, mayor of Mesa, Arizona. They had narrowed it down to one, two or three facilities.  Jabil Circuit Inc. (JBL), an electronics company, is closing a factory in nearby Tempe, eliminating about 1,400 jobs. Motorola Inc., once Mesas largest employer, began shutting its plants starting more than a decade ago. The facility acquired by Apple was slated to employ 600 people -- and 4,000 more if successful -- before the former owner, solar-panel maker First Solar Inc. (FSLR), pulled out in 2012.  Case Study  The recovery is happening slowly, Smith, a Republican who is running for Arizona governor, said in an interview.  Mesas experience serves as a case study for what may unfold in towns nationally as President Barack Obama champions manufacturing as an economic growth engine. While the U.S. economy has added 500,000 new manufacturing jobs in the past four years, it lost 5.5 million such jobs from 1998 to 2013, according to the Labor Department.  Related:Boomers Turn On, Tune In, Drop Out of U.S. Labor ForceObama Recovery Fails to Resonate as Americans Left BehindApples Mesa facility is an example of the kind of manufacturing where the U.S. can be competitive. The factory, which Apple is operating with GT Advanced Technologies Inc. (GTAT), will focus on a material called synthetic sapphire, which is used to make watches and solar cells and strengthen iPhone screens.  Making Sapphire  The material requires furnaces to spark a reaction in which cylinders of sapphire grow over about a month, then can be sliced to less than a millimeter thick for use on gadget screens, said Eric Virey, an analyst with Yole Developpement, a research firm that studies the market.  Apples Mesa plant will make an unprecedented amount of synthetic sapphire, Virey said.  When its operating at full capacity, this plant is going to be producing as much as two times the current worldwide capacity, he said. The factory will be able to make enough sapphire for 80 million to 100 million iPhones a year, he said.  The process is highly technical and requires a small and well-trained workforce, Virey said. That makes Apples new facility unlike those run overseas by manufacturer Foxconn Technology Group (2354), where thousands of workers put iPhones and iPads together by hand on an assembly line. U.S. factories make sense economically when they have expensive and complex equipment that require skilled workers to operate, said Jim McGregor, a technology industry analyst who lives in Mesa.  Very Skilled  Everybody wants to bring industry and growth to their region -- its extremely competitive, McGregor said in an interview. Were not talking about low-skill labor.  Apple itself has said it plans to expand the companys U.S. manufacturing, after facing criticism over how much work it outsources to China. While most of its manufacturing continues to be done in that country, Apple is doing more work in the U.S. The new Mac Pro desktop computer, which debuted in December, is being manufactured in Austin, Texas. Apple Chief Executive Officer Tim Cook has said the company has an obligation to create U.S. jobs.  Kristin Huguet, a spokeswoman for Apple, referred to a statement the Cupertino, California-based company made at the time it announced the new factory.  We are proud to expand our domestic manufacturing initiative with a new facility in Arizona, creating more than 2,000 jobs in engineering, manufacturing and construction, Apple said.  Jeff Nestel-Patt, a spokesman for GT Advanced, and Steve Krum, a spokesman for First Solar, declined to comment.  Mesas Fall  Mesa is a sprawling 133-square-mile suburb of about 450,000 people located 15 miles east of Phoenix. This month, thousands of tourists will visit when it opens a new baseball stadium for Chicago Cubs spring training. In addition to tourism, the economy has been tied to new-home construction and was hit hard when the real-estate market crashed in 2007.  We went from the top of the heap in job creation to the bottom in a matter of months, said Karrin Taylor, executive vice president of DMB Associates Inc., a property owner developing the area around Apples new plant. Our housing market was leading our growth in the last up cycle and when that fell, everything fell.  Project Cascade  When Apple began exploring the vacant factory in Mesa in September, state and local officials had no idea it was the iPhone maker since the inquiry came from an anonymous electronics company, Smith said. The company peppered the citys engineering and public works departments with questions: What are the power supply details? Does the desert community have enough water? Whats the capacity of the sewer system?  There was all this speculation and we didnt know who it was, the mayor said. Working in Mesas favor was that the facility had been built in the past few years and could be up and running within months, he said.  Apple later revealed itself -- and then had Mesa officials sign non-disclosure agreements to keep the talks secret. The code name for the operation was Project Cascade, according to government records.  Once we reached the short list, the Apple people showed up and started talking, Smith said. They had narrowed it down to one, two or three facilities.  Renewable Energy  Then it was up to Mesa to land Apple, versus other towns. Time was of the essence since Arizona had lost out on the previous Apple facility to Texas less than two years earlier and was nervous others might trump its bid. Officials typically had just a few days to respond to Apples questions, Smith said.  One sticking point: power. Apple wanted the facility to use 100 percent renewable energy and negotiated with the state and local power company, Salt River Project, about how to make that happen. New solar and geothermal projects are being built because of the project. Apple also got officials to agree to construct a new power substation for the plant.  Its not like getting an extension cord and plugging it in, Smith said. These deals live and die many times before they come together.  Construction permits were also issued in 30 days, with some coming in less than 24 hours, said Barry Broome, CEO of the Greater Phoenix Economic Council, which was involved in the deal. That compares with months in other areas, he said.  Some Sweeteners  Arizona gave Apple additional sweeteners, a common yet controversial practice by state and local governments trying to lure new employers. An Arizona state public-private group called Arizona Commerce Authority gave Apple a $10 million grant for building improvements and to help with job recruitment. Apple also won the ability from the state to designate the area around the factory a foreign trade zone, which lets it cut property taxes by more than 70 percent.  Apple and other companies typically get tax breaks in states where they add operations. Apple had received more than $135 million in incentives from state and local governments to build data centers in Nevada, Oregon and North Carolina, according to announcements of those projects. Texas gave the iPhone maker about $30 million in 2012 for the operations center its building there.  Tax breaks are common because the prospect of new high-technology jobs creates an auction among states and local governments to outbid each other with incentive packages, said Laura Reese, director of global urban studies at Michigan State University in East Lansing, Michigan.  What youre probably going to do is overbid or overreward incentives because you know youre competing against other governments and you dont know what theyre offering, she said.  Sealed Deal  Sandra Watson, who runs the Arizona Commerce Authority, said the incentives are tied to Apple creating jobs. While the state is home to operations of other technology companies, such as EBay Inc. (EBAY) and Yelp Inc. (YELP), its still trying to crawl out from the effects of the housing crisis. Arizonas unemployment rate of 7.6 percent in December ranked as the ninth highest in the U.S. and compared with 6.7 percent nationally, according to the U.S. Labor Department.  By November, Mesa had sealed the deal with Apple. On Nov. 4, GT made its sapphire partnership with Apple public. Arizona Governor Jan Brewer also weighed in.  Apple is indisputably one of the worlds most innovative companies and Im thrilled to welcome them to Arizona, Brewer said in a statement.  The Scene  Apples factory has since spurred inquiries from other companies interested in becoming a neighbor of the iPhone maker in a planned technology corridor at the citys outer edge, Smith said.  The facility, the size of about 10 football fields, sits on a patch of land near where General Motors Co. once tested new cars. Surrounding plots have already been mapped out for potential new arrivals. On a sunny afternoon last month, crews from Salt River Project were busy installing new transmission lines to feed electricity to the plant.  Residents like Barbara Young, a 54-year-old grandmother of three, are looking forward to the factorys opening. After more than a decade of working at Jabils nearby plant -- most recently as a manager -- she will lose her job there in August when the facility closes. She has applied for a job at Apples factory and while she has other potential opportunities, shes hoping to land with the iPhone maker.  I just hope to be able to find a job that I love as much and Im not just a number, Young said. It would be a dream to get a job there.  To contact the reporter on this story: Adam Satariano in Mesa, Arizona at asatariano1@bloomberg.net  To contact the editor responsible for this story: Pui-Wing Tam at ptam13@bloomberg.net ";
     
            // create an empty Annotation just with the given text
            Annotation document = new Annotation(text);

            // run all Annotators on this text
            pipeline.annotate(document);

            // these are all the sentences in this document
            // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
            List<CoreMap> sentences = document.get(SentencesAnnotation.class);

            for(CoreMap sentence: sentences) {
              // traversing the words in the current sentence
              // a CoreLabel is a CoreMap with additional token-specific methods
              for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(TextAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(NamedEntityTagAnnotation.class);    //NER Output in a For Loop    
                if(ne!=null&&"ORGANIZATION".equals(ne)){
                    result.add("\""+word+"\"");
                }

              }
        }
        return result;    

}
    
    public static void main(String arg[]) throws Exception{
        new DataProcessor("").doJob();
    }
}
