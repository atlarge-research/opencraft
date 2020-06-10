package net.glowstone.messaging.brokers.codecs;

import com.flowpowered.network.Codec;
import com.flowpowered.network.Message;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;
import net.glowstone.messaging.brokers.JmsCodec;
import net.glowstone.net.codec.KickCodec;
import net.glowstone.net.codec.play.entity.AnimateEntityCodec;
import net.glowstone.net.codec.play.entity.AttachEntityCodec;
import net.glowstone.net.codec.play.entity.CollectItemCodec;
import net.glowstone.net.codec.play.entity.DestroyEntitiesCodec;
import net.glowstone.net.codec.play.entity.EntityEffectCodec;
import net.glowstone.net.codec.play.entity.EntityEquipmentCodec;
import net.glowstone.net.codec.play.entity.EntityHeadRotationCodec;
import net.glowstone.net.codec.play.entity.EntityMetadataCodec;
import net.glowstone.net.codec.play.entity.EntityPropertyCodec;
import net.glowstone.net.codec.play.entity.EntityRemoveEffectCodec;
import net.glowstone.net.codec.play.entity.EntityRotationCodec;
import net.glowstone.net.codec.play.entity.EntityStatusCodec;
import net.glowstone.net.codec.play.entity.EntityTeleportCodec;
import net.glowstone.net.codec.play.entity.EntityVelocityCodec;
import net.glowstone.net.codec.play.entity.RelativeEntityPositionCodec;
import net.glowstone.net.codec.play.entity.RelativeEntityPositionRotationCodec;
import net.glowstone.net.codec.play.entity.SetCooldownCodec;
import net.glowstone.net.codec.play.entity.SetPassengerCodec;
import net.glowstone.net.codec.play.entity.SpawnLightningStrikeCodec;
import net.glowstone.net.codec.play.entity.SpawnMobCodec;
import net.glowstone.net.codec.play.entity.SpawnObjectCodec;
import net.glowstone.net.codec.play.entity.SpawnPaintingCodec;
import net.glowstone.net.codec.play.entity.SpawnPlayerCodec;
import net.glowstone.net.codec.play.entity.SpawnXpOrbCodec;
import net.glowstone.net.codec.play.entity.VehicleMoveCodec;
import net.glowstone.net.codec.play.game.BlockActionCodec;
import net.glowstone.net.codec.play.game.BlockBreakAnimationCodec;
import net.glowstone.net.codec.play.game.BlockChangeCodec;
import net.glowstone.net.codec.play.game.ChatCodec;
import net.glowstone.net.codec.play.game.ChunkDataCodec;
import net.glowstone.net.codec.play.game.CraftRecipeResponseCodec;
import net.glowstone.net.codec.play.game.ExperienceCodec;
import net.glowstone.net.codec.play.game.ExplosionCodec;
import net.glowstone.net.codec.play.game.HealthCodec;
import net.glowstone.net.codec.play.game.JoinGameCodec;
import net.glowstone.net.codec.play.game.MapDataCodec;
import net.glowstone.net.codec.play.game.MultiBlockChangeCodec;
import net.glowstone.net.codec.play.game.NamedSoundEffectCodec;
import net.glowstone.net.codec.play.game.PingCodec;
import net.glowstone.net.codec.play.game.PlayEffectCodec;
import net.glowstone.net.codec.play.game.PlayParticleCodec;
import net.glowstone.net.codec.play.game.PluginMessageCodec;
import net.glowstone.net.codec.play.game.PositionRotationCodec;
import net.glowstone.net.codec.play.game.RespawnCodec;
import net.glowstone.net.codec.play.game.SignEditorCodec;
import net.glowstone.net.codec.play.game.SoundEffectCodec;
import net.glowstone.net.codec.play.game.SpawnPositionCodec;
import net.glowstone.net.codec.play.game.StateChangeCodec;
import net.glowstone.net.codec.play.game.StatisticCodec;
import net.glowstone.net.codec.play.game.TimeCodec;
import net.glowstone.net.codec.play.game.TitleCodec;
import net.glowstone.net.codec.play.game.UnloadChunkCodec;
import net.glowstone.net.codec.play.game.UnlockRecipesCodec;
import net.glowstone.net.codec.play.game.UpdateBlockEntityCodec;
import net.glowstone.net.codec.play.game.UserListHeaderFooterCodec;
import net.glowstone.net.codec.play.game.UserListItemCodec;
import net.glowstone.net.codec.play.game.WorldBorderCodec;
import net.glowstone.net.codec.play.inv.CloseWindowCodec;
import net.glowstone.net.codec.play.inv.OpenWindowCodec;
import net.glowstone.net.codec.play.inv.SetWindowContentsCodec;
import net.glowstone.net.codec.play.inv.SetWindowSlotCodec;
import net.glowstone.net.codec.play.inv.SymmetricHeldItemCodec;
import net.glowstone.net.codec.play.inv.TransactionCodec;
import net.glowstone.net.codec.play.inv.WindowPropertyCodec;
import net.glowstone.net.codec.play.player.AdvancementsCodec;
import net.glowstone.net.codec.play.player.BossBarCodec;
import net.glowstone.net.codec.play.player.CameraCodec;
import net.glowstone.net.codec.play.player.CombatEventCodec;
import net.glowstone.net.codec.play.player.PlayerAbilitiesCodec;
import net.glowstone.net.codec.play.player.ResourcePackSendCodec;
import net.glowstone.net.codec.play.player.ServerDifficultyCodec;
import net.glowstone.net.codec.play.player.TabCompleteResponseCodec;
import net.glowstone.net.codec.play.player.UseBedCodec;
import net.glowstone.net.codec.play.scoreboard.ScoreboardDisplayCodec;
import net.glowstone.net.codec.play.scoreboard.ScoreboardObjectiveCodec;
import net.glowstone.net.codec.play.scoreboard.ScoreboardScoreCodec;
import net.glowstone.net.codec.play.scoreboard.ScoreboardTeamCodec;
import net.glowstone.net.message.KickMessage;
import net.glowstone.net.message.play.entity.AnimateEntityMessage;
import net.glowstone.net.message.play.entity.AttachEntityMessage;
import net.glowstone.net.message.play.entity.CollectItemMessage;
import net.glowstone.net.message.play.entity.DestroyEntitiesMessage;
import net.glowstone.net.message.play.entity.EntityEffectMessage;
import net.glowstone.net.message.play.entity.EntityEquipmentMessage;
import net.glowstone.net.message.play.entity.EntityHeadRotationMessage;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import net.glowstone.net.message.play.entity.EntityPropertyMessage;
import net.glowstone.net.message.play.entity.EntityRemoveEffectMessage;
import net.glowstone.net.message.play.entity.EntityRotationMessage;
import net.glowstone.net.message.play.entity.EntityStatusMessage;
import net.glowstone.net.message.play.entity.EntityTeleportMessage;
import net.glowstone.net.message.play.entity.EntityVelocityMessage;
import net.glowstone.net.message.play.entity.RelativeEntityPositionMessage;
import net.glowstone.net.message.play.entity.RelativeEntityPositionRotationMessage;
import net.glowstone.net.message.play.entity.SetCooldownMessage;
import net.glowstone.net.message.play.entity.SetPassengerMessage;
import net.glowstone.net.message.play.entity.SpawnLightningStrikeMessage;
import net.glowstone.net.message.play.entity.SpawnMobMessage;
import net.glowstone.net.message.play.entity.SpawnObjectMessage;
import net.glowstone.net.message.play.entity.SpawnPaintingMessage;
import net.glowstone.net.message.play.entity.SpawnPlayerMessage;
import net.glowstone.net.message.play.entity.SpawnXpOrbMessage;
import net.glowstone.net.message.play.entity.VehicleMoveMessage;
import net.glowstone.net.message.play.game.BlockActionMessage;
import net.glowstone.net.message.play.game.BlockBreakAnimationMessage;
import net.glowstone.net.message.play.game.BlockChangeMessage;
import net.glowstone.net.message.play.game.ChatMessage;
import net.glowstone.net.message.play.game.ChunkDataMessage;
import net.glowstone.net.message.play.game.CraftRecipeResponseMessage;
import net.glowstone.net.message.play.game.ExperienceMessage;
import net.glowstone.net.message.play.game.ExplosionMessage;
import net.glowstone.net.message.play.game.HealthMessage;
import net.glowstone.net.message.play.game.JoinGameMessage;
import net.glowstone.net.message.play.game.MapDataMessage;
import net.glowstone.net.message.play.game.MultiBlockChangeMessage;
import net.glowstone.net.message.play.game.NamedSoundEffectMessage;
import net.glowstone.net.message.play.game.PingMessage;
import net.glowstone.net.message.play.game.PlayEffectMessage;
import net.glowstone.net.message.play.game.PlayParticleMessage;
import net.glowstone.net.message.play.game.PluginMessage;
import net.glowstone.net.message.play.game.PositionRotationMessage;
import net.glowstone.net.message.play.game.RespawnMessage;
import net.glowstone.net.message.play.game.SignEditorMessage;
import net.glowstone.net.message.play.game.SoundEffectMessage;
import net.glowstone.net.message.play.game.SpawnPositionMessage;
import net.glowstone.net.message.play.game.StateChangeMessage;
import net.glowstone.net.message.play.game.StatisticMessage;
import net.glowstone.net.message.play.game.TimeMessage;
import net.glowstone.net.message.play.game.TitleMessage;
import net.glowstone.net.message.play.game.UnloadChunkMessage;
import net.glowstone.net.message.play.game.UnlockRecipesMessage;
import net.glowstone.net.message.play.game.UpdateBlockEntityMessage;
import net.glowstone.net.message.play.game.UserListHeaderFooterMessage;
import net.glowstone.net.message.play.game.UserListItemMessage;
import net.glowstone.net.message.play.game.WorldBorderMessage;
import net.glowstone.net.message.play.inv.CloseWindowMessage;
import net.glowstone.net.message.play.inv.HeldItemMessage;
import net.glowstone.net.message.play.inv.OpenWindowMessage;
import net.glowstone.net.message.play.inv.SetWindowContentsMessage;
import net.glowstone.net.message.play.inv.SetWindowSlotMessage;
import net.glowstone.net.message.play.inv.TransactionMessage;
import net.glowstone.net.message.play.inv.WindowPropertyMessage;
import net.glowstone.net.message.play.player.AdvancementsMessage;
import net.glowstone.net.message.play.player.BossBarMessage;
import net.glowstone.net.message.play.player.CameraMessage;
import net.glowstone.net.message.play.player.CombatEventMessage;
import net.glowstone.net.message.play.player.PlayerAbilitiesMessage;
import net.glowstone.net.message.play.player.ResourcePackSendMessage;
import net.glowstone.net.message.play.player.ServerDifficultyMessage;
import net.glowstone.net.message.play.player.TabCompleteResponseMessage;
import net.glowstone.net.message.play.player.UseBedMessage;
import net.glowstone.net.message.play.scoreboard.ScoreboardDisplayMessage;
import net.glowstone.net.message.play.scoreboard.ScoreboardObjectiveMessage;
import net.glowstone.net.message.play.scoreboard.ScoreboardScoreMessage;
import net.glowstone.net.message.play.scoreboard.ScoreboardTeamMessage;

