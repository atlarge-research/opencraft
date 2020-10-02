package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.EntityTeleportMessage;

public class EntityTeleportCodecTest extends CodecTest<EntityTeleportMessage> {

    @Override
    protected Codec<EntityTeleportMessage> createCodec() {
        return new EntityTeleportCodec();
    }

    @Override
    protected EntityTeleportMessage createMessage() {
        return new EntityTeleportMessage(1, 2.0, 3.0, 4.0, 5, 6, false);
    }
}
