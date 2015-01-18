import java.io.PrintWriter;
import java.io.StringWriter;

/*
 * Searches for its strongest planet and then attacks the weakest ENEMY planet. The score is computed based on the number of
 * ships.
 */

public class SuperBullyBot
{
	public static void doTurn(PlanetWars pw)
	{
		// (1) Find my strongest planet.
		Planet source = null;
		double sourceScore = Double.MIN_VALUE;
		for (Planet myPlanet : pw.MyPlanets())
		{
			double score = myPlanet.NumShips();

			if (score > sourceScore)
			{
				sourceScore = score;
				source = myPlanet;
			}
		}

		// (2) Find the weakest enemy planet.
		Planet dest = null;
		double destScore = Double.MAX_VALUE;
		for (Planet notMyPlanet : pw.EnemyPlanets()) // <- DIFFERENT FROM BULLY_BOT
		{
			double score = (notMyPlanet.NumShips());

			if (score < destScore)
			{
				destScore = score;
				dest = notMyPlanet;
			}
		}

		// (3) Attack!
		if (source != null && dest != null)
			pw.IssueOrder(source, dest);
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
