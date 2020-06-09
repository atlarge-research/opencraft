package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.game.CraftRecipeRequestMessage;

public final class CraftRecipeRequestCodec implements Codec<CraftRecipeRequestMessage> {

    @Override
    public CraftRecipeRequestMessage decode(ByteBuf buffer) throws IOException {
        int windowId = buffer.readByte();
        int recipeId = ByteBufUtils.readVarInt(buffer);
        boolean makeAll = buffer.readBoolean();
        return new CraftRecipeRequestMessage(windowId, recipeId, makeAll);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, CraftRecipeRequestMessage message) {
        buffer.writeByte(message.getWindowId());
        ByteBufUtils.writeVarInt(buffer, message.getRecipeId());
        buffer.writeBoolean(message.isMakeAll());
        return buffer;
    }
}
