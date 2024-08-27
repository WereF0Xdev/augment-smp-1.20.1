package fox.mods;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AugmentSMP implements ModInitializer {
	public static final String MOD_ID = "augment";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier DIRT_BROKEN = new Identifier(MOD_ID, "dirt_broken");

	private Integer totalDirtBlocksBroken = 0;

	public static final Identifier EMPTY_SLOT_TEXTURE = new Identifier(AugmentSMP.MOD_ID, "textures/gui/heart_empty.png");

	@Override
	public void onInitialize() {

		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
			if (state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.DIRT) {
				StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());
				// Increment the amount of dirt blocks that have been broken
				serverState.totalDirtBlocksBroken += 1;

				// Send a packet to the client
				MinecraftServer server = world.getServer();

				PacketByteBuf data = PacketByteBufs.create();
				data.writeInt(serverState.totalDirtBlocksBroken);

				ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player.getUuid());
				server.execute(() -> {
					ServerPlayNetworking.send(playerEntity, DIRT_BROKEN, data);
				});
			}
		});

		LOGGER.info("Hello Augment SMP!");
	}
}