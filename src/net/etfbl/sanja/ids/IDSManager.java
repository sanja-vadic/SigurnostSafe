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
import lombok.val;

public class IDSManager {
	private static final String SQLI_REGEX = "(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT(INTO){0,1}|MERGE|SELECT|UPDATE|UNION(ALL){0,1})";
	private static final String XSS_REGEX = "(\\b)(on\\S+)(\\s*)=|javascript|(<\\s*)(\\/*)script";

	public static boolean checkSQLI(String parameter) {
		boolean contains = false;
		String parameterLowerCase = parameter.toLowerCase();
		// treba ubaciti sa if-om
		// nasla sam neki reqex \b(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT(INTO){0,1}|MERGE|SELECT|UPDATE|UNION(ALL){0,1})\b
		//\b(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT( +INTO){0,1}|MERGE|SELECT|UPDATE|UNION( +ALL){0,1})\b
		// '(''|[^'])*'
		
		//NIJE DOBRO jer ne pronadje npr ' or 1=1 #
		
		
		if (parameterLowerCase.contains("select *") || parameterLowerCase.contains("select from")
				|| parameterLowerCase.contains("select") 
				|| parameterLowerCase.contains("union select") || parameterLowerCase.contains("order by")
				|| parameterLowerCase.contains("insert into") 
				|| parameterLowerCase.contains("create") 
				|| parameterLowerCase.contains("group by") || parameterLowerCase.contains("delete from")
				|| parameterLowerCase.contains("or 1=1") || parameterLowerCase.contains("or '1'='1'")
				|| parameterLowerCase.contains("drop database") || parameterLowerCase.contains("drop table")
				|| parameterLowerCase.contains("update")) {
			contains = true;
		}
		
		
		
	/*	Pattern pattern = Pattern.compile(SQLI_REGEX);
		Matcher matcher = pattern.matcher(parameter);

		if (matcher.matches()) {
			contains = true;
		}*/

		return contains;
	}

	//(\b)(on\S+)(\s*)=|javascript|(<\s*)(\/*)script
	public static boolean checkXSS(String parameter) {
		boolean contains = false;
		
		/*Pattern pattern = Pattern.compile(XSS_REGEX);
		Matcher matcher = pattern.matcher(parameter);

		if (matcher.matches()) {
			contains = true;
		}*/
		
		String parameterLowerCase = parameter.toLowerCase();
		if (parameterLowerCase.contains("<script>") || parameterLowerCase.contains("</script>")
				|| parameterLowerCase.contains("eval")
				|| parameterLowerCase.contains("javascript:")
				|| parameterLowerCase.contains("onload")
				) {
			contains = true;
		}

		return contains;
	}

	public static boolean checkParameterTampering(String name, String value, ServletContext context) {
		String relativeWebPath = "/WEB-INF/ids/parameter_tampering.properties";
		String absoluteDiskPath = context.getRealPath(relativeWebPath);
		Path propertiesPath = Paths.get(absoluteDiskPath);

		Map<String, String> properties = getProperties(propertiesPath);

		String propertyRegex = properties.get(name);

		if (propertyRegex == null)
			return false;

		Pattern pattern = Pattern.compile(propertyRegex);
		Matcher matcher = pattern.matcher(value);

		if (matcher.matches()) {
			return false;
		}

		return true;
	}

	public static boolean checkBufferOverflow(String name, String value, ServletContext context) {
		String relativeWebPath = "/WEB-INF/ids/buffer_overflow.properties";
		String absoluteDiskPath = context.getRealPath(relativeWebPath);
		Path propertiesPath = Paths.get(absoluteDiskPath); 
		
		Map<String,String> properties = getProperties(propertiesPath);
		
		String propertyRule = properties.get(name);
		if (propertyRule == null || propertyRule.isEmpty()) {
			return false;
		}
		
		if(propertyRule.charAt(0) == '[') {
			//range
			propertyRule = propertyRule.replace("[", "").replace("]", "");
			String[] rangeValues = propertyRule.split("-");
			try {
				int valueAsNumber = Integer.parseInt(value);
				int lowestNumber = Integer.parseInt(rangeValues[0]);
				int highestNumber = Integer.parseInt(rangeValues[1]);
				
				if(valueAsNumber < lowestNumber || valueAsNumber > highestNumber) {
					return true;
				} 
				return false;
			}catch (Exception ex) {
				System.out.println("Parsing failed for value: " + value);
				return false;
			}
		} else if (propertyRule.charAt(0) == '{') {
			//specific number
			propertyRule = propertyRule.replace("{", "").replace("}", "");
			try {
				int numberOfCharacters = Integer.parseInt(propertyRule);
				
				if(value.length() != numberOfCharacters) {
					return true;
				} 
				return false;
			}catch (Exception ex) {
				System.out.println("Parsing failed for value: " + value);
				return false;
			}
		}
		
		return true;
	}

	private static Map<String, String> getProperties(Path path) {
		Map<String, String> properties = new HashMap<>();
		try {
			Files.readAllLines(path).forEach(line -> properties.put(line.split(":")[0], line.split(":")[1]));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

}
