package fox.mods;

import fox.mods.slot.SlotsRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.text.Text;

public class AugmentSMPClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register(new SlotsRenderer());

		ClientPlayNetworking.registerGlobalReceiver(AugmentSMP.DIRT_BROKEN, (client, handler, buf, responseSender) -> {
			int totalDirtBlocksBroken = buf.readInt();
			client.execute(() -> {
				client.player.sendMessage(Text.literal("Total dirt blocks broken: " + totalDirtBlocksBroken));
			});
		});
	}
}