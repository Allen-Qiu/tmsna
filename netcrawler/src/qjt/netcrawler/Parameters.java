package qjt.netcrawler;

import java.io.FileInputStream;
import java.util.Properties;

public class Parameters {
	private static Parameters instance = new Parameters();
	public String outdir;
	public int mode;
	public String limits;
	
	private Parameters(){
		Properties prop = new Properties();
		
        try {
			prop.load(new FileInputStream("parameters.properties"));
			outdir = prop.getProperty("outdir");
			mode = Integer.parseInt(prop.getProperty("mode"));
			limits = prop.getProperty("limits");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static Parameters getInstance(){
		return instance;
	}
}
