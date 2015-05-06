package bulldog;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;


public class ScoreboardHelper {

	static ScoreboardManager manager = Bukkit.getScoreboardManager();



	public static void createScoreboard(Player p){

		Scoreboard board = manager.getNewScoreboard();

		Objective scores = board.registerNewObjective("Bulldog", "dummy");

		scores.setDisplayName(" -=Bulldog=- ");
		scores.setDisplaySlot(DisplaySlot.SIDEBAR);

		p.sendMessage("Adding Scoreboard");
		p.setScoreboard(board);
		p.sendMessage("Scoreboard added");


	}
	public static void updateScoreboard(Player p, Arena a, Bulldog plugin){

		Objective scores = p.getScoreboard().getObjective("Bulldog");

		Score brem = scores.getScore(ChatColor.GREEN + "Bulldogs Remaining:"); //Get a fake offline player
		brem.setScore(a.bulldog.size());

		Score crem = scores.getScore(ChatColor.GREEN + "Chasers Remaining:"); //Get a fake offline player
		crem.setScore(a.chaser.size());

		Score bprox = scores.getScore(ChatColor.GREEN + "Bulldog Proximity:"); //Get a fake offline player
		bprox.setScore(/*TODO Calculate Proximity*/1);

		Score pkil = scores.getScore(ChatColor.GREEN + "Kills:"); //Get a fake offline player
		pkil.setScore(/*(int) MetadataHelper.getMetadata("Kills",plugin,p)*/1);

		Score score = scores.getScore(ChatColor.GREEN + "Health:"); //Get a fake offline player

		score.setScore((int)p.getHealth());


	}

	public static void removePlayerScoreboard(Player p){
		p.setScoreboard(manager.getNewScoreboard());

	}

}

