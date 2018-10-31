package pbt.exercise5;

import java.time.*;
import java.util.*;
import java.util.stream.*;

public class PlayerStats {
	private final int points;
	private final Duration minutes;

	public PlayerStats(List<GameEvent> playerEvents) {
		points = calculatePointsPerGame(playerEvents);
		minutes = calculateMinutesPerGame(playerEvents);
	}

	private Duration calculateMinutesPerGame(List<GameEvent> playerEvents) {
		List<GameEvent> subEvents =
				playerEvents.stream()
							.filter(GameEvent::isSubstitution)
							.collect(Collectors.toList());

		Duration minutesPerGame = Duration.ofMinutes(0);

		Duration lastIn = Duration.ofMinutes(0);
		for (GameEvent event : subEvents) {
			GameEvent.Type type = event.type();
			GameTime gameTime = event.gameTime();
			if (type == GameEvent.Type.SUB_IN) {
				lastIn = gameTime.minuteInGame();
			}
			if (type == GameEvent.Type.SUB_OUT) {
				Duration lastOut = gameTime.minuteInGame();
				Duration playTime = lastOut.minus(lastIn);
				minutesPerGame = minutesPerGame.plus(playTime);
			}
		}

		return minutesPerGame;
	}

	private int calculatePointsPerGame(List<GameEvent> playerEvents) {
		return playerEvents.stream()
						   .mapToInt(e -> toPoints(e.type()))
						   .sum();
	}

	private int toPoints(GameEvent.Type type) {
		switch (type) {
			case SCORE_1:
				return 1;
			case SCORE_2:
				return 2;
			case SCORE_3:
				return 3;
		}
		return 0;
	}

	public String minutesPerGame() {
		return String.format("%02d:%02d", minutes.toMinutes(), minutes.getSeconds() % 60);
	}

	public int pointsPerGame() {
		return points;
	}
}
