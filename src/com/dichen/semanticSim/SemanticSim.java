package com.dichen.semanticSim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dkpro.similarity.algorithms.*;
import dkpro.similarity.algorithms.api.SimilarityException;
import dkpro.similarity.algorithms.api.TextSimilarityMeasure;
import dkpro.similarity.algorithms.lexical.ngrams.WordNGramJaccardMeasure;

public class SemanticSim {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
     // this similarity measure is defined in the dkpro.similarity.algorithms.lexical-asl package
     // you need to add that to your .pom to make that example work
     // there are some examples that should work out of the box in dkpro.similarity.example-gpl 
     TextSimilarityMeasure measure = new WordNGramJaccardMeasure(3);    // Use word trigrams

     String[] tokens1 = "This is a short example text.".split(" ");   
     String[] tokens2 = "This is an example text.".split(" ");
     
     List<String> token1 = Arrays.asList(tokens1);
     List<String> token2 = Arrays.asList(tokens2);

     
     // only works from 2.1.0-SHAPSHOT onwards, for previous versions you need to convert to Collection<String> first
     double score;
    try {
        score = measure.getSimilarity(token1, token2);
        System.out.println("Similarity: " + score);

    } catch (SimilarityException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

        
        
        System.out.print("Hello world");
    }

}
