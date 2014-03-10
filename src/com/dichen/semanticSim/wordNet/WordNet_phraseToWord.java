package com.dichen.semanticSim.wordNet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dichen.semanticSim.WordNetWorker;


public class WordNet_phraseToWord extends WordNet_wordToWord implements WordNet_measurement{

    public WordNet_phraseToWord(SimilarityAlgorithm algorithmType) {
        super(algorithmType);
    }
    
    public double getSimilarity_phraseToDefinition(String phrase, String word) {
        // Get definition of word as bad of words.
        List<String> wordDescription = WordNetWorker.getSenses(word);
        
        // do alignment comparison on phrase to definition of the word.
        List<String> phraseList = new ArrayList<String>(Arrays.asList(phrase.split("\\W+")));
        
        return getSimilarityScoreByAlignment(phraseList, wordDescription);
    }

    @Override
    public double getWordNetSimilarity(String larger, String smaller) {
        // Use phrase to definition alignment measure.
        return getSimilarity_phraseToDefinition(larger, smaller);
    }
}
