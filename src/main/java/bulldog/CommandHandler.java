package bulldog;

import java.util.ArrayList;

import lib.PatPeter.SQLibrary.SQLite;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;



public class CommandHandler {



	public static Boolean doCommand(CommandSender sender, Command cmd, String label, String[] args, SQLite sqlite, Bulldog plugin){

		WorldEditPlugin we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		ArrayList<Arena> activeArenas =  plugin.activeArenas;

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
							sender.sendMessage("loading arena");
							activeArenas.add(arena);
						}
						if(sender!=null){
							arena.players.add((Player)sender);
							((Metadatable) sender).setMetadata("oldLocation",new FixedMetadataValue(plugin,((Player) sender).getLocation()));

							ItemStack[] armour = ((Player)sender).getEquipment().getArmorContents();
							ItemStack[] inv = ((Player)sender).getInventory().getContents();
							((Metadatable) sender).setMetadata("armour",new FixedMetadataValue(plugin,armour)); 
							((Metadatable) sender).setMetadata("inv",new FixedMetadataValue(plugin,inv));
							PlayerInventory inv1 = ((Player)sender).getInventory();
							inv1.clear();
							inv1.setArmorContents(new ItemStack[4]);
							((Player)sender).setFoodLevel(20);
							((Player)sender).setHealth(20);
							
							

							((Player) sender).teleport(arena.lobbySpawn.toLocation(arena.myWorld));
							arena.checkStart(plugin);
						}



					}else{
						sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.RED+" Please Specify an Arena!");
					}




				}
				else if(args[0].equals("leave")){
					for(Arena a:activeArenas){
						a.leave((Player)sender, plugin);
					}
					ScoreboardHelper.removePlayerScoreboard((Player)sender);
					for (PotionEffect effect : ((Player) sender).getActivePotionEffects()){
						((Player) sender).removePotionEffect(effect.getType());
					}

				}
				else if(args[0].equals("vote")){
					for(Arena a:activeArenas){
						if(!a.players.contains(sender)){
							sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.RED+" You Are Not In An Arena!");
						}
						else if( MetadataHelper.getMetadata("voted",plugin,(Player)sender)=="true"){
							sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.RED+" You Have Already Voted!");

						}

						else{
							((Metadatable) sender).setMetadata("voted",new FixedMetadataValue(plugin, "true"));
							a.voteCount+=1;
							sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Voted Sucessfully!");
							a.checkStart(plugin);
						}
					}
				}


			}	  					

			else{
				sender.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Command Help:");
				sender.sendMessage(ChatColor.GREEN+"/bulldog help"+ChatColor.WHITE+" Displays this help message");
				sender.sendMessage(ChatColor.GREEN+"/bulldog list"+ChatColor.WHITE+" Lists all working Bulldog Arenas.");
				sender.sendMessage(ChatColor.GREEN+"/bulldog join <arena>"+ChatColor.WHITE+" Joins the specified Arena.");
				sender.sendMessage(ChatColor.GREEN+"/bulldog leave"+ChatColor.WHITE+" Leaves the current Arena");
			}
			return true;
		}



		if (cmd.getName().equalsIgnoreCase("bulldogarena")){
			if(args.length>0){
				if(args[0].equalsIgnoreCase("create")){
					if(args[1]==null){
						sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.RED+" Please specify an arena name: /bulldogarena create <name>");
					}
					else{
						plugin.underCreation = new Arena();
						plugin.underCreation.arenaName=args[1];
						plugin.underCreation.myWorld=((Player)sender).getWorld();
						sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Arena with name: "+args[1]+" created!");
					}
				}

				else if(args[0].equalsIgnoreCase("delete")){
					if(args[1]==null){
						sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.RED+" Please specify an arena to be deleted: /bulldogarena delete <name>");
					}
					else{
						Arena.delete(sqlite,args[1]);
						sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Arena Deleted");
					}
				}


				else if(args[0].equalsIgnoreCase("setarenaregion")){

					Selection s = we.getSelection((Player)sender);
					if(s==null){
						sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.RED+"Make a selection first!");
					}
					else{
						Vector min = s.getMinimumPoint().toVector();
						Vector max = s.getMaximumPoint().toVector();

						plugin.underCreation.playingArea1=min;
						plugin.underCreation.playingArea2=max;
						sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Arena Boundary Set!");
					}
				}
				else if(args[0].equalsIgnoreCase("setlobbyregion")){

					Selection s = we.getSelection((Player)sender);
					if(s==null){
						sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.RED+" Make a selection first!");
					}
					else{
						Vector min = s.getMinimumPoint().toVector();
						Vector max = s.getMaximumPoint().toVector();

						plugin.underCreation.lobbyArea1=min;
						plugin.underCreation.lobbyArea2=max;
						sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Lobby Boundary Set!");
					}
				}
				else if(args[0].equalsIgnoreCase("setwinregion")){

					Selection s = we.getSelection((Player)sender);
					if(s==null){
						sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.RED+" Make a selection first!");
					}
					else{
						Vector min = s.getMinimumPoint().toVector();
						Vector max = s.getMaximumPoint().toVector();
						plugin.underCreation.winArea1=min;
						plugin.underCreation.winArea2=max;
						sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Winning Boundary Set!");
					}
				}
				else if(args[0].equalsIgnoreCase("setlobbyspawn")) {
					Vector p = ((Player)sender).getLocation().toVector();
					plugin.underCreation.lobbySpawn=p;
					sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Lobby spawn Set!");
				}
				else if(args[0].equalsIgnoreCase("setarenaspawn1")) {
					Vector p = ((Player)sender).getLocation().toVector();
					plugin.underCreation.arenaSpawn1=p;
					sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Arena spawn 1 Set!");
				}
				else if(args[0].equalsIgnoreCase("setarenaspawn2")) {
					Vector p = ((Player)sender).getLocation().toVector();
					plugin.underCreation.arenaSpawn2=p;
					sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Arena spawn 2 Set!");
				}
				else if(args[0].equalsIgnoreCase("verify")) {
					if (plugin.underCreation!=null && plugin.underCreation.isValid()) {
						sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" You Have A Vaild Arena\n"+ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Saving Arena.. Done!");
						plugin.underCreation.save(sqlite, sender);
					}
					else if(plugin.underCreation!=null){
						if(plugin.underCreation.playingArea1==null||plugin.underCreation.playingArea2==null){

							sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Arena Boundary not set!");
						}
						if(plugin.underCreation.lobbyArea1==null||plugin.underCreation.lobbyArea2==null){

							sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Lobby Boundary not set!");
						}
						if(plugin.underCreation.winArea1==null||plugin.underCreation.winArea2==null){

							sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Win Boundary not set!");
						}
						if(plugin.underCreation.lobbySpawn==null){

							sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Lobby Spawn not set!");
						}
						if(plugin.underCreation.arenaSpawn1==null){

							sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Arena Spawn 1 not set!");
						}
						if(plugin.underCreation.arenaSpawn2==null){

							sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Arena Spawn 2 not set!");
						}

						sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+"Arena is not valid!!!");
					}else{

						sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+"No Current Arena");

					}
				}
			}
			else{
				sender.sendMessage(ChatColor.GREEN+"[BulldogArena]"+ChatColor.WHITE+" Command Help For Arenas:");
				sender.sendMessage(ChatColor.GREEN+"/bulldogarena create <arena name>"+ChatColor.WHITE+" Creates a new temporary arena.");
				sender.sendMessage(ChatColor.GREEN+"/bulldogarena delete <arena name>"+ChatColor.WHITE+" (IN PROGRESS) Deletes the arena trom the database");
				sender.sendMessage(ChatColor.GREEN+"/bulldogarena setarenaregion"+ChatColor.WHITE+" Sets the temporary arena boundary to the current World Edit selection");
				sender.sendMessage(ChatColor.GREEN+"/bulldogarena setlobbyregion"+ChatColor.WHITE+" Sets the temporary arena's lobby boundary to the current World Edit selection");
				sender.sendMessage(ChatColor.GREEN+"/bulldogarena setwinregion"+ChatColor.WHITE+" Sets the temporary arena's winning arena to the current World Edit selection");
				sender.sendMessage(ChatColor.GREEN+"/bulldogarena setarenaspawn1"+ChatColor.WHITE+" Sets the temporary arena's BULLDOG spawn to the player's location");
				sender.sendMessage(ChatColor.GREEN+"/bulldogarena setarenaspawn2"+ChatColor.WHITE+" Sets the temporary arena's CHASER spawn to the player's location");
				sender.sendMessage(ChatColor.GREEN+"/bulldogarena setlobbyspawn"+ChatColor.WHITE+" Sets the temporary arena's lobby spawn to the player's location");
				sender.sendMessage(ChatColor.GREEN+"/bulldogarena verify"+ChatColor.WHITE+" Tests for a valid arena, then saves it into the database");
			}
			return true;
		}

		return false;
	}
}
