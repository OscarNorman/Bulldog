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
	public Vector specArea1;
	public Vector specArea2;
	public Vector lobbySpawn;
	public Vector arenaSpawn1;
	public Vector arenaSpawn2;
	public Vector specSpawn;
	public String arenaName;
	public ArrayList<Player> players;
	public Boolean playing;
	public ArrayList<Player> bulldog;
	public ArrayList<Player> chaser;

	public Boolean isPlaying(){
		return true;

	}
	public Boolean isValid(){

		return(playingArea1!=null&&
				playingArea2!=null&&
				lobbyArea1!=null&&
				lobbyArea2!=null&&
				winArea1!=null&&
				winArea2!=null&&
				specArea1!=null&&
				specArea2!=null&&
				lobbySpawn!=null&&
				arenaSpawn1!=null&&
				arenaSpawn2!=null&&
				specSpawn!=null);
	}

	public void start(){
		@SuppressWarnings("unchecked")
		ArrayList<Player> temp = (ArrayList<Player>) players.clone();
		Random rand = new Random();

		while(temp.size()>0){
			int n=rand.nextInt(temp.size());
			bulldog.add(temp.get(n));
			temp.remove(n);

			for(int i=0;i<7;i++){
				if(temp.size()>0){
					int e=rand.nextInt(temp.size());
					chaser.add(temp.get(e));
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
			
			p.sendMessage(ChatColor.GREEN+"[Bulldog]"+ChatColor.WHITE+"Game Started! go Go GO!");
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
					Potion potionthird = new Potion(4);
					potionthird.setSplash(true);
					ItemStack potion3 = potionthird.toItemStack(10);

					
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

	public void save(SQLite sqlite, CommandSender sender){
		try {
			String q = "INSERT INTO arenas (name,world, "
					+ "playingArea1_x,playingArea1_y,playingArea1_z,"
					+ "playingArea2_x,playingArea2_y,playingArea2_z,"
					+ "lobbyArea1_x,lobbyArea1_y,lobbyArea1_z,"
					+ "lobbyArea2_x,lobbyArea2_y,lobbyArea2_z,"
					+ "winArea1_x,winArea1_y,winArea1_z,"
					+ "winArea2_x,winArea2_y,winArea2_z,"
					+ "specArea1_x,specArea1_y,specArea1_z,"
					+ "specArea2_x,specArea2_y,specArea2_z,"
					+ "lobbySpawn_x,lobbySpawn_y,lobbySpawn_z,"
					+ "arenaSpawn1_x,arenaSpawn1_y,arenaSpawn1_z,"
					+ "arenaSpawn2_x,arenaSpawn2_y,arenaSpawn2_z,"
					+ "specSpawn_x, specSpawn_y,specSpawn_z) VALUES ("
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
					+ specArea1.getX()+","
					+ specArea1.getY()+","
					+ specArea1.getZ()+","
					+ specArea2.getX()+","
					+ specArea2.getY()+","
					+ specArea2.getZ()+","
					+ lobbySpawn.getX()+","
					+ lobbySpawn.getY()+","
					+ lobbySpawn.getZ()+","
					+ arenaSpawn1.getX()+","
					+ arenaSpawn1.getY()+","
					+ arenaSpawn1.getZ()+","
					+ arenaSpawn2.getX()+","
					+ arenaSpawn2.getY()+","
					+ arenaSpawn2.getZ()+","
					+ specSpawn.getX()+","
					+ specSpawn.getY()+","
					+ specSpawn.getZ()
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
					+ "specArea1_x,specArea1_y,specArea1_z,"
					+ "specArea2_x,specArea2_y,specArea2_z,"
					+ "lobbySpawn_x,lobbySpawn_y,lobbySpawn_z,"
					+ "arenaSpawn1_x,arenaSpawn1_y,arenaSpawn1_z,"
					+ "arenaSpawn2_x,arenaSpawn2_y,arenaSpawn2_z,"
					+ "specSpawn_x, specSpawn_y,specSpawn_z,world FROM arenas WHERE name='"+name+"'");
			while ( r.next() ) {
				arenaName=name;
				playingArea1=new Vector(r.getDouble(1),r.getDouble(2),r.getDouble(3));
				playingArea2=new Vector(r.getDouble(4),r.getDouble(5),r.getDouble(6));
				lobbyArea1=new Vector(r.getDouble(7),r.getDouble(8),r.getDouble(9));
				lobbyArea2=new Vector(r.getDouble(10),r.getDouble(11),r.getDouble(12));
				winArea1=new Vector(r.getDouble(13),r.getDouble(14),r.getDouble(15));
				winArea2=new Vector(r.getDouble(16),r.getDouble(17),r.getDouble(18));
				specArea1=new Vector(r.getDouble(19),r.getDouble(20),r.getDouble(21));
				specArea2=new Vector(r.getDouble(22),r.getDouble(23),r.getDouble(24));
				lobbySpawn=new Vector(r.getDouble(25),r.getDouble(26),r.getDouble(27));
				arenaSpawn1=new Vector(r.getDouble(28),r.getDouble(29),r.getDouble(30));
				arenaSpawn2=new Vector(r.getDouble(31),r.getDouble(32),r.getDouble(33));
				specSpawn=new Vector(r.getDouble(34),r.getDouble(35),r.getDouble(36));
				myWorld=p.getServer().getWorld(r.getString(37));

				players=new ArrayList<Player>();
				bulldog=new ArrayList<Player>();
				chaser=new ArrayList<Player>();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

};




