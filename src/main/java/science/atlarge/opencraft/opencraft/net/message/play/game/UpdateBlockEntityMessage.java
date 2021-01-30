package science.atlarge.opencraft.opencraft.net.message.play.game;

import com.flowpowered.network.Message;
import lombok.Data;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;

@Data
public final class UpdateBlockEntityMessage implements Message {

    private final int x;
    private final int y;
    private final int z;
    private final int action;
    private final CompoundTag nbt;

}
