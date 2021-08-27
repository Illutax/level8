package tech.dobler.level8;

import tech.dobler.level8.Card;
import tech.dobler.level8.domainvalue.Suite;
import tech.dobler.level8.domainvalue.Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CardTest
{

	@Test
	void testToString()
	{
		var card = new Card(Suite.RED, Value.EIGHT);

		final var string = card.toString();

		assertThat(string).isEqualTo("[\u001B[31mRED 8\u001B[0m]");
	}

	@ParameterizedTest
	@EnumSource(mode = EnumSource.Mode.EXCLUDE, value = Suite.class, names = "OTHER")
	void ctor_ofAllSuites_andValue(Suite suite)
	{
		for (int value = 1; value <= 15; value++)
		{
			var card = new Card(suite, value);

			assertThat(card.value().value()).isEqualTo(value);
		}
	}

}