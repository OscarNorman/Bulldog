package bulldog;


import java.sql.SQLException;
import java.util.ArrayList;

import lib.PatPeter.SQLibrary.SQLite;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Location;


public final class Bulldog extends JavaPlugin implements Listener{

	Arena underCreation;
	public SQLite sqlite;
	ArrayList<Arena> activeArenas;

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
			Player p = e.getPlayer();
			Location l=p.getLocation();
			
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
						+ "specArea1_x FLOAT,"
						+ "specArea1_y FLOAT,"
						+ "specArea1_z FLOAT,"
						+ "specArea2_x FLOAT,"
						+ "specArea2_y FLOAT,"
						+ "specArea2_z FLOAT,"
						+ "lobbySpawn_x FLOAT,"
						+ "lobbySpawn_y FLOAT,"
						+ "lobbySpawn_z FLOAT,"
						+ "arenaSpawn1_x FLOAT,"
						+ "arenaSpawn1_y FLOAT,"
						+ "arenaSpawn1_z FLOAT,"
						+ "arenaSpawn2_x FLOAT,"
						+ "arenaSpawn2_y FLOAT,"
						+ "arenaSpawn2_z FLOAT,"
						+ "specSpawn_x FLOAT,"
						+ "specSpawn_y FLOAT,"
						+ "specSpawn_z FLOAT,"
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
		return CommandHandler.doCommand(sender, cmd, label,  args, sqlite,underCreation, this);
	}
}

