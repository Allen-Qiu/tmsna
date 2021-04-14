package example;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
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

public class Example4en {
	String indexDir="D:/qjt/data/reuter R8/index/";
	String fileDir = "D:/qjt/data/reuter R8/fset";

	public static void main(String[] args) {
		Example4en t = new Example4en();
		try {
//			t.buildIndex();
			t.search();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void buildIndex() throws Exception{
		Analyzer textAnalyzer=new SimpleAnalyzer();
		IndexWriterConfig conf = new IndexWriterConfig(textAnalyzer);
		conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		Directory dir = FSDirectory.open(Paths.get(indexDir));
		IndexWriter writer=new IndexWriter(dir,conf);
		
		File fdir = new File(fileDir);
		File flist[]=fdir.listFiles();
		
		for(File file : flist){
			Document doc=new Document();
			Field pathField = new StringField("path", file.getName(), Field.Store.YES);
			doc.add(pathField);
			doc.add(new TextField("contents", new FileReader(file)));
			writer.addDocument(doc);
		}
		writer.close();
	}
	private void search() throws Exception{
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)));
		IndexSearcher is=new IndexSearcher(reader); 
		Analyzer analyzer = new SimpleAnalyzer();
		QueryParser parser = new QueryParser("contents", analyzer);
		Query query=parser.parse("chinese AND company");
		TopDocs docs=is.search(query, 10);
		ScoreDoc[] sdoc=docs.scoreDocs;
		Document doc;
		
		for(int i=0;i<sdoc.length;i++){
			doc = is.doc(sdoc[i].doc);
		    String name = doc.get("path");
		    System.out.println(name+" socre:"+sdoc[i].score);
		}

	}
}
