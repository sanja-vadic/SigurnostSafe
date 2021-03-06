package net.etfbl.sanja.ids;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import net.etfbl.sanja.ids.LogMessage.AttackType;
import net.etfbl.sanja.model.Request;

import java.util.Map.Entry;

public class IDS implements Runnable {
	private Request request;

	public IDS(Request request) {
		this.request = request;
	}
	
	@Override
	public void run() {
		String requestMethod = request.getMethod();
		ServletContext servletContext = request.getServletContext();
		if (requestMethod.equalsIgnoreCase("GET") || requestMethod.equalsIgnoreCase("POST")) {
			List<LogMessage> messages = new ArrayList<>();

			String clientAddress = request.getIpAddress();
			if (request.getUrl().contains("EmptyLogServlet")) {
				try {
					String ip = request.getParameterMap().get("clientAddress")[0];
					String method = request.getParameterMap().get("requestMethod")[0];
					String type = request.getParameterMap().get("attackType")[0];
					LogMessage logMessage = logMessageBuilder(ip, method)
									.attackType(AttackType.valueOf(type.toUpperCase()))
									.data("")
									.build();
					messages.add(logMessage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			Iterator<Map.Entry<String, String[]>> iterator = null;
			for (iterator = request.getParameterMap().entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<String, String[]> parameterEntry = iterator.next();
				String parameterKey = new String(parameterEntry.getKey());
				String[] parameterValues = new String[parameterEntry.getValue().length];
				for(int i=0; i<parameterEntry.getValue().length; ++i) {
					parameterValues[i] = parameterEntry.getValue()[i];
				}
				for (String parameter : parameterValues) {
					boolean sqliDetected = IDSManager.checkSQLI(parameter);
					boolean xssDetected = IDSManager.checkXSS(parameter);
					boolean parameterTamperingDetected = IDSManager.checkParameterTampering(parameterKey, parameter, servletContext);
					boolean bufferOverflowDetected = IDSManager.checkBufferOverflow(parameterKey, parameter, servletContext);
					if (sqliDetected) {
						LogMessage logMessage = logMessageBuilder(clientAddress, requestMethod)
								.attackType(AttackType.SQLI)
								.data("PARAMS: " + parameterKey + " = " + parameter)
								.build();
						messages.add(logMessage);
					}
					if (xssDetected) {
						LogMessage logMessage = logMessageBuilder(clientAddress, requestMethod)
								.attackType(AttackType.XSS)
								.data("PARAMS: " + parameterKey + " = " + parameter)
								.build();
						messages.add(logMessage);
					}
					if (parameterTamperingDetected) {
						LogMessage logMessage = logMessageBuilder(clientAddress, requestMethod)
								.attackType(AttackType.PARAMETER_TAMPERING)
								.data("PARAMS: " + parameterKey + " = " + parameter)
								.build();
						messages.add(logMessage);
					}
					if (bufferOverflowDetected) {
						LogMessage logMessage = logMessageBuilder(clientAddress, requestMethod)
								.attackType(AttackType.BUFFER_OVERFLOW)
								.data("PARAMS: " + parameterKey + " = " + parameter)
								.build();
						messages.add(logMessage);
					}
				}
			}

			
			//ParameterTampering za Cookie
			if (request.getCookies() != null) {
				for (Cookie cookie : request.getCookies()) {
					String cookieName = cookie.getName();
					String cookieValue = cookie.getValue();
					System.out.println("Cookie name: " + cookieName);
					System.out.println("Cookie value: " + cookieValue);
					boolean sqliDetected = IDSManager.checkSQLI(cookieValue);
					boolean xssDetected = IDSManager.checkXSS(cookieValue);
					boolean parameterTamperingDetected = IDSManager.checkParameterTampering(cookieName, cookieValue, servletContext);
					boolean bufferOverflowDetected = IDSManager.checkBufferOverflow(cookieName, cookieValue, servletContext);
					if (sqliDetected) {
						LogMessage logMessage = logMessageBuilder(clientAddress, requestMethod)
								.attackType(AttackType.SQLI)
								.data("COOKIE: " + cookieName + " = " + cookieValue)
								.build();
						messages.add(logMessage);
					}
					if (xssDetected) {
						LogMessage logMessage = logMessageBuilder(clientAddress, requestMethod)
								.attackType(AttackType.XSS)
								.data("COOKIE: " + cookieName + " = " + cookieValue)
								.build();
						messages.add(logMessage);
					}
					if (parameterTamperingDetected) {
						LogMessage logMessage = logMessageBuilder(clientAddress, requestMethod)
								.attackType(AttackType.PARAMETER_TAMPERING)
								.data("COOKIE: " + cookieName + " = " + cookieValue)
								.build();
						messages.add(logMessage);
					}
					if (bufferOverflowDetected) {
						LogMessage logMessage = logMessageBuilder(clientAddress, requestMethod)
								.attackType(AttackType.BUFFER_OVERFLOW)
								.data("COOKIE: " + cookieName + " = " + cookieValue)
								.build();
						messages.add(logMessage);
					}
				}
			}

			LogManager.multipleLog(messages);
		}

	}
	
	private LogMessage.LogMessageBuilder logMessageBuilder(String clientAddress, String requestMethod) {
		return LogMessage.builder()
				.timestamp(System.currentTimeMillis())
				.ipAddress(clientAddress)
				.requestMethod(requestMethod);
	}

}
