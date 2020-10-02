package science.atlarge.opencraft.opencraft.command.minecraft;

import static science.atlarge.opencraft.opencraft.TestUtils.itemTypeMatcher;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.when;

import science.atlarge.opencraft.opencraft.command.CommandTestWithFakePlayers;
import science.atlarge.opencraft.opencraft.inventory.GlowItemFactory;
import science.atlarge.opencraft.opencraft.inventory.GlowPlayerInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import science.atlarge.opencraft.opencraft.TestUtils;

public class ClearCommandTest extends CommandTestWithFakePlayers<ClearCommand> {
    private GlowPlayerInventory inventory;

    public ClearCommandTest() {
        super(ClearCommand::new, "ChuckNorris");
    }

    @Override
    @Before
    public void before() {
        super.before();
        when(Bukkit.getItemFactory()).thenReturn(GlowItemFactory.instance());
        inventory = new GlowPlayerInventory(fakePlayers[0]);
        inventory.setItemInMainHand(new ItemStack(Material.DIRT, 32));
        inventory.setItemInOffHand(new ItemStack(Material.DIAMOND_AXE, 1, (short) 30));
        inventory.setItem(9, new ItemStack(Material.DIRT, 64));
        inventory.setItem(10, new ItemStack(Material.DIAMOND));
        inventory.setItem(11, new ItemStack(Material.DIAMOND_AXE));
        when(fakePlayers[0].getInventory()).thenReturn(inventory);
    }

    @Test
    public void testClearAll() {
        assertTrue(command.execute(opSender, "label", new String[]{"ChuckNorris"}));
        TestUtils.checkInventory(inventory, 0, item -> true); // should be empty
    }

    @Test
    public void testCountOnly() {
        assertTrue(command.execute(opSender, "label",
                new String[]{"ChuckNorris", "minecraft:diamond_axe", "-1", "0"}));
        Mockito.verify(opSender)
                .sendMessage(eq("ChuckNorris has 2 items that match the criteria."));
        TestUtils.checkInventory(inventory, 96, TestUtils.itemTypeMatcher(Material.DIRT));
        TestUtils.checkInventory(inventory, 2, TestUtils.itemTypeMatcher(Material.DIAMOND_AXE));
        TestUtils.checkInventory(inventory, 1, TestUtils.itemTypeMatcher(Material.DIAMOND));
    }

    @Test
    public void testClearSpecificItemAll() {
        assertTrue(command.execute(opSender, "label",
                new String[]{"ChuckNorris", "minecraft:diamond_axe", "-1", "-1"}));
        TestUtils.checkInventory(inventory, 96, TestUtils.itemTypeMatcher(Material.DIRT));
        TestUtils.checkInventory(inventory, 0, TestUtils.itemTypeMatcher(Material.DIAMOND_AXE));
        TestUtils.checkInventory(inventory, 1, TestUtils.itemTypeMatcher(Material.DIAMOND));
    }

    @Test
    public void testClearSpecificItemLimited() {
        assertTrue(command.execute(opSender, "label",
                new String[]{"ChuckNorris", "minecraft:dirt", "-1", "50"}));
        TestUtils.checkInventory(inventory, 46, TestUtils.itemTypeMatcher(Material.DIRT));
        TestUtils.checkInventory(inventory, 2, TestUtils.itemTypeMatcher(Material.DIAMOND_AXE));
        TestUtils.checkInventory(inventory, 1, TestUtils.itemTypeMatcher(Material.DIAMOND));
    }

    @Test
    public void testClearSpecificItemSpecificData() {
        assertTrue(command.execute(opSender, "label",
                new String[]{"ChuckNorris", "minecraft:diamond_axe", "30", "-1"}));
        TestUtils.checkInventory(inventory, 96, TestUtils.itemTypeMatcher(Material.DIRT));
        TestUtils.checkInventory(inventory, 0,
                TestUtils.itemTypeMatcher(Material.DIAMOND_AXE).and(item -> item.getDurability() == 30));
        TestUtils.checkInventory(inventory, 1,
                TestUtils.itemTypeMatcher(Material.DIAMOND_AXE).and(item -> item.getDurability() == 0));
        TestUtils.checkInventory(inventory, 1, TestUtils.itemTypeMatcher(Material.DIAMOND));
    }
}
