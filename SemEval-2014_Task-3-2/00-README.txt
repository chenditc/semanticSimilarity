================================================================================
                              SEMEVAL-2014 TASK 3
                   CLSS: Cross Level Semantic Similarity
         David Jurgens, Mohammad Taher Pilehvar, and Roberto Navigli
================================================================================
                http://alt.qcri.org/semeval2014/task3/
================================================================================


=============
THE TASK
=============

Semantic similarity is an essential component of many applications in Natural
Language Processing (NLP). This task provides an evaluation for semantic
similarity across different sizes of text, which we refer to as lexical
levels. Similar to the previous Semantic Textual Similarity (STS) tasks, given a
pair of lexical items, a system has to automatically measure the semantic
similarity. However unlike STS that focused on comparing similar-sized texts,
Task 3 evaluates the case where larger text must be compared to smaller text.
Furthermore, Task 3 includes the comparison of text directly with a non-textual
semantic representation.


==============
LEXICAL LEVELS
==============

Lexical levels denote different sizes of text.  This task involves four types of
comparisons between five different levels, from paragraphs to senses:

1- Paragraph to Sentence (paragraph2sentence)
2- Sentence to Phrase (sentence2phrase)
3- Phrase to Word (phrase2word)
4- Word to Sense (word2sense)


==================
PACKAGE CONTENTS
==================

The training package contains the following:

00-README.txt                                     This file
evaluation/task-3-scorer.jar                      Program for scoring the outputs
keys/trial/<level>.trial.gs.txt                   Gold standard trial data
keys/trial/example/<comparison>.trial.input.txt   Example input files for the trial data
keys/training/<comparison>.training.gs.txt        Gold standard training data
trial-data/<comparison>.trial.input.txt           Paired items in trial data for rating
training-data/<comparison>.train.input.txt        Paired items in training data for rating
licenses/00-README.txt                            Information on the training data licenses 
docs/SemEval-2014Task3AnnotationGuidelines.pdf    Annotation guidelines


=============
INPUT FORMAT
=============

The input files for the first three text-to-text levels consist of three
tab-separated fields:

larger_side <TAB> smaller_size <TAB> item-id

For the 4th level, word-to-sense, the input format is as follows:

Word <TAB> sense_key <TAB> sense_number <TAB> item-id

where sense_key is the WordNet 3.0 sense key for the corresponding sense
sense_number is of the form "word#pos#sn" (sn is the sense number according to
WordNet 3.0).  The sense_key and sense_number information are equivalent and
provided as a convenience for methods who prefer one notation over the other.


For all levels, the item-id field serves two purposes.  First, where applicable,
the id contains information on the genre from which the item’s pairs were drawn.
Participants are free to use this genre information to adjust their system's
behavior.  Second, the id helps for referencing specific entities.  If
participants have questions on specific items, we encourage using the ids in
their discussion when emailing the task’s mailing list.


=============
TRAINING DATA
=============

Task 3 provides training data for developing CLSS systems.  Training data
consists of approximately 500 items per level.  Items are distributed roughly
uniformly across the rating scale.

Training data has been drawn from a variety of genres.  These genres are
intended to provide a diverse, challenging set of texts for CLSS.  The Training
Data includes the following genres for each level:

 * Paragraph-to-sentence: Newswire, Travel, Reviews, Metaphoric text, and
                          Community Question Answering (CQA) site text
 * Sentence-to-phrase: Newswire, Travel, Idiomatic text, CQA text
 * Phrase-to-word: Newswire, Descriptions, Lexicographic text, Search, and
                   Idiomatic text

Word-to-sense data includes a mixture of nouns (50%), verbs (25%) and adjectives
(25%).  Data includes pairs with senses and lemmas both in WordNet, as well as
pairs where the lemma and/or sense is not in WordNet.


=============
GOLD STANDARD
=============

The corresponding gold standard files contain human-assigned scores for each
item pair (input and gold-standard files are line-aligned).  Semantic similarity
is rated according to the following scale (which is a summary of the annotation
instructions):

4 -- [Very Similar]
The two items have very similar meanings and the most important ideas, concepts,
or actions in the larger text are represented in the smaller text.  Some less
important information may be missing, but the smaller text is a very good
summary of the larger text.

3 -- [Somewhat Similar]
The two items share many of the same important ideas, concepts, or actions, but
include slightly different details.  The smaller text may use similar but not
identical concepts (e.g., car vs. vehicle), or may omit a few of the more
important ideas present in the larger text.

2 -- [Somewhat Related but NOT Similar]
The two items have dissimilar meaning, but shared concepts, ideas, and actions
that are related.  The smaller text may use related but not necessary similar
concepts (window vs. house) but should still share some overlapping concepts,
ideas, or actions with the larger text.

1 -- [Slightly Related]
The two items describe dissimilar concepts, ideas and actions, but may share
some small details or domain in common and might be likely to be found together
in a longer document on the same topic.

0 -- [Unrelated]
The two items do not mean the same thing and are not on the same topic.

For more information on the rating scale, please see the discussion group.
Annotation instructions with examples are also provided in the docs/ directory.


==============
OUTPUT FORMAT
==============

The output file is essentially similar to the gold standard. However, an
optional tab-separated column can be added in case confidence score for a pair
is available.

The input and output files *must* contain the same number of lines, where line X
in the output corresponds to the similarity of the pair on line X in the input
file.  Missing similarity scores are not allowed (i.e., every item must have a
rating).

For an example of the format, see the example system outputs with random answers
for the trial data in the keys/trial/example/ directory.


============
EVALUATION
============

Systems will be evaluated both (1) within comparison type and (2) across all
comparison types. Systems that participate only in a single comparison type will
be excluded from the all-comparison system ranking.  However, their inclusion in
the single-comparison type setting will enable us to identify any performance
gap between more general systems and specialized ones.

The system outputs and gold standard ratings will be compared in two ways, both
using Krippendorff's alpha. In the first comparison, systems are evaluated
according to the rating's value by calculating Krippendorff's alpha using the
interval level of measurement.  For the second, we recognize that some systems
may differ in their similarity measurements relative to human judges (e.g.,
scaling the ratings by a constant factor), but would still be able to rank the
pairs correctly.  Therefore, the second comparison will calculate Krippendorff's
alpha on the pair's rankings using the ordinal level of measurement.  By using
the same measure for both evaluations, we hope to provide more easily comparable
and interpretable scores, matching existing annotator studies.
  
To test the system outputs use the command: 

java -jar task-3-scorer.jar gold.key test.key

An example command line:
java -jar evaluation/task-3-scorer.jar \
  keys/trial/phrase2word.trial.gs.txt \
  keys/trial/example/phrase2word.trial.output.txt


=============
PARTICIPATION
=============

Please visit our page in order to participate in the task:

http://alt.qcri.org/semeval2014/task3/

We invite potential participants to join our Google group:

https://groups.google.com/group/semeval-2014-clss
