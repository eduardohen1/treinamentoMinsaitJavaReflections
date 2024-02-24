package br.com.ehmf.webframework.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import com.google.gson.Gson;

import br.com.ehmf.webframework.datastructures.ControllerInstances;
import br.com.ehmf.webframework.datastructures.ControllerMap;
import br.com.ehmf.webframework.datastructures.RequestControllerData;
import br.com.ehmf.webframework.util.WebFrameworkLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class WebFrameworkDispatcherServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		//ignorar o favIcon:
		if(req.getRequestURL().toString().endsWith("/favicon.ico"))
			return;
		
		PrintWriter out = new PrintWriter(resp.getWriter());
		Gson gson = new Gson();
		
		String url = req.getRequestURI();
		String httpMethod = req.getMethod().toUpperCase(); //GET POST
		String key = httpMethod + url;
		
		//busco a informação da classe; método; parâmetros... da minha req
		RequestControllerData data = ControllerMap.values.get(key); 
		
		WebFrameworkLogger.log("WebFrameworkDispatcherServlet", "URL: " + url + "(" + httpMethod +
				") - Handler " + data.getControllerClass() + "." + data.getControllerMethod());
		
		//verificar se existe uma instancia da classe correspondente, caso não, criar uma
		Object controller;
		WebFrameworkLogger.log("WebFrameworkDispatcherServlet", "Procurar Instância da Controladora");
		try {
			controller = ControllerInstances.instace.get(data.controllerClass);
			if(controller == null) {
				WebFrameworkLogger.log("WebFrameworkDispatcherServlet", "Criar nova Instância da Controladora");
				controller = Class.forName(data.controllerClass).getDeclaredConstructor()
						.newInstance();
				ControllerInstances.instace.put(data.controllerClass, controller);
			}
			
			//Precisamos extrair o método desta classe - ou seja o método que vai atender
			//  a requisição. Vamos executar esse método e escrever a saída dele.
			Method controllerMethod = null;
			for(Method method : controller.getClass().getMethods()) {
				if(method.getName().equals(data.controllerMethod)) {
					controllerMethod = method;
					break;
				}
			}
			
			WebFrameworkLogger.log("WebFrameworkDispatcherServlet", 
					"Invocar o método " + controllerMethod.getName() + " para requisição");
			
			out.println(gson.toJson(controllerMethod.invoke(controller)));
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
