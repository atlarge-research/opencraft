package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.game.UnlockRecipesMessage;

public final class UnlockRecipesCodec implements Codec<UnlockRecipesMessage> {

    @Override
    public UnlockRecipesMessage decode(ByteBuf buffer) throws IOException {

        int action = ByteBufUtils.readVarInt(buffer);
        boolean bookOpen = buffer.readBoolean();
        boolean filterOpen = buffer.readBoolean();

        int sizeOfRecipes = ByteBufUtils.readVarInt(buffer);
        int[] recipes = new int[sizeOfRecipes];
        for (int i = 0; i < sizeOfRecipes; i++) {
            recipes[i] = ByteBufUtils.readVarInt(buffer);
        }

        if (action != UnlockRecipesMessage.ACTION_INIT) {
            return new UnlockRecipesMessage(action, bookOpen, filterOpen, recipes);
        }

        sizeOfRecipes = ByteBufUtils.readVarInt(buffer);
        int[] allRecipes = new int[sizeOfRecipes];
        for (int i = 0; i < sizeOfRecipes; i++) {
            allRecipes[i] = ByteBufUtils.readVarInt(buffer);
        }

        return new UnlockRecipesMessage(action, bookOpen, filterOpen, recipes, allRecipes);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, UnlockRecipesMessage message) {

        ByteBufUtils.writeVarInt(buffer, message.getAction());
        buffer.writeBoolean(message.isBookOpen());
        buffer.writeBoolean(message.isFilterOpen());

        ByteBufUtils.writeVarInt(buffer, message.getRecipes().length);
        for (int recipe : message.getRecipes()) {
            ByteBufUtils.writeVarInt(buffer, recipe);
        }

        if (message.getAction() == UnlockRecipesMessage.ACTION_INIT
            && message.getAllRecipes() != null) {
            ByteBufUtils.writeVarInt(buffer, message.getAllRecipes().length);
            for (int recipe : message.getAllRecipes()) {
                ByteBufUtils.writeVarInt(buffer, recipe);
            }
        }

        return buffer;
    }
}
