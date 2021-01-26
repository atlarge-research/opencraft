package science.atlarge.opencraft.opencraft.net.message.play.inv;

import com.flowpowered.network.Message;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
public final class SetWindowSlotMessage implements Message {

    private final int id;
    private final int slot;
    private final ItemStack item;

}
