/**
 * 
 */
package com.maoshen.component.other;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @description <br>
 * @author <a href="mailto:wei.jiang@lifesense.com">vakin</a>
 * @date 2015年11月13日
 * @Copyright (c) 2015, lifesense.com
 */
public class ResourceUtils {

	static Map<String, String> cache = new HashMap<String, String>();

	static List<Properties> properties = new ArrayList<Properties>();
	
	static boolean inited;

	private synchronized static void load() {
		try {
            if(!properties.isEmpty())return;
			File dir = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath());

			File[] propFiles = dir.listFiles(f -> f.getName().endsWith(".properties"));

			for (File file : propFiles) {
				Properties p = new Properties();
				p.load(new FileReader(file));
				properties.add(p);
			}
			inited = true;
		} catch (Exception e) {
			inited = true;
			throw new RuntimeException(e);
		}
	}

	public static String get(String key, String... defaults) {
		if(!inited)load();
		if (cache.containsKey(key)) {
			return cache.get(key);
		}

		String value = null;
		for (Properties prop : properties) {
			value = prop.getProperty(key);
			if (value != null) {
				cache.put(key, value);
				return value;
			}
		}
		
		value = System.getProperty(key);
		if(value != null){
			cache.put(key, value);
			return value;
		}

		if (defaults != null && defaults.length > 0) {
			return defaults[0];
		}
		
		return null;
	}

}