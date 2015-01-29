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
		PlanetWars.log("Current value: " + gameState.evaluate());

		minimax = new Minimax();
		minimax.interrupt = false;
		minimax.maxDepth = maxDepth;
		minimax.alphabeta(gameState, maxDepth, -1000000, 1000000, true);

		PlanetWars.log("Best value: " + minimax.bestMove.getGameStateAfterMove().evaluate());
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
			return gameState.evaluate();

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
}
