package net.etfbl.sanja.ids;

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

	
}
