package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.player.PlayerSwingArmMessage;

public final class PlayerSwingArmCodec implements Codec<PlayerSwingArmMessage> {

    @Override
    public PlayerSwingArmMessage decode(ByteBuf buffer) throws IOException {
        int hand = ByteBufUtils.readVarInt(buffer);
        return new PlayerSwingArmMessage(hand);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, PlayerSwingArmMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getHand());
        return buffer;
    }
}
