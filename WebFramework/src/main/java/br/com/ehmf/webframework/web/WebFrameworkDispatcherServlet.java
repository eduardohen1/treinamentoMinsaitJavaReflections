package br.com.ehmf.webframework.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.google.gson.Gson;

import br.com.ehmf.webframework.datastructures.ControllerInstances;
import br.com.ehmf.webframework.datastructures.ControllerMap;
import br.com.ehmf.webframework.datastructures.DependencyInjectionMap;
import br.com.ehmf.webframework.datastructures.MethodParam;
import br.com.ehmf.webframework.datastructures.RequestControllerData;
import br.com.ehmf.webframework.datastructures.ServiceImplementationMap;
import br.com.ehmf.webframework.util.WebFrameworkLogger;
import br.com.ehmf.webframework.util.WebFrameworkUtil;
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
		
		//String url = req.getRequestURI();
		MethodParam methodParam = WebFrameworkUtil.convertURI2MethodParam(req.getRequestURI());
		if(methodParam == null)
			return;
		String url = methodParam.getMethod();
		
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
				
				injectDependencies(controller);
				
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
			
			//meu método tem parâmetros???
			if(controllerMethod.getParameterCount() > 0) {
				WebFrameworkLogger.log("WebFrameworkDispatcherServlet", "Método " 
						+ controllerMethod.getName() + " tem parâmetros!");
				/*
				Object arg;
				Parameter parameter = controllerMethod.getParameters()[0];
				if(parameter.getAnnotations()[0].annotationType().getName()
						.equals("br.com.ehmf.webframework.annotations.WebframeworkBody")) {
					
					WebFrameworkLogger.log("", "     Procurando parâmetro da requisição do tipo " 
							+ parameter.getType().getName());
					String body = readBytesFromRequest(req);
					
					WebFrameworkLogger.log("", "     conteúdo do parâmetro: " 
							+ body);
					arg = gson.fromJson(body, parameter.getType());
					
					WebFrameworkLogger.log("WebFrameworkDispatcherServlet", 
							"Invocar o método " + controllerMethod.getName() +
							" com o parâmetro do tipo " + parameter.getType().toString() + 
							" para requisição");				
					out.println(gson.toJson(controllerMethod.invoke(controller, arg)));
				}*/
				Object arg;
				Parameter[] parameters = controllerMethod.getParameters();
				for(Parameter parameter : parameters) {
					for(Annotation annotation : parameter.getAnnotations()) {
						if(annotation.annotationType().getName()
								.equals("br.com.ehmf.webframework.annotations.WebframeworkBody")) {
							WebFrameworkLogger.log("", "     Procurando parâmetro da requisição do tipo " 
									+ parameter.getType().getName());
							String body = readBytesFromRequest(req);
							
							WebFrameworkLogger.log("", "     conteúdo do parâmetro: " 
									+ body);
							arg = gson.fromJson(body, parameter.getType());
							
							WebFrameworkLogger.log("WebFrameworkDispatcherServlet", 
									"Invocar o método " + controllerMethod.getName() +
									" com o parâmetro do tipo " + parameter.getType().toString() + 
									" para requisição");				
							out.println(gson.toJson(controllerMethod.invoke(controller, arg)));
						}else if(annotation.annotationType().getName()
								.equals("br.com.ehmf.webframework.annotations.WebframeworkPathVariable")) {
							WebFrameworkLogger.log("", "     Procurando parâmetro da requisição do tipo " 
									+ parameter.getType().getName());
							WebFrameworkLogger.log("", "     conteúdo do parâmetro: " 
									+ methodParam.getParam());
							
							arg = WebFrameworkUtil.convert2Type(methodParam.getParam(), parameter.getType());
							
							out.println(gson.toJson(controllerMethod.invoke(controller, arg)));
						}
					}
				}
			} else {
				WebFrameworkLogger.log("WebFrameworkDispatcherServlet", 
						"Invocar o método " + controllerMethod.getName() + " para requisição");				
				out.println(gson.toJson(controllerMethod.invoke(controller)));
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void injectDependencies(Object controller) throws Exception {
		//ver apenas os campos anotados por Inject
		for(Field attr : controller.getClass().getDeclaredFields()) {
			String attrTipo = attr.getType().getName();
			WebFrameworkLogger.log("WebFrameworkDispatcherServlet", "Injetar " + attr.getName() + " do tipo " + attrTipo);
			Object serviceImpl;
			if(DependencyInjectionMap.objects.get(attrTipo)== null) {
				//tem declaração da interface?
				String implType = ServiceImplementationMap.implementations.get(attrTipo);
				if(implType != null) {
					WebFrameworkLogger.log("WebFrameworkDispatcherServlet", "Procurar Instâncias de " + implType);
					serviceImpl = DependencyInjectionMap.objects.get(implType);
					if(serviceImpl == null) {
						WebFrameworkLogger.log("WebFrameworkDispatcherServlet", "Injetar novo objeto");
						serviceImpl = Class.forName(implType).getDeclaredConstructor()
								.newInstance();
						DependencyInjectionMap.objects.put(implType, serviceImpl);
					}
					//atribuir essa instancia ao atributo anotado - Injeção de dependência.
					attr.setAccessible(true);
					attr.set(controller, serviceImpl);
					WebFrameworkLogger.log("WebFrameworkDispatcherServlet", "Objeto injetado com sucesso!");
				}
			}
			
		}
	}

	private String readBytesFromRequest(HttpServletRequest req) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(req.getInputStream()));
		while((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}
		return stringBuilder.toString();
	}

}
