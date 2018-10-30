package pbt.exercise3;

import java.time.*;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.*;

@Label("A game time")
class GameTimeTests {

	@Example
	void can_calculate_minute_in_game() {
		assertThat(GameTime.at(1, 1, 1).minuteInGame()).isEqualTo(Duration.ofMinutes(1).plusSeconds(1));
		assertThat(GameTime.at(4, 0, 59).minuteInGame()).isEqualTo(Duration.ofMinutes(30).plusSeconds(59));
	}

	@Group
	class can_be_created {

		@Example
		void in_quarter_1_through_4() {
			assertThat(GameTime.at(1, 1, 1).quarter()).isEqualTo(1);
			assertThat(GameTime.at(4, 1, 1).quarter()).isEqualTo(4);
		}

		@Example
		void in_minute_0_through_9() {
			assertThat(GameTime.at(1, 0, 1).minute()).isEqualTo(0);
			assertThat(GameTime.at(4, 9, 1).minute()).isEqualTo(9);
		}

		@Example
		void in_second_0_through_59() {
			assertThat(GameTime.at(1, 0, 0).second()).isEqualTo(0);
			assertThat(GameTime.at(4, 9, 59).second()).isEqualTo(59);
		}

	}

	@Group
	class cannot_be_created {

		@Example
		void if_quarter_is_outside_1_through_4() {
			assertThatThrownBy(() -> GameTime.at(0, 1, 1))
					.isInstanceOf(IllegalArgumentException.class);
			assertThatThrownBy(() -> GameTime.at(5, 1, 1))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Example
		void if_minute_is_outside_0_through_9() {
			assertThatThrownBy(() -> GameTime.at(1, -1, 1))
					.isInstanceOf(IllegalArgumentException.class);
			assertThatThrownBy(() -> GameTime.at(1, 10, 1))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Example
		void if_second_is_outside_0_through_59() {
			assertThatThrownBy(() -> GameTime.at(1, 0, -1))
					.isInstanceOf(IllegalArgumentException.class);
			assertThatThrownBy(() -> GameTime.at(1, 0, 60))
					.isInstanceOf(IllegalArgumentException.class);
		}

	}

	@Group
	class is_ordered {

		@Example
		void firstly_by_quarter() {
			GameTime q1 = GameTime.at(1, 2, 3);
			GameTime q2 = GameTime.at(2, 2, 3);
			assertThat(q1).isLessThan(q2);
			assertThat(q1).isEqualTo(q1);
		}

		@Example
		void secondly_by_minute() {
			GameTime m1 = GameTime.at(2, 1, 3);
			GameTime m2 = GameTime.at(2, 2, 3);
			assertThat(m1).isLessThan(m2);
			assertThat(m1).isEqualTo(m1);
		}

		@Example
		void by_second_last() {
			GameTime s1 = GameTime.at(2, 2, 1);
			GameTime s2 = GameTime.at(2, 2, 2);
			assertThat(s1).isLessThan(s2);
			assertThat(s1).isEqualTo(s1);
		}

	}

}
