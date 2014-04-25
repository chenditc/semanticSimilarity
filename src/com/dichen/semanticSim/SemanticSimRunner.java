package com.dichen.semanticSim;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.netlib.util.doubleW;
import org.netlib.util.intW;


import com.dichen.semanticSim.InputParser.TaskType;
import com.dichen.semanticSim.wordNet.WordNet_DescriptionToDescription;
import com.dichen.semanticSim.wordNet.WordNet_measurement;
import com.dichen.semanticSim.wordNet.WordNet_phraseToWord;
import com.dichen.semanticSim.wordNet.WordNet_sentenceToPhrase;
import com.dichen.semanticSim.wordNet.WordNet_wordToDescription;
import com.dichen.semanticSim.wordNet.WordNet_wordToWord;
import com.dichen.semanticSim.wordNet.WordNet_wordToWord.SimilarityAlgorithm;

public class SemanticSimRunner implements Callable<Double> {

    private static WordNet_measurement measure;
    private SimilarityAlgorithm algorithm;
    private String word;
    private String sense;
    private int approach;
    
    private void setMeasure(TaskType taskType) {
        if (measure != null) {
            return;
        }
        
        if (taskType == TaskType.word2sense) {
            // three approach here
            if (approach == 1) {
                measure = new WordNet_wordToWord(algorithm);
            }
            else if (approach == 2) {
                measure = new WordNet_wordToDescription(algorithm);
            }
            else {
                measure = new WordNet_DescriptionToDescription(algorithm);
            }
        }
        else if (taskType == TaskType.phrase2word){
            // two approach herem can change it in the class
            measure = new WordNet_phraseToWord(algorithm, approach);
        }
        else if (taskType == TaskType.sentence2phrase) {
            // three approach here, can change it in the class
            measure = new WordNet_sentenceToPhrase(algorithm, approach);
        }
    }
    
    public SemanticSimRunner(int runApproach, TaskType taskType, SimilarityAlgorithm inputAlgorithm, String word1, String sense2) {
        algorithm = inputAlgorithm;
        setMeasure(taskType);
        word = word1;
        sense = sense2;
        approach = runApproach;
    }

    @Override
    public Double call() throws Exception {
        try {
            double score = measure.getWordNetSimilarity(word, sense);
            score = score < 0 ? 0 : score;
            measure.save();
            return new Double(score);
        } catch (Exception e) {
            return 0.0;
        }

    }

}
