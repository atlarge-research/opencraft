package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import java.util.ArrayList;
import java.util.List;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.MapDataMessage;

public class MapDataCodecTest extends CodecTest<MapDataMessage> {

    @Override
    protected Codec<MapDataMessage> createCodec() {
        return new MapDataCodec();
    }

    @Override
    protected MapDataMessage createMessage() {
        List<MapDataMessage.Icon> icons = new ArrayList<>();
        return new MapDataMessage(1, 2, icons, new MapDataMessage.Section(1, 2, 3, 4, new byte[]{ 1, 2 }));
    }
}
