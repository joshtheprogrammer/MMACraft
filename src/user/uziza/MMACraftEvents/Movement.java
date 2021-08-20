package user.uziza.MMACraftEvents;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import user.uziza.MMACraft.MMACraft;

public class Movement implements Listener {
	private Map<UUID, String> playerActionMap = new HashMap<>();
	private Set<UUID> timeOutSet = new HashSet<>();
	
	private Plugin plugin = MMACraft.getPlugin(MMACraft.class);
	
	PotionEffect tackling = new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200);

	@EventHandler
	public void movement(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
			if(timeOutSet.contains(player.getUniqueId())) {
				if (!player.isSneaking()) {
					timeOutSet.remove(player.getUniqueId());
				}
			}
			else {
				if (!player.isInWater()) {
					if (!player.isFlying()) {
						if (player.isOnGround()) {
							if (!player.isSprinting()) {
								if (player.isSneaking()) {
									playerActionMap.put(player.getUniqueId(), "crouch");
								}
							}
							else {
								if (player.isSneaking()) {
									playerActionMap.put(player.getUniqueId(), "tackle");
								}
							}
						}	
						else {
							if (player.isSneaking()) {
								playerActionMap.put(player.getUniqueId(), "flyingknee");
							}
						}
					}	
				}
			}
	}
	
	@EventHandler
	public void attack(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity interact = event.getRightClicked();
		
		if (interact instanceof LivingEntity) {
			LivingEntity victim = (LivingEntity) interact;
			if (!(victim instanceof Player)) {
				if (victim instanceof Tameable) {
					Tameable tameable_victim = (Tameable) victim;
					if (!tameable_victim.isTamed()) {
						if (playerActionMap.get(player.getUniqueId()) != null) {
							if (playerActionMap.get(player.getUniqueId()) == "tackle") {
								if (victim.getLocation().getY()-player.getLocation().getY() == 0) {
									
									Vector push = new Vector(player.getLocation().getDirection().getX()*.60, 0, player.getLocation().getDirection().getZ()*.60);
									playerActionMap.remove(player.getUniqueId());
									timeOutSet.add(player.getUniqueId());
									player.setVelocity(push.multiply(2));
									victim.setVelocity(push.multiply(2));
								}
							}
							else if (playerActionMap.get(player.getUniqueId()) == "crouch") {
								if (player.getInventory().getItemInHand().getType() == Material.AIR) {
									if (victim.getLocation().getY()-player.getLocation().getY() == 0) {
										Location look = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw() + 210, player.getLocation().getPitch());
										Vector push = new Vector(player.getLocation().getDirection().getX()*.45, player.getLocation().getDirection().getY()*.75, player.getLocation().getDirection().getZ()*.45);
										playerActionMap.remove(player.getUniqueId());
										timeOutSet.add(player.getUniqueId());
										player.teleport(look);
										victim.setVelocity(push.multiply(-2));
									}	
								}
							}
							else if (playerActionMap.get(player.getUniqueId()) == "flyingknee") {
								if (player.isSprinting()) {
									victim.damage(2);
								}
								else {
									victim.damage(1);
								}
								Vector push = new Vector(player.getLocation().getDirection().getX()*.15, .15, player.getLocation().getDirection().getZ()*.15);
								playerActionMap.remove(player.getUniqueId());
								timeOutSet.add(player.getUniqueId());
								victim.setVelocity(push.multiply(1.5));
							}
						}
					}
				}
				else {
					if (playerActionMap.get(player.getUniqueId()) != null) {
						if (playerActionMap.get(player.getUniqueId()) == "tackle") {
							if (victim.getLocation().getY()-player.getLocation().getY() == 0) {
								
								Vector push = new Vector(player.getLocation().getDirection().getX()*.60, 0, player.getLocation().getDirection().getZ()*.60);
								playerActionMap.remove(player.getUniqueId());
								timeOutSet.add(player.getUniqueId());
								player.setVelocity(push.multiply(2));
								victim.setVelocity(push.multiply(2));
							}
						}
						else if (playerActionMap.get(player.getUniqueId()) == "crouch") {
							if (player.getInventory().getItemInHand().getType() == Material.AIR) {
								if (victim.getLocation().getY()-player.getLocation().getY() == 0) {
									Location look = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw() + 210, player.getLocation().getPitch());
									Vector push = new Vector(player.getLocation().getDirection().getX()*.45, player.getLocation().getDirection().getY()*.75, player.getLocation().getDirection().getZ()*.45);
									playerActionMap.remove(player.getUniqueId());
									timeOutSet.add(player.getUniqueId());
									player.teleport(look);
									victim.setVelocity(push.multiply(-2));
								}	
							}
						}
						else if (playerActionMap.get(player.getUniqueId()) == "flyingknee") {
							if (player.isSprinting()) {
								victim.damage(2);
							}
							else {
								victim.damage(1);
							}
							Vector push = new Vector(player.getLocation().getDirection().getX()*.15, .15, player.getLocation().getDirection().getZ()*.15);
							playerActionMap.remove(player.getUniqueId());
							timeOutSet.add(player.getUniqueId());
							victim.setVelocity(push.multiply(1.5));
						}
					}
				}
			}
			else {
				if (playerActionMap.get(player.getUniqueId()) != null) {
					if (playerActionMap.get(player.getUniqueId()) == "tackle") {
						if (playerActionMap.get(victim.getUniqueId()) != "tackle" || playerActionMap.get(victim.getUniqueId()) != "flyingknee" || playerActionMap.get(victim.getUniqueId()) != "crouch") {
							if (victim.getLocation().getY()-player.getLocation().getY() == 0) {
								
								playerActionMap.remove(player.getUniqueId());
								timeOutSet.add(player.getUniqueId());
								Vector push = new Vector(player.getLocation().getDirection().getX()*.60, 0, player.getLocation().getDirection().getZ()*.60);
								player.setVelocity(push.multiply(2));
								victim.setVelocity(push.multiply(2));
							}
						}
						else if (playerActionMap.get(victim.getUniqueId()) == "tackle") {
							playerActionMap.put(player.getUniqueId(), "crouch");
							playerActionMap.put(victim.getUniqueId(), "crouch");
						}
					}
					else if (playerActionMap.get(player.getUniqueId()) == "crouch") {
						if (playerActionMap.get(victim.getUniqueId()) == "tackle") {
							playerActionMap.remove(victim.getUniqueId());
							playerActionMap.put(victim.getUniqueId(), "crouch");
						}
						else if (playerActionMap.get(victim.getUniqueId()) != "flyingknee" || playerActionMap.get(victim.getUniqueId()) != "crouch") {
							if (player.getInventory().getItemInHand().getType() == Material.AIR) {
								if (victim.getLocation().getY()-player.getLocation().getY() == 0) {
									Location look = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw() + 210, player.getLocation().getPitch());
									Vector push = new Vector(player.getLocation().getDirection().getX()*.45, player.getLocation().getDirection().getY()*.75, player.getLocation().getDirection().getZ()*.45);
									playerActionMap.remove(player.getUniqueId());
									timeOutSet.add(player.getUniqueId());
									player.teleport(look);
									victim.setVelocity(push.multiply(-2));
								}	
							}
						}
						
					}
					else if (playerActionMap.get(player.getUniqueId()) == "flyingknee") {
						playerActionMap.remove(player.getUniqueId());
						timeOutSet.add(player.getUniqueId());
						if (playerActionMap.get(victim.getUniqueId()) == "tackle") {
							playerActionMap.remove(victim.getUniqueId());
							timeOutSet.add(victim.getUniqueId());
							if (player.isSprinting()) {
								victim.damage(9);
							}
							else {
								victim.damage(8);
							}
							
							
						}
						else if (playerActionMap.get(victim.getUniqueId()) == "crouch") {
							playerActionMap.remove(victim.getUniqueId());
							timeOutSet.add(victim.getUniqueId());
							if (player.isSprinting()) {
								victim.damage(5);
							}
							else {
								victim.damage(4);
							}
						}
						else {
							if (player.isSprinting()) {
								victim.damage(2);
							}
							else {
								victim.damage(1);
							}
						}
						Vector push = new Vector(player.getLocation().getDirection().getX()*.15, .15, player.getLocation().getDirection().getZ()*.15);
						victim.setVelocity(push.multiply(1.5));
					}
				}
			}
		}
	}
}
