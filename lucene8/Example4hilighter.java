package example;

import java.io.File;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
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
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import qjt.chinesefile.ChineseFileReader;

public class Example4hilighter {
	String indexDir="D:/qjt/data/rdbgidx2";
	String filesDir = "D:/qjt/data/rdbg";
	Analyzer textAnalyzer; 

	public static void main(String[] args) {
		Example4hilighter t = new Example4hilighter();
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
		Query query=parser.parse("Ã«Ôó¶«");
		TopDocs docs=is.search(query, 10);
		ScoreDoc[] sdoc=docs.scoreDocs;
		Document doc;
		
		/* highlighter */
		Formatter formatter = new SimpleHTMLFormatter();
		QueryScorer scorer = new QueryScorer(query);
		Highlighter highlighter = new Highlighter(formatter, scorer);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 30);
		highlighter.setTextFragmenter(fragmenter);
		
		for(int i=0;i<sdoc.length;i++){
			doc = is.doc(sdoc[i].doc);
		    String name = doc.get("path");
		    System.out.println(name+" socre:"+sdoc[i].score);
		    
		    String text = doc.get("contents");
		    TokenStream stream = TokenSources.getAnyTokenStream(reader, sdoc[i].doc, "contents", textAnalyzer);
		    String[] frags = highlighter.getBestFragments(stream, text, 3);
            for (String frag : frags) {
                System.out.print(frag+"......");
            }
            System.out.println();
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
			doc.add(new TextField("contents", getContent(file), Store.YES));
			writer.addDocument(doc);
		}
	}
	private static String getContent(File f) throws Exception{
		ChineseFileReader cfr=new ChineseFileReader(f);
		StringBuffer sb=new StringBuffer();
		String line;
		while((line=cfr.readline())!=null){
			 sb.append(line.trim());
		}
		return sb.toString();
	}


}
