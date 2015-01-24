public class SimpleAdaptiveBot
{
	public static final int RANDOM_BOT = 0;
	public static final int BULLY_BOT = 1;
	public static final int SUPER_BULLY_BOT = 2;

	public static void doTurn(PlanetWars pw)
	{
		switch (evaluateSituation(pw))
		{
			case RANDOM_BOT:
				RandomBot.doTurn(pw);
			break;

			case BULLY_BOT:
				BullyBot.doTurn(pw);
			break;

			case SUPER_BULLY_BOT:
				SuperBullyBot.doTurn(pw);
			break;

			default:
				PlanetWars.log("Skip turn");
			break;
		}
	}

	/** Returns the strategy that matches the situation best */
	public static int evaluateSituation(PlanetWars pw)
	{
		for (Planet notMyPlanet : pw.NotMyPlanets())
		{
			if (notMyPlanet.NumShips() <= notMyPlanet.GrowthRate())
			{
				return BULLY_BOT;
			}
		}

		return SUPER_BULLY_BOT;
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
