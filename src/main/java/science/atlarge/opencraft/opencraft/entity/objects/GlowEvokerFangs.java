package science.atlarge.opencraft.opencraft.entity.objects;

import com.flowpowered.network.Message;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import science.atlarge.opencraft.opencraft.entity.GlowEntity;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SpawnObjectMessage;
import science.atlarge.opencraft.opencraft.util.Position;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.LivingEntity;

public class GlowEvokerFangs extends GlowEntity implements EvokerFangs {

    @Getter
    @Setter
    private LivingEntity owner;

    public GlowEvokerFangs(Location location) {
        super(location);
        setBoundingBox(0.5, 0.8);
    }

    @Override
    public List<Message> createSpawnMessage() {
        List<Message> result = new LinkedList<>();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        int yaw = Position.getIntYaw(location);
        int pitch = Position.getIntPitch(location);
        result.add(new SpawnObjectMessage(
                entityId, this.getUniqueId(), 79, x, y, z, pitch, yaw, 0, 0, 0, 0));
        return result;
    }

    @Override
    public EntityType getType() {
        return EntityType.EVOKER_FANGS;
    }
}
