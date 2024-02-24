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
		return "Return Hello world!!!";
	}	
	
	@WebframeworkGetMethod("/produto")
	public Produto exibirProduto() {
		Produto p = new Produto(1,"Nome1",5432.1,"teste.jpg");
		return p;
	}
	
	@WebframeworkPostMethod("/produto")
	public String cadastrarProduto(@WebframeworkBody Produto produtoNovo) {
		System.out.println(produtoNovo);
		return "Produto cadastrado";
	}
	
	@WebframeworkGetMethod("/teste")
	public String teste() {
		return "Testes";
	}

	
}