/**
 * The composite codec combines all gameplay related codecs into a single codec for use by the JMS based brokers.
 */
public final class CompositeCodec implements JmsCodec<Message> {

    private final Map<Integer, Class<? extends Message>> types;
    private final Map<Class<? extends Message>, Integer> opcodes;
    private final Map<Class<? extends Message>, Codec<? extends Message>> codecs;
    private final AtomicInteger counter;
    private final ByteBufAllocator allocator;

    /**
     * Create a composite codec.
     */
    public CompositeCodec() {

        types = new HashMap<>();
        opcodes = new HashMap<>();
        codecs = new HashMap<>();
        counter = new AtomicInteger();

        allocator = UnpooledByteBufAllocator.DEFAULT;

        register(SpawnObjectMessage.class, new SpawnObjectCodec());
        register(SpawnXpOrbMessage.class, new SpawnXpOrbCodec());
        register(SpawnLightningStrikeMessage.class, new SpawnLightningStrikeCodec());
        register(SpawnMobMessage.class, new SpawnMobCodec());
        register(SpawnPaintingMessage.class, new SpawnPaintingCodec());
        register(SpawnPlayerMessage.class, new SpawnPlayerCodec());
        register(AnimateEntityMessage.class, new AnimateEntityCodec());
        register(StatisticMessage.class, new StatisticCodec());
        register(BlockBreakAnimationMessage.class, new BlockBreakAnimationCodec());
        register(UpdateBlockEntityMessage.class, new UpdateBlockEntityCodec());
        register(BlockActionMessage.class, new BlockActionCodec());
        register(BlockChangeMessage.class, new BlockChangeCodec());
        register(BossBarMessage.class, new BossBarCodec());
        register(ServerDifficultyMessage.class, new ServerDifficultyCodec());
        register(TabCompleteResponseMessage.class, new TabCompleteResponseCodec());
        register(ChatMessage.class, new ChatCodec());
        register(MultiBlockChangeMessage.class, new MultiBlockChangeCodec());
        register(TransactionMessage.class, new TransactionCodec());
        register(CloseWindowMessage.class, new CloseWindowCodec());
        register(OpenWindowMessage.class, new OpenWindowCodec());
        register(SetWindowContentsMessage.class, new SetWindowContentsCodec());
        register(WindowPropertyMessage.class, new WindowPropertyCodec());
        register(SetWindowSlotMessage.class, new SetWindowSlotCodec());
        register(SetCooldownMessage.class, new SetCooldownCodec());
        register(PluginMessage.class, new PluginMessageCodec());
        register(NamedSoundEffectMessage.class, new NamedSoundEffectCodec());
        register(KickMessage.class, new KickCodec());
        register(EntityStatusMessage.class, new EntityStatusCodec());
        register(ExplosionMessage.class, new ExplosionCodec());
        register(UnloadChunkMessage.class, new UnloadChunkCodec());
        register(StateChangeMessage.class, new StateChangeCodec());
        register(PingMessage.class, new PingCodec());
        register(ChunkDataMessage.class, new ChunkDataCodec());
        register(PlayEffectMessage.class, new PlayEffectCodec());
        register(PlayParticleMessage.class, new PlayParticleCodec());
        register(JoinGameMessage.class, new JoinGameCodec());
        register(MapDataMessage.class, new MapDataCodec());
        // TODO: Entity packet
        register(RelativeEntityPositionMessage.class, new RelativeEntityPositionCodec());
        register(RelativeEntityPositionRotationMessage.class, new RelativeEntityPositionRotationCodec());
        register(EntityRotationMessage.class, new EntityRotationCodec());
        register(VehicleMoveMessage.class, new VehicleMoveCodec());
        register(SignEditorMessage.class, new SignEditorCodec());
        register(CraftRecipeResponseMessage.class, new CraftRecipeResponseCodec());
        register(PlayerAbilitiesMessage.class, new PlayerAbilitiesCodec());
        register(CombatEventMessage.class, new CombatEventCodec());
        register(UserListItemMessage.class, new UserListItemCodec());
        register(PositionRotationMessage.class, new PositionRotationCodec());
        register(UseBedMessage.class, new UseBedCodec());
        register(UnlockRecipesMessage.class, new UnlockRecipesCodec());
        register(DestroyEntitiesMessage.class, new DestroyEntitiesCodec());
        register(EntityRemoveEffectMessage.class, new EntityRemoveEffectCodec());
        register(ResourcePackSendMessage.class, new ResourcePackSendCodec());
        register(RespawnMessage.class, new RespawnCodec());
        register(EntityHeadRotationMessage.class, new EntityHeadRotationCodec());
        // TODO: Select Advancement Tab
        register(WorldBorderMessage.class, new WorldBorderCodec());
        register(CameraMessage.class, new CameraCodec());
        register(HeldItemMessage.class, new SymmetricHeldItemCodec());
        register(ScoreboardDisplayMessage.class, new ScoreboardDisplayCodec());
        register(EntityMetadataMessage.class, new EntityMetadataCodec());
        register(AttachEntityMessage.class, new AttachEntityCodec());
        register(EntityVelocityMessage.class, new EntityVelocityCodec());
        register(EntityEquipmentMessage.class, new EntityEquipmentCodec());
        register(ExperienceMessage.class, new ExperienceCodec());
        register(HealthMessage.class, new HealthCodec());
        register(ScoreboardObjectiveMessage.class, new ScoreboardObjectiveCodec());
        register(SetPassengerMessage.class, new SetPassengerCodec());
        register(ScoreboardTeamMessage.class, new ScoreboardTeamCodec());
        register(ScoreboardScoreMessage.class, new ScoreboardScoreCodec());
        register(SpawnPositionMessage.class, new SpawnPositionCodec());
        register(TimeMessage.class, new TimeCodec());
        register(TitleMessage.class, new TitleCodec());
        register(SoundEffectMessage.class, new SoundEffectCodec());
        register(UserListHeaderFooterMessage.class, new UserListHeaderFooterCodec());
        register(CollectItemMessage.class, new CollectItemCodec());
        register(EntityTeleportMessage.class, new EntityTeleportCodec());
        register(AdvancementsMessage.class, new AdvancementsCodec());
        register(EntityPropertyMessage.class, new EntityPropertyCodec());
        register(EntityEffectMessage.class, new EntityEffectCodec());
    }

