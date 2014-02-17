package com.dichen.semanticSim;

import java.awt.print.Printable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.netlib.util.doubleW;

import de.tudarmstadt.ukp.dkpro.lexsemresource.Entity;
import de.tudarmstadt.ukp.dkpro.lexsemresource.LexicalSemanticResource;
import de.tudarmstadt.ukp.dkpro.lexsemresource.LexicalSemanticResource.LexicalRelation;
import de.tudarmstadt.ukp.dkpro.lexsemresource.LexicalSemanticResource.SemanticRelation;
import de.tudarmstadt.ukp.dkpro.lexsemresource.core.ResourceFactory;
import de.tudarmstadt.ukp.dkpro.lexsemresource.exception.LexicalSemanticResourceException;
import de.tudarmstadt.ukp.dkpro.lexsemresource.exception.ResourceLoaderException;
import dkpro.similarity.algorithms.*;
import dkpro.similarity.algorithms.api.SimilarityException;
import dkpro.similarity.algorithms.api.TextSimilarityMeasure;
import dkpro.similarity.algorithms.lexical.ngrams.WordNGramJaccardMeasure;
import dkpro.similarity.algorithms.lsr.LexSemRelationComparator;
import dkpro.similarity.algorithms.lsr.LexSemResourceComparator;
import dkpro.similarity.algorithms.lsr.path.LinComparator;

public class SemanticSim {

    /**
     * @param args
     */
    public static void main(String[] args) {     
        LexicalSemanticResource resource;
        try {
            String inputword1 = "illuminate";
            String inputWord2 = "bright%3:00:00::";
            
            // Get resource from local file
            resource = ResourceFactory.getInstance().get("wordnet", "en");
            TextSimilarityMeasure measure = new LinComparator(resource);

            // Get definition from wordnet.
            WordNetWorker parser = new WordNetWorker();
            String senseDescription = parser.getSense(inputWord2);
            String[] descriptionWords = senseDescription.split("\\W+");

            //TODO: probably we should lemmatize the input before pass in. Not documented.     
            double score = 0;
            for (String word : descriptionWords) {
                double tempScore = measure.getSimilarity(inputword1, word);
                System.out.println("Similarity: " + tempScore + word);
                score = score > tempScore ? score : tempScore;
            }
            System.out.println("Similarity: " + score*5);


        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } 

        
        System.out.print("Finish running");
    }

}
