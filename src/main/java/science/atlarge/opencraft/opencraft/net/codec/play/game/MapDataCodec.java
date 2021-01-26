package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import science.atlarge.opencraft.opencraft.net.message.play.game.MapDataMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.MapDataMessage.Icon;
import science.atlarge.opencraft.opencraft.net.message.play.game.MapDataMessage.Section;

public final class MapDataCodec implements Codec<MapDataMessage> {

    @Override
    public MapDataMessage decode(ByteBuf buffer) throws IOException {

        int id = ByteBufUtils.readVarInt(buffer);
        byte scale = buffer.readByte();

        int size = ByteBufUtils.readVarInt(buffer);
        List<Icon> icons = new ArrayList<>(size);
        for (int index = 0; index < size; index++) {
            int typeAndFacing = buffer.readByte();
            int type = (byte) (typeAndFacing & 0x0F);
            int facing = (byte) (typeAndFacing >> 4);
            int x = buffer.readByte();
            int y = buffer.readByte();
            Icon icon = new Icon(type, facing, x, y);
            icons.add(icon);
        }

        byte sectionWidth = buffer.readByte();
        if (sectionWidth == 0) {
            return new MapDataMessage(id, scale, icons, null);
        }
        byte sectionHeight = buffer.readByte();
        byte sectionX = buffer.readByte();
        byte sectionY = buffer.readByte();
        int sectionDataLength = ByteBufUtils.readVarInt(buffer);
        byte[] sectionData = new byte[sectionDataLength];
        buffer.readBytes(sectionData);
        Section section = new Section(sectionWidth, sectionHeight, sectionX, sectionY, sectionData);

        return new MapDataMessage(id, scale, icons, section);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, MapDataMessage message) {

        List<Icon> icons = message.getIcons();

        ByteBufUtils.writeVarInt(buffer, message.getId());
        buffer.writeByte(message.getScale());

        ByteBufUtils.writeVarInt(buffer, icons.size());
        for (Icon icon : icons) {
            buffer.writeByte(icon.facing << 4 | icon.type);
            buffer.writeByte(icon.x);
            buffer.writeByte(icon.y);
        }
        Section section = message.getSection();
        if (section == null) {
            buffer.writeByte(0);
        } else {
            buffer.writeByte(section.width);
            buffer.writeByte(section.height);
            buffer.writeByte(section.x);
            buffer.writeByte(section.y);
            ByteBufUtils.writeVarInt(buffer, section.data.length);
            buffer.writeBytes(section.data);
        }

        return buffer;
    }
}
