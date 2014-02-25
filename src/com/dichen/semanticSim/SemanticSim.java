package com.dichen.semanticSim;

import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.netlib.util.doubleW;
import org.netlib.util.intW;

import com.dichen.semanticSim.InputParser.TaskType;

import de.tudarmstadt.ukp.dkpro.lexsemresource.Entity;
import de.tudarmstadt.ukp.dkpro.lexsemresource.LexicalSemanticResource;
import de.tudarmstadt.ukp.dkpro.lexsemresource.LexicalSemanticResource.LexicalRelation;
import de.tudarmstadt.ukp.dkpro.lexsemresource.LexicalSemanticResource.SemanticRelation;
import de.tudarmstadt.ukp.dkpro.lexsemresource.core.ResourceFactory;
import de.tudarmstadt.ukp.dkpro.lexsemresource.exception.LexicalSemanticResourceException;
import de.tudarmstadt.ukp.dkpro.lexsemresource.exception.ResourceLoaderException;
import dkpro.similarity.algorithms.*;
import dkpro.similarity.algorithms.api.SimilarityException;
import dkpro.similarity.algorithms.api.TextSimilarityMeasure;
import dkpro.similarity.algorithms.lexical.ngrams.WordNGramJaccardMeasure;
import dkpro.similarity.algorithms.lsr.LexSemRelationComparator;
import dkpro.similarity.algorithms.lsr.LexSemResourceComparator;
import dkpro.similarity.algorithms.lsr.path.JiangConrathComparator;
import dkpro.similarity.algorithms.lsr.path.LeacockChodorowComparator;
import dkpro.similarity.algorithms.lsr.path.LinComparator;
import dkpro.similarity.algorithms.lsr.path.PathBasedComparator;
import dkpro.similarity.algorithms.lsr.path.PathLengthComparator;
import dkpro.similarity.algorithms.lsr.path.ResnikComparator;
import dkpro.similarity.algorithms.lsr.path.WuPalmerComparator;

public class SemanticSim {

    /**
     * @param args
     */
    public static void main(String[] args) {     
        LexicalSemanticResource resource;
        try {
            InputParser inputParser = new  InputParser(TaskType.word2sense);
            inputParser.readFile(System.getenv("DKPRO_HOME") + "/SemEval-2014_Task-3-2/data/training/word2sense.train.input.tsv");
            
            List<String> wordList1 = inputParser.getWordList1();
            List<String> wordList2 = inputParser.getWordList2();

            for (int i = 0; i < wordList1.size(); i++) {
                String inputWord1 = wordList1.get(i);
                String inputWord2 = wordList2.get(i);
                
                // Get resource from local file
                resource = ResourceFactory.getInstance().get("wordnet", "en");
//                TextSimilarityMeasure measure = new LinComparator(resource);
//                TextSimilarityMeasure measure = new JiangConrathComparator(resource);
                // normalize the score if using LeacockChodorowComparator.
//                TextSimilarityMeasure measure = new LeacockChodorowComparator(resource);
//                TextSimilarityMeasure measure = new ResnikComparator(resource);
                TextSimilarityMeasure measure = new WuPalmerComparator(resource);

                // Get definition from wordnet.
                String senseDescription = WordNetWorker.getSense(inputWord2);
                List<String> senseDescription1 = WordNetWorker.getSenses(inputWord1);
                
                // skip some sense that is not in the wordnet dictionary.
                if (senseDescription == null) continue;
                
                String[] descriptionWords = senseDescription.split("\\W+");
                
                // use word vs word sense comparision.
                /*
                descriptionWords = new String[1];
                int index = inputWord2.indexOf('%');
                descriptionWords[0] = inputWord2;
                if (index != -1){
                    descriptionWords[0] = inputWord2.substring(0, index);
                }
                */

                //TODO: probably we should lemmatize the input before pass in. Not documented. 
                double score = 0;
                for (String description1 : senseDescription1) {
                    // for each tentative sense, calculate similarity score, pick the highest
                    List<Double> tempScoreList = new ArrayList<Double>();
                    // for each word in sense, average them.
                    for (String word : descriptionWords) {
                        String[] description1Words = description1.split("\\W+");
                        for (String description1Word : description1Words) {
                            double tempScore = measure.getSimilarity(description1Word, word);
                            if (tempScore < 0) {
                                continue;
                            }
                            tempScoreList.add(new Double(tempScore));
                        }
                    }
                    double sum = 0;
                    for (Double tempScore : tempScoreList) {
                        sum += tempScore.doubleValue();
                    }
                    double avg = sum / tempScoreList.size();
                    score = score > avg ? score : avg;
                }

                score = score * 2;
                if (score > 1) {
                    score = 1;
                }

                /* normalize the score if using LeacockChodorowComparator.
                score = score / 3.5;
                if (score > 1) {
                    score = 1;
                }
                */
                
//                System.out.println("Similarity: " + String.format("%1$,.1f", score*4) + "\tbetween " + inputword1 + " " + inputWord2);
              System.out.println(String.format("%1$,.1f", score*4));

            }



        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } 

        
        System.out.print("Finish running");
    }

}
