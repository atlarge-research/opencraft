package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.game.CraftRecipeResponseMessage;

public final class CraftRecipeResponseCodec implements Codec<CraftRecipeResponseMessage> {

    @Override
    public CraftRecipeResponseMessage decode(ByteBuf buffer) throws IOException {
        int windowId = buffer.readByte();
        int recipeId = ByteBufUtils.readVarInt(buffer);
        return new CraftRecipeResponseMessage(windowId, recipeId);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, CraftRecipeResponseMessage message) {
        buffer.writeByte(message.getWindowId());
        ByteBufUtils.writeVarInt(buffer, message.getRecipeId());
        return buffer;
    }
}
