package br.com.ehmf.mywebtestframework.service;

import br.com.ehmf.webframework.annotations.WebframeworkService;

@WebframeworkService
public class ServiceImplementation implements IService {

	@Override
	public String chamadaCustom(String mensagem) {
		return "Teste chamada servico: " + mensagem;
	}

}
