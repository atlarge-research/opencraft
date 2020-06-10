package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.play.player.ServerDifficultyMessage;
import org.bukkit.Difficulty;

public final class ServerDifficultyCodec implements Codec<ServerDifficultyMessage> {

    @Override
    public ServerDifficultyMessage decode(ByteBuf buffer) {
        int difficulty = buffer.readUnsignedByte();
        return new ServerDifficultyMessage(Difficulty.values()[difficulty]);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, ServerDifficultyMessage message) {
        return buffer.writeByte(message.getDifficulty().ordinal());
    }
}
