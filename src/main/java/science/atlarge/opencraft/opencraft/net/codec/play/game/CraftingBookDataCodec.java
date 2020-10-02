package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.game.CraftingBookDataMessage;

public final class CraftingBookDataCodec implements Codec<CraftingBookDataMessage> {

    @Override
    public CraftingBookDataMessage decode(ByteBuf buffer) throws IOException {
        int type = ByteBufUtils.readVarInt(buffer);
        if (type == CraftingBookDataMessage.TYPE_DISPLAYED_RECIPE) {
            int recipeId = buffer.readInt();
            return new CraftingBookDataMessage(type, recipeId);
        } else if (type == CraftingBookDataMessage.TYPE_STATUS) {
            boolean bookOpen = buffer.readBoolean();
            boolean filter = buffer.readBoolean();
            return new CraftingBookDataMessage(type, bookOpen, filter);
        }
        return null;
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, CraftingBookDataMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getType());
        if (message.getType() == CraftingBookDataMessage.TYPE_DISPLAYED_RECIPE) {
            buffer.writeInt(message.getRecipeId());
        } else if (message.getType() == CraftingBookDataMessage.TYPE_STATUS) {
            buffer.writeBoolean(message.isBookOpen());
            buffer.writeBoolean(message.isFilter());
        }
        return buffer;
    }
}
