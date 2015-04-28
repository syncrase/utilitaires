package utilitaires.printing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import utilitaires.GlobalText;

public class InFile {

	/**
	 * This method write in file and can also clear his content if nothing is writed and append equals false
	 * 
	 * @param stringToWriteInFile
	 *            What you want to write in your file
	 * @param path
	 *            Complete path (<path>/<filename>.<extension>) of your file in which you want to write<br/>
	 *            If there's no indication, the path is relative to the project folder which call this method
	 */
	public static void writeString(String stringToWriteInFile, String path, boolean append) {

		// references : http://www.tutorialspoint.com/java/java_date_time.htm

		stringToWriteInFile = utilitaires.formating.forhumans.Html.indent1(stringToWriteInFile);

		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(path, append));
			out.write("*********************** :-)  " + new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz").format(new Date())
					+ "  (-: ***********************" + GlobalText.LINE_FEED + stringToWriteInFile + GlobalText.LINE_FEED);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

//	public static void test(){
//		
//		Test2 t = new Test2(){
//			str="",i=1
//		};
//	}
}

class Test2 {
	String str;
	int i;
	public Test2(String str, int i) {
		super();
		this.str = str;
		this.i = i;
	}
	
}
