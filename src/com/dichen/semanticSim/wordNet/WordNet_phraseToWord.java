package com.dichen.semanticSim.wordNet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.netlib.util.doubleW;
import org.netlib.util.intW;

import com.dichen.semanticSim.WordNetWorker;


public class WordNet_phraseToWord extends WordNet_wordToWord implements WordNet_measurement{
    private int approach;
    public WordNet_phraseToWord(SimilarityAlgorithm algorithmType, int runApproach) {
        super(algorithmType);
        this.approach = runApproach;
    }
    
    /**
     * Extract the definition of word and put it as a bag of words.
     * Do similarity measure between the bag of words and the phrase.
     * @param phrase
     * @param word
     * @return
     */
    public double getSimilarity_phraseToDefinition(String phrase, String word) {
        // Get definition of word as bad of words.
        List<String> wordDescription = WordNetWorker.getSenses(word);
        if (wordDescription.size() == 0) {
            wordDescription = new ArrayList<String>(Arrays.asList(word.split("\\W+")));
        }
        
        // do alignment comparison on phrase to definition of the word.
        List<String> phraseList = new ArrayList<String>(Arrays.asList(phrase.split("\\W+")));
                
        return getSimilarityScoreByAlignment(phraseList, wordDescription);
    }
    
    /**
     * Extract the definition of word and put it as a bag of words.
     * Extract the definition of each word in phrase and put them as a bad of word.
     * Do similarity measure between two bag of words and the phrase.
     * @param phrase
     * @param word
     * @return
     */
    public double getSimilarity_DefinitionToDefinition(String phrase, String word) {
        // Get definition of word as bad of words.
        List<String> wordDescription = WordNetWorker.getSenses(word);
        if (wordDescription.size() == 0) {
            wordDescription = new ArrayList<String>(Arrays.asList(word.split("\\W+")));
        }
        
        // do alignment comparison on definition of phrase to definition of the word.
        List<String> phraseList = new ArrayList<String>(Arrays.asList(phrase.split("\\W+")));
        List<String> phraseDefinitionList = new ArrayList<String>();
        
        for (String tempWord : phraseList) {
            phraseDefinitionList.addAll(WordNetWorker.getSenses(tempWord));
        }
        

        return getSimilarityScoreByAlignment(wordDescription, phraseDefinitionList);
    }

    @Override
    public double getWordNetSimilarity(String larger, String smaller) {
        // Use phrase to definition alignment measure.
        if (approach == 1) {
            return getSimilarity_phraseToDefinition(larger, smaller);
        }
        else {
            return getSimilarity_DefinitionToDefinition(larger, smaller);
        }
    }
}
