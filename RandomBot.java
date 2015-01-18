import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Random;

/*
 * RandomBot - an example bot that picks up one of his planets and send half of the ships from that planet to a random target
 * planet.
 */
public class RandomBot
{
	public static void doTurn(PlanetWars pw)
	{
		// (0) Create a random number generator
		Random random = new Random();

		// (1) Pick one of my planets at random.
		Planet source = null;
		List<Planet> myPlanets = pw.MyPlanets();

		if (myPlanets.size() > 0)
		{
			Integer randomSource = random.nextInt(myPlanets.size());
			source = myPlanets.get(randomSource);
		}

		// (2) Pick a target planet at random
		Planet dest = null;
		List<Planet> allPlanets = pw.NotMyPlanets();

		if (allPlanets.size() > 0)
		{
			Integer randomTarget = random.nextInt(allPlanets.size());
			dest = allPlanets.get(randomTarget);
		}

		// (3) Send half the ships from source to destination
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
