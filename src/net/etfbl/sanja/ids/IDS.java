package net.etfbl.sanja.ids;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.etfbl.sanja.ids.LogMessage.AttackType;

import java.util.Map.Entry;

public class IDS implements Runnable {
	// private String clientAddress;
	// private Map<String, String[]> map;
	// private Set<Entry<String, String[]>> set;
	// private Iterator<Entry<String, String[]>> iterator;
	private HttpServletRequest request;

	// public IDS(Map<String, String[]> map, String clientAddress ) {
	// this.clientAddress = clientAddress;
	// this.map = map;
	// set = map.entrySet();
	// iterator = set.iterator();
	// }

	public IDS(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public void run() {
		String requestMethod = request.getMethod();
		if (requestMethod.equalsIgnoreCase("GET") || requestMethod.equalsIgnoreCase("POST")) {
			List<LogMessage> messages = new ArrayList<>();

			String clientAddress = request.getRemoteAddr();
			
			Iterator<Map.Entry<String, String[]>> iterator = null;
			for (iterator = request.getParameterMap().entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<String, String[]> parameterEntry = iterator.next();
				String parameterKey = parameterEntry.getKey();
				String[] parameterValues = parameterEntry.getValue();
				for (String parameter : parameterValues) {
					boolean sqliDetected = IDSManager.checkSQLI(parameter);
					boolean xssDetected = IDSManager.checkXSS(parameter);
					if (sqliDetected) {
						LogMessage logMessage = LogMessage.builder()
								.timestamp(System.currentTimeMillis())
								.ipAddress(clientAddress)
								.requestMethod(requestMethod)
								.attackType(AttackType.SQLI)
								.data("PARAMS: " + parameterKey + " = " + parameter)
								.build();
						messages.add(logMessage);
					}
					if (xssDetected) {
						LogMessage logMessage = LogMessage.builder()
								.timestamp(System.currentTimeMillis())
								.ipAddress(clientAddress)
								.requestMethod(requestMethod)
								.attackType(AttackType.XSS)
								.data("PARAMS: " + parameterKey + " = " + parameter)
								.build();
						messages.add(logMessage);
					}
				}
			}

			if (request.getCookies() != null) {
				for (Cookie cookie : request.getCookies()) {
					String cookieName = cookie.getName();
					String cookieValue = cookie.getValue();
					boolean sqliDetected = IDSManager.checkSQLI(cookieValue);
					boolean xssDetected = IDSManager.checkXSS(cookieValue);
					if (sqliDetected) {
						LogMessage logMessage = LogMessage.builder()
								.timestamp(System.currentTimeMillis())
								.ipAddress(clientAddress)
								.requestMethod(requestMethod)
								.attackType(AttackType.SQLI)
								.data("COOKIE: " + cookieName + " = " + cookieValue)
								.build();
						messages.add(logMessage);
					}
					if (xssDetected) {
						LogMessage logMessage = LogMessage.builder()
								.timestamp(System.currentTimeMillis())
								.ipAddress(clientAddress)
								.requestMethod(requestMethod)
								.attackType(AttackType.XSS)
								.data("COOKIE: " + cookieName + " = " + cookieValue)
								.build();
						messages.add(logMessage);
					}
				}
			}

			LogManager.multipleLog(messages);
		}

		// while(iterator.hasNext()){
		//
		// Map.Entry<String,String[]> entry =
		// (Map.Entry<String,String[]>)iterator.next();
		//
		// String key = entry.getKey();
		// String[] value = entry.getValue();
		//
		// System.out.println("-----> KEY = " + key);
		//
		// for(int i = 0; i < value.length; i++) {
		// if(IDSManager.checkSQL(value[i])) {
		// System.out.println("DESIO SE NAPAD sa adrese: " + clientAddress);
		// }
		// System.out.println("-----> VALUE = " + value[i]);
		// }
		// }

	}

}
