package com.dichen.semanticSim.wordNet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.dichen.semanticSim.WordNetWorker;

public class WordNet_wordToDescription extends WordNet_wordToWord implements WordNet_measurement{

    public WordNet_wordToDescription(SimilarityAlgorithm algorithmType) {
        super(algorithmType);
    }
    
    /**
     * 
     * @param word      word
     * @param senseKey  wordNetSenseKey
     * @return      Similarity score between word and most similar word of description.
     */
    public double getSimilarity_WordToDescription(String word, String senseKey) {
        try {
            List<String> descriptionWords = new ArrayList<String>(WordNetWorker.getSense(senseKey));        
            return getSimilarityScore(word, descriptionWords);
        } catch (Exception e) {
            return 0.0;
        }
    }
    

    
    @Override
    public double getWordNetSimilarity(String larger, String smaller) {
        // Use word to description direct measure.
        return getSimilarity_WordToDescription(larger, smaller);
    }

}
