import java.util.List;

public class Minimax
{
	private static Minimax minimax;

	private Move bestMove;
	private int maxDepth;
	private boolean interrupt;

	/** Returns the best move possible according to the minimax algorithm with alpha–beta pruning */
	public static Move findBestMove(GameState gameState, int maxDepth)
	{
		PlanetWars.log("Current value: " + calcHeuristicValue(gameState));

		minimax = new Minimax();
		minimax.interrupt = false;
		minimax.maxDepth = maxDepth;
		minimax.alphabeta(gameState, maxDepth, -1000000, 1000000, true);

		PlanetWars.log("Best value: " + calcHeuristicValue(minimax.bestMove.getGameStateAfterMove()));
		return minimax.bestMove;
	}

	/**
	 * The minimax algorithm with alpha–beta pruning <br>
	 * Pseudocode: https://en.wikipedia.org/wiki/Alpha-beta_pruning#Pseudocode
	 */
	private int alphabeta(GameState gameState, int depth, int alpha, int beta, boolean max)
	{
		List<Move> moveList = gameState.simulateAllPossibleMoves(max);

		if (depth == 0 || interrupt || moveList.isEmpty())
			return calcHeuristicValue(gameState);

		int value;
		if (max)
		{
			for (Move move : moveList)
			{
				value = alphabeta(move.getGameStateAfterMove(), depth - 1, alpha, beta, false);

				if (value > alpha)
				{
					alpha = value;

					if (depth == maxDepth)
						bestMove = move;
				}

				if (beta <= alpha)
					break;
			}
			return alpha;
		}
		else
		{
			for (Move move : moveList)
			{
				value = alphabeta(move.getGameStateAfterMove(), depth - 1, alpha, beta, true);

				if (value < beta)
					beta = value;

				if (beta <= alpha)
					break;
			}
			return beta;
		}
	}

	/** Forces minimax to stop */
	public static void interrupt()
	{
		minimax.interrupt = true;

		if (minimax.bestMove != null)
			PlanetWars.log("Minimax was interrupted");
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
