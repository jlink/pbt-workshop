package pbt.solution5;

import java.util.*;
import java.util.stream.*;

import pbt.exercise5.*;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;

import static org.assertj.core.api.Assertions.*;

class BasketballGamePropertiesSolution {

	@Property
	void can_be_created_with_5_to_12_players(@ForAll("playerNumbers") @Size(min = 5, max = 12) List<Integer> playerNumbers) {
		Set<String> players = toPlayers(playerNumbers);
		BasketballGame game = new BasketballGame(players);
		assertThat(game.players()).hasSize(playerNumbers.size());
	}

	@Property
	void cannot_have_more_than_12_players(@ForAll("playerNumbers") @Size(min = 13, max = 24) List<Integer> playerNumbers) {
		Set<String> moreThan12Players = toPlayers(playerNumbers);
		assertThatThrownBy(() -> new BasketballGame(moreThan12Players)).isInstanceOf(IllegalArgumentException.class);
	}

	@Property
	void cannot_have_less_than_5_players(@ForAll("playerNumbers") @Size(max = 4) List<Integer> playerNumbers) {
		Set<String> max4Players = toPlayers(playerNumbers);
		assertThatThrownBy(() -> new BasketballGame(max4Players)).isInstanceOf(IllegalArgumentException.class);
	}

	@Property
	void has_player_list_sorted_by_number(@ForAll("playerNumbers") @Size(min = 5, max = 12) List<Integer> playerNumbers) {
		BasketballGame game = new BasketballGame(toPlayers(playerNumbers));

		playerNumbers.sort(Integer::compareTo);
		List<String> sortedPlayers = playerNumbers.stream().map(n -> Integer.toString(n)).collect(Collectors.toList());

		assertThat(game.players()).isEqualTo(sortedPlayers);
	}


	private Set<String> toPlayers(@ForAll("playerNumbers") @Size(min = 5, max = 12) List<Integer> playerNumbers) {
		return playerNumbers.stream().map(n -> Integer.toString(n)).collect(Collectors.toSet());
	}

	@Provide
	SizableArbitrary<List<Integer>> playerNumbers() {
		return Arbitraries.integers().between(0, 99).unique().list();
	}

}
