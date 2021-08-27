package domainvalue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.valid4j.errors.RequireViolation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValueTest
{

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 })
	void valueOf(int input)
	{
		var value = Value.valueOf(input);

		final var string = value.toString();

		assertThat(string).isEqualTo(String.valueOf(input));
	}

	@ParameterizedTest
	@ValueSource(ints = { 16, Integer.MAX_VALUE })
	void valueOfWithValuesAbove15_throws(int input)
	{
		assertThrows(RequireViolation.class, () -> Value.valueOf(input));
	}

}