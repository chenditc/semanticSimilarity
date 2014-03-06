package com.dichen.semanticSim;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.netlib.util.doubleW;


import com.dichen.semanticSim.wordNet.WordNet_wordToDescription;

public class SemanticSimRunner implements Callable<Double> {

    private WordNet_wordToDescription measure;
    private String word;
    private String sense;
    
    public SemanticSimRunner(WordNet_wordToDescription tempMeasure, String word1, String sense2) {
        measure = tempMeasure;
        word = word1;
        sense = sense2;
    }

    @Override
    public Double call() throws Exception {
        System.out.println("start value!");
        try {
            double score = measure.getSimilairtyScore(word, sense);
            score = score < 0 ? 0 : score;
            System.out.println("get value!");
            return new Double(score);
        } catch (Exception e) {
            System.out.println("get error!");

            return 0.0;
        }

    }

}
