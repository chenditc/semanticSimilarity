package com.dichen.semanticSim;

import java.util.HashMap;
import java.util.Map;

import com.dichen.semanticSim.wordNet.WordNet_wordToWord.SimilarityAlgorithm;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // If argument specify a algorithm, use that one, otherwise run all of them.
        if (args.length > 0) {
            try {
                System.out.println("Run algorithm:" + args[0]);
                SimilarityAlgorithm algorithm = SimilarityAlgorithm.valueOf(args[0]);
                SemanticSim similairty = new SemanticSim(algorithm);
                similairty.run();
                return;
            } catch (Exception e) {
                System.err.println("Not valid argument");
            }
        }
        else {
            // try all algorithms
            for (SimilarityAlgorithm algorithm : SimilarityAlgorithm.values()) {
                SemanticSim similairty = new SemanticSim(algorithm);
                similairty.run();
                return;
            }
        }
       
    }

}
