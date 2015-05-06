package bulldog;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UpdateScoreboardTimer


{


	private final Bulldog plugin;

	Arena arena;

	private int countdownTimer;
	public UpdateScoreboardTimer(Bulldog i, Arena arena){
		this.plugin = i;
		this.arena = arena;
	}
	public void start(){
		this.countdownTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable(){
			public void run(){
				for(Player player:arena.players)
					ScoreboardHelper.updateScoreboard(player, arena, plugin);

			}
		}, 0L, 2L);
	}
	public void cancel(){
		Bukkit.getScheduler().cancelTask(this.countdownTimer);
		for(Player player:arena.players){
			ScoreboardHelper.removePlayerScoreboard(player);
		}
		arena=null;
	
	}
}



