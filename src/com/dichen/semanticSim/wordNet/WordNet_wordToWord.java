package com.dichen.semanticSim.wordNet;

import java.awt.print.Printable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.netlib.util.intW;


import com.dichen.semanticSim.InputParser;
import com.dichen.semanticSim.OutputWriter;

import de.tudarmstadt.ukp.dkpro.lexsemresource.LexicalSemanticResource;
import de.tudarmstadt.ukp.dkpro.lexsemresource.core.ResourceFactory;
import de.tudarmstadt.ukp.dkpro.lexsemresource.exception.ResourceLoaderException;
import dkpro.similarity.algorithms.api.SimilarityException;
import dkpro.similarity.algorithms.api.TextSimilarityMeasure;
import dkpro.similarity.algorithms.lsr.path.JiangConrathComparator;
import dkpro.similarity.algorithms.lsr.path.LeacockChodorowComparator;
import dkpro.similarity.algorithms.lsr.path.LinComparator;
import dkpro.similarity.algorithms.lsr.path.ResnikComparator;
import dkpro.similarity.algorithms.lsr.path.WuPalmerComparator;
import dkpro.similarity.algorithms.vsm.ExplicitSemanticAnalysisComparator;
import dkpro.similarity.algorithms.vsm.VectorAggregation;
import dkpro.similarity.algorithms.vsm.VectorComparator;
import dkpro.similarity.algorithms.vsm.VectorNorm;
import dkpro.similarity.algorithms.vsm.store.IndexedDocumentsVectorReaderBase;
import dkpro.similarity.algorithms.vsm.store.LuceneVectorReader;
import dkpro.similarity.algorithms.vsm.store.vectorindex.VectorIndexReader;

public class WordNet_wordToWord implements WordNet_measurement{

    // TODO: Use web-database if necessary.
    private Map<String, Double> scoreCacheMap = new HashMap<String, Double>();
    
    private int initial = 0;
    
    public enum SimilarityAlgorithm {
        ESA,
        LIN,
        JIANG_CONRATH,
        RESNIK,
        WUPALMER,
        LEACOCK_CHODOROW
    }
    
    // required resource for similarity measurement.
    private LexicalSemanticResource resource;
    // Do not use this variable directly, call the warpper method.
    private TextSimilarityMeasure measure;
    // Algorithm type for this measurement class.
    private SimilarityAlgorithm algorithm;
    

    /**
     * Get similarity score from algorithm.
     * Fetch score from cache if it is already computed.
     * 
     * @param word1     Must be word, not sense key.
     * @param word2     Must be word, not sense key.
     * @return          Similarity Score
     */
    private double getSimilarity(String word1, String word2) {
        // get key for word pair.
        // The key is word:word pattern
        String key = word1 + ":" + word2;
        if (word1.compareTo(word2) > 0) {
            key = word2 + ":" + word1;
        }
        
        // If already cached, return the value.
        if (scoreCacheMap.containsKey(key)) {
            return scoreCacheMap.get(key).doubleValue();
        }
        
        // Calculate the value, cache it and return it.
        Double result = 0.0;
        try {
            result = new Double(measure.getSimilarity(word1, word2));
        } catch (SimilarityException e) {
            result = 0.0;
            e.printStackTrace();
        }
        scoreCacheMap.put(key, result);
        
        return result.doubleValue();
    }

    
    public static synchronized LexicalSemanticResource getResource(){
        try {
            return ResourceFactory.getInstance().get("wordnet", "en");
        } catch (ResourceLoaderException e) {
            System.err.println("set up wordnet resource fail");

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
            else if (algorithmType == SimilarityAlgorithm.ESA) {
                VectorIndexReader reader = new VectorIndexReader(new File(
                        System.getenv("DKPRO_HOME") + "/ESA/VectorIndexes/wp_eng_lem_nc_c-2")); 
                reader.setVectorAggregation(VectorAggregation.CENTROID);
                
                VectorComparator cmp = new VectorComparator(reader);

                return cmp;
            }
            else {
                System.err.println("set up wordnet comparator fail, unknown algorithm");
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("set up wordnet comparator fail");
            return null;
        }
    }

    /**
     * Set up cache map if there is corresponding local file.
     * @param algorithmType
     */
    private void setupCacheMap() {
        String fileName = "cacheMap" + algorithm.toString();
        Map<String, Double> temp = OutputWriter.readFromFile(fileName);
        if (temp != null) {
            scoreCacheMap = temp;
            initial = scoreCacheMap.size();
        }
    }
    
    /**
     * Save cache map to file.
     * @param algorithmType
     */
    private void saveCacheMap() {
        String fileName = "cacheMap" + algorithm.toString();
        OutputWriter.writeToFile(fileName, scoreCacheMap);
    }
    
    /**
     * Save the cache map, call this function when certain amount of computation has done.
     * 
     */
    public void save() {
        if (scoreCacheMap.size() > 1.1 * initial) {
            saveCacheMap();
            initial = scoreCacheMap.size();
        }
    }
    
    /**
     * 
     * @param algorithmType Specify what kind of similarity algorithm should use.
     *                      Refer to the enum in WordNet_wordToWord.SimilarityAlgorithm
     */
    public WordNet_wordToWord(SimilarityAlgorithm algorithmType) {
            algorithm = algorithmType;
        
            while(resource == null) {
                resource = getResource();
            }
            while (measure == null) {
                measure = getComparator(algorithmType, resource);
            }
            
            setupCacheMap();
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
        measureScore = getSimilarity(word1, word2);

        
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
                double temp = getSimilarity(word1, word2);
                // update the maximum score
                measureScore = measureScore > temp ? measureScore : temp;
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
                double temp = getSimilarity(word1, word2);
                if (temp > simScore) {
                    simScore = temp;
                    matchWord = word2;
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
        /*
        double sum = 0;
        for (Double score : scoreMap.values()) {
            sum += score.doubleValue();
        }
        return sum / scoreMap.size();        
        */
        
        // TODO: test code for issue #3:
        //          Use top 40% of the similarity score for averaging the description to description approach.
        double sum = 0;
        List<Double> scoreList = new ArrayList<Double>(scoreMap.values());
        // sort in acsending order.
        Collections.sort(scoreList);
        Collections.reverse(scoreList);
        for (int i = 0; i < (scoreList.size() * 0.4); i++) {
            sum += scoreList.get(i);
        }        
        
        return sum / (scoreList.size()*0.4);
        
        

    }


    @Override
    public double getWordNetSimilarity(String larger, String smaller) {        
        // Use word to word direct measure.
        return getSimilairtyScore(larger, smaller);
    }
    
    
    
    

}
