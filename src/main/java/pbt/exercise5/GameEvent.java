package pbt.exercise5;

public class GameEvent {

	public enum Type {
		SCORE_1,
		SCORE_2,
		SCORE_3,
		SUB_IN,
		SUB_OUT
	}

	private final Type type;
	private final GameTime gameTime;
	private final String player;

	public GameEvent(Type type, GameTime gameTime, String player) {

		this.type = type;
		this.gameTime = gameTime;
		this.player = player;
	}

	public Type type() {
		return type;
	}

	public GameTime gameTime() {
		return gameTime;
	}

	public String player() {
		return player;
	}

	public boolean isSubstitution() {
		return type == Type.SUB_IN || type == Type.SUB_OUT;
	}

	@Override
	public String toString() {
		return String.format("[%s] Player %2s: %s", gameTime, player, type);
	}
}
