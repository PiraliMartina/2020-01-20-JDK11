package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Coppie;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {

		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"),
						res.getString("continent"), res.getString("country"), res.getInt("curator_approved"),
						res.getString("dated"), res.getString("department"), res.getString("medium"),
						res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"),
						res.getString("rights_type"), res.getString("role"), res.getString("room"),
						res.getString("style"), res.getString("title"));

				result.add(artObj);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Exhibition> listExhibitions() {

		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"),
						res.getString("exhibition_title"), res.getInt("begin"), res.getInt("end"));

				result.add(exObj);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> listRoles() {

		String sql = "SELECT DISTINCT role FROM authorship ORDER BY role";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString(1));
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void getArtistByRole(Map<Integer, Artist> mappaArtisti, String ruolo) {
		String sql = "SELECT DISTINCT artists.artist_id, name FROM artists, authorship WHERE artists.artist_id=authorship.artist_id AND role=?";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				String nome = res.getString(2);
				int id = res.getInt(1);
				if(!mappaArtisti.containsKey(id))
					mappaArtisti.put(id, new Artist(id, nome));
				
			}
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public List<Coppie> getCoppie(Map<Integer, Artist> mappaArtisti, String ruolo) {
		String sql = "SELECT a1.artist_id, a2.artist_id, COUNT(*)" + 
				" FROM (SELECT DISTINCT exhibition_id, artist_id FROM exhibition_objects, authorship" + 
				" WHERE authorship.object_id=exhibition_objects.object_id AND role=?) AS a1," +
				" (SELECT DISTINCT exhibition_id, artist_id FROM exhibition_objects, authorship" + 
				" WHERE authorship.object_id=exhibition_objects.object_id AND role=?) AS a2"+
				" WHERE a1.exhibition_id=a2.exhibition_id AND a1.artist_id!=a2.artist_id AND a1.artist_id<a2.artist_id" + 
				" GROUP BY a1.artist_id, a2.artist_id";
		List<Coppie> result = new ArrayList<Coppie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			st.setString(2, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Artist a1 = mappaArtisti.get(res.getInt(1));
				Artist a2 = mappaArtisti.get(res.getInt(2));
				result.add(new Coppie(a1, a2, res.getInt(3)));
				
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}


}
