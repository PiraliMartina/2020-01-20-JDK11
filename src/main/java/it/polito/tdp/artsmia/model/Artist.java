package it.polito.tdp.artsmia.model;

public class Artist {
	
	private int id;
	private String nome;
	
	public Artist(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public int getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}
	
	

}
