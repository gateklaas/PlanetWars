public class SimpleAdaptiveBot
{
	public static void doTurn(PlanetWars pw)
	{
		int method = 0;
		for (Planet notMyPlanet : pw.NotMyPlanets())
		{
			if (notMyPlanet.NumShips() <= notMyPlanet.GrowthRate())
			{
				method = 1;
				break;
			}
		}

		switch (method)
		{
			case 0:
				SuperBullyBot.doTurn(pw);
			break;

			case 1:
				BullyBot.doTurn(pw);
			break;

			default:
				RandomBot.doTurn(pw);
			break;
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
		{}
	}
}
