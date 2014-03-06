package com.dichen.semanticSim;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

public class OutputWriter {

    public OutputWriter() {
        // TODO Auto-generated constructor stub
    }
    
    public static void writeToFile(String fileName, List<Double> scores) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                  new FileOutputStream("outputs/" + fileName), "utf-8"));
            for (Double score : scores) {
                writer.write(String.format("%1$,.1f", score) + '\n');
            }
        } catch (IOException ex) {
          // report
        } finally {
           try {writer.close();} catch (Exception ex) {}
        }
    }

}
