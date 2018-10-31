package pbt.exercise3;

import java.util.*;

import net.jqwik.api.*;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;

@Label("A basketball game")
class BasketballGameTests {

	@Example
	void can_be_created_with_12_players() {
		Set<String> players = new HashSet<>(asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
		BasketballGame game = new BasketballGame(players);
		assertThat(game.players()).hasSize(12);
	}

	@Example
	void has_player_list_sorted_by_number() {
		Set<String> players = new HashSet<>(asList("1", "11", "12", "4", "41", "42", "9"));
		BasketballGame game = new BasketballGame(players);
		assertThat(game.players()).containsExactly(
				"1", "4", "9", "11", "12", "41", "42"
		);
	}

	@Example
	void cannot_have_more_than_12_players() {
		Set<String> players = new HashSet<>(asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"));
		assertThatThrownBy(() -> new BasketballGame(players)).isInstanceOf(IllegalArgumentException.class);
	}

	@Group
	class can_receive {

		@Example
		void scoring_events_for_registered_player() {
			Set<String> players = asSet("1", "22", "33", "44", "55", "66", "77", "88", "99");
			BasketballGame game = new BasketballGame(players);

			GameEvent player1scores1 = new GameEvent(GameEvent.Type.SCORE_1, GameTime.at(1, 4, 23), "1");
			game.receiveEvent(player1scores1);

			GameEvent player22scores2 = new GameEvent(GameEvent.Type.SCORE_2, GameTime.at(2, 0, 59), "22");
			game.receiveEvent(player22scores2);

			GameEvent player33scores3 = new GameEvent(GameEvent.Type.SCORE_3, GameTime.at(4, 9, 59), "33");
			game.receiveEvent(player33scores3);

			assertThat(game.events()).hasSize(3);
			assertThat(game.eventsFor("1")).hasSize(1);
			assertThat(game.eventsFor("99")).hasSize(0);
		}

		@Example
		void SUB_IN_event_for_player_not_on_field() {
			Set<String> players = asSet("1", "22", "33", "44", "55", "66", "77", "88", "99");
			BasketballGame game = new BasketballGame(players);

			GameEvent player1subIn = new GameEvent(GameEvent.Type.SUB_IN, GameTime.at(1, 4, 23), "1");
			game.receiveEvent(player1subIn);
			assertThat(game.eventsFor("1")).hasSize(1);
		}

		@Example
		void SUB_OUT_event_for_player_on_field() {
			Set<String> players = asSet("1", "22", "33", "44", "55", "66", "77", "88", "99");
			BasketballGame game = new BasketballGame(players);

			GameEvent player1subIn = new GameEvent(GameEvent.Type.SUB_IN, GameTime.at(1, 4, 23), "1");
			game.receiveEvent(player1subIn);
			GameEvent player1subOut = new GameEvent(GameEvent.Type.SUB_OUT, GameTime.at(1, 4, 23), "1");
			game.receiveEvent(player1subOut);

			assertThat(game.eventsFor("1")).hasSize(2);
		}
	}

	private Set<String> asSet(String... players) {
		return new HashSet<>(asList(players));
	}

	@Group
	class cannot_receive {

		@Example
		void events_for_unregistered_player() {
			Set<String> players = asSet("22", "33", "44", "55", "66", "77", "88", "99");
			BasketballGame game = new BasketballGame(players);

			GameEvent player22scores2 = new GameEvent(GameEvent.Type.SCORE_2, GameTime.at(2, 0, 59), "22");
			game.receiveEvent(player22scores2);

			GameEvent unregisteredPlayerScores = new GameEvent(GameEvent.Type.SCORE_1, GameTime.at(3, 4, 23), "11");
			assertThatThrownBy( () -> game.receiveEvent(unregisteredPlayerScores));

			assertThat(game.events()).hasSize(1);
		}

		@Example
		void SUB_OUT_event_for_player_not_on_field() {
			Set<String> players = asSet("1", "22", "33", "44", "55", "66", "77", "88", "99");
			BasketballGame game = new BasketballGame(players);

			GameEvent player1subOut = new GameEvent(GameEvent.Type.SUB_OUT, GameTime.at(1, 4, 23), "1");
			assertThatThrownBy(() -> game.receiveEvent(player1subOut)).isInstanceOf(EventOutOfOrder.class);

			assertThat(game.eventsFor("1")).hasSize(0);
		}

		@Example
		void SUB_IN_event_for_player_on_field() {
			Set<String> players = asSet("1", "22", "33", "44", "55", "66", "77", "88", "99");
			BasketballGame game = new BasketballGame(players);

			GameEvent player1subIn = new GameEvent(GameEvent.Type.SUB_IN, GameTime.at(1, 4, 23), "1");
			game.receiveEvent(player1subIn);

			GameEvent player1subInAgain = new GameEvent(GameEvent.Type.SUB_IN, GameTime.at(1, 5, 23), "1");
			assertThatThrownBy(() -> game.receiveEvent(player1subInAgain)).isInstanceOf(EventOutOfOrder.class);

			assertThat(game.eventsFor("1")).hasSize(1);
		}

	}
}
