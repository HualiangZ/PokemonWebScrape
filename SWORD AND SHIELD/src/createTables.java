import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class createTables {
	public static void main(String[] args) {
		ArrayList<String> name = new ArrayList<String>();
		String sql = "SELECT Pokemon FROM SwordAndShield;";
		try (Connection c = DriverManager.getConnection("jdbc:sqlite:SwordAndShield.db");
				PreparedStatement pstmt = c.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				name.add(rs.getString("Pokemon"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(name.size());
		int x = 0;
		// tableName = i;
		try (Connection c = DriverManager.getConnection("jdbc:sqlite:SwordAndShield.db")) {
			for (String i : name) {
				String str = i.replaceAll("[^a-zA-Z0-9]", "");
				PreparedStatement pstmt = c.prepareStatement("CREATE TABLE "+ str +" (MoveID TEXT, Move TEXT) ");
				pstmt.execute();
				//System.out.println("done");
				x++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(x);

		
	}
}
