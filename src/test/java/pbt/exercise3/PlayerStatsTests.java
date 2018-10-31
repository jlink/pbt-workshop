package pbt.exercise3;

import java.util.*;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.*;
import static pbt.exercise3.GameEvent.Type.*;

@Label("Player Statistics")
class PlayerStatsTests {

	private Set<String> players = new HashSet<>(Arrays.asList("0", "1", "2", "3", "4", "5"));
	private BasketballGame game = new BasketballGame(players);

	private static final String PLAYER_0 = "0";
	private static final String PLAYER_1 = "1";
	private static final String PLAYER_2 = "2";

	@Example
	void cannot_get_stats_for_unregistered_player() {
		assertThatThrownBy(() -> game.statsFor("99")).isInstanceOf(IllegalArgumentException.class);
	}

	@Group
	class minutes_per_game {

		@Example
		void for_dnp_player() {
			PlayerStats playerStats = game.statsFor(PLAYER_0);
			assertThat(playerStats.minutesPerGame()).isEqualTo("00:00");
		}

		@Example
		void player_with_single_substitution_time() {
			GameEvent player1in1 = new GameEvent(SUB_IN, GameTime.at(1, 0, 0), PLAYER_1);
			GameEvent player1out1 = new GameEvent(SUB_OUT, GameTime.at(1, 5, 23), PLAYER_1);
			game.receiveEvent(player1in1);
			game.receiveEvent(player1out1);

			PlayerStats playerStats = game.statsFor(PLAYER_1);
			assertThat(playerStats.minutesPerGame()).isEqualTo("05:23");
		}

		@Example
		void player_with_several_subs_not_crossing_quarters() {
			GameEvent player1in1 = new GameEvent(SUB_IN, GameTime.at(1, 0, 0), PLAYER_1);
			GameEvent player1out1 = new GameEvent(SUB_OUT, GameTime.at(1, 5, 23), PLAYER_1);
			GameEvent player1in2 = new GameEvent(SUB_IN, GameTime.at(1, 7, 30), PLAYER_1);
			GameEvent player1out2 = new GameEvent(SUB_OUT, GameTime.at(1, 8, 40), PLAYER_1);
			GameEvent player1in3 = new GameEvent(SUB_IN, GameTime.at(3, 0, 0), PLAYER_1);
			GameEvent player1out3 = new GameEvent(SUB_OUT, GameTime.at(3, 3, 45), PLAYER_1);

			game.receiveEvent(player1in1);
			game.receiveEvent(player1out1);
			game.receiveEvent(player1in2);
			game.receiveEvent(player1out2);
			game.receiveEvent(player1in3);
			game.receiveEvent(player1out3);

			PlayerStats playerStats = game.statsFor(PLAYER_1);

			// 5:23 + 1:10 + 3:45 = 10:28
			assertThat(playerStats.minutesPerGame()).isEqualTo("10:18");
		}

		@Example
		void player_with_subs_crossing_quarters() {
			GameEvent player1in1 = new GameEvent(SUB_IN, GameTime.at(1, 0, 0), PLAYER_1);
			GameEvent player1out1 = new GameEvent(SUB_OUT, GameTime.at(1, 5, 23), PLAYER_1);
			GameEvent player1in2 = new GameEvent(SUB_IN, GameTime.at(1, 7, 30), PLAYER_1);
			GameEvent player1out2 = new GameEvent(SUB_OUT, GameTime.at(2, 2, 40), PLAYER_1);
			GameEvent player1in3 = new GameEvent(SUB_IN, GameTime.at(3, 0, 0), PLAYER_1);
			GameEvent player1out3 = new GameEvent(SUB_OUT, GameTime.at(4, 3, 45), PLAYER_1);

			game.receiveEvent(player1in1);
			game.receiveEvent(player1out1);
			game.receiveEvent(player1in2);
			game.receiveEvent(player1out2);
			game.receiveEvent(player1in3);
			game.receiveEvent(player1out3);

			PlayerStats playerStats = game.statsFor(PLAYER_1);

			// 5:23 + 5:10 + 13:45 = 24:18
			assertThat(playerStats.minutesPerGame()).isEqualTo("24:18");
		}

	}

	@Group
	class points_per_game {

		@Example
		void for_dnp_player() {
			PlayerStats playerStats = game.statsFor(PLAYER_0);
			assertThat(playerStats.pointsPerGame()).isEqualTo(0);
		}

		@Example
		void for_player_with_single_score() {
			GameEvent player1scores3 = new GameEvent(SCORE_3, GameTime.at(1, 2, 3), PLAYER_1);
			game.receiveEvent(player1scores3);
			PlayerStats playerStats = game.statsFor(PLAYER_1);

			assertThat(playerStats.pointsPerGame()).isEqualTo(3);
		}

		@Example
		void for_player_with_few_scores() {
			GameEvent player2scores3 = new GameEvent(SCORE_3, GameTime.at(1, 2, 3), PLAYER_2);
			GameEvent player2scores1 = new GameEvent(SCORE_1, GameTime.at(2, 4, 6), PLAYER_2);
			GameEvent player2scores2 = new GameEvent(SCORE_2, GameTime.at(4, 8, 12), PLAYER_2);
			game.receiveEvent(player2scores3);
			game.receiveEvent(player2scores1);
			game.receiveEvent(player2scores2);
			PlayerStats playerStats = game.statsFor(PLAYER_2);

			assertThat(playerStats.pointsPerGame()).isEqualTo(6);
		}

	}
}
