package br.com.ehmf.webframework.web;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import br.com.ehmf.webframework.annotations.WebframeworkGetMethod;
import br.com.ehmf.webframework.annotations.WebframeworkPostMethod;
import br.com.ehmf.webframework.datastructures.ControllerMap;
import br.com.ehmf.webframework.datastructures.MethodParam;
import br.com.ehmf.webframework.datastructures.RequestControllerData;
import br.com.ehmf.webframework.datastructures.ServiceImplementationMap;
import br.com.ehmf.webframework.explorer.ClassExplorer;
import br.com.ehmf.webframework.util.WebFrameworkLogger;
import br.com.ehmf.webframework.util.WebFrameworkUtil;

public class WebFrameworkWebApplication {
	
	public static void run(Class<?> sourceClass) {
		//desligar todos os logs do apache tomcat
		java.util.logging.Logger.getLogger("org.apache")
			.setLevel(java.util.logging.Level.OFF);
		long ini, fim;
		
		WebFrameworkLogger.showBanner();
		
		try {
			
			//class explorer
			//começar a criar um método de extração de metadados:
			/*
			List<String> allClasses = ClassExplorer.retrieveAllCalsses(sourceClass);
			allClasses.forEach(p -> {
				WebFrameworkLogger.log("Class Explorer", "Class found: " + p);
			});*/
			extractMetadata(sourceClass);
			
			ini = System.currentTimeMillis();
			
			WebFrameworkLogger.log("Embeded Web Container", "Iniciando WebframeworkWebApplication");
			Tomcat tomcat = new Tomcat();
			Connector connector = new Connector();
			connector.setPort(8080);
			tomcat.setConnector(connector);
			WebFrameworkLogger.log("Embeded Web Container", "Iniciando na porta 8080");
			
			//contexto olhando a raiz da aplicação:
			
			//procurando classes na raiz da app
			Context context = tomcat.addContext("", new File(".").getAbsolutePath());
			Tomcat.addServlet(context, "WebFrameworkDispatcherServlet", new WebFrameworkDispatcherServlet());
			
			//tudo que digitar na URL vai cair neste ponto:
			context.addServletMappingDecoded("/*", "WebFrameworkDispatcherServlet");
			
			fim = System.currentTimeMillis();
			WebFrameworkLogger.log("Embeded Web Container", "Tomcat iniciado em "
					+ (double)((fim - ini)) +
					"ms");
			
			//start:
			tomcat.start();
			tomcat.getServer().await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	private static void extractMetadata(Class<?> sourceClass) {
		try {
			List<String> allClasses = ClassExplorer.retrieveAllCalsses(sourceClass);
			for(String classe : allClasses) {
				//recuperar as anotações da classe
				Annotation annotations[] = Class.forName(classe).getAnnotations();
				for (Annotation classAnnotation : annotations) {
					if(classAnnotation.annotationType().getName()
							.equals("br.com.ehmf.webframework.annotations.WebframeworkController")) {
						WebFrameworkLogger.log("Metadata Explorer", "Found a Controller: " + classe);
						extractMethods(classe);
					}else if(classAnnotation.annotationType().getName()
							.equals("br.com.ehmf.webframework.annotations.WebframeworkService")) {
						WebFrameworkLogger.log("Metadata Explorer", "Found a Service Implementation: " + classe);
						for(Class<?> interfaceWeb : Class.forName(classe).getInterfaces()) {
							WebFrameworkLogger.log("Metadata Explorer", "     Class implements" + interfaceWeb.getName());
							ServiceImplementationMap.implementations.put(interfaceWeb.getName(), classe);
						}
					}
				}
			}
			for(RequestControllerData item : ControllerMap.values.values()) {
				WebFrameworkLogger.log("", "     " + item.getHttpMethod() + ":" + item.getUrl() +
											" [" + item.getControllerClass() + "." + item.getControllerMethod() + "]" + 
											(item.getParameter().length() > 0 ? " - Expected parameter " + item.getParameter() : "")
						);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void extractMethods(String className) throws Exception {
		String httpMethod = "";
		String path = "";
		String parameter = "";
		
		//recuperar todos os métodos da classe
		for(Method method : Class.forName(className).getDeclaredMethods()) {
			parameter = "";
			//WebFrameworkLogger.log(" - ", method.getName());
			for(Annotation annotation : method.getAnnotations()) {
				if(annotation.annotationType().getName()
						.equals("br.com.ehmf.webframework.annotations.WebframeworkGetMethod")) {
					httpMethod = "GET";
					path = ((WebframeworkGetMethod)annotation).value();
					
					//verificar se existe parâmetro no path.
					MethodParam methodParam = WebFrameworkUtil.convertURI2MethodParam(path);
					if(methodParam != null) {
						path = methodParam.getMethod();
						if(methodParam.getParam() != null)
							parameter = methodParam.getParam(); 
					}
					
				}else if(annotation.annotationType().getName()
						.equals("br.com.ehmf.webframework.annotations.WebframeworkPostMethod")) {
					httpMethod = "POST";
					path = ((WebframeworkPostMethod)annotation).value();
					
					//verificar se existe parâmetro no path.
					MethodParam methodParam = WebFrameworkUtil.convertURI2MethodParam(path);
					if(methodParam != null) {
						path = methodParam.getMethod();
						if(methodParam.getParam() != null)
							parameter = methodParam.getParam(); 
					}
				}
			}
			//WebFrameworkLogger.log(" - CHAVE: ", httpMethod + path);
			RequestControllerData getData = 
					new RequestControllerData(httpMethod, path, className, method.getName(),
							parameter);
			ControllerMap.values.put(httpMethod + path, getData); 
		}
		
	}
	

}
