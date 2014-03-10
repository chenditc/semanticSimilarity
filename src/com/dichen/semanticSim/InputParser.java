package com.dichen.semanticSim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.netlib.util.intW;

public class InputParser {

    enum TaskType {
        word2sense,
        phrase2word,
        sentence2phrase,
        paragraph2sentence,
    }
    
    private TaskType inpuType;
    // List of string in larger side.
    private List<String> wordList1 = new ArrayList<String>();
    // List of string in smaller side.
    private List<String> wordList2 = new ArrayList<String>();

    
    
    /**
     * This class will distinguish the input type by the file name.
     * 
     * @param taskType
     */
    public InputParser() {
        // TODO: tentatively use 
    }
    
    public TaskType getTaskType(){
        return inpuType;
    }
    
    /**
     * Change the input type according to the file name ending. 
     * Read and parse the file to two list of strings.
     * @param fileName
     * @throws IOException
     */
    public void readFile(String fileName) throws IOException {
        if (fileName.contains("word2sense")) {
            inpuType = TaskType.word2sense;
        }
        else if (fileName.contains("phrase2word")) {
            inpuType = TaskType.phrase2word;
        }
        else if (fileName.contains("sentence2phrase")) {
            inpuType = TaskType.sentence2phrase;
        }
        else if (fileName.contains("paragraph2sentence")) {
            inpuType = TaskType.paragraph2sentence;
        }
        else {
            inpuType = TaskType.paragraph2sentence;
        }
        
        
        if (inpuType == TaskType.word2sense) {
            parseAndSaveWord2senseInput(fileName);
        }
        else if (inpuType == TaskType.phrase2word) {
            parseAndSavePhrase2WordInput(fileName);
        }
    }
    
    public void parseAndSaveWord2senseInput(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine();

        while (line != null) {
            String[] wordsStrings = line.split("\\t");
            if (wordsStrings.length > 1) {
                // parse out the pos.
                String tempString = wordsStrings[0];
                if (tempString.indexOf("#") > 0){
                    tempString = tempString.substring(0,tempString.indexOf("#"));
                }

                wordList1.add(tempString);
                wordList2.add(wordsStrings[1]);
            }
            
            line = br.readLine();
        }

        br.close();
    }
    
    public void parseAndSavePhrase2WordInput(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine();

        while (line != null) {
            String[] wordsStrings = line.split("\\t");
            if (wordsStrings.length > 1) {
                // parse out the pos.
                int wordStringIndex = wordsStrings.length - 2;
                String word = wordsStrings[wordStringIndex];
                StringBuilder phraseBuilder = new StringBuilder();
                for (int i = 0; i < wordStringIndex; i++) {
                    phraseBuilder.append(wordsStrings[i]);
                }

                wordList1.add(phraseBuilder.toString());
                wordList2.add(word);
            }
            
            line = br.readLine();
        }

        br.close();
    }
    
    public static String getWordFromSenseKey(String sensekey){
        int index = sensekey.indexOf('%');
        return index == -1 ? sensekey : sensekey.substring(0, index);
    }
    
    // List of string in larger side.
    public List<String> getWordList1() {
        return wordList1;
    }
    
    // List of string in smaller side.
    public List<String> getWordList2() {
        return wordList2;
    }

}
