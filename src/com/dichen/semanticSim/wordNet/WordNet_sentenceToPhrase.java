package com.dichen.semanticSim.wordNet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.netlib.util.intW;

import com.dichen.semanticSim.WordNetWorker;

public class WordNet_sentenceToPhrase extends WordNet_wordToWord {
    private int approach;
    public WordNet_sentenceToPhrase(SimilarityAlgorithm algorithmType, int runApproach) {
        super(algorithmType);
        approach = runApproach;
    }

    /**
     * Put the sentence and the phrase as bad of words
     * compare two bag of words using alignment.
     * @param sentence
     * @param phrase
     * @return
     */
    private double getSimilarity_sentenceToPhrase(String sentence, String phrase) {
        List<String> sentenceList = new ArrayList<String>(Arrays.asList(sentence.split("\\W+")));
        List<String> phraseList = new ArrayList<String>(Arrays.asList(phrase.split("\\W+")));
        return getSimilarityScoreByAlignment(sentenceList, phraseList);
    }
    
    /**
     * Extract the definition of phrase and put it as a bag of words.
     * Do similarity measure between the bag of words and the sentence.
     * @param sentence
     * @param phrase
     * @return
     */
    private double getSimilarity_sentenceToDefinition(String sentence, String phrase) {
        // Get definition of sentence and phrase as bad of words.
        List<String> phraseDescription = WordNetWorker.getSenses(phrase);
        if (phraseDescription.size() == 0) {
            phraseDescription = new ArrayList<String>(Arrays.asList(phrase.split("\\W+")));
        }
        
        List<String> sentenceList = new ArrayList<String>(Arrays.asList(sentence.split("\\W+")));
        
        // do alignment comparison on sentence to definition of the phrase.
        return getSimilarityScoreByAlignment(phraseDescription, sentenceList);
    }
    
    /**
     * Extract the deinition of each word in phrase and sentence, 
     * then compare two bag of words.
     * @param sentence
     * @param phrase
     * @return
     */
    private double getSimilarity_DefinitionToDefinition(String sentence, String phrase) {
        // Get definition of sentence and phrase as bad of words.
        List<String> phraseDescription = WordNetWorker.getSenses(phrase);
        if (phraseDescription.size() == 0) {
            phraseDescription = new ArrayList<String>(Arrays.asList(phrase.split("\\W+")));
        }
        List<String> sentenceDescription = WordNetWorker.getSenses(phrase);
        if (phraseDescription.size() == 0) {
            phraseDescription = new ArrayList<String>(Arrays.asList(phrase.split("\\W+")));
        }
        
        // do alignment comparison on definition of sentence to definition of the phrase.
        return getSimilarityScoreByAlignment(phraseDescription, sentenceDescription);
    }
    
    @Override
    public double getWordNetSimilarity(String larger, String smaller) {
        // Use phrase to definition alignment measure.
        if (approach == 1) {
            return getSimilarity_sentenceToPhrase(larger, smaller);
        }
        else if (approach == 2) {
            return getSimilarity_sentenceToDefinition(larger, smaller);
        }
        else {
            return getSimilarity_DefinitionToDefinition(larger, smaller);
        }      
    }
}
