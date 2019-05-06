package test;

import libsvm.classifier.svm_predict;
import libsvm.classifier.svm_scale;
import libsvm.classifier.svm_train;

public class Test {
	public static void main(String[] args){
		test();
	}
	public static void test (){
		String datafile="F:/data/svm/svmguide1-train.txt";// �����ļ�
		String testfile="F:/data/svm/svmguide1-test.txt";// �����ļ�
		String scalefile="scale.out";// �궨��������ļ�
		String modelfile="model.out";// ѵ����õ���ģ���ļ�
		String predfile="pred.out";// Ԥ��������ļ�
		String tScaleFile="tscale.out";// �궨��ĵĲ��Լ��ļ�
		String scaleparam="scaleparam.out";// �洢�궨�������ļ�

		// �궨
		String[] argDScale={"-l","0","-f",scalefile,"-s",scaleparam,datafile};
		String[] argTScale={"-l","0","-f",tScaleFile,"-r",scaleparam,testfile};
		// svm������ ����Ľ��������ǩ
		String[] argTrain={"-s","0","-t","2","-g","1",scalefile,modelfile};
		//String[] argTrain={"-s","0","-t","0","-c","0.3",scalefile,modelfile};
		String[] argPred={tScaleFile,modelfile,predfile};
		try{
			svm_scale.main(argDScale);
			svm_scale.main(argTScale);
			svm_train.main(argTrain);
			svm_predict.main(argPred);
		}catch(Exception e){
			e.printStackTrace();
		}}

}
