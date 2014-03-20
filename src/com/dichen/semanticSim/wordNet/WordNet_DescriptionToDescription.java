package com.dichen.semanticSim.wordNet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dichen.semanticSim.WordNetWorker;

public class WordNet_DescriptionToDescription extends WordNet_wordToDescription implements WordNet_measurement{

    public WordNet_DescriptionToDescription(SimilarityAlgorithm algorithmType) {
        super(algorithmType);
    }

    /**
     * 
     * @param word      word
     * @param senseKey  wordNetSenseKey
     * @return      Similarity score between word description and most similar word of description.
     */
    public double getSimilarity_DescriptionToDescription_ByAlignment(String word, String senseKey) {
        try {
            List<String> wordDescription = WordNetWorker.getSenses(word);
            List<String> senseDescription = new ArrayList<String>(WordNetWorker.getSense(senseKey));

            // If the word is not included in the wordnet.
            if (wordDescription.size() == 0){
                wordDescription.add(word);
            }
                    
            return getSimilarityScoreByAlignment(wordDescription, senseDescription);
        } catch (Exception e) {
            // TODO: handle exception
            return 0.0;
        }
        
    }
    
    @Override
    public double getWordNetSimilarity(String larger, String smaller) {
        // Use description to description measure.
        return getSimilarity_DescriptionToDescription_ByAlignment(larger, smaller);
    }
}
