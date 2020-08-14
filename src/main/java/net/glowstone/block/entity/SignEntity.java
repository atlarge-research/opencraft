package net.glowstone.block.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.flowpowered.network.Message;
import java.util.Arrays;
import net.glowstone.GlowWorld;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.entity.state.GlowSign;
import net.glowstone.constants.GlowBlockEntity;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.message.play.game.UpdateBlockEntityMessage;
import net.glowstone.util.TextMessage;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.Location;
import org.bukkit.Material;

public class SignEntity extends BlockEntity {

    private final TextMessage[] lines = new TextMessage[4];

    /**
     * Creates the entity for the given sign block.
     *
     * @param block a sign block (wall or post)
     */
    public SignEntity(GlowBlock block) {
        super(block);
        setSaveId("minecraft:sign");

        if (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) {
            throw new IllegalArgumentException(
                "Sign must be WALL_SIGN or SIGN_POST, got " + block.getType());
        }

        Arrays.fill(lines, new TextMessage(""));
    }

    @Override
    public void update(GlowPlayer player) {
        sendSignChange(getBlock().getLocation(), lines);
    }

    /**
     * Send a sign change using complete TextMessages instead of strings.
     *
     * @param location the location of the sign
     * @param lines the new text on the sign or null to clear it
     * @throws IllegalArgumentException if location is null
     * @throws IllegalArgumentException if lines is non-null and has a length less than 4
     */
    public void sendSignChange(Location location, TextMessage[] lines)
            throws IllegalArgumentException {

        checkNotNull(location, "location cannot be null");
        checkNotNull(lines, "lines cannot be null");
        checkArgument(lines.length == 4, "lines.length must equal 4");

        CompoundTag tag = new CompoundTag();
        GlowWorld world = getBlock().getWorld();
        saveNbt(tag);

        Message message = new UpdateBlockEntityMessage(location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                GlowBlockEntity.SIGN.getValue(),
                tag
        );
        world.broadcastAfterBlockChange(location, message);
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
        for (int i = 0; i < lines.length; ++i) {
            final int finalI = i;
            tag.readString("Text" + (i + 1), line -> lines[finalI] = TextMessage.decode(line));
        }
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
        for (int i = 0; i < lines.length; ++i) {
            tag.putString("Text" + (i + 1), lines[i].encode());
        }
    }

    @Override
    public GlowBlockState getState() {
        return new GlowSign(block);
    }

    /**
     * Get the lines of text on the sign.
     *
     * @return The sign's lines.
     */
    public String[] getLines() {
        String[] result = new String[lines.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = lines[i].asPlaintext();
        }
        return result;
    }

    /**
     * Set the lines of text on the sign.
     *
     * @param text The lines of text.
     * @throws IllegalArgumentException If the wrong number of lines is provided.
     */
    public void setLines(String... text) {
        if (text.length != lines.length) {
            throw new IllegalArgumentException(
                "Provided lines were length " + text.length + ", must be " + lines.length);
        }

        for (int i = 0; i < lines.length; ++i) {
            lines[i] = new TextMessage(text[i] == null ? "" : text[i]);
        }
    }
}
