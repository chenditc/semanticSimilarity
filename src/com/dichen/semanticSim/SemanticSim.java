package com.dichen.semanticSim;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.netlib.util.doubleW;
import org.netlib.util.intW;



import com.dichen.semanticSim.InputParser.TaskType;
import com.dichen.semanticSim.wordNet.WordNet_phraseToWord;
import com.dichen.semanticSim.wordNet.WordNet_wordToDescription;
import com.dichen.semanticSim.wordNet.WordNet_wordToWord.SimilarityAlgorithm;

public class SemanticSim extends Thread{

    private SimilarityAlgorithm algorithm;
    private String fileName;
    private int approach;
    public SemanticSim(SimilarityAlgorithm runAlgorithm, String inputFileName, int runApproach) {
        algorithm = runAlgorithm;
        fileName = inputFileName;
        approach = runApproach;
    }
    
    /**
     * @param args
     */
    public void run() {
        calculate();
    }
    
    public void calculate() {    
        List<Double> resultDoubles = new ArrayList<Double>();
        
        System.out.println("Start running " + algorithm);
 
        
        try {
            // Get data
            InputParser inputParser = new  InputParser();
            inputParser.readFile(System.getenv("DKPRO_HOME") + fileName);
            
            // word list 1 is the list of words
            List<String> wordList1 = inputParser.getWordList1();
            // word list 2 is the list of sense, represented in the sense key of word net.
            List<String> wordList2 = inputParser.getWordList2();

            if (wordList1.size() != wordList2.size()) {
                System.err.println("The size of word and sense list does not match");
                return;
            }
            
            for (int i = 0; i < wordList1.size(); i++) {
                String word1 = wordList1.get(i);
                String sense2 = wordList2.get(i);
                SemanticSimRunner runner = new SemanticSimRunner(approach,inputParser.getTaskType(), algorithm, word1, sense2);
                double tempResult = 0;
                tempResult = runner.call();
                resultDoubles.add(tempResult);
            }
            
//                System.out.println("Similarity: " + String.format("%1$,.1f", score*4) + "\tbetween " + inputword1 + " " + inputWord2);
//              System.out.println(String.format("%1$,.1f", 0));

            String outputFileName = "raw.";
            outputFileName += inputParser.dataType;

            if (approach == 1) {
                OutputWriter.writeToFile("ls"+ algorithm + ".STS.output." + inputParser.getTaskType() + ".txt", resultDoubles);
            }
            else if (approach == 2) {
                OutputWriter.writeToFile("ld"+ algorithm + ".STS.output." + inputParser.getTaskType() + ".txt", resultDoubles);
            }
            else {
                OutputWriter.writeToFile("dd"+ algorithm + ".STS.output." + inputParser.getTaskType() + ".txt", resultDoubles);
            }
            
            /*
            if (approach == 1) {
                OutputWriter.writeToFile(outputFileName + inputParser.getTaskType() + "." + "large_small" + "."+ algorithm, resultDoubles);
            }
            else if (approach == 2) {
                OutputWriter.writeToFile(outputFileName + inputParser.getTaskType() + "." + "large_description" + "."+ algorithm, resultDoubles);
            }
            else {
                OutputWriter.writeToFile(outputFileName + inputParser.getTaskType() + "." + "description_description" + "."+ algorithm, resultDoubles);
            }
            */
            
            System.out.println("Finish running " + algorithm);

        } catch (Exception e1) {
            e1.printStackTrace();
        } 


    }

   

}
