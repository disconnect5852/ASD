package asd.wasd.qd;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class teszt {

	public static void main(String[] args) {
		Path pth= Paths.get("teszt.xml");
		String result="asd";
		try {
			result= new String(Files.readAllBytes(pth), Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result);
		
		Pattern pattern=Pattern.compile("(?s)(?<=\\<taskId\\>)(.*?)(?=\\<\\/taskId\\>)");
		Matcher matcher = pattern.matcher(result);
		Set<String> varlist=new HashSet<String>();
		while (matcher.find()) {
			varlist.add(matcher.group(1));		
		}
		
		for (String string : varlist) {
			System.out.println(string);
		}
	}

}
