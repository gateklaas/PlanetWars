public class MMBot
{
	public static void doTurn(PlanetWars planetWars)
	{
		// Start timer
		MinimaxInterruptTimer timer = new MinimaxInterruptTimer();
		timer.start();

		// Find our best move, look 4 moves ahead
		GameState currentGameState = new GameState(planetWars.Planets());
		Move bestMove = Minimax.findBestMove(currentGameState, 4);

		if (bestMove == null)
		{
			// We're out of moves. Game over
			System.exit(0);
			return;
		}

		// Attack!
		planetWars.IssueOrder(bestMove.getSourcePlanet(), bestMove.getDestinationPlanet());
	}

	static class MinimaxInterruptTimer extends Thread
	{
		public MinimaxInterruptTimer()
		{
			// Not a vital process, thus low priority.
			setPriority(Thread.MIN_PRIORITY);
		}

		@Override
		public void run()
		{
			// Wait 800 milliseconds
			try
			{
				Thread.sleep(800);
			}
			catch (InterruptedException e)
			{}

			// Stops minimax neatly if it is still not finished
			Minimax.interrupt();
		}
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
