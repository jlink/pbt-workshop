package pbt.exercise3;

import java.util.*;

public class CSVLineParser {

	public static final char SEPARATOR = ',';
	public static final char QUOTE = '"';

	public static CSVLine parse(String line) {
		return new CSVLine(tokenizeLine(line));
	}

	private static List<String> tokenizeLine(String line) {
		List<String> fields = new ArrayList<>();

		String current = "";
		boolean inQuote = false;
		char[] charArray = line.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			switch (c) {
				case QUOTE:
					if (nextCharIsAlsoQuote(charArray, i)) {
						current += QUOTE;
						i++;
					} else {
						inQuote = !inQuote;
					}
					break;
				case SEPARATOR:
					if (inQuote) {
						current += c;
					} else {
						fields.add(current);
						current = "";
					}
					break;
				default:
					current += c;
			}
		}
		fields.add(current);

		return fields;
	}

	private static boolean nextCharIsAlsoQuote(char[] charArray, int currentIndex) {
		return charArray.length > currentIndex + 1 && charArray[currentIndex + 1] == QUOTE;
	}
}
