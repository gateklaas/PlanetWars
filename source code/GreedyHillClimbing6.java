import java.util.List;

public class GreedyHillClimbing6
{
	/** Returns the best move possible according to greedy hill climbing */
	public static Move findBestMove(GameState gameState)
	{
		PlanetWars.log("Current value: " + evaluateGameState(gameState));

		List<Move> allpossibleMovesList = gameState.simulateAllPossibleMoves(true);
		int bestValue = -1000000;
		Move bestMove = null;

		for (Move move : allpossibleMovesList)
		{
			int value = evaluateGameState(move.getGameStateAfterMove());

			if (value > bestValue)
			{
				bestMove = move;
				bestValue = value;
			}
		}

		PlanetWars.log("Best value: " + bestValue);
		return bestMove;
	}

	/**
	 * Evaluate the current game-state. <br>
	 * Returns the heuristic value. <br>
	 * A high value is good, a low value is bad.
	 */
	public static int evaluateGameState(GameState gameState)
	{
		int value = 0;

		for (Planet planet : gameState.planetArray)
		{
			if (planet.Owner() >= 2) // if the enemy owns this planet
			{
				value -= planet.GrowthRate() + planet.NumShips();
			}
		}

		return value;
	}
}
