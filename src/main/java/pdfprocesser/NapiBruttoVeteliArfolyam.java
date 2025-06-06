package pdfprocesser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Azért van minden magyar névvel és nem angol névvel, mert as-is parseoljuk
 * magyar PDFből és sokkal könnyebb lekövetni, hogy melyik adat hova tartozik.
 */
public class NapiBruttoVeteliArfolyam {

	private Date allomanyErteknapja;
	private String ertekpapirMegnevezese;
	private Date lejaratDatuma;
	private Double allomanyNeverteken;
	private String allomanyNevertekenDeviza;
	private Double bruttoVeteliArfolyam;
	private Double allomanyBruttoVeteliArfolyamertekben;
	private String allomanyBruttoVeteliArfolyamertekbenDeviza;

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd");

	public NapiBruttoVeteliArfolyam() {
		super();
	}

	public NapiBruttoVeteliArfolyam(String allomanyErteknapja, String row) {
		super();
		try {

			this.allomanyErteknapja = DATE_FORMAT.parse(allomanyErteknapja);
			fillWithRawRow(row);

		} catch (Exception e) {
			System.out.println("hiba: " + row);
			e.printStackTrace();
		}
	}

	public void fillWithRawRow(String line) throws ParseException {
		// Split into tokens
		String[] tokens = line.trim().split("\\s+");

		// Extract known positions from end
		allomanyBruttoVeteliArfolyamertekbenDeviza = tokens[tokens.length - 1];
		allomanyBruttoVeteliArfolyamertekben = parseNumber(tokens[tokens.length - 2]);
		// Skip % at tokens.length - 3
		bruttoVeteliArfolyam = Double.parseDouble(tokens[tokens.length - 4].replace(",", "."));
		allomanyNevertekenDeviza = tokens[tokens.length - 5];
		allomanyNeverteken = parseNumber(tokens[tokens.length - 6]);
		lejaratDatuma = DATE_FORMAT.parse(tokens[tokens.length - 7]);

		// Everything before that is the name
		StringBuilder nameBuilder = new StringBuilder();
		for (int i = 0; i < tokens.length - 7; i++) {
			nameBuilder.append(tokens[i]);
			if (i != tokens.length - 8) {
				nameBuilder.append(" ");
			}
		}
		ertekpapirMegnevezese = nameBuilder.toString();
	}

	private double parseNumber(String raw) {
		return Double.parseDouble(raw.replace(",", ""));
	}

	@Override
	public String toString() {
		return "NapiBruttoVeteliArfolyam [allomanyErteknapja=" + allomanyErteknapja + ", ertekpapirMegnevezese="
				+ ertekpapirMegnevezese + ", lejaratDatuma=" + lejaratDatuma + ", allomanyNeverteken="
				+ allomanyNeverteken + ", allomanyNevertekenDeviza=" + allomanyNevertekenDeviza
				+ ", bruttoVeteliArfolyam=" + bruttoVeteliArfolyam + ", allomanyBruttoVeteliArfolyamertekben="
				+ allomanyBruttoVeteliArfolyamertekben + ", allomanyBruttoVeteliArfolyamertekbenDeviza="
				+ allomanyBruttoVeteliArfolyamertekbenDeviza + "]";
	}

	public String getErtekpapirMegnevezese() {
		return ertekpapirMegnevezese;
	}

	public void setErtekpapirMegnevezese(String ertekpapirMegnevezese) {
		this.ertekpapirMegnevezese = ertekpapirMegnevezese;
	}

	public Date getLejaratDatuma() {
		return lejaratDatuma;
	}

	public void setLejaratDatuma(Date lejaratDatuma) {
		this.lejaratDatuma = lejaratDatuma;
	}

	public Double getAllomanyNeverteken() {
		return allomanyNeverteken;
	}

	public void setAllomanyNeverteken(Double allomanyNeverteken) {
		this.allomanyNeverteken = allomanyNeverteken;
	}

	public Double getBruttoVeteliArfolyam() {
		return bruttoVeteliArfolyam;
	}

	public void setBruttoVeteliArfolyam(Double bruttoVeteliArfolyam) {
		this.bruttoVeteliArfolyam = bruttoVeteliArfolyam;
	}

	public Double getAllomanyBruttoVeteliArfolyamertekben() {
		return allomanyBruttoVeteliArfolyamertekben;
	}

	public void setAllomanyBruttoVeteliArfolyamertekben(Double allomanyBruttoVeteliArfolyamertekben) {
		this.allomanyBruttoVeteliArfolyamertekben = allomanyBruttoVeteliArfolyamertekben;
	}

	public Date getAllomanyErteknapja() {
		return allomanyErteknapja;
	}

	public void setAllomanyErteknapja(Date allomanyErteknapja) {
		this.allomanyErteknapja = allomanyErteknapja;
	}

	public String getAllomanyNevertekenDeviza() {
		return allomanyNevertekenDeviza;
	}

	public void setAllomanyNevertekenDeviza(String allomanyNevertekenDeviza) {
		this.allomanyNevertekenDeviza = allomanyNevertekenDeviza;
	}

	public String getAllomanyBruttoVeteliArfolyamertekbenDeviza() {
		return allomanyBruttoVeteliArfolyamertekbenDeviza;
	}

	public void setAllomanyBruttoVeteliArfolyamertekbenDeviza(String allomanyBruttoVeteliArfolyamertekbenDeviza) {
		this.allomanyBruttoVeteliArfolyamertekbenDeviza = allomanyBruttoVeteliArfolyamertekbenDeviza;
	}

}
