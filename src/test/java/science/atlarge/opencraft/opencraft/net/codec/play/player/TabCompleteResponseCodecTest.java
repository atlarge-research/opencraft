package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import java.util.ArrayList;
import java.util.List;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.TabCompleteResponseMessage;

public class TabCompleteResponseCodecTest extends CodecTest<TabCompleteResponseMessage> {

    @Override
    protected Codec<TabCompleteResponseMessage> createCodec() {
        return new TabCompleteResponseCodec();
    }

    @Override
    protected TabCompleteResponseMessage createMessage() {
        List<String> completions = new ArrayList<>();
        completions.add("one");
        completions.add("two");
        return new TabCompleteResponseMessage(completions);
    }
}
