	import org.jsoup.*;
	import org.jsoup.nodes.*;
	import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.*;
	import java.io.*;

public class SwordAndShieldPokemons {

	public static void main(String[] args) {
		final String url = "https://www.eurogamer.net/pokemon-sword-shield-galar-pokedex-6017";
		JSONArray pokemon = null;
		int x = 0;
		try {
			final Document document = Jsoup.connect(url).get();
			//int ticker = 1;
			pokemon = new JSONArray();
			// go through the rows in the table of the url and add them to the json array

			for (Element row : document.select("table tr")) {
				if (row.select("td:nth-of-type(1)").text().equals("")) {
					// check if the first row is empty if so skip it
					continue;
				} else {
					// get the correct box for the correct variable;
					// pokemon id
					final String name = row.select("td:nth-of-type(2)").text();// pokemon name
					final String t = row.select("td:nth-of-type(3)").text();// pokemon type1
					//final String t1 = row.select("td:nth-of-type(5)").text();// pokemon type2
					String[] ty;
					ArrayList<String> typesArr = new ArrayList<String>();
					if(t.contains("/")) {
						 ty = t.split(" / ");
						 typesArr.add(ty[0]);
						 typesArr.add(ty[1]);
					}else {
						typesArr.add(t);
					}
					
					String sql = "INSERT INTO SwordAndShield(Pokemon, Type1, Type2) VALUES(?,?,?)";
					try (Connection c = DriverManager.getConnection("jdbc:sqlite:SwordAndShield.db");
						PreparedStatement pstmt = c.prepareStatement(sql)) {
							pstmt.setString(1, name);
							pstmt.setString(2, typesArr.get(0));
							try {
								pstmt.setString(3, typesArr.get(1));
							}catch(IndexOutOfBoundsException e) {
								pstmt.setString(3, "");
							}
							pstmt.executeUpdate();
						
					}catch(SQLException e){
						e.printStackTrace();
					}
					
					
				}
				
			}


		} catch (Exception e) {
			e.printStackTrace();
		}


		System.out.println(pokemon);
	}
}