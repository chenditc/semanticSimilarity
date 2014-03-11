package com.dichen.semanticSim.wordNet;


public interface WordNet_measurement {
    /**
     * Get similarity measurement according to different algorithm,
     * algorithm is setup by initialize different instance.
     * @param larger    larger side of text
     * @param smaller   smaller side of text
     * @return          similarity score
     */
    public double getWordNetSimilarity(String larger, String smaller);
    
    /**
     * Save the current cache for future speed up.
     */
    public void save();
}
