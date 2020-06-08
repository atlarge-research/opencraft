package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.EntityEquipmentMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EntityEquipmentCodecTest extends CodecTest<EntityEquipmentMessage> {

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
