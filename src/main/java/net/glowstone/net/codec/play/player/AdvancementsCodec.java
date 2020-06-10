package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.glowstone.advancement.GlowAdvancement;
import net.glowstone.advancement.GlowAdvancementDisplay;
import net.glowstone.net.GlowBufUtils;
import net.glowstone.net.message.play.player.AdvancementsMessage;
import net.glowstone.util.TextMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.inventory.ItemStack;

public class AdvancementsCodec implements Codec<AdvancementsMessage> {

    @Override
    public AdvancementsMessage decode(ByteBuf buffer) throws IOException {

        boolean clear = buffer.readBoolean();

        int advancementCount = ByteBufUtils.readVarInt(buffer);
        Map<NamespacedKey, Advancement> advancements = new HashMap<>(advancementCount);
        for (int advancementIndex = 0; advancementIndex < advancementCount; advancementIndex++) {

            NamespacedKey key = readNamedspacedKey(buffer);

            boolean hasParent = buffer.readBoolean();
            GlowAdvancement parent = null;
            if (hasParent) {
                NamespacedKey parentKey = readNamedspacedKey(buffer);
                parent = new GlowAdvancement(parentKey, null);
            }

            boolean hasDisplay = buffer.readBoolean();
            GlowAdvancementDisplay display = null;
            if (hasDisplay) {

                TextMessage title = GlowBufUtils.readChat(buffer);
                TextMessage description = GlowBufUtils.readChat(buffer);
                ItemStack icon = GlowBufUtils.readSlot(buffer);

                int typeIndex = ByteBufUtils.readVarInt(buffer);
                GlowAdvancementDisplay.FrameType type = GlowAdvancementDisplay.FrameType.values()[typeIndex];

                // flags
                buffer.readInt();

                float x = buffer.readFloat();
                float y = buffer.readFloat();

                display = new GlowAdvancementDisplay(title, description, icon, type, x, y);
            }


            GlowAdvancement advancement = new GlowAdvancement(key, parent, display);
            advancements.put(key, advancement);
        }

        int removalCount = ByteBufUtils.readVarInt(buffer);
        List<NamespacedKey> removals = new ArrayList<>();
        for (int removalIndex = 0; removalIndex < removalCount; removalIndex++) {
            NamespacedKey key = readNamedspacedKey(buffer);
            removals.add(key);
        }

        ByteBufUtils.readVarInt(buffer);

        return new AdvancementsMessage(clear, advancements, removals);
    }

    private NamespacedKey readNamedspacedKey(ByteBuf buffer) throws IOException {
        String concatenated = ByteBufUtils.readUTF8(buffer);
        String[] components = concatenated.split(":");
        if (components.length != 2) {
            throw new DecoderException("Could not parse name-spaced key: " + concatenated);
        }
        String namespace = components[0];
        String key = components[1];
        return new NamespacedKey(namespace, key);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, AdvancementsMessage message) throws IOException {

        buffer.writeBoolean(message.isClear());

        ByteBufUtils.writeVarInt(buffer, message.getAdvancements().size());
        for (NamespacedKey key : message.getAdvancements().keySet()) {
            ByteBufUtils.writeUTF8(buffer, key.toString());
            GlowAdvancement advancement = (GlowAdvancement) message.getAdvancements().get(key);
            advancement.encode(buffer);
        }

        ByteBufUtils.writeVarInt(buffer, message.getRemoveAdvancements().size());
        for (NamespacedKey key : message.getRemoveAdvancements()) {
            ByteBufUtils.writeUTF8(buffer, key.toString());
        }

        ByteBufUtils.writeVarInt(buffer, 0);

        return buffer;
    }
}
