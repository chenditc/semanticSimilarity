package com.dichen.semanticSim;

import java.util.HashMap;
import java.util.Map;

import org.netlib.util.intW;

import com.dichen.semanticSim.wordNet.WordNet_wordToWord.SimilarityAlgorithm;

public class Main {

    /**
     * argument should be: filename, approach, algorithm
     * fileName: relative path of file
     * approach: integer from 1 to 3
     * algorithm: String from enum SimilarityAlgorithm
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage:" + "argument should be: filename, approach, algorithm");
            System.out.println("fileName: relative path of file");
            System.out.println("approach: integer from 1 to 3");
            System.out.println("algorithm: String from enum SimilarityAlgorithm");
            return;
        }
        
        
        // If argument specify a algorithm, use that one, otherwise run all of them.
        String fileName = args[0];
        int approach = new Integer(args[1]).intValue();
        
        
        if (args.length > 2) {
            try {
                System.out.println("Run algorithm:" + args[2]);
                SimilarityAlgorithm algorithm = SimilarityAlgorithm.valueOf(args[2]);
                SemanticSim similairty = new SemanticSim(algorithm, fileName, approach);
                similairty.run();
                return;
            } catch (Exception e) {
                System.err.println("Not valid argument");
            }
        }
        else {
            // try all algorithms
            for (SimilarityAlgorithm algorithm : SimilarityAlgorithm.values()) {
                SemanticSim similairty = new SemanticSim(algorithm, fileName, approach);
                similairty.run();
                return;
            }
        }
       
    }

}
