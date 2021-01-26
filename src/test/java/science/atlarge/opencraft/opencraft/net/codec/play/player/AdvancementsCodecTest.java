package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.AdvancementsMessage;
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
