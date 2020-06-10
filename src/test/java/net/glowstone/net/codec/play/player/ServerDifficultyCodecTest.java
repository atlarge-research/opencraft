package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.ServerDifficultyMessage;
import org.bukkit.Difficulty;

public class ServerDifficultyCodecTest extends CodecTest<ServerDifficultyMessage> {

    @Override
    protected Codec<ServerDifficultyMessage> createCodec() {
        return new ServerDifficultyCodec();
    }

    @Override
    protected ServerDifficultyMessage createMessage() {
        return new ServerDifficultyMessage(Difficulty.NORMAL);
    }
}
