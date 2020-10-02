package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.bytebuddy.build.Plugin;
import net.bytebuddy.build.ToStringPlugin;
import net.glowstone.advancement.GlowAdvancement;
import net.glowstone.advancement.GlowAdvancementDisplay;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.AdvancementsMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;

public class AdvancementsCodecTest extends CodecTest<AdvancementsMessage> {

    @Override
    protected Codec<AdvancementsMessage> createCodec() {
        return new AdvancementsCodec();
    }

    @Override
    protected AdvancementsMessage createMessage() {
        Map<NamespacedKey, Advancement> advancements = new HashMap<>();
        List<NamespacedKey> removeAdvancements = new ArrayList<>();
        return new AdvancementsMessage(false, advancements, removeAdvancements);
    }
}
