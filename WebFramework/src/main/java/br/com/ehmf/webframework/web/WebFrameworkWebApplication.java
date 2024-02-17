package br.com.ehmf.webframework.web;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import br.com.ehmf.webframework.util.WebFrameworkLogger;

public class WebFrameworkWebApplication {
	
	public static void run() {
		//desligar todos os logs do apache tomcat
		java.util.logging.Logger.getLogger("org.apache")
			.setLevel(java.util.logging.Level.OFF);
		long ini, fim;
		
		WebFrameworkLogger.showBanner();
		
		try {
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

}