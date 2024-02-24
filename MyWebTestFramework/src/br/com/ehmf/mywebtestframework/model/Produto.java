package br.com.ehmf.mywebtestframework.model;

public class Produto {
	
	private int id;
	private String nome;
	private double valor;
	private String linkFoto;
	
	public Produto() {}
	
	public Produto(int id, String nome, double valor, String linkFoto) {
		this.id = id;
		this.nome = nome;
		this.valor = valor;
		this.linkFoto = linkFoto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getLinkFoto() {
		return linkFoto;
	}

	public void setLinkFoto(String linkFoto) {
		this.linkFoto = linkFoto;
	}
		
	@Override
	public String toString() {		
		return "[ id = " + this.id + ", nome = " + this.nome +
				", preco = " + this.valor + ", linkFoto = " + this.linkFoto +
				"]";
	}
	
}
