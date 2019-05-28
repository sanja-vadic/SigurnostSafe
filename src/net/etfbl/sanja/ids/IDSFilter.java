package net.etfbl.sanja.ids;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import net.etfbl.sanja.model.Request;

/**
 * Servlet Filter implementation class IDSFilter
 */
@WebFilter(filterName = "/IDSFilter", urlPatterns = "/*")
public class IDSFilter implements Filter {

    /**
     * Default constructor. 
     */
    public IDSFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		System.out.println("IDS Filter: " + req.getRequestURL().toString());
		
		Request requestModel = new Request(req);
		IDS ids = new IDS(requestModel);
		Thread thread = new Thread(ids);
		thread.start();
		
		chain.doFilter(request, response);
		
	}
	

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
