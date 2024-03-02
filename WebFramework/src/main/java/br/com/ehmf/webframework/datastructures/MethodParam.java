package br.com.ehmf.webframework.datastructures;

public class MethodParam {
	
	private String method;
	private String param;

	public MethodParam() {}
	public MethodParam(String method, String param) {
		this.method = method;
		this.param  = param;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	
	
	
}
