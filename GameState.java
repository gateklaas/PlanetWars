import java.util.LinkedList;
import java.util.List;

public class GameState
{
	public Planet[] planetArray;

	public GameState(Planet[] planetArray)
	{
		this.planetArray = planetArray;
	}

	public GameState(List<Planet> planetList)
	{
		planetArray = new Planet[planetList.size()];
		planetList.toArray(planetArray);
	}

	/** Simulate all possible moves. Returns a list with the simulated moves. */
	public List<Move> simulateAllPossibleMoves(boolean max)
	{
		List<Move> allPossibleMovesList = new LinkedList<Move>();

		if (max)
		{
			// max: send fleet from myPlanet to notMyPlanet
			for (Planet srcPlanet : planetArray)
				if (srcPlanet.Owner() == 1)
					for (Planet dstPlanet : planetArray)
						if (dstPlanet.Owner() != 1)
							allPossibleMovesList.add(Move.simulate(this, srcPlanet, dstPlanet));
		}
		else
		{
			// min: send fleet from enemyPlanet to notEnemyPlanet
			for (Planet srcPlanet : planetArray)
				if (srcPlanet.Owner() >= 2)
					for (Planet dstPlanet : planetArray)
						if (dstPlanet.Owner() < 2)
							allPossibleMovesList.add(Move.simulate(this, srcPlanet, dstPlanet));
		}

		return allPossibleMovesList;
	}

	/** Returns a thorough copy of this object */
	@Override
	public Object clone()
	{
		Planet[] newPlanetArray = new Planet[planetArray.length];

		for (Planet planet : planetArray)
			newPlanetArray[planet.PlanetID()] = (Planet) planet.clone();

		return new GameState(newPlanetArray);
	}
}
