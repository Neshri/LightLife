package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ShaderReader {
	
	private String filePath;
	
	public ShaderReader(String filePath) {
		filePath += ".sh";
		this.filePath = filePath;
	}
	
	public String getCode() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String send = "";
		String str;
		while ((str = reader.readLine()) != null) {
			send += str;
		}
		reader.close();
		return send;
	}
	

}
