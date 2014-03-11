package com.dichen.semanticSim;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

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
                writer.write(new Double(score).toString() + '\n');

            }
        } catch (IOException ex) {
          // report
        } finally {
           try {writer.close();} catch (Exception ex) {}
        }
    }
    
    /**
     * Write a map with type: Map<String, Double> to file.
     * @param fileName
     * @param cacheMap
     */
    public static void writeToFile(String fileName, Map<String, Double> cacheMap) {
        ObjectOutputStream writer = null;
        try {
            writer = new ObjectOutputStream(
                  new FileOutputStream("outputs/" + fileName));
            writer.writeObject(cacheMap);
        } catch (IOException ex) {
          // report
        } finally {
           try {writer.close();} catch (Exception ex) {}
        }
    }
    
    public static Map<String, Double> readFromFile(String fileName) {
        ObjectInputStream reader = null;
        try {
            reader = new ObjectInputStream(
                  new FileInputStream("outputs/" + fileName));
            Object temp = reader.readObject();
            
            return (Map<String, Double>) temp;
        } catch (IOException ex) {
          // report
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
           try {reader.close();} catch (Exception ex) {}
        }
        return null;
    }

}
