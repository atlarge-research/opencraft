package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.TabCompleteMessage;
import org.bukkit.util.BlockVector;

public class TabCompleteCodecTest extends CodecTest<TabCompleteMessage> {

    @Override
    protected Codec<TabCompleteMessage> createCodec() {
        return new TabCompleteCodec();
    }

    @Override
    protected TabCompleteMessage createMessage() {
        return new TabCompleteMessage("one", true, new BlockVector(1.0f, 2.0f, 3.0f));
    }
}
