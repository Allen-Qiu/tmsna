package qjt.wm.ir;

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

public class Test2019Reuter {
	String indexDir="D:/qjt/lucene/reuteridx";
	String filesDir = "D:/qjt/data/reuter R8/fset";
	Analyzer textAnalyzer; 

	public static void main(String[] args) {
		Test2019Reuter t = new Test2019Reuter();
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
		textAnalyzer = new SimpleAnalyzer();
		QueryParser parser = new QueryParser("contents", textAnalyzer);
		Query query=parser.parse("british AND company");
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
		textAnalyzer = new SimpleAnalyzer();
		IndexWriterConfig conf = new IndexWriterConfig(textAnalyzer);
		conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
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
