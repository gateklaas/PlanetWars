import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/** Place file inside PlanetWars-master. Run Logger to start */
public class Logger
{
	// Path to this directory
	public static final String PATH = "C:/Users/klaas/Workspaces/Python/PlanetWars-master/";

	// All bots
	public static final String[] BOTS = { "java RandomBot", "java BullyBot", "java AdaptiveBot", "java LookaheadBot",
			"java SuperBullyBot", "java GreedyBot", "java MMBot", "java SimpleAdaptiveBot" };

	// All maps
	public static final String[] MAPS = { "maps/8planets/map1.txt", "maps/8planets/map2.txt", "maps/8planets/map3.txt" };

	public static void main(String[] args) throws IOException
	{
		// WHAT DO WE WANT TO LOG: (only three options)

		// logOneVsOne("java RandomBot", "java RandomBot", "serial", "10000");
		// logOneVsAll("java MMBot", "serial", "10000");
		logAllVsAll("serial", "10000");
	}

	/** All BOTS vs all BOTS on all MAPS. Log results. */
	public static void logAllVsAll(String mode, String time)
	{
		boolean fail;
		Map<Battle, Battle.Result> battleMap = new HashMap<Battle, Battle.Result>();

		do
		{
			fail = false;

			if (Battle.allVsAll(battleMap, mode, time))
				fail = true;

			System.out.println("Logging");
			if (Battle.Result.log(battleMap))
				fail = true;

			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{}
			System.out.println("Retry failed battles");
		}
		while (fail);
	}

	/** One bot vs all BOTS on all MAPS. Log results. */
	public static void logOneVsAll(String bot, String mode, String time)
	{
		boolean fail;
		Map<Battle, Battle.Result> battleMap = new HashMap<Battle, Battle.Result>();

		do
		{
			fail = false;

			if (Battle.oneVsAll(battleMap, bot, mode, time))
				fail = true;

			System.out.println("Logging");
			if (Battle.Result.log(bot, battleMap))
				fail = true;

			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{}
			System.out.println("Retry failed battles");
		}
		while (fail);
	}

	/** One bot vs one bot on all MAPS. Log results. */
	public static void logOneVsOne(String bot, String opponent, String mode, String time)
	{
		boolean fail;
		Map<Battle, Battle.Result> battleMap = new HashMap<Battle, Battle.Result>();

		do
		{
			fail = false;

			if (Battle.oneVsOne(battleMap, bot, opponent, mode, time))
				fail = true;

			System.out.println("Logging");
			if (Battle.Result.log(bot, battleMap))
				fail = true;

			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{}
			System.out.println("Retry failed battles");
		}
		while (fail);
	}

	static class Timer
	{
		private long startTime;

		public void start()
		{
			startTime = System.nanoTime();
		}

		public void end(PlanetWars pw)
		{
			long elapsedTime = System.nanoTime() - startTime;
			System.err.println("time " + elapsedTime);
			System.err.flush();
		}
	}

	static class Battle
	{
		private final String player1;
		private final String player2;
		private final String map;
		private final String mode;
		private final String time;

		/** do all BOTS vs all BOTS */
		public static boolean allVsAll(Map<Battle, Result> battleMap, String mode, String time)
		{
			boolean battleFailed = false;
			for (String player1 : BOTS)
				if (oneVsAll(battleMap, player1, mode, time))
					battleFailed = true;
			return battleFailed;
		}

		/** do bot vs all BOTS */
		public static boolean oneVsAll(Map<Battle, Result> battleMap, String player1, String mode, String time)
		{
			boolean battleFailed = false;
			for (String player2 : BOTS)
				if (oneVsOne(battleMap, player1, player2, mode, time))
					battleFailed = true;
			return battleFailed;
		}

		/** do bot vs bot */
		public static boolean oneVsOne(Map<Battle, Result> battleMap, String player1, String player2, String mode, String time)
		{
			boolean battleFailed = false;
			for (String map : MAPS)
			{
				try
				{
					Battle battle = new Battle(player1, player2, map, mode, time);
					if (battleMap.get(battle) == null)
						battleMap.put(battle, battle.start());
				}
				catch (IOException e)
				{
					battleFailed = true;
					System.err.println("Battle failed: " + e.getMessage());
				}

				try
				{
					Battle battle = new Battle(player2, player1, map, mode, time);
					if (battleMap.get(battle) == null)
						battleMap.put(battle, battle.start());
				}
				catch (IOException e)
				{
					battleFailed = true;
					System.err.println("Battle failed: " + e.getMessage());
				}
			}
			return battleFailed;
		}

		/** Construct Battle environment */
		public Battle(String player1, String player2, String map, String mode, String time)
		{
			this.player1 = player1;
			this.player2 = player2;
			this.map = map;
			this.mode = mode;
			this.time = time;
		}

