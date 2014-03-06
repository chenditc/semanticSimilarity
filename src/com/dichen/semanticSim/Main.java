package com.dichen.semanticSim;

import com.dichen.semanticSim.wordNet.WordNet_wordToWord.SimilarityAlgorithm;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // try all algorithms
        for (SimilarityAlgorithm algorithm : SimilarityAlgorithm.values()) {
            SemanticSim similairty = new SemanticSim(algorithm);
            similairty.run();
        }
    }

}
