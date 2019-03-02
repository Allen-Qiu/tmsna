package qjt.netcrawler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * download mode 2 : download html file starting from seed url
 * */
public class DownloadMode2 implements Download{
	private HashSet<String> visited;
	@Override
	public void run(LinkedList<String> que){
		String url;
		int k = 1;
		visited = new HashSet<String>();
		
		while(!que.isEmpty()){
			url = que.removeFirst();
			if(visited.contains(url)) continue;
			visited.add(url);
			get(url, k+".html", que);
			k++;
		}
	}
	private void get(String url, String fname, LinkedList<String> que){
		String html;
		Parameters param = Parameters.getInstance();
		String file = param.outdir+"/"+fname;
		PrintWriter pw;
		
		try {
			Document doc = Jsoup.connect(url).get();
			pw = new PrintWriter(new File(file));
			html = doc.html();
			pw.println("<url>"+url+"</url>");
			pw.print(html);
			pw.close();
			
			extractURL(doc, que, param);
		} catch (IOException e) {
			System.out.println("Download "+url+" fail");
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
}
