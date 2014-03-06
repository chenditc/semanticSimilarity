package com.dichen.semanticSim.wordNet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.dichen.semanticSim.WordNetWorker;

public class WordNet_wordToDescription extends WordNet_wordToWord {

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
            List<String> descriptionWords = new ArrayList<String>(Arrays.asList(WordNetWorker.getSense(senseKey)));
            return getSimilarityScore(word, descriptionWords);
        } catch (Exception e) {
            return 0.0;
        }
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
            List<String> senseDescription = new ArrayList<String>(Arrays.asList(WordNetWorker.getSense(senseKey)));
            return getSimilarityScoreByAlignment(wordDescription, senseDescription);
        } catch (Exception e) {
            // TODO: handle exception
            return 0.0;
        }
        
    }

}
