package br.com.ehmf.mywebtestframework.controller;

import br.com.ehmf.mywebtestframework.model.Produto;
import br.com.ehmf.mywebtestframework.service.IService;
import br.com.ehmf.webframework.annotations.WebframeworkBody;
import br.com.ehmf.webframework.annotations.WebframeworkController;
import br.com.ehmf.webframework.annotations.WebframeworkGetMethod;
import br.com.ehmf.webframework.annotations.WebframeworkInject;
import br.com.ehmf.webframework.annotations.WebframeworkPostMethod;

@WebframeworkController
public class HelloController {

	@WebframeworkInject
	private IService iService;
	
	@WebframeworkGetMethod("/hello")
	public String returnHelloWorld() {
		return "Return Hello world!!!";
	}	
	
	@WebframeworkGetMethod("/produto")
	public Produto exibirProduto() {
		Produto p = new Produto(1,"Nome1",5432.1,"teste.jpg");
		return p;
	}
	
	@WebframeworkPostMethod("/produto")
	public Produto cadastrarProduto(@WebframeworkBody Produto produtoNovo) {
		System.out.println(produtoNovo);
		return produtoNovo;
	}
	
	@WebframeworkGetMethod("/teste")
	public String teste() {
		return "Testes";
	}
	
	@WebframeworkGetMethod("/injected")
	public String chamadaCustom() {
		return iService.chamadaCustom("Hello injected");
	}

	
}
