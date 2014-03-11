package com.dichen.semanticSim;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;



import com.dichen.semanticSim.InputParser.TaskType;
import com.dichen.semanticSim.wordNet.WordNet_phraseToWord;
import com.dichen.semanticSim.wordNet.WordNet_wordToDescription;
import com.dichen.semanticSim.wordNet.WordNet_wordToWord.SimilarityAlgorithm;

public class SemanticSim extends Thread{

    private SimilarityAlgorithm algorithm;
    public SemanticSim(SimilarityAlgorithm runAlgorithm) {
        // TODO Auto-generated constructor stub
        algorithm = runAlgorithm;
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
            inputParser.readFile(System.getenv("DKPRO_HOME") + "/SemEval-2014_Task-3-2/data/training/phrase2word.train.input.tsv");
            
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
                SemanticSimRunner runner = new SemanticSimRunner(inputParser.getTaskType(), algorithm, word1, sense2);
                resultDoubles.add(runner.call());
            }
            
            
            // create thread pool and for each word pair, run the thread pool
            /*
            ExecutorService threadPoolService = Executors.newFixedThreadPool(16);
            List<Future<Double>> futures = new ArrayList<Future<Double>>();
            
            for (int i = 0; i < wordList1.size(); i++) {
                String word1 = wordList1.get(i);
                String sense2 = wordList2.get(i);
                
                SemanticSimRunner runner = new SemanticSimRunner(new WordNet_wordToDescription(algorithm), word1, sense2);
                futures.add(threadPoolService.submit(runner));
            }
            
            threadPoolService.shutdown();
            threadPoolService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            
            
            // Add result into result list
            for (Future<Double> result : futures) {
                System.out.println("start add result");

                resultDoubles.add(result.get());
                System.out.println("add result");


            }
            */
//                System.out.println("Similarity: " + String.format("%1$,.1f", score*4) + "\tbetween " + inputword1 + " " + inputWord2);
//              System.out.println(String.format("%1$,.1f", 0));

            OutputWriter.writeToFile("raw.training." + inputParser.getTaskType() + "." + "phrase_description" + "."+ algorithm, resultDoubles);

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } 


        System.out.println("Finish running " + algorithm);
    }

   

}
