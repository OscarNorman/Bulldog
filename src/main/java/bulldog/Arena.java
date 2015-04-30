package bulldog;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import lib.PatPeter.SQLibrary.SQLite;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.util.Vector;




public class Arena {

	public World myWorld;
	public Vector playingArea1;
	public Vector playingArea2;
	public Vector lobbyArea1;
	public Vector lobbyArea2;
	public Vector winArea1;
	public Vector winArea2;
	public Vector lobbySpawn;
	public Vector arenaSpawn1;
	public Vector arenaSpawn2;
	public String arenaName;

	public int voteCount;
	public ArrayList<Player> players;
	public boolean playing;
	public ArrayList<Player> bulldog;
	public ArrayList<Player> bulldogFull;
	public ArrayList<Player> chaser;
	public ArrayList<Player> chaserFull;

	public Boolean isPlaying(){
		return true;

	}

	public Boolean playerPlaying(Player p){
		if(players.contains(p)){
			return true;
		}
		return false;

	}

	public void playerDead(Player p){
		bulldog.remove(p);
		chaser.remove(p);
	}

	public Boolean isGameOver(){
		if(bulldog.isEmpty()){

			return true;
		}
		if(chaser.isEmpty()){

			return true;
		}
		return false;
	}


	public Boolean isValid(){

		return(playingArea1!=null&&
				playingArea2!=null&&
				lobbyArea1!=null&&
				lobbyArea2!=null&&
				winArea1!=null&&
				winArea2!=null&&
				lobbySpawn!=null&&
				arenaSpawn1!=null&&
				arenaSpawn2!=null);
	}

	public void restorePlayer(Player p,Plugin plugin){
		Object loc = MetadataHelper.getMetadata("oldLocation",plugin,p);
		if(loc!=null){
			plugin.getLogger().info("Loc = "+ loc.toString());
			p.teleport((Location) loc);
		}else{
			plugin.getLogger().info("Loc was NULL");
		}
		//clear invs
		PlayerInventory inv = p.getInventory();
		inv.clear();
		inv.setArmorContents(new ItemStack[4]);

		ItemStack[] invStack=(ItemStack[])MetadataHelper.getMetadata("inv",plugin,p);
		if(invStack!=null){
			plugin.getLogger().info("Stack = "+ invStack.toString());
			p.getInventory().setContents(invStack);
		}
		ItemStack[] armStack=(ItemStack[])MetadataHelper.getMetadata("armour",plugin,p);
		if(armStack!=null){
			plugin.getLogger().info("armStack = "+ armStack.toString());
			p.getEquipment().setArmorContents(armStack);
		}

	}

	public void gameOver(Plugin plugin){
		//TODO: Find out who won
		Boolean bulldogsWon=true;

		if(this.bulldog.isEmpty()){
			bulldogsWon=false;
		}
		else if(this.chaser.isEmpty()){
			bulldogsWon=true;
		}
		else if(bulldogFinished()){
			bulldogsWon=true;
		}

		// Teleport players out of arena, last known location
		for(Player p : players){
			restorePlayer(p,plugin);

		}
		//TODO: update stats

		//Send Victory Messages etc.
		for(Player p:bulldogFull){
			if(bulldogsWon){
				p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Your Team Won! Go Bulldogs!");	
			}else{
				p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Your Team Lost! Chasers Won!");
			}

		}
		for(Player p:chaserFull){
			if(!bulldogsWon){
				p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Your Team Won! Go Chasers!");	
			}else{
				p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Your Team Lost! Bulldogs Escaped!");
			}

		}
		bulldog.clear();
		bulldogFull.clear();
		chaser.clear();
		chaserFull.clear();
		players.clear();
		playing=false;
	}

	public Boolean bulldogFinished(){
		double minX=Math.min(winArea1.getX(), winArea2.getX());
		double minZ=Math.min(winArea1.getZ(), winArea2.getZ());

		double maxX=Math.max(winArea1.getX(), winArea2.getX());
		double maxZ=Math.max(winArea1.getZ(), winArea2.getZ());
		for(Player p:bulldog){
			if(p.getLocation().getX()>=minX&&p.getLocation().getX()<=maxX&&p.getLocation().getZ()>=minZ&&p.getLocation().getZ()<=maxZ){
				return true;
			}
		}
		return false;
	}