		/** Start Battle and return result */
		public Result start() throws IOException
		{
			System.out.println();
			System.out.println(player1 + " vs " + player2 + " map: " + map + " mode: " + mode);

			Result result = new Result();
			int retries;

			processStart:
			for (retries = 0; retries < 3; retries++)
			{
				Process process = null;
				ProcessBuilder pb = new ProcessBuilder("java", "-jar", PATH + "tools/PlayGame.jar", PATH + "tools/" + map, "\""
						+ player1 + "\"", "\"" + player2 + "\"", mode, time);
				pb.directory(new File(PATH));

				try
				{
					process = pb.start();
					InputStreamReader inputReader = new InputStreamReader(process.getInputStream());
					InputStreamReader errorReader = new InputStreamReader(process.getInputStream());
					long stopTime = new Date().getTime() + 15000;
					boolean alive = true;

					while (alive)
					{
						while (inputReader.ready())
							inputReader.read();

						while (inputReader.ready())
							errorReader.read();

						System.out.print(".");

						try
						{
							Thread.sleep(500);
						}
						catch (InterruptedException e)
						{}

						if (stopTime < new Date().getTime())
							process.destroy();

						alive = false;
						try
						{
							process.exitValue();
						}
						catch (IllegalThreadStateException e)
						{
							alive = true;
						}
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

				if (process == null)
					throw new IOException("Failed to start process");

				BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));

				while (br.ready())
				{
					if (!result.parseLine(br.readLine()))
					{
						System.out.println("Timeout -> restart");
						continue processStart;
					}
				}

				break;
			}

			if (result.winner == null)
			{
				if (retries == 0)
					throw new IOException("Unknown error. Number of turns: " + result.turns);
				else
					throw new IOException("Player too slow");
			}

			System.out.println("Winner: " + result.winner + " Number of turns: " + result.turns);
			return result;
		}

		static class Result
		{
			String winner;
			int turns;
			long p1Time, p2Time;
			int t1, t2;

			/** Returns false on error */
			public boolean parseLine(String line) throws IOException
			{
				if (turns < 100)
				{
					line = line.trim();
					String[] words = line.split(" ");

					if (words.length > 2 && words[2].equals("timeout:"))
						return false;
					else if (words.length > 2 && words[0].equals("Player") && words[2].equals("Wins!"))
						winner = words[1];
					else if (words[0].equals("Draw!"))
						winner = "draw";
					else if (words[0].equals("Turn"))
						turns = Integer.parseInt(words[1]);
					else if (words[0].equals("Player") && line.length() >= 9)
					{
						if (words.length == 4 && words[2].equals("time"))
						{
							if (words[1].equals("1:"))
							{
								t1++;
								p1Time = p1Time * (t1 - 1) / t1 + Long.parseLong(words[3]) / t1;
							}
							else
							{
								t2++;
								p2Time = p2Time * (t2 - 1) / t2 + Long.parseLong(words[3]) / t2;
							}
						}
						else
						{
							System.out.println(line);
						}
					}
					else if (line.length() > 0 && !words[0].equals("Engine"))
						throw new IOException(line);
				}
				else
				{
					winner = "draw";
				}

				return true;
			}

			/** Log results of all BOTS */
			public static boolean log(Map<Battle, Result> battleMap)
			{
				boolean loggingFailed = false;
				for (String bot : BOTS)
					if (log(bot, battleMap))
						loggingFailed = true;
				return loggingFailed;
			}

			/** Log only the results of bot */
			public static boolean log(String bot, Map<Battle, Result> battleMap)
			{
				boolean loggingFailed = false;
				try
				{
					FileWriter[] writers = new FileWriter[3];
					writers[0] = new FileWriter(bot + " lose.csv");
					writers[1] = new FileWriter(bot + " draw.csv");
					writers[2] = new FileWriter(bot + " won.csv");

					for (FileWriter writer : writers)
						writer.write("Opponent,Turns,Map,Avarage turn time\n");

					for (String opponent : BOTS)
					{
						for (Entry<Battle, Result> entry : battleMap.entrySet())
						{
							Battle battle = entry.getKey();
							Result result = entry.getValue();

							if (battle == null || result == null)
							{
								loggingFailed = true;
							}
							else if (bot.equals(battle.player1) && opponent.equals(battle.player2))
							{
								long time = result.averageTurnTime(true);
								String line = opponent + "," + result.turns + "," + battle.map + "," + time + "\n";
								try
								{
									if ((result.winner.equals("2")))
										writers[0].write(line);
									else if (result.winner.equals("draw"))
										writers[1].write(line);
									if ((result.winner.equals("1")))
										writers[2].write(line);
								}
								catch (IOException e)
								{
									System.err.println(e.getMessage());
									loggingFailed = true;
								}
							}
							else if (bot.equals(battle.player2) && opponent.equals(battle.player1))
							{
								long time = result.averageTurnTime(false);
								String line = opponent + "," + result.turns + "," + battle.map + "," + time + "\n";
								try
								{
									if ((result.winner.equals("1")))
										writers[0].write(line);
									else if (result.winner.equals("draw"))
										writers[1].write(line);
									if ((result.winner.equals("2")))
										writers[2].write(line);
								}
								catch (IOException e)
								{
									System.err.println(e.getMessage());
									loggingFailed = true;
								}
							}
						}
					}

					for (FileWriter writer : writers)
						writer.close();
				}
				catch (IOException e)
				{
					loggingFailed = true;
					System.err.println(e.getMessage());
				}

				return loggingFailed;
			}

			/** Returns the average turn time in milliseconds */
			public long averageTurnTime(boolean player1)
			{
				if (player1)
					return p1Time / 1000000; // nano to milli
				else
					return p2Time / 1000000; // nano to milli
			}
		}

		@Override
		public int hashCode()
		{
			return (player1 + player2 + map + mode + time).hashCode();
		}

		@Override
		public boolean equals(Object obj)
		{
			if (super.equals(obj))
				return true;

			if (!(obj instanceof Battle))
				return false;

			Battle b = (Battle) obj;

			return player1.equals(b.player1) && player2.equals(b.player2) && map.equals(b.map) && mode.equals(b.mode)
					&& time.equals(b.time);
		}
	}
}
