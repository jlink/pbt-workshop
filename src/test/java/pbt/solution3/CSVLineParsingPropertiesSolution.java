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
			CSVLine line = CSVLineParser.parse(field);
			assertThat(line).containsOnly(field);
		}

		@Property
		void quoted_field(@ForAll("fieldValue") String fieldValue) {
			// Mask bug with empty quoted string
			Assume.that(!fieldValue.isEmpty());

			// Mask bug with quote as first char
			Assume.that(!fieldValue.startsWith("\""));

			String quotedField = quote(fieldValue);
			CSVLine line = CSVLineParser.parse(quotedField);
			assertThat(line).containsOnly(fieldValue);
		}
	}

	@Group
	class line_with_separators {

		@Property
		void quoted_fields(@ForAll("unquotedFields") List<String> fields) {
			// Mask bug with empty quoted string
			Assume.that(fields.stream().noneMatch(String::isEmpty));

			// Mask bug with quote as first char in quoted string
			Assume.that(fields.stream().noneMatch(s -> s.startsWith("\"")));

			String parseLine = fields.stream().map(f -> quote(f)).collect(Collectors.joining(","));
			CSVLine line = CSVLineParser.parse(parseLine);
			assertThat(line.fields()).isEqualTo(fields);
		}

		@Property
		void unquoted_fields(@ForAll("fieldsWithoutSeparatorAndLeadingQuote") List<String> fields) {
			String parseLine = fields.stream().collect(Collectors.joining(","));
			CSVLine line = CSVLineParser.parse(parseLine);
			assertThat(line.fields()).isEqualTo(fields);
		}

		@Property
		void unquoted_fields_are_trimmed(@ForAll("fieldsWithoutSeparatorAndLeadingQuote") List<String> fields) {
			String parseLine = fields.stream().collect(Collectors.joining(" , "));
			CSVLine line = CSVLineParser.parse(parseLine);
			assertThat(line.fields()).isEqualTo(fields);
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
	Arbitrary<List<String>> fieldsWithoutSeparatorAndLeadingQuote() {
		return unquotedFieldWithoutSeparator()
				.map(String::trim)
				.filter(f -> !f.startsWith("\""))
				.list().ofMinSize(2).ofMaxSize(100);
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
