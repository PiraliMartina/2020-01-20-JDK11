package it.polito.tdp.artsmia.model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private ArtsmiaDAO dao;
	private Map<Integer, Artist> mappaArtisti;
	private Graph<Artist, DefaultWeightedEdge> grafo;
	private List<Artist> bestList;
	private int bestPeso;

	public Model() {
		this.dao = new ArtsmiaDAO();
	}

	public List<String> listRoles() {
		return dao.listRoles();
	}

	public Map<Integer, Artist> getMappaArtisti() {
		return mappaArtisti;
	}

	public void creaGrafo(String ruolo) {
		grafo = new SimpleWeightedGraph<Artist, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		mappaArtisti = new TreeMap<Integer, Artist>();

		dao.getArtistByRole(mappaArtisti, ruolo);

		// VERTICI
		Graphs.addAllVertices(grafo, mappaArtisti.values());

		// ARCHI
		for (Coppie c : dao.getCoppie(mappaArtisti, ruolo)) {
			Graphs.addEdge(grafo, c.getArtista1(), c.getArtista2(), c.getPeso());
		}

	}

	public int numVertex() {
		return grafo.vertexSet().size();
	}

	public int numEdges() {
		return grafo.edgeSet().size();
	}

	public String artistiConnessi() {
		String s = "";
		List<Coppie> coppie = new LinkedList<Coppie>();
		for (DefaultWeightedEdge e : grafo.edgeSet()) {
			coppie.add(new Coppie(grafo.getEdgeTarget(e), grafo.getEdgeSource(e), (int) grafo.getEdgeWeight(e)));
			coppie.sort(new Comparator<Coppie>() {

				@Override
				public int compare(Coppie o1, Coppie o2) {
					return o2.getPeso() - o1.getPeso();
				}

			});
		}
		for (Coppie c : coppie) {
			s += c.getArtista1().getNome() + " - " + c.getArtista2().getNome() + " " + c.getPeso() + "\n";
		}
		return s;
	}

	public void calcolaPercorso(Artist a) {
		bestList = new LinkedList<Artist>();
		bestPeso = 0;

		List<Artist> parziale = new LinkedList<Artist>();

		parziale.add(a);

		for (DefaultWeightedEdge e : grafo.edgesOf(a)) {
			int peso = (int) grafo.getEdgeWeight(e);
			ricorsiva(parziale, a, peso);
		}
	}

	private void ricorsiva(List<Artist> parziale, Artist a, int peso) {

		if (parziale.size() > bestList.size()) {
			bestList = new LinkedList<Artist>(parziale);
			bestPeso = peso;
		}

		for (DefaultWeightedEdge e : grafo.edgesOf(a)) {
			int pesoArco = (int) grafo.getEdgeWeight(e);
			if (pesoArco == peso) {
				Artist artista = grafo.getEdgeTarget(e);
				if (!parziale.contains(artista)) {
					parziale.add(artista);
					ricorsiva(parziale, artista, peso);
					parziale.remove(artista);
				}
			}
		}
	}
	
	public String getBest() {
		String s="Tutti questi artisti hanno esposto "+bestPeso+" volte \n";
		for(Artist a: bestList) {
			s+=a.getNome()+"\n";
		}
		return s;
	}

}
