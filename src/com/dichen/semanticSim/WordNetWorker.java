package com.dichen.semanticSim;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.tudarmstadt.ukp.dkpro.wsd.si.lsr.LsrSenseInventory;
import de.tudarmstadt.ukp.dkpro.wsd.si.wordnet.candidates.WordNetSenseKeyToSynset;

public class WordNetWorker {
    private static Map<String, String> wordNetMap;

    
    /**
     * 
     * @return
     * @throws IllegalArgumentException
     * @throws IOException
     */
    static Map<String, String> getWordNetMap() throws IllegalArgumentException, IOException{
        if (wordNetMap == null){
            URL wordNetSenseMapLocation;
            wordNetSenseMapLocation = new File(System.getenv("DKPRO_HOME") + "/de.tudarmstadt.ukp.dkpro.lexsemresource.core.ResourceFactory/wordnet3/dict/index.sense").toURI().toURL();
            wordNetMap = WordNetSenseKeyToSynset.getSenseMap(wordNetSenseMapLocation);

        }
        return wordNetMap;
    }
    
    /**
     * 
     * @param senseKey  The input should be the sense key in wordnet 3.1
     * @return  The sense strings in wordnet3.1. return null if there is an exception.
     */
    public static List<String> getSense(String senseKey){
        // get sense map.
        try {
            // Get the sense number from the sense key.
            String temp = getWordNetMap().get(senseKey);
            
            if (temp == null) {
                System.err.println("There is no sense key in wordnet: " + senseKey);
                List<String> tempList = new ArrayList<String>();
                tempList.add(senseKey.split("%")[0]);
                return tempList;
            }
            
            String senseSyn = temp.substring(0, temp.length()-1);
            senseSyn = Integer.parseInt(senseSyn) + "";
            
            // Get the word from sense key, search by word.
            LsrSenseInventory senseInventory = new LsrSenseInventory("wordnet", "en");
            String word = senseKey.split("%")[0];
            List<String> senses = senseInventory.getSenses(word);

            // fileter the sense that match the sense number, return the description.
            for (String sense : senses) {
                if (sense.contains(senseSyn)){
                    String[] tempArray = senseInventory.getSenseDescription(sense).split("\\W+");
                    List<String> tempList = new ArrayList<String>(Arrays.asList(tempArray));
                    return tempList;

                }
            }
            
            // If no sense found, return empty string.
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }        
    }
    
    /**
     * Return all words in all description of inputWord.
     * @param inputWord input is a normal word, not sense key.
     * @return a list of description words.
     */
    public static List<String> getSenses(String inputWord){
        // split if there is a space between
        
        
        // get sense map.
        try {
            List<String> inputwords = new ArrayList<String>(Arrays.asList(inputWord.split("\\W+")));
            List<String> result = new ArrayList<String>();

            for (String word : inputwords) {
                // Get the word from sense key, search by word.
                LsrSenseInventory senseInventory = new LsrSenseInventory("wordnet", "en");
                List<String> senses = senseInventory.getSenses(word);

                for (String sense : senses) {
                    List<String> descriptionWordsList = Arrays.asList(senseInventory.getSenseDescription(sense).split("\\W+"));
                    result.addAll(descriptionWordsList);

                }
            }

            // If no sense found, return empty string.
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }        
    }
}
