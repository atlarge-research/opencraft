package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import java.util.ArrayList;
import java.util.List;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.TabCompleteResponseMessage;

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
