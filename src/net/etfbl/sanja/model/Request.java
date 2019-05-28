package net.etfbl.sanja.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Request {
	private Map<String, String[]> parameterMap;
	private List<Cookie> cookies;
	private String ipAddress;
	private String method;
	private ServletContext servletContext;
	private String url;
	private String body;

	public Request(HttpServletRequest req) {
		this.ipAddress = req.getRemoteAddr();
		this.method = req.getMethod();
		this.servletContext = req.getServletContext();
		this.url = req.getRequestURL().toString();
		parameterMap = new HashMap<>();
		for (Map.Entry<String, String[]> entry : req.getParameterMap().entrySet()) {
			this.parameterMap.put(entry.getKey(), entry.getValue());
		}
		cookies = new ArrayList<>();
		if (req.getCookies() != null) {
			for (Cookie cookie : req.getCookies()) {
				this.cookies.add(cookie);
			}
		}
		try {
			this.body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
