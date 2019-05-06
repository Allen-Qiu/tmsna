package test;

import libsvm.classifier.svm_predict;
import libsvm.classifier.svm_scale;
import libsvm.classifier.svm_train;

public class Test {
	public static void main(String[] args){
		test();
	}
	public static void test (){
		String datafile="F:/data/svm/svmguide1-train.txt";// 数据文件
		String testfile="F:/data/svm/svmguide1-test.txt";// 测试文件
		String scalefile="scale.out";// 标定后的数据文件
		String modelfile="model.out";// 训练后得到的模型文件
		String predfile="pred.out";// 预测结果输出文件
		String tScaleFile="tscale.out";// 标定后的的测试集文件
		String scaleparam="scaleparam.out";// 存储标定参数的文件

		// 标定
		String[] argDScale={"-l","0","-f",scalefile,"-s",scaleparam,datafile};
		String[] argTScale={"-l","0","-f",tScaleFile,"-r",scaleparam,testfile};
		// svm参数， 输出的结果是类别标签
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
