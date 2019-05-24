package net.etfbl.sanja.ids;

import java.io.IOException;
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
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class IDSFilter
 */
@WebFilter("/IDSFilter")
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
		
		String clientAddress = request.getRemoteAddr();
		
		Map<String, String[]> map = req.getParameterMap();
        Set<Entry<String, String[]>> set = map.entrySet();
        Iterator<Entry<String, String[]>> iterator = set.iterator();
        
        IDS ids = new IDS(map, clientAddress);
        Thread thread = new Thread(ids);
        //razliciti tredovi upisuju u isti fajl
        thread.start();

      /*      while(iterator.hasNext()){

                Map.Entry<String,String[]> entry = (Map.Entry<String,String[]>)iterator.next();

                String key = entry.getKey();
                String[] value = entry.getValue();
                System.out.println("KEY = " + key);
                
                
                for(int i = 0; i < value.length; i++) {
                	if(IDSManager.checkSQL(value[i])) {
                		System.out.println("DESIO SE NAPAD sa adrese: " + clientAddress);
                	}
                	System.out.println("**** VALUE = " + value[i]);
                }
            }*/
            
            HttpServletResponse res = (HttpServletResponse) response;
		
		//chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
