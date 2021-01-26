package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import science.atlarge.opencraft.opencraft.net.message.play.game.ExplosionMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.ExplosionMessage.Record;

public class ExplosionCodec implements Codec<ExplosionMessage> {

    @Override
    public ExplosionMessage decode(ByteBuf buffer) throws IOException {

        float x = buffer.readFloat();
        float y = buffer.readFloat();
        float z = buffer.readFloat();
        float radius = buffer.readFloat();

        int size = buffer.readInt();
        Collection<Record> records = new ArrayList<>(size);
        for (int index = 0; index < size; index++) {
            byte recordX = buffer.readByte();
            byte recordY = buffer.readByte();
            byte recordZ = buffer.readByte();
            Record record = new Record(recordX, recordY, recordZ);
            records.add(record);
        }

        float playerMotionX = buffer.readFloat();
        float playerMotionY = buffer.readFloat();
        float playerMotionZ = buffer.readFloat();

        return new ExplosionMessage(x, y, z, radius, playerMotionX, playerMotionY, playerMotionZ, records);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, ExplosionMessage message) throws IOException {

        buffer.writeFloat(message.getX());
        buffer.writeFloat(message.getY());
        buffer.writeFloat(message.getZ());
        buffer.writeFloat(message.getRadius());

        Collection<Record> records = message.getRecords();
        buffer.writeInt(records.size());
        for (Record record : records) {
            buffer.writeByte(record.getX());
            buffer.writeByte(record.getY());
            buffer.writeByte(record.getZ());
        }

        buffer.writeFloat(message.getPlayerMotionX());
        buffer.writeFloat(message.getPlayerMotionY());
        buffer.writeFloat(message.getPlayerMotionZ());

        return buffer;
    }
}
