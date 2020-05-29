package it.polito.tdp.artsmia.model;

public class Coppie {

	private Artist artista1;
	private Artist artista2;
	private int peso;
	
	public Coppie(Artist artista1, Artist artista2, int peso) {
		this.artista1 = artista1;
		this.artista2 = artista2;
		this.peso = peso;
	}

	public Artist getArtista1() {
		return artista1;
	}

	public Artist getArtista2() {
		return artista2;
	}

	public int getPeso() {
		return peso;
	}
	
	
	
	
}
