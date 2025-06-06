package pdfprocesser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Beolvassa a mappából az összes PDF-et Egyenként feldolgozza egy-egy
 * táblázatba Ezeket a táblázatokat betölti adatbázisba, vagy aki ahova akarja.
 */
public class PDFTableExtractor {

	private static final String TABLE_START = "dátuma névértéken árfolyamértéken";
	private static final String TABLE_END_v1 = "Állomány értéke összesen:";
	private static final String TABLE_END_v2 = "Az alkalmazott árfolyam és kimutatott állomány";

	public static void main(String[] args) {

		try {
			String directoryPath = "C:\\DEV\\dev-workspace\\csanszi-budget\\pdf";
			File[] allPDFPath = getAllPDFPath(directoryPath);
			
//			for (File file : allPDFPath) {
			for(int i =0;i<allPDFPath.length;i++) {
				System.out.println(i+"/"+allPDFPath.length);
				File file = allPDFPath[i];
				System.out.println(file.getName());
				
				List<NapiBruttoVeteliArfolyam> tables = extractTableData(file);
			}
		} catch (Exception e) {
			// leszarom a hibákat, nincs benne. ha hiba van essen össze
			e.printStackTrace();
			System.exit(0);
		}
	}

	private static File[] getAllPDFPath(String directoryPath) {
	      // Using File class create an object for specific directory
	      File directory = new File(directoryPath);
	      
	      // Using listFiles method we get all the files of a directory 
	      // return type of listFiles is array
	      File[] files = directory.listFiles();
	      
	      // Print name of the all files present in that path
//	      if (files != null) {
//	        for (File file : files) {
//	          System.out.println(file.getName());
//	        }
//	      }
	      return files;
	}

	public static List<NapiBruttoVeteliArfolyam> extractTableData(File file)
			throws IOException, SQLException, ClassNotFoundException {

		PDDocument document = Loader.loadPDF(file);
		PDFTextStripper stripper = new PDFTextStripper();
		stripper.setSortByPosition(true); // Enable sorting by position
		String fullText = stripper.getText(document);

		String allomanyErteknapja = extractAllomanyErteknapjaFromFullText(fullText);
		System.out.println(allomanyErteknapja);

		List<String> rows = new ArrayList<String>(Arrays.asList(fullText.split("\n")));

		int start = fullText.indexOf(TABLE_START);

		List<NapiBruttoVeteliArfolyam> tableContent = new ArrayList();
		for (int i = 0; i < rows.size(); i++) {
			String row = rows.get(i).trim();

			if (row.contains(TABLE_START)) {

				while (i < rows.size()) {
					i++;
					row = rows.get(i);

					if (row.contains(TABLE_END_v1) || row.contains(TABLE_END_v2)) {
						break;
					}
					NapiBruttoVeteliArfolyam napiBruttoVeteliArfolyam = new NapiBruttoVeteliArfolyam(allomanyErteknapja,
							row);
					System.out.println(napiBruttoVeteliArfolyam);
					NapiBruttoVeteliArfolyamDAO.upsert(napiBruttoVeteliArfolyam);
					tableContent.add(napiBruttoVeteliArfolyam);
//					System.out.println(napiBruttoVeteliArfolyam);
				}

			}
		}

		return tableContent;
	}

	private static String extractAllomanyErteknapjaFromFullText(String fullText) {
		String regex = "Az alkalmazott árfolyam és kimutatott állomány értéknapja: (\\d{4}\\.\\d{2}\\.\\d{2})";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(fullText);

		if (matcher.find()) {
			String date = matcher.group(1);
			return date;
		}
		return null;
	}

	

}
