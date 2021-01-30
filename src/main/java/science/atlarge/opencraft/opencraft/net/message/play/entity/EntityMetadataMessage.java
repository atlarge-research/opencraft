package science.atlarge.opencraft.opencraft.net.message.play.entity;

import com.flowpowered.network.Message;
import java.util.List;
import lombok.Data;
import science.atlarge.opencraft.opencraft.entity.meta.MetadataMap.Entry;

@Data
public final class EntityMetadataMessage implements Message {

    private final int id;
    private final List<Entry> entries;

}
