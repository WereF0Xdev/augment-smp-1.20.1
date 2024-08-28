package fox.mods;

import fox.mods.slot.SlotsRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.text.Text;

public class AugmentSMPClient implements ClientModInitializer {

	public static PlayerData playerData = new PlayerData();

	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register(new SlotsRenderer());

		ClientPlayNetworking.registerGlobalReceiver(AugmentSMP.DEATH, (client, handler, buf, responseSender) -> {

			client.execute(() -> {

				int playerSlots = StateSaverAndLoader.getPlayerState(client.player).slots;
				client.player.sendMessage(Text.literal("Slots: " + playerSlots));
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(AugmentSMP.INITIAL_SYNC, (client, handler, buf, responseSender) -> {
			playerData.slots = buf.readInt();

			client.execute(() -> {
				client.player.sendMessage(Text.literal("Initial slots: " + playerData.slots));
			});
		});
	}
}