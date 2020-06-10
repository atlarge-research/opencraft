package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.glowstone.entity.AttributeManager;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.EntityPropertyMessage;
import org.bukkit.attribute.AttributeModifier;

public class EntityPropertyCodecTest extends CodecTest<EntityPropertyMessage> {

    @Override
    protected Codec<EntityPropertyMessage> createCodec() {
        return new EntityPropertyCodec();
    }

    @Override
    protected EntityPropertyMessage createMessage() {
        int id = 1;
        Map<String, AttributeManager.Property> properties = new HashMap<>();
        AttributeManager.Key key = AttributeManager.Key.KEY_ARMOR;
        double value = 10.0;
        Collection<AttributeModifier> modifiers = new ArrayList<>();
        properties.put(key.toString(), new AttributeManager.Property(key, value, modifiers));
        return new EntityPropertyMessage(id, properties);
    }
}
