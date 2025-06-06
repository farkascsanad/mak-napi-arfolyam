package pdfprocesser;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class NapiBruttoVeteliArfolyamDAO {

	public NapiBruttoVeteliArfolyamDAO() {
	}

	public static Connection getDBConnection() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "admin"); // teszt
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void upsert(NapiBruttoVeteliArfolyam nap) throws SQLException, ClassNotFoundException {
		String sql = "  INSERT INTO raw.napi_brutto_veteli_arfolyam (\r\n"
				+ "                allomany_erteknapja, ertekpapir_megnevezese, lejarat_datuma,\r\n"
				+ "                allomany_neverteken, allomany_neverteken_deviza,\r\n"
				+ "                brutto_veteli_arfolyam, allomany_brutto_veteli_arfolyamertekben,\r\n"
				+ "                allomany_brutto_veteli_arfolyamertekben_deviza\r\n"
				+ "            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)\r\n"
				+ "            ON CONFLICT (allomany_erteknapja, ertekpapir_megnevezese)\r\n"
				+ "            DO UPDATE SET\r\n" + "                lejarat_datuma = EXCLUDED.lejarat_datuma,\r\n"
				+ "                allomany_neverteken = EXCLUDED.allomany_neverteken,\r\n"
				+ "                allomany_neverteken_deviza = EXCLUDED.allomany_neverteken_deviza,\r\n"
				+ "                brutto_veteli_arfolyam = EXCLUDED.brutto_veteli_arfolyam,\r\n"
				+ "                allomany_brutto_veteli_arfolyamertekben = EXCLUDED.allomany_brutto_veteli_arfolyamertekben,\r\n"
				+ "                allomany_brutto_veteli_arfolyamertekben_deviza = EXCLUDED.allomany_brutto_veteli_arfolyamertekben_deviza; ";

		try (Connection connection = getDBConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

			stmt.setDate(1, new java.sql.Date(nap.getAllomanyErteknapja().getTime()));
			stmt.setString(2, nap.getErtekpapirMegnevezese());
			stmt.setDate(3,
					nap.getLejaratDatuma() != null ? new java.sql.Date(nap.getLejaratDatuma().getTime()) : null);
			stmt.setObject(4, nap.getAllomanyNeverteken());
			stmt.setString(5, nap.getAllomanyNevertekenDeviza());
			stmt.setObject(6, nap.getBruttoVeteliArfolyam());
			stmt.setObject(7, nap.getAllomanyBruttoVeteliArfolyamertekben());
			stmt.setString(8, nap.getAllomanyBruttoVeteliArfolyamertekbenDeviza());

			stmt.executeUpdate();
		}
	}
}