    private <T extends Message> void register(Class<T> type, Codec<T> codec) {
        int opcode = counter.getAndIncrement();
        types.put(opcode, type);
        opcodes.put(type, opcode);
        codecs.put(type, codec);
    }

    @Override
    public javax.jms.Message encode(Session session, Message flowMessage) throws JMSException {

        Class<? extends Message> type = flowMessage.getClass();
        Integer opcode = opcodes.get(type);
        Codec<? extends Message> codec = codecs.get(type);

        if (opcode == null || codec == null) {
            throw new RuntimeException("Unknown Flow message type: " + type.getName());
        }

        ByteBuf buffer = allocator.buffer();
        try {

            ByteBufUtils.writeVarInt(buffer, opcode);
            //noinspection unchecked
            buffer = ((Codec<Message>) codec).encode(buffer, flowMessage);

            int length = buffer.readableBytes();
            int index = buffer.readerIndex();
            byte[] bytes = new byte[length];
            buffer.getBytes(index, bytes);

            BytesMessage bytesMessage = session.createBytesMessage();
            bytesMessage.writeBytes(bytes);
            return bytesMessage;

        } catch (IOException exception) {
            throw new RuntimeException("Could not encode Flow message: " + flowMessage, exception);

        } finally {
            buffer.release();
        }
    }

