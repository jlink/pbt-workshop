package pbt.solution3;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.csv.*;
import pbt.exercise3.*;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;

@Group
class CSVLineParsingPropertiesSolution {

	private final String ALLOWED_CHARACTERS =
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
					"abcdefghijklmnopqrstuvwxyz" +
					"0123456789" +
					" !\"#$%&'()*+,-./" +
					":;<=>?@" +
					"[\\]^_`" +
					"{|}~";

	@Group
	class line_without_separator_returns_single_field {

		@Property
		void unquoted_field(@ForAll("unquotedFieldWithoutSeparator") String field) {
			CSVLine line = CSVLineParser.parse(field);
			assertThat(line).containsOnly(field);
		}

		@Property
		void quoted_field(@ForAll("fieldValue") String fieldValue) {
			// Mask bug with quote as first char
			Assume.that(!fieldValue.startsWith("\""));

			String quotedField = quote(fieldValue);
			CSVLine line = CSVLineParser.parse(quotedField);
			assertThat(line).containsOnly(fieldValue);
		}

		@Example
		void quoted_field_that_starts_with_quote() {
			String fieldValue = "\"value";

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

			String parseLine = csvLineFromFields(fields);
			CSVLine line = CSVLineParser.parse(parseLine);
			assertThat(line.fields()).isEqualTo(fields);
		}

		@Example
			// Extracted after finding bug with property
		void empty_quoted_field_with_follower() {
			List<String> fields = asList("", "value");
			String parseLine = csvLineFromFields(fields);
			CSVLine line = CSVLineParser.parse(parseLine);
			assertThat(line.fields()).containsExactly("", "value");
		}

		@Example
		void quote_in_second_field() {
			List<String> fields = asList("A", "\"");
			String parseLine = csvLineFromFields(fields);
			CSVLine line = CSVLineParser.parse(parseLine);
			assertThat(line.fields()).containsExactly("A", "\"");
		}

		@Property
		void unquoted_fields(@ForAll("fieldsWithoutSeparatorAndLeadingQuote") List<String> fields) {
			String parseLine = String.join(",", fields);
			CSVLine line = CSVLineParser.parse(parseLine);
			assertThat(line.fields()).isEqualTo(fields);
		}

		@Property
		void unquoted_fields_are_trimmed(@ForAll("fieldsWithoutSeparatorAndLeadingQuote") List<String> fields) {
			String parseLine = String.join(" , ", fields);
			CSVLine line = CSVLineParser.parse(parseLine);
			assertThat(line.fields()).isEqualTo(fields);
		}

	}

	@Group
	class Patterns {

		// Fuzzying
		@Property
		boolean do_not_explode(@ForAll("csvValueStrings") @StringLength(max = 1024) String line) {
			return CSVLineParser.parse(line).size() > 0;
		}

		// Commutativity
		@Property
		void order_of_fields_should_not_change_number_of_fields(@ForAll("unquotedFields") List<String> fields, @ForAll Random random) {
			String parseLine = csvLineFromFields(fields);

			ArrayList<String> shuffledFields = new ArrayList<>(fields);
			Collections.shuffle(shuffledFields, random);
			String shuffledParseLine = csvLineFromFields(shuffledFields);

			CSVLine csvLine = CSVLineParser.parse(parseLine);
			CSVLine shuffledCsvLine = CSVLineParser.parse(shuffledParseLine);
			assertThat(csvLine.size()).isEqualTo(shuffledCsvLine.size());
		}

		// Test oracle
		@Property(shrinking = ShrinkingMode.BOUNDED)
		void produces_same_fields_as_apache_commons_csv(@ForAll("unquotedFields") List<String> fields) throws IOException {
			// Mask bug with empty quoted string
			Assume.that(fields.stream().noneMatch(String::isEmpty));

			// Mask bug with quote as first char in quoted string
			Assume.that(fields.stream().noneMatch(s -> s.startsWith("\"")));

			String parseLine = csvLineFromFields(fields);

			CSVLine csvLine = CSVLineParser.parse(parseLine);
			CSVRecord commonsLine = parseWithApacheCommons(parseLine);

			assertThat(commonsLine).containsExactlyElementsOf(csvLine);
		}

		private CSVRecord parseWithApacheCommons(String parseLine) throws IOException {
			CSVParser commonsParser = CSVFormat.newFormat(',').withQuote('"').parse(new StringReader(parseLine));
			return commonsParser.iterator().next();
		}

	}

	@Provide
	StringArbitrary csvValueStrings() {
		return Arbitraries.strings().withChars(ALLOWED_CHARACTERS.toCharArray());
	}


	@Provide
	Arbitrary<String> unquotedFieldWithoutSeparator() {
		return fieldValue()
					   .map(this::removeSeparator)
					   .filter(field -> !field.startsWith("\""));
	}

	@Provide
	Arbitrary<String> fieldValue() {
		return csvValueStrings().ofMaxLength(500).map(this::removeNewLines);
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

	private String csvLineFromFields(List<String> fields) {
		return fields.stream().map(f -> quote(f)).collect(Collectors.joining(","));
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
