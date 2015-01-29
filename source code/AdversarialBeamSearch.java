import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class AdversarialBeamSearch
{
	static class ComparableGameState implements Comparable<ComparableGameState>
	{
		int depth;
		Move move;
		GameState gameState;
		int value;

		@Override
		public int compareTo(ComparableGameState o)
		{
			return value - o.value;
		}
	}

	/** Returns the best move possible found by beam search where the adversary is a SuperBullyBot */
	public static Move findBestMove(GameState gameState, int beamSize, int nodeLimit)
	{
		PlanetWars.log("Current value: " + gameState.evaluate());
		Move bestMove = null;

		ComparableGameState cgs = new ComparableGameState();
		cgs.gameState = gameState;
		cgs.depth = 0;
		cgs.value = -1000000;

		LinkedList<ComparableGameState> beamList = new LinkedList<ComparableGameState>();
		beamList.add(cgs);

		for (int i = 0; i < nodeLimit && !beamList.isEmpty(); i++)
		{
			cgs = beamList.removeLast();

			if (cgs.depth == 1)
				bestMove = cgs.move;

			if (cgs.gameState != null)
			{
				List<Move> moveList = cgs.gameState.simulateAllPossibleMoves(true);
				for (Move move : moveList)
				{
					if (beamList.size() == beamSize)
						beamList.removeFirst();

					ComparableGameState newCgs = new ComparableGameState();
					newCgs.depth = cgs.depth + 1;
					newCgs.move = move;

					Move adversaryMove = SuperBullyBot.findEnemyBestMove(move.getGameStateAfterMove());

					if (adversaryMove == null)
					{
						newCgs.gameState = null;
						newCgs.value = move.getGameStateAfterMove().evaluate();
					}
					else
					{
						newCgs.gameState = adversaryMove.getGameStateAfterMove();
						newCgs.value = newCgs.gameState.evaluate();
					}

					ListIterator<ComparableGameState> iterator = beamList.listIterator();
					while (iterator.hasNext())
					{
						if (newCgs.compareTo(iterator.next()) < 0)
						{
							iterator.previous();
							iterator.add(newCgs);
							break;
						}
					}

					if (!iterator.hasNext())
						iterator.add(newCgs);
				}
			}
		}

		if (bestMove != null)
			PlanetWars.log("Best value: " + bestMove.getGameStateAfterMove().evaluate());

		return bestMove;
	}
}
