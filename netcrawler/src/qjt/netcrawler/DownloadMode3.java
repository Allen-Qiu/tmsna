package qjt.netcrawler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 下载种子网页中的图片
 * */
public class DownloadMode3 implements Download{
	Parameters param;
	
	public void run(LinkedList<String> que){
		param = Parameters.getInstance();
		String url;
		int k = 1;
		try{
			while(!que.isEmpty()){
				url = que.removeFirst();
				get(url, k+".html");
				k++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void get(String url, String fname) throws Exception{
		Document document = Jsoup
                .connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10 * 1000)
                .get();
        
        Elements imageElements = document.select("img");
        
        for(Element imageElement : imageElements){
            
            //make sure to get the absolute URL using abs: prefix
            String strImageURL = imageElement.attr("abs:src");
            downloadImage(strImageURL);
            
        }
	}
	private void downloadImage(String strImageURL){
        String strImageName = 
                strImageURL.substring( strImageURL.lastIndexOf("/") + 1 );
        
        System.out.println("Saving: " + strImageName + ", from: " + strImageURL);
        
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
            e.printStackTrace();
        }
        
    }
}
