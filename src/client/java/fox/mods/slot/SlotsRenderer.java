package fox.mods.slot;

import fox.mods.AugmentSMP;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class SlotsRenderer implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {

        MinecraftClient client = MinecraftClient.getInstance();

        int w = client.getWindow().getScaledWidth();
        int h = client.getWindow().getScaledHeight();

        drawContext.drawTexture(AugmentSMP.EMPTY_SLOT_TEXTURE, w / 2 + -32, h - 75, 0, 0, 22, 22, 22, 22);

        drawContext.drawTexture(AugmentSMP.EMPTY_SLOT_TEXTURE, w / 2 + -76, h - 75, 0, 0, 22, 22, 22, 22);

        drawContext.drawTexture(AugmentSMP.EMPTY_SLOT_TEXTURE, w / 2 + 12, h - 75, 0, 0, 22, 22, 22, 22);

        drawContext.drawTexture(AugmentSMP.EMPTY_SLOT_TEXTURE, w / 2 + 56, h - 75, 0, 0, 22, 22, 22, 22);
    }
}

