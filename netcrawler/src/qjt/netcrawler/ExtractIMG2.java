package qjt.netcrawler;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractIMG2 {

	public static void main(String[] args) {
		String url="http://www.swufe.edu.cn";
		run(url);
	}
	private static void run(String url){
		String imgurl;
		
		try{
			Document doc = Jsoup.connect(url).get();
			Elements links = doc.select("img[src]");
			
	        for (Element link : links) {
	        	imgurl = link.attr("abs:src");
	        	System.out.println(imgurl);
	        	save(imgurl);
	        }
		}catch(Exception e){
        	e.printStackTrace();
		}
	}
	/**
	 * urlStrig是图片的url地址，fname是要保存在本地的文件名
	 * */
	private static void save(String urlString) throws Exception{
		URL url = new URL(urlString);       
        InputStream in = url.openStream( ); 
        String fname;
        String[] str=urlString.split("/");
        fname="d:/temp/"+str[str.length-1];
        
        RandomAccessFile rout=new RandomAccessFile(fname,"rw");
        int bytes_read;
        byte[] buffer=new byte[4096];
		
		while((bytes_read = in.read(buffer)) != -1){
			rout.write(buffer,0,bytes_read);
		}
		rout.close();
	}
}
