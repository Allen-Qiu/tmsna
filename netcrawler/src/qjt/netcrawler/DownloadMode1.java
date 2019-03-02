package qjt.netcrawler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.jsoup.Jsoup;
/**
 * download mode 1 : only download html file of seed url
 * */
public class DownloadMode1 implements Download{
	
	public void run(LinkedList<String> que){
		String url;
		int k = 1;
		
		while(!que.isEmpty()){
			url = que.removeFirst();
			get(url, k+".html");
			k++;
		}
	}
	private void get(String url, String fname){
		String html;
		Parameters param = Parameters.getInstance();
		String file = param.outdir+"/"+fname;
		PrintWriter pw;
		
		try {
			pw = new PrintWriter(new File(file));
			html = Jsoup.connect(url).get().html();
			System.out.println(url);
			pw.print(html);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
