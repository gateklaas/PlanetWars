import java.util.List;

public class GreedyHillClimbing
{
	/** Returns the best move possible according to greedy hill climbing */
	public static Move findBestMove(GameState gameState)
	{
		PlanetWars.log("Current value: " + gameState.evaluate());

		List<Move> allpossibleMovesList = gameState.simulateAllPossibleMoves(true);
		int bestValue = -1000000;
		Move bestMove = null;

		for (Move move : allpossibleMovesList)
		{
			int value = move.getGameStateAfterMove().evaluate();

			if (value > bestValue)
			{
				bestMove = move;
				bestValue = value;
			}
		}

		PlanetWars.log("Best value: " + bestValue);
		return bestMove;
	}
}
