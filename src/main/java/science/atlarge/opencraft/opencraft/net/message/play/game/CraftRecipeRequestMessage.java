package science.atlarge.opencraft.opencraft.net.message.play.game;

import com.flowpowered.network.Message;
import lombok.Data;

@Data
public final class CraftRecipeRequestMessage implements Message {

    private final int windowId;
    private final int recipeId;
    private final boolean makeAll;

}
