package bulldog;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FireTimer


{


	private final Bulldog plugin;

	Arena arena;
	private int countdownTimer;

	public FireTimer(Bulldog i, Arena arena){
		this.plugin = i;
		this.arena = arena;
	}
	public void startCountdown(final int time, final Player p){
		this.countdownTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable(){
			int i = time;
			public void run(){

				this.i--;
				if (this.i <= 0) FireTimer.this.cancel(p);
			
			}
		}, 0L, 20L);
	}

	public void cancel(Player p){
		Bukkit.getScheduler().cancelTask(this.countdownTimer);
		p.setFireTicks(0);
	}
}


