package domainvalue;

public class Level
{
	private final int level;
	private final Difficulty difficulty;

	public Level(final int level, final Difficulty difficulty)
	{
		this.level = level;
		this.difficulty = difficulty;
	}

	@Override
	public String toString()
	{
		String currentLevel;
		switch (difficulty)
		{
			case EASY -> {
				switch (level)
				{
					case 1 -> currentLevel = "2 runs of 3";
					case 2 -> currentLevel = "4 pairs";
					case 3 -> currentLevel = "1 run of 3 AND 4 cards of one suite";
					case 4 -> currentLevel = "1 run of 6 AND 1 pair";
					case 5 -> currentLevel = "1 run of 7";
					case 6 -> currentLevel = "1 pair AND 2 triples";
					case 7 -> currentLevel = "1 tripple AND 5 cards of one suite";
					case 8 -> currentLevel = "1 pair AND 1 triple AND 1 quadruple";
					default -> throw new IllegalStateException("THIS SHOULD NEVER HAPPEN " + level + " " + difficulty);
				}
				return currentLevel;
			}
			case HARD -> throw new IllegalStateException("THIS SHOULDN'T HAVE HAPPENED YET " + level + " " + difficulty);
			default -> throw new IllegalStateException("THIS SHOULD NEVER HAPPEN " + level + " " + difficulty);
		}
	}

	public enum Difficulty
	{
		EASY,
		HARD
	}

}
