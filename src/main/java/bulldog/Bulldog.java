package bulldog;


import java.sql.SQLException;
import java.util.ArrayList;

import lib.PatPeter.SQLibrary.SQLite;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;



public final class Bulldog extends JavaPlugin implements Listener{

	Arena underCreation;
	public SQLite sqlite;
	ArrayList<Arena> activeArenas;

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		for(Arena a:activeArenas){
			if(a.bulldogFinished()){
				a.gameOver(this);
			}
		}
	}

	@EventHandler
	public void onBlockUpdate(BlockPhysicsEvent e, Entity sender){
		for(Arena a:activeArenas){		
			double lMinX=Math.min(a.lobbyArea1.getX(), a.lobbyArea2.getX());
			double lMinZ=Math.min(a.lobbyArea1.getZ(), a.lobbyArea2.getZ());

			double lMaxX=Math.max(a.lobbyArea1.getX(), a.lobbyArea2.getX());
			double lMaxZ=Math.max(a.lobbyArea1.getZ(), a.lobbyArea2.getZ());

			double aMinX=Math.min(a.playingArea1.getX(), a.playingArea2.getX());
			double aMinZ=Math.min(a.playingArea1.getZ(), a.playingArea2.getZ());

			double aMaxX=Math.max(a.playingArea1.getX(), a.playingArea2.getX());
			double aMaxZ=Math.max(a.playingArea1.getZ(), a.playingArea2.getZ());

			double wMinX=Math.min(a.winArea1.getX(), a.lobbyArea2.getX());
			double wMinZ=Math.min(a.winArea1.getZ(), a.lobbyArea2.getZ());

			double wMaxX=Math.max(a.winArea1.getX(), a.winArea2.getX());
			double wMaxZ=Math.max(a.winArea1.getZ(), a.winArea2.getZ());
			if((e.getBlock().getLocation().getX()>=aMinX&&e.getBlock().getX()<=aMaxX&&e.getBlock().getLocation().getZ()>=aMinZ&&e.getBlock().getLocation().getZ()<=aMaxZ)
					||(e.getBlock().getLocation().getX()>=lMinX&&e.getBlock().getX()<=lMaxX&&e.getBlock().getLocation().getZ()>=lMinZ&&e.getBlock().getLocation().getZ()<=lMaxZ)
					||(e.getBlock().getLocation().getX()>=wMinX&&e.getBlock().getX()<=wMaxX&&e.getBlock().getLocation().getZ()>=wMinZ&&e.getBlock().getLocation().getZ()<=wMaxZ)){
				
				if(sender instanceof Player){
					sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.RED+" You Can't Edit Blocks In A Saved Arena!");
				}
				e.isCancelled();
			
			}
		}
	}


	@EventHandler
	public void onEntityDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){	
			Player p = (Player) e.getEntity();
			if(e.getDamage()>=p.getHealth()){	
				ArrayList<Arena> deadArenas = new ArrayList<Arena>();
				for(Arena arena : activeArenas){
					if(arena.playerPlaying(p)){

						arena.restorePlayer(p,this);
						p.setHealth(20);
						FireTimer countdown = new FireTimer(this, arena);
						countdown.startCountdown(1,p);

						arena.playerDead(p);

						if(arena.isGameOver()){
							arena.gameOver(this);
							deadArenas.add(arena);
						}

					}
				}
				activeArenas.removeAll(deadArenas);
				e.setCancelled(true);
			}

		}

	}


	@Override
	public void onEnable() {
		// TODO Insert logic to be performed when the plugin is enabled
		getLogger().info("Waking up...");
		activeArenas=new ArrayList<Arena>();
		getServer().getPluginManager().registerEvents(this,  this);


		sqlConnection();
		sqlTableCheck();

	}
	@Override
	public void onDisable() {
		// TODO Insert logic to be performed when the plugin is disabled
		getLogger().info("Closing bulldog.. '(");
		sqlite.close(); 
	}

	public void sqlConnection() {
		sqlite = new SQLite(getLogger(), "Bulldog", this.getDataFolder().getAbsolutePath(), "Bulldog", ".sqlite");
		//Make sure sqlite is the same as the variable you specified at the top of the plugin!
		try {
			sqlite.open();
		} catch (Exception e) {
			getLogger().info(e.getMessage());
			getPluginLoader().disablePlugin(this);
		}
	}


	@SuppressWarnings("deprecation")
	public void sqlTableCheck() {
		if(sqlite.checkTable("arenas")){
			return;
		}else{
			try {
				sqlite.query("CREATE TABLE arenas (name VARCHAR(16) PRIMARY KEY, "
						+ "playingArea1_x FLOAT,"
						+ "playingArea1_y FLOAT,"
						+ "playingArea1_z FLOAT,"
						+ "playingArea2_x FLOAT,"
						+ "playingArea2_y FLOAT,"
						+ "playingArea2_z FLOAT,"
						+ "lobbyArea1_x FLOAT,"
						+ "lobbyArea1_y FLOAT,"
						+ "lobbyArea1_z FLOAT,"
						+ "lobbyArea2_x FLOAT,"
						+ "lobbyArea2_y FLOAT,"
						+ "lobbyArea2_z FLOAT,"
						+ "winArea1_x FLOAT,"
						+ "winArea1_y FLOAT,"
						+ "winArea1_z FLOAT,"
						+ "winArea2_x FLOAT,"
						+ "winArea2_y FLOAT,"
						+ "winArea2_z FLOAT,"
						+ "lobbySpawn_x FLOAT,"
						+ "lobbySpawn_y FLOAT,"
						+ "lobbySpawn_z FLOAT,"
						+ "arenaSpawn1_x FLOAT,"
						+ "arenaSpawn1_y FLOAT,"
						+ "arenaSpawn1_z FLOAT,"
						+ "arenaSpawn2_x FLOAT,"
						+ "arenaSpawn2_y FLOAT,"
						+ "arenaSpawn2_z FLOAT,"
						+ "world VARCHAR(64));");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			getLogger().info("arenas has been created");
		}
	}




	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return CommandHandler.doCommand(sender, cmd, label,  args, sqlite, this);
	}
}

