package net.glowstone.net.protocol;

import net.glowstone.net.codec.JsonCodec;
import net.glowstone.net.codec.SetCompressionCodec;
import net.glowstone.net.codec.play.entity.*;
import net.glowstone.net.codec.play.game.*;
import net.glowstone.net.codec.play.inv.*;
import net.glowstone.net.codec.play.player.*;
import net.glowstone.net.handler.play.game.*;
import net.glowstone.net.handler.play.inv.*;
import net.glowstone.net.handler.play.player.*;
import net.glowstone.net.message.KickMessage;
import net.glowstone.net.message.SetCompressionMessage;
import net.glowstone.net.message.play.entity.*;
import net.glowstone.net.message.play.game.*;
import net.glowstone.net.message.play.inv.*;
import net.glowstone.net.message.play.player.*;

public final class PlayProtocol extends GlowProtocol {
    public PlayProtocol() {
        super("PLAY", 0x49);

        inbound(0x00, PingMessage.class, PingCodec.class, PingHandler.class);
        inbound(0x01, IncomingChatMessage.class, IncomingChatCodec.class, ChatHandler.class);
        inbound(0x02, InteractEntityMessage.class, InteractEntityCodec.class, InteractEntityHandler.class);
        inbound(0x03, PlayerUpdateMessage.class, PlayerUpdateCodec.class, PlayerUpdateHandler.class);
        inbound(0x04, PlayerPositionMessage.class, PlayerPositionCodec.class, PlayerUpdateHandler.class);
        inbound(0x05, PlayerLookMessage.class, PlayerLookCodec.class, PlayerUpdateHandler.class);
        inbound(0x06, PlayerPositionLookMessage.class, PlayerPositionLookCodec.class, PlayerUpdateHandler.class);
        inbound(0x07, DiggingMessage.class, DiggingCodec.class, DiggingHandler.class);
        inbound(0x08, BlockPlacementMessage.class, BlockPlacementCodec.class, BlockPlacementHandler.class);
        inbound(0x09, HeldItemMessage.class, HeldItemCodec.class, HeldItemHandler.class);
        inbound(0x0A, PlayerSwingArmMessage.class, PlayerSwingArmCodec.class, PlayerSwingArmHandler.class);
        inbound(0x0B, PlayerActionMessage.class, PlayerActionCodec.class, PlayerActionHandler.class);
        inbound(0x0C, SteerVehicleMessage.class, SteerVehicleCodec.class, SteerVehicleHandler.class);
        inbound(0x0D, CloseWindowMessage.class, CloseWindowCodec.class, CloseWindowHandler.class);
        inbound(0x0E, WindowClickMessage.class, WindowClickCodec.class, WindowClickHandler.class);
        inbound(0x0F, TransactionMessage.class, TransactionCodec.class, TransactionHandler.class);
        inbound(0x10, CreativeItemMessage.class, CreativeItemCodec.class, CreativeItemHandler.class);
        inbound(0x11, EnchantItemMessage.class, EnchantItemCodec.class, EnchantItemHandler.class);
        inbound(0x12, UpdateSignMessage.class, UpdateSignCodec.class, UpdateSignHandler.class);
        inbound(0x13, PlayerAbilitiesMessage.class, PlayerAbilitiesCodec.class, PlayerAbilitiesHandler.class);
        inbound(0x14, TabCompleteMessage.class, TabCompleteCodec.class, TabCompleteHandler.class);
        inbound(0x15, ClientSettingsMessage.class, ClientSettingsCodec.class, ClientSettingsHandler.class);
        inbound(0x16, ClientStatusMessage.class, ClientStatusCodec.class, ClientStatusHandler.class);
        inbound(0x17, PluginMessage.class, PluginMessageCodec.class, PluginMessageHandler.class);

        outbound(0x00, PingMessage.class, PingCodec.class);
        outbound(0x01, JoinGameMessage.class, JoinGameCodec.class);
        outbound(0x02, ChatMessage.class, ChatCodec.class);
        outbound(0x03, TimeMessage.class, TimeCodec.class);
        outbound(0x04, EntityEquipmentMessage.class, EntityEquipmentCodec.class);
        outbound(0x05, SpawnPositionMessage.class, SpawnPositionCodec.class);
        outbound(0x06, HealthMessage.class, HealthCodec.class);
        outbound(0x07, RespawnMessage.class, RespawnCodec.class);
        outbound(0x08, PositionRotationMessage.class, PositionRotationCodec.class);
        outbound(0x09, HeldItemMessage.class, HeldItemCodec.class);
        outbound(0x0B, AnimateEntityMessage.class, AnimateEntityCodec.class);
        outbound(0x0C, SpawnPlayerMessage.class, SpawnPlayerCodec.class);
        outbound(0x0D, CollectItemMessage.class, CollectItemCodec.class);
        outbound(0x0E, SpawnObjectMessage.class, SpawnObjectCodec.class);
        outbound(0x0F, SpawnMobMessage.class, SpawnMobCodec.class);
        outbound(0x10, SpawnPaintingMessage.class, SpawnPaintingCodec.class);
        outbound(0x11, SpawnXpOrbMessage.class, SpawnXpOrbCodec.class);
        outbound(0x12, EntityVelocityMessage.class, EntityVelocityCodec.class);
        outbound(0x13, DestroyEntitiesMessage.class, DestroyEntitiesCodec.class);
        outbound(0x15, RelativeEntityPositionMessage.class, RelativeEntityPositionCodec.class);
        outbound(0x16, EntityRotationMessage.class, EntityRotationCodec.class);
        outbound(0x17, RelativeEntityPositionRotationMessage.class, RelativeEntityPositionRotationCodec.class);
        outbound(0x18, EntityTeleportMessage.class, EntityTeleportCodec.class);
        outbound(0x19, EntityHeadRotationMessage.class, EntityHeadRotationCodec.class);
        outbound(0x1A, EntityStatusMessage.class, EntityStatusCodec.class);
        outbound(0x1B, AttachEntityMessage.class, AttachEntityCodec.class);
        outbound(0x1C, EntityMetadataMessage.class, EntityMetadataCodec.class);
        outbound(0x1D, EntityEffectMessage.class, EntityEffectCodec.class);
        outbound(0x1E, EntityRemoveEffectMessage.class, EntityRemoveEffectCodec.class);
        outbound(0x1F, ExperienceMessage.class, ExperienceCodec.class);
        outbound(0x21, ChunkDataMessage.class, ChunkDataCodec.class);
        outbound(0x22, MultiBlockChangeMessage.class, MultiBlockChangeCodec.class);
        outbound(0x23, BlockChangeMessage.class, BlockChangeCodec.class);
        outbound(0x24, BlockActionMessage.class, BlockActionCodec.class);
        outbound(0x26, ChunkBulkMessage.class, ChunkBulkCodec.class);
        outbound(0x28, PlayEffectMessage.class, PlayEffectCodec.class);
        outbound(0x29, PlaySoundMessage.class, PlaySoundCodec.class);
        outbound(0x2A, PlayParticleMessage.class, PlayParticleCodec.class);
        outbound(0x2B, StateChangeMessage.class, StateChangeCodec.class);
        outbound(0x2C, SpawnLightningStrikeMessage.class, SpawnLightningStrikeCodec.class);
        outbound(0x2D, OpenWindowMessage.class, OpenWindowCodec.class);
        outbound(0x2E, CloseWindowMessage.class, CloseWindowCodec.class);
        outbound(0x2F, SetWindowSlotMessage.class, SetWindowSlotCodec.class);
        outbound(0x30, SetWindowContentsMessage.class, SetWindowContentsCodec.class);
        outbound(0x31, WindowPropertyMessage.class, WindowPropertyCodec.class);
        outbound(0x32, TransactionMessage.class, TransactionCodec.class);
        outbound(0x33, UpdateSignMessage.class, UpdateSignCodec.class);
        outbound(0x34, MapDataMessage.class, MapDataCodec.class);
        outbound(0x36, SignEditorMessage.class, SignEditorCodec.class);
        outbound(0x37, StatisticMessage.class, StatisticCodec.class);
        outbound(0x38, UserListItemMessage.class, UserListItemCodec.class);
        outbound(0x39, PlayerAbilitiesMessage.class, PlayerAbilitiesCodec.class);
        outbound(0x3A, TabCompleteResponseMessage.class, TabCompleteResponseCodec.class);
        outbound(0x3F, PluginMessage.class, PluginMessageCodec.class);
        outbound(0x40, KickMessage.class, JsonCodec.class);
        outbound(0x46, SetCompressionMessage.class, SetCompressionCodec.class);
    }
}
