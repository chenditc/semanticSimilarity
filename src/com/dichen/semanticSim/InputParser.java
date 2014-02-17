package com.dichen.semanticSim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputParser {

    enum TaskType {
        word2sense
    }
    
    TaskType inpuType;
    private List<String> wordList1 = new ArrayList<String>();
    private List<String> wordList2 = new ArrayList<String>();

    
    
    public InputParser(TaskType taskType) {
        inpuType = taskType;
    }
    
    public void readFile(String fileName) throws IOException {
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
    
    public List<String> getWordList1() {
        return wordList1;
    }
    
    public List<String> getWordList2() {
        return wordList2;
    }

}
