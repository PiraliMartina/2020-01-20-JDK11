package it.polito.tdp.artsmia;

import java.net.URL;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.appendText("\n"+model.artistiConnessi());
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	try {
    		int artistaID = Integer.parseInt(txtArtista.getText());
    		if(!model.getMappaArtisti().containsKey(artistaID)) {
    			txtResult.setText("Artista non trovato");
    			return;
    		}
    		Artist artista = model.getMappaArtisti().get(artistaID);
    		model.calcolaPercorso(artista);
    		txtResult.setText(model.getBest());
    	} catch (NumberFormatException e) {
			e.printStackTrace();
			txtResult.setText("Non hai introdotto un numero");
		} catch (Exception e) {
			e.printStackTrace();
			txtResult.setText("ERRORE!!!");
		}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	try {
			txtResult.clear();
			
			model.creaGrafo(boxRuolo.getValue());
			txtResult.setText(String.format("Grafo creato con %d vetici e %d archi \n", model.numVertex(), model.numEdges()));
		} catch (Exception e) {
			e.printStackTrace();
			txtResult.setText("ERRORE!!!");
		}
    }

    public void setModel(Model model) {
    	this.model = model;
    	
    	List<String> roles = model.listRoles();
    	boxRuolo.getItems().addAll(roles);
    	boxRuolo.setValue(roles.get(0));
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
