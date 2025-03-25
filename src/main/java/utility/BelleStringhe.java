package utility;

public class BelleStringhe {
	public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String PULISCI_TERMINALE = "\033\143";

	private final static String SPAZIO = " ";
	private final static String ACAPO = "\n";

	public static char[] generaCornice(int l) {
		l += 4;
		char[] res = new char[l];

		for (int i = 0; i < l; i++)
			res[i] = '-';

		return res;
	}

	public static char[] generaCornice(int l, char c) {
		l += 4;
		char[] res = new char[l];

		for (int i = 0; i < l; i++)
			res[i] = c;

		return res;
	}

	public static String incornicia(String s) {

		StringBuffer res = new StringBuffer();

		res.append(generaCornice(s.length()));
		res.append('|' + SPAZIO + s + SPAZIO + '|');
		res.append(generaCornice(s.length()));

		return res.toString();

	}

	public static String incolonna(String s, int larghezza) {
		StringBuffer res = new StringBuffer(larghezza);
		int numCharDaStampare = Math.min(larghezza, s.length());
		res.append(s.substring(0, numCharDaStampare));

		for (int i = s.length() + 1; i <= larghezza; i++) {
			res.append(SPAZIO);
		}

		return res.toString();
	}

	public static String centrata(String s, int larghezza) {
		StringBuffer res = new StringBuffer(larghezza);
		if (larghezza <= s.length())
			res.append(s.substring(larghezza));
		else {
			int spaziPrima = (larghezza - s.length()) / 2;
			int spaziDopo = larghezza - spaziPrima - s.length();
			for (int i = 1; i <= spaziPrima; i++)
				res.append(SPAZIO);

			res.append(s);

			for (int i = 1; i <= spaziDopo; i++)
				res.append(SPAZIO);
		}
		return res.toString();

	}

	public static String ripetiChar(char elemento, int larghezza) {
		StringBuffer result = new StringBuffer(larghezza);
		for (int i = 0; i < larghezza; i++) {
			result.append(elemento);
		}
		return result.toString();

	}

	public static String rigaIsolata(String daIsolare) {
		StringBuffer result = new StringBuffer();
		result.append(ACAPO);
		result.append(daIsolare);
		result.append(ACAPO);
		return result.toString();
	}

	/**
	 * Metodo che permette di creare una tabella a partire da una matrice di stringhe
	 * @param matrice
	 * @return
	 */
	public static String tabella(String[][] matrice) {
		StringBuffer result = new StringBuffer();
		int[] larghezze = new int[matrice[0].length];
		for (int i = 0; i < matrice[0].length; i++) {
			larghezze[i] = 0;
			for (int j = 0; j < matrice.length; j++) {
				if (matrice[j][i].length() > larghezze[i] - 3) {
					larghezze[i] = matrice[j][i].length() + 3;
				}
			}
		}
		for (int i = 0; i < matrice.length; i++) {
			for (int j = 0; j < matrice[0].length; j++) {
				result.append(incolonna(matrice[i][j], larghezze[j]));
			}
			result.append("\n");
		}
		return result.toString();
	}

}
