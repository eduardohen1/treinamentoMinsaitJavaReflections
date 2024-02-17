package br.com.ehmf.mywebtestframework.controller;

import br.com.ehmf.mywebtestframework.model.Produto;
import br.com.ehmf.webframework.annotations.WebframeworkBody;
import br.com.ehmf.webframework.annotations.WebframeworkController;
import br.com.ehmf.webframework.annotations.WebframeworkGetMethod;
import br.com.ehmf.webframework.annotations.WebframeworkPostMethod;

@WebframeworkController
public class HelloController {

	@WebframeworkGetMethod("/hello")
	public String returnHelloWorld() {
		return "Hello world!!!";
	}	
	
	@WebframeworkGetMethod("/produto")
	public Produto exibirPorduto() {
		Produto p = new Produto(1,"Nome1",2000.0,"teste.jpg");
		return p;
	}
	
	@WebframeworkPostMethod("/produto")
	public String cadastrarProduto(@WebframeworkBody Produto produtoNovo) {
		System.out.println(produtoNovo);
		return "Produto cadastrado";
	}
	
	public String teste() {
		return "Testes";
	}
	
	
}
