package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import science.atlarge.opencraft.opencraft.net.message.play.game.StatisticMessage;

public final class StatisticCodec implements Codec<StatisticMessage> {

    @Override
    public StatisticMessage decode(ByteBuf buffer) throws IOException {
        int size = ByteBufUtils.readVarInt(buffer);
        Map<String, Integer> values = new HashMap<>(size);
        for (int index = 0; index < size; index++) {
            String key = ByteBufUtils.readUTF8(buffer);
            int value = ByteBufUtils.readVarInt(buffer);
            values.put(key, value);
        }
        return new StatisticMessage(values);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, StatisticMessage message) throws IOException {
        Map<String, Integer> map = message.getValues();
        ByteBufUtils.writeVarInt(buffer, map.size());
        for (Entry<String, Integer> entry : map.entrySet()) {
            ByteBufUtils.writeUTF8(buffer, entry.getKey());
            ByteBufUtils.writeVarInt(buffer, entry.getValue());
        }
        return buffer;
    }
}