    @Override
    public Message decode(Session session, javax.jms.Message jmsMessage) throws JMSException {

        if (!(jmsMessage instanceof BytesMessage)) {
            Class<?> type = jmsMessage.getClass();
            throw new RuntimeException("Received unsupported JMS message type: " + type.getName());
        }

        BytesMessage bytesMessage = (BytesMessage) jmsMessage;
        int length = (int) bytesMessage.getBodyLength();
        byte[] bytes = new byte[length];
        int read = bytesMessage.readBytes(bytes);

        if (read == -1) {
            throw new RuntimeException("Reached end of stream");
        }

        if (read != length) {
            throw new RuntimeException("Did not read enough bytes");
        }

        ByteBuf buffer = Unpooled.wrappedBuffer(bytes);

        try {

            int opcode = ByteBufUtils.readVarInt(buffer);
            Class<? extends Message> type = types.get(opcode);
            Codec<? extends Message> codec = codecs.get(type);

            if (type == null || codec == null) {
                throw new RuntimeException("Unknown opcode: " + opcode);
            }

            try {
                return codec.decode(buffer);
            } catch (IOException exception) {
                throw new RuntimeException("Could not decode JMS message: " + type.getName(), exception);
            }

        } catch (IOException exception) {
            throw new RuntimeException("Could not read opcode");

        } finally {
            buffer.release();
        }
    }
}
