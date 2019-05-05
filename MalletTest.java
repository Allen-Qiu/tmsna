package classification;

import java.util.BitSet;
import java.util.Iterator;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.FeatureSelectingClassifierTrainer;
import cc.mallet.classify.NaiveBayes;
import cc.mallet.classify.NaiveBayesTrainer;
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
import cc.mallet.types.Alphabet;
import cc.mallet.types.ExpGain;
import cc.mallet.types.FeatureCounts;
import cc.mallet.types.FeatureSelection;
import cc.mallet.types.FeatureSelector;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.GradientGain;
import cc.mallet.types.InfoGain;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.KLGain;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.LabelVector;

/**
 * 考察怎样使用mallet进行文档分类
 * */
public class MalletTest {
	public static void main(String[] args) {
		int num=2000;				// 特征选择时选择的特征数
		String trainfile="d:/qjt/data/reuter R8/r8-train-no-stop-id.txt";
		String testfile="d:/qjt/data/reuter R8/r8-test-no-stop-id.txt";
//		String trainfile="c:/qjt/data/reuter R8/1.txt";
//		String testfile="c:/qjt/data/reuter R8/1-test.txt";
		Pipe instancePipe = new SerialPipes (new Pipe[] {
				new Target2Label (),							  // Target String -> class label
				new Input2CharSequence (),				  // Data File -> String containing contents
				new CharSequence2TokenSequence (),  // Data String -> TokenSequence
				new TokenSequenceLowercase (),		  // TokenSequence words lowercased
				new TokenSequenceRemoveStopwords (),// Remove stopwords from sequence
				new TokenSequence2FeatureSequence(),// Replace each Token with a feature index
				new FeatureSequence2FeatureVector(),// Collapse word order into a "feature vector"
			});
		InstanceList trainList = new InstanceList (instancePipe);
		InstanceList testList = new InstanceList (instancePipe);
		try{
			trainList.addThruPipe(new CsvIterator(trainfile,"(\\w+)\\s+([\\w-]+)\\s+(.*)", 3, 2, 1));

//			FeatureSelector fselector=new FeatureSelector(new FeatureCounts.Factory(),num);
			FeatureSelector fselector=new FeatureSelector(new InfoGain.Factory(),num);
			
			fselector.selectFeaturesFor(trainList);
			
			testList.addThruPipe(new CsvIterator(testfile,"(\\w+)\\s+([\\w-]+)\\s+(.*)", 3, 2, 1));
			ClassifierTrainer<NaiveBayes> naiveBayesTrainer = new NaiveBayesTrainer ();
			FeatureVector f,d;
			int len=trainList.size();
			Instance in;
			for(int i=0;i<len;i++){
				in=trainList.get(i);
				f=(FeatureVector)in.getData();
				d=FeatureVector.newFeatureVector(f, f.getAlphabet(), trainList.getFeatureSelection());
				in.unLock();
				in.setData(d);
				in.lock();
			}
			Classifier classifier=naiveBayesTrainer.train(trainList);
			System.out.println ("The testing accuracy is "+ classifier.getAccuracy(testList));
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
