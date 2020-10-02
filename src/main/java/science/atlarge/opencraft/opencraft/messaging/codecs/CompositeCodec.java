package science.atlarge.opencraft.opencraft.messaging.codecs;

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
import science.atlarge.opencraft.opencraft.net.codec.KickCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.AnimateEntityCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.AttachEntityCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.CollectItemCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.DestroyEntitiesCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.EntityEffectCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.EntityEquipmentCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.EntityHeadRotationCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.EntityMetadataCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.EntityPropertyCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.EntityRemoveEffectCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.EntityRotationCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.EntityStatusCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.EntityTeleportCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.EntityVelocityCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.RelativeEntityPositionCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.RelativeEntityPositionRotationCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.SetCooldownCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.SetPassengerCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.SpawnLightningStrikeCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.SpawnMobCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.SpawnObjectCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.SpawnPaintingCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.SpawnPlayerCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.SpawnXpOrbCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.entity.VehicleMoveCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.BlockActionCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.BlockBreakAnimationCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.BlockChangeCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.ChatCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.ChunkDataCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.CraftRecipeResponseCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.ExperienceCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.ExplosionCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.HealthCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.JoinGameCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.MapDataCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.MultiBlockChangeCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.NamedSoundEffectCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.PingCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.PlayEffectCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.PlayParticleCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.PluginMessageCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.PositionRotationCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.RespawnCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.SignEditorCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.SoundEffectCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.SpawnPositionCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.StateChangeCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.StatisticCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.TimeCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.TitleCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.UnloadChunkCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.UnlockRecipesCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.UpdateBlockEntityCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.UserListHeaderFooterCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.UserListItemCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.game.WorldBorderCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.inv.CloseWindowCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.inv.OpenWindowCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.inv.SetWindowContentsCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.inv.SetWindowSlotCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.inv.SymmetricHeldItemCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.inv.TransactionCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.inv.WindowPropertyCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.player.AdvancementsCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.player.BossBarCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.player.CameraCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.player.CombatEventCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.player.PlayerAbilitiesCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.player.ResourcePackSendCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.player.ServerDifficultyCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.player.TabCompleteResponseCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.player.UseBedCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.scoreboard.ScoreboardDisplayCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.scoreboard.ScoreboardObjectiveCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.scoreboard.ScoreboardScoreCodec;
import science.atlarge.opencraft.opencraft.net.codec.play.scoreboard.ScoreboardTeamCodec;
import science.atlarge.opencraft.opencraft.net.message.KickMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.AnimateEntityMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.AttachEntityMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.CollectItemMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.DestroyEntitiesMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityEffectMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityEquipmentMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityHeadRotationMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityMetadataMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityPropertyMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityRemoveEffectMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityRotationMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityStatusMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityTeleportMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityVelocityMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.RelativeEntityPositionMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.RelativeEntityPositionRotationMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SetCooldownMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SetPassengerMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SpawnLightningStrikeMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SpawnMobMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SpawnObjectMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SpawnPaintingMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SpawnPlayerMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SpawnXpOrbMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.VehicleMoveMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.BlockActionMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.BlockBreakAnimationMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.BlockChangeMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.ChatMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.ChunkDataMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.CraftRecipeResponseMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.ExperienceMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.ExplosionMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.HealthMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.JoinGameMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.MapDataMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.MultiBlockChangeMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.NamedSoundEffectMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.PingMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.PlayEffectMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.PlayParticleMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.PluginMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.PositionRotationMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.RespawnMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.SignEditorMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.SoundEffectMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.SpawnPositionMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.StateChangeMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.StatisticMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.TimeMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.TitleMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.UnloadChunkMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.UnlockRecipesMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.UpdateBlockEntityMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.UserListHeaderFooterMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.UserListItemMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.WorldBorderMessage;
import science.atlarge.opencraft.opencraft.net.message.play.inv.CloseWindowMessage;
import science.atlarge.opencraft.opencraft.net.message.play.inv.HeldItemMessage;
import science.atlarge.opencraft.opencraft.net.message.play.inv.OpenWindowMessage;
import science.atlarge.opencraft.opencraft.net.message.play.inv.SetWindowContentsMessage;
import science.atlarge.opencraft.opencraft.net.message.play.inv.SetWindowSlotMessage;
import science.atlarge.opencraft.opencraft.net.message.play.inv.TransactionMessage;
import science.atlarge.opencraft.opencraft.net.message.play.inv.WindowPropertyMessage;
import science.atlarge.opencraft.opencraft.net.message.play.player.AdvancementsMessage;
import science.atlarge.opencraft.opencraft.net.message.play.player.BossBarMessage;
import science.atlarge.opencraft.opencraft.net.message.play.player.CameraMessage;
import science.atlarge.opencraft.opencraft.net.message.play.player.CombatEventMessage;
import science.atlarge.opencraft.opencraft.net.message.play.player.PlayerAbilitiesMessage;
import science.atlarge.opencraft.opencraft.net.message.play.player.ResourcePackSendMessage;
import science.atlarge.opencraft.opencraft.net.message.play.player.ServerDifficultyMessage;
import science.atlarge.opencraft.opencraft.net.message.play.player.TabCompleteResponseMessage;
import science.atlarge.opencraft.opencraft.net.message.play.player.UseBedMessage;
import science.atlarge.opencraft.opencraft.net.message.play.scoreboard.ScoreboardDisplayMessage;
import science.atlarge.opencraft.opencraft.net.message.play.scoreboard.ScoreboardObjectiveMessage;
import science.atlarge.opencraft.opencraft.net.message.play.scoreboard.ScoreboardScoreMessage;
import science.atlarge.opencraft.opencraft.net.message.play.scoreboard.ScoreboardTeamMessage;
import science.atlarge.opencraft.messaging.brokers.JmsCodec;

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
