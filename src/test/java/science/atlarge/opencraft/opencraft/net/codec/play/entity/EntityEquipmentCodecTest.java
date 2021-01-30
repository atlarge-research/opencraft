package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.MockedCodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityEquipmentMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EntityEquipmentCodecTest extends MockedCodecTest<EntityEquipmentMessage> {

    @Override
    protected Codec<EntityEquipmentMessage> createCodec() {
        return new EntityEquipmentCodec();
    }

    @Override
    protected EntityEquipmentMessage createMessage() {
        ItemStack item = new ItemStack(Material.DIRT, 1);
        return new EntityEquipmentMessage(1, 2, item);
    }
}
