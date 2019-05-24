package net.etfbl.sanja.ids;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class IDS implements Runnable{
	private String clientAddress;
	private Map<String, String[]> map;
	private Set<Entry<String, String[]>> set;
	private Iterator<Entry<String, String[]>> iterator;
	
	public IDS(Map<String, String[]> map, String clientAddress ) {
		this.clientAddress = clientAddress;
		this.map = map;
		set = map.entrySet();
		iterator = set.iterator();
	}

	@Override
	public void run() {
		 while(iterator.hasNext()){

             Map.Entry<String,String[]> entry = (Map.Entry<String,String[]>)iterator.next();

             String key = entry.getKey();
             String[] value = entry.getValue();
             
             System.out.println("-----> KEY = " + key);
             
             for(int i = 0; i < value.length; i++) {
             	if(IDSManager.checkSQL(value[i])) {
             		System.out.println("DESIO SE NAPAD sa adrese: " + clientAddress);
             	}
             	System.out.println("-----> VALUE = " + value[i]);
             }
         }
		
	}
	

}
