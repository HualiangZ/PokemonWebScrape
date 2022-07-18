import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.*;
import java.io.*;

public class AddMoves {

	public static void main(String[] args) throws IOException {
		ArrayList<String> names = new ArrayList<String>();
		String sql = "SELECT Pokemon FROM SwordAndShield;";
		String f = "";
		// get all pokemons and store them in to arraylist
		try (Connection c = DriverManager.getConnection("jdbc:sqlite:SwordAndShield.db");
				PreparedStatement pstmt = c.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				names.add(rs.getString("Pokemon"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int x = 0;// counter to see which one is done
		int fx = 0;// counter to see how many failed

		// loop through all pokemon in arraylist, connect to rankedboost url and add all
		// moves to sqlite database
		for (String n : names) {

			// connect to the rankedboost url and add all moves of the pokemon into sqlite
			// database
			try {
				Document doc = Jsoup.connect("https://rankedboost.com/pokemon-sword-shield/" + n + "/#moves").get();
				Elements body = doc.select("div#moves.rb-section-content-area ");
				// System.out.println(body.select("div.MoveRow"));
				String table = n;
				if(table.contains("-")) {
					table = table.replaceAll("-", "");
				}
				String sql1 = "INSERT INTO " + table + " (MoveID, Move) VALUES(?,?)";
				// connect to sqlite database
				try (Connection c = DriverManager.getConnection("jdbc:sqlite:SwordAndShield.db");
						PreparedStatement pstmt = c.prepareStatement(sql1)) {
					for (Element e : body.select("tr.table-tr-data-rb ")) {
						String moveid = e.select("td.table-td-data-rb:nth-of-type(1)").text();
						String name = e.select("td.table-td-data-rb:nth-of-type(2)").text();
						if(name != "" && moveid != "") {
							pstmt.setString(1, moveid);
							pstmt.setString(2, name);
							pstmt.executeUpdate();
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} catch (Exception e) {
				//any fail to connect to web will be stored in f
				f = f + " / " + n  + " / ";
				fx++;
				System.out.println(fx + " fails");
			}

			x++;
			System.out.println(x);
		}
		//put all failed to connect in to a .txt
		try (BufferedWriter out = new BufferedWriter(new FileWriter("gen1.txt"))) {
			out.write(f);
		} catch (IOException a) {
			System.err.println(a);
		}
	}
}
