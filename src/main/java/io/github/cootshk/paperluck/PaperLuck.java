package io.github.cootshk.paperluck;

//import org.bukkit.entity.Player;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperLuck extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Plugin Started!"); // Startup
        getServer().getPluginManager().registerEvents(this,this); // Run the plugin
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){ // When a player joins
        System.out.println("Player joined!");
        event.setJoinMessage("Welcome!");
    }
    @EventHandler
    public void PlayerInteractEvent(Player player, int previous, int current){
    //Subscribe to BeesYT
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Plugin Stopped!");
    }
}
