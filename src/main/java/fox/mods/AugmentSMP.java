package fox.mods;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class AugmentSMP implements ModInitializer {
	public static final String MOD_ID = "augment";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier DEATH = new Identifier(MOD_ID, "death");

	public static final Identifier INITIAL_SYNC = new Identifier(MOD_ID, "initial_sync");

	private Integer totalSlots = 0;

	public static final Identifier EMPTY_SLOT_TEXTURE = new Identifier(AugmentSMP.MOD_ID, "textures/gui/heart_empty.png");
	public static final Identifier BANNED_SLOT_TEXTURE = new Identifier(AugmentSMP.MOD_ID, "textures/gui/heart_banned.png");

	@Override
	public void onInitialize() {

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			PlayerData playerState = StateSaverAndLoader.getPlayerState(handler.getPlayer());
			PacketByteBuf data = PacketByteBufs.create();
			data.writeInt(playerState.slots);
			server.execute(() -> {
				ServerPlayNetworking.send(handler.getPlayer(), INITIAL_SYNC, data);
			});
		});

		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			PlayerData playerState = StateSaverAndLoader.getPlayerState(newPlayer);
			playerState.slots -= 1;

			int playerSlots = StateSaverAndLoader.getPlayerState(newPlayer).slots;
			newPlayer.sendMessage(Text.literal("Slots: " + playerSlots));

			if (playerSlots < -1) {
				MinecraftServer server = newPlayer.getServer();
				GameProfile profile = newPlayer.getGameProfile();

				BannedPlayerEntry entry = new BannedPlayerEntry(
						profile,
						new Date(),
						"Augment SMP",
						null,
						"§cYou have no more Slots!"
				);

				server.getPlayerManager().getUserBanList().add(entry);

				newPlayer.networkHandler.disconnect(Text.literal("§cYou have no more Slots!"));
			}

			StateSaverAndLoader.getServerState(newPlayer.getServer()).markDirty();

		});

		LOGGER.info("Hello Augment SMP!");
	}
}