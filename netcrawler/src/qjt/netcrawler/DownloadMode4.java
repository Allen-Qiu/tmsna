package qjt.netcrawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownloadMode4 implements Download{
	Parameters param;
	private HashSet<String> visited;
	private HashSet<String> downedImages;
	
	public DownloadMode4(){
		param = Parameters.getInstance();
		visited = new HashSet<String>();
		downedImages = new HashSet<String>();
	}
	
	@Override
	public void run(LinkedList<String> que){
		String url;

		while(!que.isEmpty()){
			url = que.removeFirst();
			if(visited.contains(url)) continue;
			visited.add(url);
			get(url, que);
		}
	}
	private void get(String url, LinkedList<String> que){
		Parameters param = Parameters.getInstance();
		
		try {
			Document doc = Jsoup.connect(url).get();
			getImage(doc, url);
			extractURL(doc, que, param);
//			System.out.println(url);
		} catch (Exception e) {
			System.out.println("Download "+url+" fail");
//			e.printStackTrace();
		}
	}
	private void extractURL(Document doc, 
							LinkedList<String> que, 
							Parameters param){
        Elements links = doc.select("a[href]");
        String url;
        
        for (Element link : links) {
        	url = link.attr("abs:href");
        	if(check(url, param)&&!visited.contains(url)&&!que.contains(url)){
				que.add(url);
			}
        }
	}
	/**
	 * 检查超链是否符合设定的规则（parameter.properties中的limits）
	 * */
	private boolean check(String url, Parameters param){
		String regx="https?://(.+?)/";
		Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(url);
		String s;
		
		if(m.find()){
			s=m.group(1);
			if(s.contains(param.limits)){
				return true;
			}
		}
		
		return false;
	}
	private void getImage(Document document, String url) throws Exception{
        Elements imageElements = document.select("img");
        
        for(Element imageElement : imageElements){
            
            //make sure to get the absolute URL using abs: prefix
            String strImageURL = imageElement.attr("abs:src");
            if(downedImages.contains(strImageURL)) continue;
            downloadImage(strImageURL);
            downedImages.add(strImageURL);
        }
	}
	private void downloadImage(String strImageURL){
        String strImageName = 
                strImageURL.substring( strImageURL.lastIndexOf("/") + 1 );
        
        System.out.println("Downloading: " + strImageURL);
        
        try {
            
            //open the stream from URL
            URL urlImage = new URL(strImageURL);
            InputStream in = urlImage.openStream();
            
            byte[] buffer = new byte[4096];
            int n = -1;
            
            OutputStream os = 
                new FileOutputStream(param.outdir + "/" + strImageName );
            
            while ( (n = in.read(buffer)) != -1 ){
                os.write(buffer, 0, n);
            }
            
            os.close();
        } catch (IOException e) {
//            e.printStackTrace();
        }
        
    }
	public static void main(String[] args){
		String url = "http://it.swufe.edu.cn//2015-04/28/2015042810433114301890111.jpg";
		DownloadMode4 d = new DownloadMode4();
		d.downloadImage(url);
	}
}
