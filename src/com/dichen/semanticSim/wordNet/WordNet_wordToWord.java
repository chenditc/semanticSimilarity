package com.dichen.semanticSim.wordNet;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.netlib.util.doubleW;

import com.dichen.semanticSim.InputParser;

import de.tudarmstadt.ukp.dkpro.lexsemresource.LexicalSemanticResource;
import de.tudarmstadt.ukp.dkpro.lexsemresource.core.ResourceFactory;
import de.tudarmstadt.ukp.dkpro.lexsemresource.exception.LexicalSemanticResourceException;
import de.tudarmstadt.ukp.dkpro.lexsemresource.exception.ResourceLoaderException;
import dkpro.similarity.algorithms.api.SimilarityException;
import dkpro.similarity.algorithms.api.TextSimilarityMeasure;
import dkpro.similarity.algorithms.lsr.path.JiangConrathComparator;
import dkpro.similarity.algorithms.lsr.path.LeacockChodorowComparator;
import dkpro.similarity.algorithms.lsr.path.LinComparator;
import dkpro.similarity.algorithms.lsr.path.ResnikComparator;
import dkpro.similarity.algorithms.lsr.path.WuPalmerComparator;

public class WordNet_wordToWord {

    public enum SimilarityAlgorithm {
        LIN,
        JIANG_CONRATH,
        RESNIK,
        WUPALMER,
        LEACOCK_CHODOROW
    }
    
    // required resource for similarity measurement.
    private LexicalSemanticResource resource;
    private TextSimilarityMeasure measure;
    
    public static synchronized LexicalSemanticResource getResource(){
        try {
            return ResourceFactory.getInstance().get("wordnet", "en");
        } catch (ResourceLoaderException e) {
            return null;
        }
    }
    
    public static synchronized TextSimilarityMeasure getComparator(SimilarityAlgorithm algorithmType, LexicalSemanticResource resource ){
        try {
            if (algorithmType == SimilarityAlgorithm.LIN){
                return new LinComparator(resource);
            }
            else if (algorithmType == SimilarityAlgorithm.JIANG_CONRATH) {
                return new JiangConrathComparator(resource);
            }
            else if (algorithmType == SimilarityAlgorithm.LEACOCK_CHODOROW) {
                return new LeacockChodorowComparator(resource);
            }
            else if (algorithmType == SimilarityAlgorithm.RESNIK) {
                return new ResnikComparator(resource);
            }
            else if (algorithmType == SimilarityAlgorithm.WUPALMER) {
                return new WuPalmerComparator(resource);
            }
            else {
                return null;
            }
            
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 
     * @param algorithmType Specify what kind of similarity algorithm should use.
     *                      Refer to the enum in WordNet_wordToWord.SimilarityAlgorithm
     */
    public WordNet_wordToWord(SimilarityAlgorithm algorithmType) {
            while(resource == null) {
                resource = getResource();
            }
            while (measure == null) {
                measure = getComparator(algorithmType, resource);
            }
    }

    /**
     * If the wordnet resource is not exist, or the measure object set up fail,
     * Input can be sense key.
     * This function return -1 on failure. 
     * @param word1
     * @param word2
     * @return The similarity score for the two list of words.
     */
    public double getSimilairtyScore(String word1, String word2){
        if (resource == null || measure == null) {
            System.err.println("null resource or measure");
            return -1;
        }
        //TODO: probably we should lemmatize the input before pass in.
        //      Since some words are not in word net.
        
        // normalize two words, if any of them is a sense key, extract the word
        word1 = InputParser.getWordFromSenseKey(word1);
        word2 = InputParser.getWordFromSenseKey(word2);

        double measureScore = -1;
        try {
            measureScore = measure.getSimilarity(word1, word2);
        } catch (SimilarityException e) {
            measureScore = -1;
            System.err.println("SimilarityException");
            e.printStackTrace();
        }
        
        return measureScore;
    }
    
    /**
     * If the wordnet resource is not exist, or the measure object set up fail,
     * All input should be words, but not senseKey
     * This function return -1 on failure. 
     * @param word1
     * @param words2
     * @return The maximum similarity between word1 and the word in words2.
     */
    public double getSimilarityScore(String word1, Collection<String> words2) {
        if (resource == null || measure == null) {
            System.err.println("null resource or measure");
            return -1;
        }
        
        // normalize two words, if any of them is a sense key, extract the word
        word1 = InputParser.getWordFromSenseKey(word1);

        double measureScore = -1;
        for (String word2 : words2) {
            try {
                double temp = measure.getSimilarity(word1, word2);
                // update the maximum score
                measureScore = measureScore > temp ? measureScore : temp;
            } catch (SimilarityException e) {
            }
        }

        return measureScore;
    }
    
    /**
     * Use alignment similarity measure for two collection of words.
     * Two input array need to be removable.
     * If the wordnet resource is not exist, or the measure object set up fail,
     * All input should be words, but not senseKey
     * This function return -1 on failure. 
     * @param words1
     * @param words2
     * @return The maximum similarity between word1 and the word in words2.
     */
    public double getSimilarityScoreByAlignment(List<String> words1, List<String> words2) {
        if (resource == null || measure == null) {
            System.err.println("null resource or measure");
            return -1;
        }
       
        // Find the smaller length of words, and put it as words1.
        if (words1.size() > words2.size()) {
            List<String> temp = words1;
            words1 = words2;
            words2 = temp;
        }
        
        Map<String, Double> scoreMap = new TreeMap<String, Double>(); 
        
        for (String word1 : words1) {
            double simScore = -1;
            String matchWord = "";
            // Find maximum similarity score for word1.
            for (String word2 : words2) {
                try {
                    double temp = measure.getSimilarity(word1, word2);
                    if (temp > simScore) {
                        simScore = temp;
                        matchWord = word2;
                    }
                } catch (SimilarityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
            // put the score into map and erase the word2.
            if (simScore != -1) {
                scoreMap.put(word1, simScore);
                words2.remove(matchWord);
            }
            else{
                scoreMap.put(word1, 0.0);
            }
        }
        
        // calculate the average similarity score.
        double sum = 0;
        for (Double score : scoreMap.values()) {
            sum += score.doubleValue();
        }
        return sum / scoreMap.size();

    }
    
    
    
    

}
