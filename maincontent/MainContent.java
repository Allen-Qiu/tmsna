package qjt.wm;

public class MainContent {
	public static void main(String[] args) {
		String file = "1.txt";
//		String str = parser.Main.get(file);
		String  str = parser.Main.getChinese(file);
		System.out.println(str);

	}
}
