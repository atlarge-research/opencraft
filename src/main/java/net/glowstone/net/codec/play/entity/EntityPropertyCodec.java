package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import net.glowstone.entity.AttributeManager;
import net.glowstone.net.GlowBufUtils;
import net.glowstone.net.message.play.entity.EntityPropertyMessage;
import org.bukkit.attribute.AttributeModifier;

public class EntityPropertyCodec implements Codec<EntityPropertyMessage> {

    @Override
    public EntityPropertyMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        int propertiesSize = buffer.readInt();
        Map<String, AttributeManager.Property> properties = new HashMap<>(propertiesSize);
        for (int propertyIndex = 0; propertyIndex < propertiesSize; propertyIndex++) {

            String name = ByteBufUtils.readUTF8(buffer);
            double value = buffer.readDouble();

            int modifiersSize = ByteBufUtils.readVarInt(buffer);
            Collection<AttributeModifier> modifiers = new ArrayList<>(modifiersSize);
            for (int modifierIndex = 0; modifierIndex < modifiersSize; modifierIndex++) {

                UUID uuid = GlowBufUtils.readUuid(buffer);
                double amount = buffer.readDouble();
                byte ordinal = buffer.readByte();

                AttributeModifier.Operation operation = AttributeModifier.Operation.values()[ordinal];
                AttributeModifier modifier = new AttributeModifier(uuid, name, amount, operation);
                modifiers.add(modifier);
            }

            AttributeManager.Key key = AttributeManager.Key.fromName(name);
            AttributeManager.Property property = new AttributeManager.Property(key, value, modifiers);
            property.getValue();
            properties.put(name, property);
        }
        return new EntityPropertyMessage(id, properties);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, EntityPropertyMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        Map<String, AttributeManager.Property> props = message.getProperties();
        buffer.writeInt(props.size());
        for (Entry<String, AttributeManager.Property> property : props.entrySet()) {

            ByteBufUtils.writeUTF8(buffer, property.getKey());
            buffer.writeDouble(property.getValue().getValue());

            Collection<AttributeModifier> modifiers = property.getValue().getModifiers();
            if (modifiers == null) {
                ByteBufUtils.writeVarInt(buffer, 0);
            } else {
                ByteBufUtils.writeVarInt(buffer, modifiers.size());
                for (AttributeModifier modifier : modifiers) {
                    GlowBufUtils.writeUuid(buffer, modifier.getUniqueId());
                    buffer.writeDouble(modifier.getAmount());
                    buffer.writeByte(modifier.getOperation().ordinal());
                }
            }
        }

        return buffer;
    }
}
