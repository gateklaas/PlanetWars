import java.io.PrintWriter;
import java.io.StringWriter;

/*
 * A bit smarter kind of bot, who searches for its strongest planet and then attacks the weakest planet. The score is computed
 * based on the number of ships.
 */

public class BullyBot
{
	public static void doTurn(PlanetWars planetWars)
	{
		// (1) Find my strongest planet.
		Planet source = null;
		double sourceScore = Double.MIN_VALUE;
		for (Planet myPlanet : planetWars.MyPlanets())
		{
			double score = myPlanet.NumShips();

			if (score > sourceScore)
			{
				sourceScore = score;
				source = myPlanet;
			}
		}

		// (2) Find weakest enemy or neutral planet.
		Planet dest = null;
		double destScore = Double.MAX_VALUE;
		for (Planet notMyPlanet : planetWars.NotMyPlanets())
		{
			double score = notMyPlanet.NumShips();

			if (score < destScore)
			{
				destScore = score;
				dest = notMyPlanet;
			}
		}

		// (3) Attack!
		if (source != null && dest != null)
			planetWars.IssueOrder(source, dest);
	}

	/** Returns the best enemy move */
	public static Move findEnemyBestMove(GameState gameState)
	{
		// (1) Find the enemies strongest planet.
		Planet source = null;
		double sourceScore = Double.MIN_VALUE;
		for (Planet enemyPlanet : gameState.planetArray)
		{
			if (enemyPlanet.Owner() >= 2)
			{
				double score = enemyPlanet.NumShips();

				if (score > sourceScore)
				{
					sourceScore = score;
					source = enemyPlanet;
				}
			}
		}
		if (source == null)
			return null;

		// (2) Find my or neutrals weakest planet.
		Planet dest = null;
		double destScore = Double.MAX_VALUE;
		for (Planet notEnemyPlanet : gameState.planetArray)
		{
			if (notEnemyPlanet.Owner() < 2)
			{
				double score = notEnemyPlanet.NumShips();

				if (score < destScore)
				{
					destScore = score;
					dest = notEnemyPlanet;
				}
			}
		}
		if (dest == null)
			return null;

		// (3) Attack!
		return Move.simulate(gameState, source, dest);
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
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			String stackTrace = writer.toString();
			System.err.println(stackTrace);
			System.exit(1); // just stop now. we've got a problem
		}
	}
}
