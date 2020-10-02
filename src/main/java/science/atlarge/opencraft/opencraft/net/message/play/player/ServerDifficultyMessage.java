package science.atlarge.opencraft.opencraft.net.message.play.player;

import com.flowpowered.network.Message;
import lombok.Data;
import org.bukkit.Difficulty;

@Data
public final class ServerDifficultyMessage implements Message {

    private final Difficulty difficulty;

}
