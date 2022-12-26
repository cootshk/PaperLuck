package io.github.cootshk.paperluck;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;

import java.lang.Math;

public final class PaperLuck extends JavaPlugin implements Listener{
    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Plugin Started!"); // Startup
        getServer().getPluginManager().registerEvents(this, this); // Run the plugin
        Objects.requireNonNull(this.getCommand("kit")).setExecutor(new PaperLuck()); // Setup command
    }


    //Loot table
    private final Plugin plugin = getPlugin(PaperLuck.class);
    private final NamespacedKey key = new NamespacedKey(plugin, "PaperLuck");
    private final Collection<ItemStack> items = new ArrayList<>();

    public Collection<ItemStack> populateLoot(Random random, LootContext context) {
        double luck_modifier;

        double luck_level = context.getLuck();
        if (luck_level > 0) {
            luck_modifier = Math.pow(luck_level, 1.5) / 3;
        }
        else if (luck_level == 0){
            luck_modifier = 1;
        }
        else {
            luck_modifier = 0;
        }
        int random_modifier = random.nextInt(5);
        int item_count = (int) ((Math.floor(luck_modifier/2)+1) * random_modifier);

        int diamond_count;
        int coal_count;

        if (item_count > 1) {
            diamond_count = random.nextInt(item_count / 4);
            coal_count = item_count - diamond_count;
        }
        else if (item_count == 1){
            diamond_count = 0;
            coal_count = 1;
        }
        else {
            diamond_count = 0;
            coal_count = 0;
        }



        ItemStack diamonds = new ItemStack(Material.DIAMOND, diamond_count);
        ItemStack coals = new ItemStack(Material.COAL, coal_count);

        items.add(diamonds);
        items.add(coals);

        return items;
    }

    public void fillInventory(Inventory inventory, Random random, LootContext context) {

    }

    public NamespacedKey getKey() {
        return key;
    }

    //Item interaction handler
    @EventHandler
    public void PaperInteracted(PlayerInteractEvent event){
        @Nullable ItemStack item = event.getItem();
        if (item == null){
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (item.getType() == Material.PAPER){
            System.out.println("Paper has been used");
            if (meta.hasCustomModelData()){
                if (meta.getCustomModelData() == 1){
                    Player player = event.getPlayer();
                    double luck_mod = player.getAttribute(Attribute.GENERIC_LUCK).getValue();
                    Location location = event.getPlayer().getLocation();

                    LootContext.Builder builder = new LootContext.Builder(location);
                    builder.lootedEntity(event.getPlayer());
                    builder.lootingModifier(1);
                    builder.luck((float) luck_mod);
                    builder.killer(player);
                    LootContext lootContext = builder.build();

                    Collection<ItemStack> drops = this.populateLoot(new Random(), lootContext);
                    ArrayList<ItemStack> items = (ArrayList<ItemStack>) drops;

                    for (int a = 0; a < 2; a++) {
                        if (items.get(a).getAmount() > 0) {
                            location.getWorld().dropItemNaturally(location, items.get(a));
                        }
                    }

                    event.getItem().add(-1);
                }
                //other paper was used

            }
            //Generic paper was used

        }
        //paper was not used
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Plugin Stopped!");
    }
}
