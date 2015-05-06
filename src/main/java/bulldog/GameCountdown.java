package bulldog;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GameCountdown


{


	private final Bulldog plugin;

	Arena arena;
	private int countdownTimer;

	public GameCountdown(Bulldog i, Arena arena){
		this.plugin = i;
		this.arena = arena;
	}
	public void startCountdown(final int time, final String msg){
		this.countdownTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable(){
			int i = time;
			public void run(){

				for(Player p : arena.players){
					if(i==60){
						p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+msg.replace("#", Integer.toString(i)));
					}
					else if(i>50){
					//do nothing	
					}
					else if(i==50){
					p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+msg.replace("#", Integer.toString(i)));
					}
					else if(i>40){
					//do nothing	
					}
					else if(i==40){
					p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+msg.replace("#", Integer.toString(i)));
					}
					else if(i>30){
					//do nothing	
					}
					else if(i==30){
					p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+msg.replace("#", Integer.toString(i)));
					}
					else if(i>20){
						//do nothing	
						}
					else if(i==20){
						p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+msg.replace("#", Integer.toString(i)));
					}
					else if(i>10){
						//do nothing	
					}
					else if(i==10){
						p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+msg.replace("#", Integer.toString(i)));
					}
					else{
						p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+msg.replace("#", Integer.toString(i)));
					}
				}
				this.i--;
				if (this.i <= 0) GameCountdown.this.cancel();
			}
		}
		, 0L, 20L);
	}
	public void cancel(){
		Bukkit.getScheduler().cancelTask(this.countdownTimer);
		arena.start(plugin);
	}
}

