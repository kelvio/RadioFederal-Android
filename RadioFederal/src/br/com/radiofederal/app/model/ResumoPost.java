package br.com.radiofederal.app.model;

public class ResumoPost {

	private String nome = "";
	private String data = "";
	private String numeroComentarios = "";
	private String nomeCategoria = "";
	private String resumo = "";
	private String nomeAutor = "";
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getNumeroComentarios() {
		return numeroComentarios;
	}

	public void setNumeroComentarios(String numeroComentarios) {
		this.numeroComentarios = numeroComentarios;
	}

	public String getNomeCategoria() {
		return nomeCategoria;
	}

	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public String getNomeAutor() {
		return nomeAutor;
	}

	public void setNomeAutor(String nomeAutor) {
		this.nomeAutor = nomeAutor;
	}

	
}
