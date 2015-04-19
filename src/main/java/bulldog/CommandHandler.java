package bulldog;

import java.util.ArrayList;

import lib.PatPeter.SQLibrary.SQLite;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;



public class CommandHandler {



	public static Boolean doCommand(CommandSender sender, Command cmd, String label, String[] args, SQLite sqlite, Arena underCreation, Plugin plugin){

		WorldEditPlugin we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		ArrayList<Arena> activeArenas = new ArrayList<Arena>();

		if (Bukkit.getServer().getConsoleSender() == sender) {
			sender.sendMessage("This command can only be run by a player.");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("bulldog")){ 
			if(args.length>0){
				if(args[0].equals("list")){
					sender.sendMessage("Arena List:");
					ArrayList<String> l = Arena.getArenas(sqlite);
					for(String temp : l){
						sender.sendMessage(">"+temp);
					}

				}
				if(args[0].equals("join")){
					if(args.length>1){
						Arena arena=null;
						for(Arena a:activeArenas){
							if(a.arenaName.equals(args[1])){
								arena = a;
							}

						}
						if(arena==null){
							arena=new Arena();
							arena.load(sqlite,args[1],plugin);
							activeArenas.add(arena);
						}
						arena.players.add((Player)sender);
						((Player) sender).teleport(arena.lobbySpawn.toLocation(arena.myWorld));
						if(arena.players.size()>=1){

							GameCountdown countdown = new GameCountdown((Bulldog)plugin, arena);
							countdown.startCountdown(60, "Game starting in # seconds!");


						}

					}else{
						sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.RED+" Please Specify an Arena!");
					}



				}
			}	  					

			else{
				sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Sorry, Command help message is a work in progress.");
			}
			return true;
		}


		if (cmd.getName().equalsIgnoreCase("bulldogarena")){
			if(args.length>0){
				if(args[0].equalsIgnoreCase("create")){
					if(args[1]==null){
						sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.RED+" Please specify an arena name: /bulldogarena create name");
					}
					else{
						underCreation = new Arena();
						underCreation.arenaName=args[1];
						underCreation.myWorld=((Player)sender).getWorld();
						sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Arena with name: "+args[1]+" created!");
					}
				}
				else if(args[0].equalsIgnoreCase("setarenaregion")){

					Selection s = we.getSelection((Player)sender);
					if(s==null){
						sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.RED+"Make a selection first!");
					}
					else{
						Vector min = s.getMinimumPoint().toVector();
						Vector max = s.getMaximumPoint().toVector();

						underCreation.playingArea1=min;
						underCreation.playingArea2=max;
						sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Arena Boundary Set!");
					}
				}
				else if(args[0].equalsIgnoreCase("setlobbyregion")){

					Selection s = we.getSelection((Player)sender);
					if(s==null){
						sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.RED+" Make a selection first!");
					}
					else{
						Vector min = s.getMinimumPoint().toVector();
						Vector max = s.getMaximumPoint().toVector();

						underCreation.lobbyArea1=min;
						underCreation.lobbyArea2=max;
						sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Lobby Boundary Set!");
					}
				}
				else if(args[0].equalsIgnoreCase("setspecregion")){

					Selection s = we.getSelection((Player)sender);
					if(s==null){
						sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.RED+" Make a selection first!");
					}
					else{
						Vector min = s.getMinimumPoint().toVector();
						Vector max = s.getMaximumPoint().toVector();

						underCreation.specArea1=min;
						underCreation.specArea2=max;
						sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Specator Boundary Set!");
					}
				}
				else if(args[0].equalsIgnoreCase("setwinregion")){

					Selection s = we.getSelection((Player)sender);
					if(s==null){
						sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.RED+" Make a selection first!");
					}
					else{
						Vector min = s.getMinimumPoint().toVector();
						Vector max = s.getMaximumPoint().toVector();
						underCreation.winArea1=min;
						underCreation.winArea2=max;
						sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Winning Boundary Set!");
					}
				}
				else if(args[0].equalsIgnoreCase("setlobbyspawn")) {
					Vector p = ((Player)sender).getLocation().toVector();
					underCreation.lobbySpawn=p;
					sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Lobby spawn Set!");
				}
				else if(args[0].equalsIgnoreCase("setarenaspawn1")) {
					Vector p = ((Player)sender).getLocation().toVector();
					underCreation.arenaSpawn1=p;
					sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Arena spawn 1 Set!");
				}
				else if(args[0].equalsIgnoreCase("setarenaspawn2")) {
					Vector p = ((Player)sender).getLocation().toVector();
					underCreation.arenaSpawn2=p;
					sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Arena spawn 2 Set!");
				}
				else if(args[0].equalsIgnoreCase("setspecspawn")) {
					Vector p = ((Player)sender).getLocation().toVector();
					underCreation.specSpawn=p;
					sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Spectator spawn Set!");
				}
				else if(args[0].equalsIgnoreCase("verify")) {
					if (underCreation!=null && underCreation.isValid()) {
						sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" You Have A Vaild Arena\n"+ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Saving Arena.. Done!");
						underCreation.save(sqlite, sender);
					}
					else {
						if(underCreation.playingArea1==null||underCreation.playingArea2==null){

							sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Arena Boundary not set!");
						}
						if(underCreation.lobbyArea1==null||underCreation.lobbyArea2==null){

							sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Lobby Boundary not set!");
						}
						if(underCreation.winArea1==null||underCreation.winArea2==null){

							sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Win Boundary not set!");
						}
						if(underCreation.specArea1==null||underCreation.specArea2==null){

							sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Spectator Boundary not set!");
						}
						if(underCreation.lobbySpawn==null){

							sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Lobby Spawn not set!");
						}
						if(underCreation.arenaSpawn1==null){

							sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Arena Spawn 1 not set!");
						}
						if(underCreation.arenaSpawn2==null){

							sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Arena Spawn 2 not set!");
						}
						if(underCreation.specSpawn==null){

							sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Spectator Spawn not set!");
						}

						sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+"Arena is not valid!!!");
					}
				}
			}
			else{
				sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" TODO: Command Help");
			}
			return true;
		}

		return false;
	}
}
