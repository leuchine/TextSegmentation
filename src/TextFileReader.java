import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.lang.System;

class TextFileReader {
	
	public TextFileReader() {
		
	}
	
	public String readDocument(String filePath) throws IOException {
		String stringContent = new String();
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			System.out.println(line);
			while(line != null) {
				sb.append(line);
				if(!line.equals("")) {
					sb.append(" ");
				}
				line = br.readLine();
				System.out.println(line);
			}
			
			stringContent = sb.toString();
		} catch(Exception e) {
			
		} finally {
			br.close();
		}
		
		return  stringContent.toLowerCase();
	}
}
