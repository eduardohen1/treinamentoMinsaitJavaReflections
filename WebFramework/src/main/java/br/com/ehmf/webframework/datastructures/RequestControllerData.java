package br.com.ehmf.webframework.datastructures;

public class RequestControllerData {
	
	public String httpMethod;
	public String url;
	public String controllerClass;
	public String controllerMethod;
	public String parameter;
	
	public RequestControllerData() {}

	public RequestControllerData(String httpMethod, String url, String controllerClass, String controllerMethod) {
		this.httpMethod = httpMethod;
		this.url = url;
		this.controllerClass = controllerClass;
		this.controllerMethod = controllerMethod;
		this.parameter = "";
	}
	
	public RequestControllerData(String httpMethod, String url, String controllerClass, 
			String controllerMethod, String parameter) {
		this.httpMethod = httpMethod;
		this.url = url;
		this.controllerClass = controllerClass;
		this.controllerMethod = controllerMethod;
		this.parameter = parameter;
	}
	
	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getControllerClass() {
		return controllerClass;
	}

	public void setControllerClass(String controllerClass) {
		this.controllerClass = controllerClass;
	}

	public String getControllerMethod() {
		return controllerMethod;
	}

	public void setControllerMethod(String controllerMethod) {
		this.controllerMethod = controllerMethod;
	}
	
	
	
	
}
