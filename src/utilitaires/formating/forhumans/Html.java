package utilitaires.formating.forhumans;

import java.util.ArrayList;
import java.util.List;

import utilitaires.GlobalText;

public class Html {

	public static String indent(String stringToIndent) {
		int strLength = stringToIndent.length();
		int index = 0;
		StringBuilder sb = new StringBuilder();
		List<String> l = new ArrayList<String>();
		int indentation = 0;

		// Je récupère une balise
		// Je passe à la ligne et l'indentation est augmentée
		// Je récupère la dite balise ci-dessus, l'indentation est diminuée

		char c;
		String tag = "";
		boolean getTag = false;
		boolean getEndingTag = false;

		int varInt;

		while (index < strLength) {
			c = stringToIndent.charAt(index);

			if (c == '<') {
				getTag = true;

				sb.append(GlobalText.LINE_FEED);
				for (int i = 0; i < indentation; i++) {
					sb.append(GlobalText.TABULATION);
				}
				sb.append(c);
			} else if (getTag == true && c != '>') {
				sb.append(c);
				if (c == '/' && sb.lastIndexOf("<") == sb.length() - 2) {
					getEndingTag = true;
				} else {
					if (c == ' ' && tag != "") {
						getTag = false;
					} else {
						tag += c;
					}

				}
			} else if (c == '>') {
				sb.append(c);
				if (getEndingTag == false) {
					l.add(tag);

					if (!tag.equals("br")) {
						indentation++;
					}
					tag = "";
				} else {
					varInt = l.lastIndexOf(tag);
					if (varInt != -1) {
						l.remove(varInt);
					}

					if (!tag.equals("br")) {
						indentation--;
					}
					tag = "";
				}
				getTag = false;
				getEndingTag = false;

				sb.append(GlobalText.LINE_FEED);
				for (int i = 0; i < indentation; i++) {
					sb.append(GlobalText.TABULATION);
				}
			} else {
				sb.append(c);
			}

			index++;
		}

		return sb.toString();
	}

	public static String indent1(String stringToIndent) {

		int strLength = stringToIndent.length();
		int cursor = 0;
		StringBuilder sb = new StringBuilder();
		int indentation = 0;
		char c;
		String buffer = "";

		boolean doctype = false, autofermante = false, meta = false, fermante = false;

		while (cursor < strLength) {
			c = stringToIndent.charAt(cursor);

			if (c == '<') {
				buffer += c;
				cursor++;
				c = stringToIndent.charAt(cursor);
				while (c != '>') {

					buffer += c;
					cursor++;
					c = stringToIndent.charAt(cursor);
				}
				buffer += c + GlobalText.LINE_FEED;
				cursor++;

				/*
				 * j'incrémente l'indentation si: - ce n'est pas un doctype - ce
				 * n'est pas une balise autofermante - ce n'est pas une balise
				 * meta - ce n'est pas une balise fermante
				 */
				doctype = buffer.contains("doctype");
				autofermante = buffer.contains("/>");
				meta = buffer.contains("meta");
				fermante = buffer.contains("</");

				if (!doctype && !autofermante && !meta && !fermante) {
					indentation++;
				}
				if (buffer.contains("</")) {
					indentation -= 1;
				}

				sb.append(buffer);
				buffer = "";
				// sb.append(GlobalText.LINE_FEED);
				for (int i = 0; i < indentation; i++) {
					sb.append(GlobalText.TABULATION);
				}

			} else {
				while (c != '<') {
					sb.append(c);
					cursor++;
					c = stringToIndent.charAt(cursor);
				}
				indentation--;
				sb.append(GlobalText.LINE_FEED);
				for (int i = 0; i < indentation - 1; i++) {
					sb.append(GlobalText.TABULATION);
				}
			}

		}

		return sb.toString();
	}

	public static String indent2(String stringToIndent) {

		if(stringToIndent.charAt(0)!='<'){
			return stringToIndent;
		}
		
		int strLength = stringToIndent.length();
		int cursor = 0;
		StringBuilder sb = new StringBuilder();
		int indentation = 0;
		char c;
		String buffer = "";
		boolean script = false, first = true, second = true, third = true;

		// Il me faut un fichier contenant toutes les balises que je load au
		// début dans un tableau
		// Si je tombre sur un chevron, je passe en mode de détection de tag et
		// j'analyse lettre à lettre
		// Si ce n'est pas un tag je sors du mode de détection de tag
		boolean doctype = false, autofermante = false, meta = false, fermante = false;

		while (cursor < strLength) {
			c = stringToIndent.charAt(cursor);

			if (c == '<') {
				buffer += c;
				cursor++;
				c = stringToIndent.charAt(cursor);
				while (c != '>') {

					buffer += c;
					cursor++;
					c = stringToIndent.charAt(cursor);

//					if (!script) {
//						if (buffer.contains("script")) {
//							script = true;
//						}
//					}

				}
				buffer += c + GlobalText.LINE_FEED;
				cursor++;

				/*
				 * j'incrémente l'indentation si: - ce n'est pas un doctype - ce
				 * n'est pas une balise autofermante - ce n'est pas une balise
				 * meta - ce n'est pas une balise fermante
				 */
				doctype = buffer.contains("doctype");
				autofermante = buffer.contains("/>");
				meta = buffer.contains("meta");
				fermante = buffer.contains("</");

				if (!doctype && !autofermante && !meta && !fermante) {
					indentation++;
				}
				if (buffer.contains("</")) {
					indentation -= 1;
				}

				sb.append(buffer);
				buffer = "";
				// sb.append(GlobalText.LINE_FEED);
				for (int i = 0; i < indentation; i++) {
					sb.append(GlobalText.TABULATION);
				}

			} else {
//				if (script) {
//					try {
//						first = script ? (stringToIndent.charAt(cursor + 1) == '/')
//								: true;
//						second = script ? (stringToIndent.charAt(cursor + 2) == 's')
//								: true;
//						third = script ? (stringToIndent.charAt(cursor + 3) == 'c')
//								: true;
//					} catch (Exception e) {
//						System.out.println(e.getMessage());
//					}
//				}

				while (c != '<' && second ) {
					sb.append(c);
					cursor++;
					c = stringToIndent.charAt(cursor);
				}
				script = false;
				indentation--;
				sb.append(GlobalText.LINE_FEED);
				for (int i = 0; i < indentation; i++) {
					sb.append(GlobalText.TABULATION);
				}
			}

		}

		return sb.toString();
	}

}
