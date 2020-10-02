package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.play.player.PlayerAbilitiesMessage;

public final class PlayerAbilitiesCodec implements Codec<PlayerAbilitiesMessage> {

    @Override
    public PlayerAbilitiesMessage decode(ByteBuf buffer) {
        int flags = buffer.readUnsignedByte();
        float flySpeed = buffer.readFloat();
        float walkSpeed = buffer.readFloat();
        return new PlayerAbilitiesMessage(flags, flySpeed, walkSpeed);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, PlayerAbilitiesMessage message) {
        buffer.writeByte(message.getFlags());
        buffer.writeFloat(message.getFlySpeed());
        buffer.writeFloat(message.getWalkSpeed());
        return buffer;
    }
}
