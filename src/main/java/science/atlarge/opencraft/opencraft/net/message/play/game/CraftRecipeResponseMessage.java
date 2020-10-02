package science.atlarge.opencraft.opencraft.net.message.play.game;

import com.flowpowered.network.Message;
import lombok.Data;

@Data
public final class CraftRecipeResponseMessage implements Message {

    private final int windowId;
    private final int recipeId;

}
