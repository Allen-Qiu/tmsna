package lda;

import java.io.File;
import java.io.FileNotFoundException;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.SimpleLDA;
import cc.mallet.types.InstanceList;

public class LDATest {
	String file="www2016.txt";
	int numTopics=10;
	
	public static void main(String[] args) {
		LDATest lda=new LDATest();
		try {
			lda.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void run() throws Exception{
		Pipe instancePipe = new SerialPipes (new Pipe[] {
				new Target2Label (),							  // Target String -> class label
				new Input2CharSequence (),				  // Data File -> String containing contents
				new CharSequence2TokenSequence (),  // Data String -> TokenSequence
				new TokenSequenceLowercase (),		  			  // TokenSequence words lowercased
				new TokenSequenceRemoveStopwords (),			  // Remove stopwords from sequence
				new TokenSequence2FeatureSequence(),// Replace each Token with a feature index
//				new FeatureSequence2FeatureVector(),// Collapse word order into a "feature vector"
			});
		InstanceList trainList = new InstanceList (instancePipe);
		try {
			trainList.addThruPipe(new CsvIterator(file,"(\\w+)\\s+([\\w-]+)\\s+(.*)", 3, 2, 1));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		SimpleLDA lda = new SimpleLDA (numTopics, 50.0, 0.01);
		lda.addInstances(trainList);
		lda.sample(1000);
	}

}
