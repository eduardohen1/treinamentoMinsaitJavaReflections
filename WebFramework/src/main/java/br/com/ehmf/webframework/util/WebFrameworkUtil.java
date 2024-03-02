package br.com.ehmf.webframework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.ehmf.webframework.datastructures.MethodParam;

public class WebFrameworkUtil {
	
	
	/*
	 *      /retornavalor/{valor}
	 */
	
	public static MethodParam convertURI2MethodParam(String requestURI) { 
		Pattern pattern = Pattern.compile("([^/]+)(?:/([^/]+))?");
		Matcher matcher = pattern.matcher(requestURI);
		if(matcher.find()) {
			String method = matcher.group(1); //nome do metodo
			String param  = matcher.group(2); //parametro
			
			MethodParam methodParam = new MethodParam();
			methodParam.setMethod("/" + method);
			if(param != null)
				methodParam.setParam(param);
			return methodParam;
		}else {
			WebFrameworkLogger.log("WebFrameworkUtil", 
					"A URI não corresponde ao padrão esperado.");
		}
		return null;
	}
	
	public static Object convert2Type(String value, Class<?> type) {
		if(value == null) 
			value = "";
		
		if(type.isAssignableFrom(String.class)) {
			return value;
		}else if(type.isAssignableFrom(Integer.class) || type.getName().equals("int")) {
			if(isNumeric(value))
				return Integer.parseInt(value);
			return 0;
		}else if (type.isAssignableFrom(Double.class) || type.getName().equals("double")) {
			if(isNumeric(value))
				return Double.parseDouble(value);
			return 0.0;
		}
		return null;
	}

	/**
	 * Verifica se o parametro eh um valor numerico
	 * @param value valor a ser verificado
	 * @return retorna verdadeiro caso o parametro seja numerico
	 */
	private static boolean isNumeric(String value) {
		if(value == null)
			return false;
		try {
			Double.parseDouble(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
