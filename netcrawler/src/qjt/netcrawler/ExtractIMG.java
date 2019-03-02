package qjt.netcrawler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从网页中下载图片
 * */
public class ExtractIMG {

	public static void main(String[] args) {
		ExtractIMG e=new ExtractIMG();
		String urlString="http://www.swufe.edu.cn";
		try {
			e.run(urlString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	public void run(String urlString) throws Exception{
		URL url = new URL(urlString);       // Create the URL
        InputStream in = url.openStream( );        		// Open a stream to it
        String contents=getContent(in);
        Vector<String> vec=extractIMGURL(contents);
        
        for(int i=0;i<vec.size();i++){
        	System.out.println(vec.get(i));
        	saveIMG(vec.get(i));
        }
//        System.out.println(contents);
	}
	private String getContent(InputStream in)throws IOException{
		int bytes_read;
		byte[] buffer=new byte[4096];
		ByteArrayOutputStream ba=new ByteArrayOutputStream();
		StringBuilder sb=new StringBuilder();
		
		while((bytes_read = in.read(buffer)) != -1){
        	ba.reset();
        	ba.write(buffer);
        	sb.append(ba.toString());
		}
		return sb.toString();
	}
	private Vector<String> extractIMGURL(String str){
		String prefix="http://www.swufe.edu.cn/";
		String regx="<img src=\"?(.+?\\.(jpg|jpeg|bmp|png))\"?";
		Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Matcher m;
		String s;
		Integer location=0;
		Vector<String> res=new Vector<String>();
		
		while(location<str.length()){
			m = p.matcher(str);
			if(m.find(location)){
				s=m.group(1);
				res.add(prefix+s);
				location=m.end();
			}else{
				break;
			}
		}
		return res;
	}
	/**
	 * urlStrig是图片的url地址，fname是要保存在本地的文件名
	 * */
	private void saveIMG(String urlString) throws Exception{
		URL url = new URL(urlString);       
        InputStream in = url.openStream( ); 
        String fname;
        String regx="([^/]+\\.(jpg|jpeg|bmp|png))";
        Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher m;
        String s;
        m = p.matcher(urlString);
        if(m.find()){
        	fname="d:/"+m.group(1);
        }else{
        	return;
        }

        
        RandomAccessFile rout=new RandomAccessFile(fname,"rw");
        int bytes_read;
        byte[] buffer=new byte[4096];
		
		while((bytes_read = in.read(buffer)) != -1){
			rout.write(buffer,0,bytes_read);
		}
		rout.close();
	}
}









