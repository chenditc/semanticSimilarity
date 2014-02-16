package com.dichen.semanticSim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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

public class SemanticSim {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
     // this similarity measure is defined in the dkpro.similarity.algorithms.lexical-asl package
     // you need to add that to your .pom to make that example work
     // there are some examples that should work out of the box in dkpro.similarity.example-gpl 
     TextSimilarityMeasure measure2 = new WordNGramJaccardMeasure(3);    // Use word trigrams
     
     LexicalSemanticResource resource;
    try {
        // Get resource from local file
        resource = ResourceFactory.getInstance().get("wordnet", "en");
        
        
        /*
         *  Lexical relation can be:
         *        antonymy,
         *        synonymy 
         *          
         *  Semantic relation can be:
         *        holonymy,
         *        hypernymy,
         *        hyponymy,
         *        meronymy,
         *        cohyponymy,
         *        other 
         */
        // Put all relation into collection.
        // TODO: might specify some of them.
        Set<LexicalRelation> lexicalRelations = new HashSet<LexicalSemanticResource.LexicalRelation>(Arrays.asList(LexicalRelation.values()));
        Set<SemanticRelation> semanticRelations = new HashSet<LexicalSemanticResource.SemanticRelation>(Arrays.asList(SemanticRelation.values()));

        TextSimilarityMeasure measure = new LexSemRelationComparator(resource, lexicalRelations, semanticRelations);
        
        String[] tokens1 = "job".split(" ");   
        String[] tokens2 = "bike".split(" ");
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
     


     


        
        
        System.out.print("Hello world");
    }

}
