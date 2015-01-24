import java.util.List;

public class GreedyHillClimbing
{
	/** Returns the best move possible according to greedy hill climbing */
	public static Move findBestMove(GameState gameState)
	{
		PlanetWars.log("Current value: " + calcHeuristicValue(gameState));

		List<Move> allpossibleMovesList = gameState.simulateAllPossibleMoves(true);
		int bestValue = -1000000;
		Move bestMove = null;

		for (Move move : allpossibleMovesList)
		{
			int value = calcHeuristicValue(move.getGameStateAfterMove());

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
	 * Returns a value that describes how good we are doing. <br>
	 * A high value is good, a low value is bad.
	 */
	public static int calcHeuristicValue(GameState gameState)
	{
		int value = 0;

		for (Planet planet : gameState.planetArray)
		{
			if (planet.Owner() == 1) // if we own this planet
			{
				value += planet.GrowthRate() * 5 + planet.NumShips();
			}
			else if (planet.Owner() >= 2) // if the enemy owns this planet
			{
				value -= planet.GrowthRate() * 5 + planet.NumShips();
			}
		}

		return value;
	}
}
