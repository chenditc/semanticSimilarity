package com.dichen.semanticSim;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
            // Get resource from local file
            resource = ResourceFactory.getInstance().get("wordnet", "en");
            

            TextSimilarityMeasure measure = new LinComparator(resource);
            
            String[] tokens1 = "job is book".split(" ");   
            String[] tokens2 = "bike are text".split(" ");
            List<String> token1 = Arrays.asList(tokens1);
            List<String> token2 = Arrays.asList(tokens2);

            //TODO: probably we should lemmatize the input before pass in. Not documented.
            double score;
            score = measure.getSimilarity(token1, token2);
            System.out.println("Similarity: " + score);


        } catch (ResourceLoaderException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (LexicalSemanticResourceException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SimilarityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.print("Finish running");
    }

}
