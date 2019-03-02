package qjt.netcrawler;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

/**
 * 主界面
 * */
public class Main {
	
	public static void run(){
		RandomAccessFile raf;
		LinkedList<String> que = new LinkedList<String>();
		String line;
		Download down;
		Parameters param = Parameters.getInstance();
		
		try {
			/* 读取种子url */
			raf = new RandomAccessFile("seed.txt","r");
			while((line=raf.readLine())!=null){
				if(line.startsWith("//")) continue;
				que.add(line.trim());
			}
			
			/* download */
			switch(param.mode){
				case 1: down = new DownloadMode1(); break;
				case 2: down = new DownloadMode2(); break;
				case 3: down = new DownloadMode3(); break;
				case 4: down = new DownloadMode4(); break;
				default:
					down = null;
			}
			
			if(down != null){
				down.run(que);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
