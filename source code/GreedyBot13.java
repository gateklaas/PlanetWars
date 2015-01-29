public class GreedyBot13
{
	public static void doTurn(PlanetWars planetWars)
	{
		// Find our best move
		GameState currentGameState = new GameState(planetWars.Planets());
		Move bestMove = GreedyHillClimbing13.findBestMove(currentGameState);

		if (bestMove == null)
		{
			// We're out of moves. Game over
			System.exit(0);
			return;
		}

		// Attack!
		planetWars.IssueOrder(bestMove.getSourcePlanet(), bestMove.getDestinationPlanet());
	}

	public static void main(String[] args)
	{
		String line = "";
		String message = "";
		int c;
		try
		{
			while ((c = System.in.read()) >= 0)
			{
				switch (c)
				{
					case '\n':
						if (line.equals("go"))
						{
							PlanetWars pw = new PlanetWars(message);
							doTurn(pw);
							pw.FinishTurn();
							message = "";
						}
						else
						{
							message += line + "\n";
						}
						line = "";
					break;
					default:
						line += (char) c;
					break;
				}
			}
		}
		catch (Exception e)
		{

		}
	}
}