	public void start(){
		@SuppressWarnings("unchecked")
		ArrayList<Player> temp = (ArrayList<Player>) players.clone();
		Random rand = new Random();


		while(temp.size()>0){
			int n=rand.nextInt(temp.size());
			bulldog.add(temp.get(n));
			bulldogFull.add(temp.get(n));
			temp.remove(n);

			for(int i=0;i<7;i++){
				if(temp.size()>0){
					int e=rand.nextInt(temp.size());
					chaser.add(temp.get(e));
					chaserFull.add(temp.get(e));
					temp.remove(e);

				}
			}
		}
		for(Player p : bulldog){

			PlayerInventory inventory = p.getInventory(); 
			if(p.hasPermission("bulldog.kit.level5")){
				ItemStack primarywep = new ItemStack(Material.DIAMOND_SWORD, 1);
				primarywep.addEnchantment(Enchantment.DAMAGE_ALL, 5); 
				primarywep.addEnchantment(Enchantment.KNOCKBACK, 2);

				ItemStack secondarywep = new ItemStack(Material.BOW, 1);
				ItemStack arrows = new ItemStack(Material.ARROW, 32);

				Potion potion = new Potion(2);
				ItemStack potion1 = potion.toItemStack(10);
				Potion potionsecond = new Potion(5);
				ItemStack potion2 = potionsecond.toItemStack(10);
				Potion potionthird = new Potion(14);
				ItemStack potion3 = potionthird.toItemStack(1);

				ItemStack helm = new ItemStack(Material.DIAMOND_HELMET);
				ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
				ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS);
				ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);

				inventory.addItem(primarywep,secondarywep,potion1,potion2,potion3,arrows);
				inventory.setHelmet(helm);
				inventory.setChestplate(chest);
				inventory.setLeggings(legs);
				inventory.setBoots(boots);
			}
			else if(p.hasPermission("bulldog.kit.level4")){
				ItemStack primarywep = new ItemStack(Material.IRON_SWORD, 1);
				primarywep.addEnchantment(Enchantment.DAMAGE_ALL, 1); 

				ItemStack secondarywep = new ItemStack(Material.BOW, 1);
				ItemStack arrows = new ItemStack(Material.ARROW, 16);

				Potion potion = new Potion(2);
				ItemStack potion1 = potion.toItemStack(10);
				Potion potionsecond = new Potion(5);
				ItemStack potion2 = potionsecond.toItemStack(10);


				ItemStack helm = new ItemStack(Material.DIAMOND_HELMET);
				ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
				ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS);
				ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);

				inventory.addItem(primarywep,secondarywep,potion1,potion2,arrows);
				inventory.setHelmet(helm);
				inventory.setChestplate(chest);
				inventory.setLeggings(legs);
				inventory.setBoots(boots);
			}
			else if(p.hasPermission("bulldog.kit.level3")){
				ItemStack primarywep = new ItemStack(Material.IRON_SWORD, 1); 

				ItemStack secondarywep = new ItemStack(Material.BOW, 1);
				ItemStack arrows = new ItemStack(Material.ARROW, 8);

				Potion potion = new Potion(2);
				ItemStack potion1 = potion.toItemStack(8);
				Potion potionsecond = new Potion(5);
				ItemStack potion2 = potionsecond.toItemStack(5);


				ItemStack helm = new ItemStack(Material.IRON_HELMET);
				ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE);
				ItemStack legs = new ItemStack(Material.IRON_LEGGINGS);
				ItemStack boots = new ItemStack(Material.IRON_BOOTS);

				inventory.addItem(primarywep,secondarywep,potion1,potion2,arrows);
				inventory.setHelmet(helm);
				inventory.setChestplate(chest);
				inventory.setLeggings(legs);
				inventory.setBoots(boots);
			}
			else if(p.hasPermission("bulldog.kit.level2")){
				ItemStack primarywep = new ItemStack(Material.IRON_SWORD, 1); 

				Potion potion = new Potion(2);
				ItemStack potion1 = potion.toItemStack(5);
				Potion potionsecond = new Potion(5);
				ItemStack potion2 = potionsecond.toItemStack(3);


				ItemStack helm = new ItemStack(Material.IRON_HELMET);
				ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE);
				ItemStack legs = new ItemStack(Material.IRON_LEGGINGS);
				ItemStack boots = new ItemStack(Material.IRON_BOOTS);

				inventory.addItem(primarywep,potion1,potion2);
				inventory.setHelmet(helm);
				inventory.setChestplate(chest);
				inventory.setLeggings(legs);
				inventory.setBoots(boots);
			}
			else{
				ItemStack primarywep = new ItemStack(Material.STONE_SWORD, 1); 

				Potion potion = new Potion(2);
				ItemStack potion1 = potion.toItemStack(3);
				Potion potionsecond = new Potion(5);
				ItemStack potion2 = potionsecond.toItemStack(2);


				ItemStack helm = new ItemStack(Material.IRON_HELMET);
				ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE);
				ItemStack legs = new ItemStack(Material.IRON_LEGGINGS);
				ItemStack boots = new ItemStack(Material.IRON_BOOTS);

				inventory.addItem(primarywep,potion1,potion2);
				inventory.setHelmet(helm);
				inventory.setChestplate(chest);
				inventory.setLeggings(legs);
				inventory.setBoots(boots);
			}

			p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+" Game Started! Go Go Go!");
			p.teleport(arenaSpawn1.toLocation(myWorld));

		}
		for(Player p : chaser){

			PlayerInventory inventory = p.getInventory(); 
			if(p.hasPermission("bulldog.kit.level5")){
				ItemStack primarywep = new ItemStack(Material.DIAMOND_SWORD, 1);
				primarywep.addEnchantment(Enchantment.DAMAGE_ALL, 5); 
				primarywep.addEnchantment(Enchantment.FIRE_ASPECT, 1);

				ItemStack secondarywep = new ItemStack(Material.BOW, 1);
				secondarywep.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
				ItemStack arrows = new ItemStack(Material.ARROW, 64);
				ItemStack arrows2 = new ItemStack(Material.ARROW, 64);

				Potion potion = new Potion(2);
				ItemStack potion1 = potion.toItemStack(32);
				Potion potionsecond = new Potion(13);
				potionsecond.setSplash(true);
				ItemStack potion2 = potionsecond.toItemStack(32);
				Potion potionthird = new Potion(4);
				ItemStack potion3 = potionthird.toItemStack(20);

				ItemStack helm = new ItemStack(Material.DIAMOND_HELMET);
				ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
				ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS);
				ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);

				inventory.addItem(primarywep,secondarywep,potion1,potion2,potion3,arrows,arrows2);
				inventory.setHelmet(helm);
				inventory.setChestplate(chest);
				inventory.setLeggings(legs);
				inventory.setBoots(boots);
			}
			else if(p.hasPermission("bulldog.kit.level4")){
				ItemStack primarywep = new ItemStack(Material.IRON_SWORD, 1);
				primarywep.addEnchantment(Enchantment.DAMAGE_ALL, 1); 

				ItemStack secondarywep = new ItemStack(Material.BOW, 1);
				ItemStack arrows = new ItemStack(Material.ARROW, 16);

				Potion potion = new Potion(2);
				ItemStack potion1 = potion.toItemStack(24);
				Potion potionsecond = new Potion(13);
				potionsecond.setSplash(true);
				ItemStack potion2 = potionsecond.toItemStack(20);



				ItemStack helm = new ItemStack(Material.IRON_HELMET);
				ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE);
				ItemStack legs = new ItemStack(Material.IRON_LEGGINGS);
				ItemStack boots = new ItemStack(Material.IRON_BOOTS);

				inventory.addItem(primarywep,secondarywep,potion1,potion2,arrows);
				inventory.setHelmet(helm);
				inventory.setChestplate(chest);
				inventory.setLeggings(legs);
				inventory.setBoots(boots);
			}
			else if(p.hasPermission("bulldog.kit.level3")){
				ItemStack primarywep = new ItemStack(Material.IRON_SWORD, 1); 

				ItemStack secondarywep = new ItemStack(Material.BOW, 1);
				ItemStack arrows = new ItemStack(Material.ARROW, 16);

				Potion potion = new Potion(2);
				ItemStack potion1 = potion.toItemStack(6);
				Potion potionsecond = new Potion(13);
				potionsecond.setSplash(true);
				ItemStack potion2 = potionsecond.toItemStack(5);


				ItemStack helm = new ItemStack(Material.IRON_HELMET);
				ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE);
				ItemStack legs = new ItemStack(Material.IRON_LEGGINGS);
				ItemStack boots = new ItemStack(Material.IRON_BOOTS);

				inventory.addItem(primarywep,secondarywep,potion1,potion2,arrows);
				inventory.setHelmet(helm);
				inventory.setChestplate(chest);
				inventory.setLeggings(legs);
				inventory.setBoots(boots);
			}
			else if(p.hasPermission("bulldog.kit.level2")){
				ItemStack primarywep = new ItemStack(Material.STONE_SWORD, 1); 

				ItemStack secondarywep = new ItemStack(Material.BOW, 1);
				ItemStack arrows = new ItemStack(Material.ARROW, 8);

				Potion potion = new Potion(2);
				ItemStack potion1 = potion.toItemStack(5);
				Potion potionsecond = new Potion(13);
				potionsecond.setSplash(true);
				ItemStack potion2 = potionsecond.toItemStack(3);


				ItemStack helm = new ItemStack(Material.CHAINMAIL_HELMET);
				ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE);
				ItemStack legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
				ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);

				inventory.addItem(primarywep,potion1,potion2,secondarywep,arrows);
				inventory.setHelmet(helm);
				inventory.setChestplate(chest);
				inventory.setLeggings(legs);
				inventory.setBoots(boots);
			}
			else{
				ItemStack primarywep = new ItemStack(Material.WOOD_SWORD, 1); 

				Potion potion = new Potion(2);
				ItemStack potion1 = potion.toItemStack(1);
				Potion potionsecond = new Potion(13);
				potionsecond.setSplash(true);
				ItemStack potion2 = potionsecond.toItemStack(2);


				ItemStack helm = new ItemStack(Material.CHAINMAIL_HELMET);
				ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
				ItemStack legs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
				ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);

				inventory.addItem(primarywep,potion1,potion2);
				inventory.setHelmet(helm);
				inventory.setChestplate(chest);
				inventory.setLeggings(legs);
				inventory.setBoots(boots);
			}

			p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+"Game Started! go Go GO!");
			p.teleport(arenaSpawn2.toLocation(myWorld));

		}
	}



	public void checkStart(Plugin plugin){
		if(!playing){  

			if(players.size()==8||voteCount==(players.size()/100)*80){

				GameCountdown countdown = new GameCountdown((Bulldog)plugin,this);
				countdown.startCountdown(30, "Game starting in # seconds!");
				playing=true;
			}
		}else{
			//do nothing

		}
	}

	public void leave(Player player, Plugin plugin){
		players.remove(player);
		bulldog.remove(player);
		chaser.remove(player);
		bulldogFull.remove(player);
		chaserFull.remove(player);
		for(Player p : players){
			p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+player.getDisplayName()+" Has Left The Arena!");
		}
		restorePlayer(player,plugin);
	}

	public void save(SQLite sqlite, CommandSender sender){
		try {
			String q = "INSERT INTO arenas (name,world, "
					+ "playingArea1_x,playingArea1_y,playingArea1_z,"
					+ "playingArea2_x,playingArea2_y,playingArea2_z,"
					+ "lobbyArea1_x,lobbyArea1_y,lobbyArea1_z,"
					+ "lobbyArea2_x,lobbyArea2_y,lobbyArea2_z,"
					+ "winArea1_x,winArea1_y,winArea1_z,"
					+ "winArea2_x,winArea2_y,winArea2_z,"
					+ "lobbySpawn_x,lobbySpawn_y,lobbySpawn_z,"
					+ "arenaSpawn1_x,arenaSpawn1_y,arenaSpawn1_z,"
					+ "arenaSpawn2_x,arenaSpawn2_y,arenaSpawn2_z) VALUES ("
					+ "'"+arenaName+"','"+myWorld.getName()+"',"
					+ playingArea1.getX()+","
					+ playingArea1.getY()+","
					+ playingArea1.getZ()+","
					+ playingArea2.getX()+","
					+ playingArea2.getY()+","
					+ playingArea2.getZ()+","
					+ lobbyArea1.getX()+","
					+ lobbyArea1.getY()+","
					+ lobbyArea1.getZ()+","
					+ lobbyArea2.getX()+","
					+ lobbyArea2.getY()+","
					+ lobbyArea2.getZ()+","
					+ winArea1.getX()+","
					+ winArea1.getY()+","
					+ winArea1.getZ()+","
					+ winArea2.getX()+","
					+ winArea2.getY()+","
					+ winArea2.getZ()+","
					+ lobbySpawn.getX()+","
					+ lobbySpawn.getY()+","
					+ lobbySpawn.getZ()+","
					+ arenaSpawn1.getX()+","
					+ arenaSpawn1.getY()+","
					+ arenaSpawn1.getZ()+","
					+ arenaSpawn2.getX()+","
					+ arenaSpawn2.getY()+","
					+ arenaSpawn2.getZ()
					+ ");";
			sqlite.query(q);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	public static ArrayList<String> getArenas(SQLite sqlite) {
		ArrayList<String> res = new ArrayList<String>();
		try {
			ResultSet r = sqlite.query("SELECT name FROM arenas");
			while ( r.next() ) {
				String name = r.getString(1);
				res.add(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  res;
	}
	public void load(SQLite sqlite, String name, Plugin p){
		try {
			ResultSet r = sqlite.query("SELECT playingArea1_x,playingArea1_y,playingArea1_z,"
					+ "playingArea2_x,playingArea2_y,playingArea2_z,"
					+ "lobbyArea1_x,lobbyArea1_y,lobbyArea1_z,"
					+ "lobbyArea2_x,lobbyArea2_y,lobbyArea2_z,"
					+ "winArea1_x,winArea1_y,winArea1_z,"
					+ "winArea2_x,winArea2_y,winArea2_z,"
					+ "lobbySpawn_x,lobbySpawn_y,lobbySpawn_z,"
					+ "arenaSpawn1_x,arenaSpawn1_y,arenaSpawn1_z,"
					+ "arenaSpawn2_x,arenaSpawn2_y,arenaSpawn2_z,"
					+ "world FROM arenas WHERE name='"+name+"'");
			while ( r.next() ) {
				arenaName=name;
				playingArea1=new Vector(r.getDouble(1),r.getDouble(2),r.getDouble(3));
				playingArea2=new Vector(r.getDouble(4),r.getDouble(5),r.getDouble(6));
				lobbyArea1=new Vector(r.getDouble(7),r.getDouble(8),r.getDouble(9));
				lobbyArea2=new Vector(r.getDouble(10),r.getDouble(11),r.getDouble(12));
				winArea1=new Vector(r.getDouble(13),r.getDouble(14),r.getDouble(15));
				winArea2=new Vector(r.getDouble(16),r.getDouble(17),r.getDouble(18));
				lobbySpawn=new Vector(r.getDouble(25),r.getDouble(26),r.getDouble(27));
				arenaSpawn1=new Vector(r.getDouble(28),r.getDouble(29),r.getDouble(30));
				arenaSpawn2=new Vector(r.getDouble(31),r.getDouble(32),r.getDouble(33));
				myWorld=p.getServer().getWorld(r.getString(37));

				voteCount = 0;
				players=new ArrayList<Player>();
				bulldog=new ArrayList<Player>();
				chaser=new ArrayList<Player>();
				bulldogFull=new ArrayList<Player>();
				chaserFull=new ArrayList<Player>();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	public static void delete(SQLite sqlite, String name){
		String query = "DELETE FROM arenas WHERE name="+name+";";
		try {
			sqlite.query(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

};




