package example;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Example4cn {
	String indexDir="D:/qjt/data/rdbgidx";
	String filesDir = "D:/qjt/data/rdbg";
	Analyzer textAnalyzer; 

	public static void main(String[] args) {
		Example4cn t = new Example4cn();
		try {
//			t.buildIndex();
			t.searchIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void searchIndex() throws Exception{
		IndexReader reader = DirectoryReader.open(
				FSDirectory.open(Paths.get(indexDir)));
		IndexSearcher is=new IndexSearcher(reader); 
		textAnalyzer = new SmartChineseAnalyzer();
		QueryParser parser = new QueryParser("contents", textAnalyzer);
		Query query=parser.parse("Ï°½üÆ½");
		TopDocs docs=is.search(query, 10);
		ScoreDoc[] sdoc=docs.scoreDocs;
		Document doc;
		
		for(int i=0;i<sdoc.length;i++){
			doc = is.doc(sdoc[i].doc);
		    String name = doc.get("path");
		    System.out.println(name+" socre:"+sdoc[i].score);
		}
	}
	private void buildIndex() throws Exception{
		textAnalyzer = new SmartChineseAnalyzer();
		IndexWriterConfig conf = new IndexWriterConfig(textAnalyzer);
		conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		Directory dir = FSDirectory.open(Paths.get(indexDir));
		IndexWriter writer=new IndexWriter(dir,conf);
		addDoc(writer);
		writer.close();
	}
	private void addDoc(IndexWriter writer) throws Exception{
		File fdir = new File(filesDir);
		String[] flist = fdir.list();
		Document doc;
		File file;
		
		for(int i=0;i<flist.length;i++){
			System.out.println(flist[i]);
			file = new File(filesDir+"/"+flist[i]);
			doc = new Document();
			Field pathField = new StringField("path", file.getName(), 
					Field.Store.YES);
			doc.add(pathField);
			doc.add(new TextField("contents", new FileReader(file)));
			writer.addDocument(doc);
		}
	}

}
