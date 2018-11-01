package pbt.solution3;

import java.util.*;
import java.util.stream.*;

import pbt.exercise3.*;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.*;

@Group
class CSVLineParsingPropertiesSolution {

	@Group
	class line_without_separator_returns_single_field {

		@Property
		void unquoted_field(@ForAll("unquotedFieldWithoutSeparator") String field) {
			// Mask bug with quote in unquoted field
			Assume.that(!field.contains("\""));

			CSVLine line = CSVLineParser.parse(field);
			assertThat(line).containsOnly(field);
		}

		@Property
		void quoted_field(@ForAll("fieldValue") String fieldValue) {
			// Mask bug with empty quoted string
			Assume.that(!fieldValue.isEmpty());

			String quotedField = quote(fieldValue);
			CSVLine line = CSVLineParser.parse(quotedField);
			assertThat(line).containsOnly(fieldValue);
		}
	}

	@Group
	class line_with_separators {

		@Property
		@Report(Reporting.GENERATED)
		void quoted_fields(@ForAll("unquotedFields") List<String> fields) {
			// Mask bug with empty quoted string
			Assume.that(fields.stream().noneMatch(String::isEmpty));

			String parseLine = fields.stream().map(f -> quote(f)).collect(Collectors.joining());
			CSVLine line = CSVLineParser.parse(parseLine);
			assertThat(line).isEqualTo(fields);
		}


	}

	@Provide
	Arbitrary<String> unquotedFieldWithoutSeparator() {
		return fieldValue()
					   .map(this::removeSeparator)
					   .filter(field -> !field.startsWith("\""));
	}

	@Provide
	Arbitrary<String> fieldValue() {
		return Arbitraries.strings().ascii().ofMaxLength(500).map(this::removeNewLines);
	}


	@Provide
	Arbitrary<List<String>> unquotedFields() {
		return fieldValue().list().ofMinSize(2).ofMaxSize(100);
	}

	private String quote(String field) {
		return String.format("\"%s\"", duplicateQuotes(field));
	}

	private String duplicateQuotes(String aString) {
		String doubleQuote = "" + CSVLineParser.QUOTE + CSVLineParser.QUOTE;
		return aString.replaceAll(Character.toString(CSVLineParser.QUOTE), doubleQuote);
	}

	private String removeSeparator(String aString) {
		return aString.replaceAll(Character.toString(CSVLineParser.SEPARATOR), "");
	}

	private String removeNewLines(String aString) {
		return aString.replaceAll("[\\n\\r]", "");
	}

}
