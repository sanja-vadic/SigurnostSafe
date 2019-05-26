package net.etfbl.sanja.ids;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;


public class IDSManager {
	
	public static boolean checkSQLI(String parameter) {
		boolean contains = false;
		String parameterLowerCase = parameter.toLowerCase();
		//treba ubaciti sa if-om
		if(parameterLowerCase.contains("select *") 
				|| parameterLowerCase.contains("select from") 
				|| parameterLowerCase.contains("union select") 
				|| parameterLowerCase.contains("order by") 
				|| parameterLowerCase.contains("group by") 
				|| parameterLowerCase.contains("delete from") 
				|| parameterLowerCase.contains("or 1=1") || parameterLowerCase.contains("or '1'='1'")
				|| parameterLowerCase.contains("drop database")
				|| parameterLowerCase.contains("drop table") 
				|| parameterLowerCase.contains("update")) {
			contains = true;
		}
		
		return contains;
	}
	
	public static boolean checkXSS(String parameter) {
		boolean contains = false;
		String parameterLowerCase = parameter.toLowerCase();
		if(parameterLowerCase.contains("<script>") 
				|| parameterLowerCase.contains("</script>") 
				|| parameterLowerCase.contains("eval"))
				 {
			contains = true;
		}
		
		return contains;
	}
	
	public static boolean checkParameterTampering(String name, String value, ServletContext context) {
		String relativeWebPath = "/WEB-INF/ids/parameter_tampering.properties";
		String absoluteDiskPath = context.getRealPath(relativeWebPath);
		Path propertiesPath = Paths.get(absoluteDiskPath); 
		
		Map<String,String> properties = getProperties(propertiesPath);
		
		String propertyRegex = properties.get(name);
		
		if(propertyRegex == null)
			return false;
		
		Pattern pattern = Pattern.compile(propertyRegex);
		Matcher matcher = pattern.matcher(value);
		
		if(matcher.matches()) {
			return false;
		}
		
		return true;
	}
	
	private static Map<String, String> getProperties(Path path) {
		Map<String, String> properties = new HashMap<>();
		try {
			Files.readAllLines(path)
				.forEach(line -> properties.put(line.split(":")[0], line.split(":")[1]));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	
}
