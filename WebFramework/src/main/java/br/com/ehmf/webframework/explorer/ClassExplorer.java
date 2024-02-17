package br.com.ehmf.webframework.explorer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ClassExplorer {
	
	public static List<String> retrieveAllCalsses(Class<?> sourceClass){
		return packagExplorer(sourceClass.getPackageName());
	}

	private static List<String> packagExplorer(String packageName) {
		ArrayList<String> classNames = new ArrayList<String>();
		try {
			//dado a pasta onde tenho os pacotes do projeto com getResourceAsStream, defino raiz como package
			InputStream stream = ClassLoader.getSystemClassLoader()
					.getResourceAsStream(packageName.replaceAll("\\.", "/"));
			BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(stream));
			String line;
			while((line = reader.readLine()) != null) {
				if(line.endsWith(".class")) {
					String className = packageName + "." + 
							line.substring(0, line.lastIndexOf(".class"));
					classNames.add(className);
				}else {
					//recursividade!!!
					classNames.addAll(packagExplorer(packageName + "." + line));
				}
			}
			return classNames;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
