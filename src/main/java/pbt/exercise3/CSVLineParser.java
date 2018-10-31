package pbt.exercise3;

import java.util.*;
import java.util.stream.*;

public class CSVLineParser {

	public static final String SEPARATOR = ",";
	public static final String QUOTE = "\"";

	public static CSVLine parse(String line) {
		List<String> values = (null == line)
									  ? Collections.emptyList()
									  : tokenizeLine(line);
		return new CSVLine(values);
	}

	private static List<String> tokenizeLine(String line) {
		List<String> values = Arrays.asList(line.split(SEPARATOR));
		values = values.stream().map(v -> v.startsWith(QUOTE) ? removeQuotes(v) : v).collect(Collectors.toList());
		return values;
	}

	private static String removeQuotes(String v) {
		return v.substring(1, v.length() - 1);
	}
}
