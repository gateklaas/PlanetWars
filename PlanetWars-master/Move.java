public class Move
{
	private Planet srcPlanetBeforeMove;
	private Planet dstPlanetBeforeMove;
	private GameState gameStateAfterMove;

	public Move(Planet srcPlanetBeforeMove, Planet dstPlanetBeforeMove, GameState gameStateAfterMove)
	{
		this.srcPlanetBeforeMove = srcPlanetBeforeMove;
		this.dstPlanetBeforeMove = dstPlanetBeforeMove;
		this.gameStateAfterMove = gameStateAfterMove;
	}

	/** Simulate a move AND create the new game-state */
	public static Move simulate(GameState gameStateBeforeMove, Planet srcPlanetBeforeMove, Planet dstPlanetBeforeMove)
	{
		// the new game-state will be similar to the old game-state
		GameState gameStateAfterMove = (GameState) gameStateBeforeMove.clone();

		// find same planets in new game-state
		Planet srcPlanetAfterMove = gameStateAfterMove.planetArray[srcPlanetBeforeMove.PlanetID()];
		Planet dstPlanetAfterMove = gameStateAfterMove.planetArray[dstPlanetBeforeMove.PlanetID()];

		// send fleet
		int fleetSize = srcPlanetBeforeMove.NumShips() / 2;
		srcPlanetAfterMove.RemoveShips(fleetSize);

		// after fleet arrival
		if (dstPlanetAfterMove.Owner() == srcPlanetAfterMove.Owner())
		{
			dstPlanetAfterMove.AddShips(fleetSize);
		}
		else
		{
			dstPlanetAfterMove.RemoveShips(fleetSize);

			if (dstPlanetAfterMove.NumShips() < 0)
			{
				dstPlanetAfterMove.Owner(srcPlanetAfterMove.Owner());
				dstPlanetAfterMove.RemoveShips(dstPlanetAfterMove.NumShips() * 2);
			}
		}

		// simulate planet growth
		for (Planet planet : gameStateAfterMove.planetArray)
			if (planet.Owner() != 0)
				planet.AddShips(planet.GrowthRate());

		// make move
		return new Move(srcPlanetBeforeMove, dstPlanetBeforeMove, gameStateAfterMove);
	}

	public Planet getSourcePlanet()
	{
		return srcPlanetBeforeMove;
	}

	public Planet getDestinationPlanet()
	{
		return dstPlanetBeforeMove;
	}

	public GameState getGameStateAfterMove()
	{
		return gameStateAfterMove;
	}
}
