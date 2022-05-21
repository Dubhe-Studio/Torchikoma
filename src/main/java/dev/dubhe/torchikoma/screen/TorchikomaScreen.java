package dev.dubhe.torchikoma.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.menu.TorchikomaMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TorchikomaScreen extends AbstractDepartInvScreen<TorchikomaMenu> {
    private static final ResourceLocation BACKGROUND = Torchikoma.getId("textures/gui/torchikoma.png");

    public TorchikomaScreen(TorchikomaMenu pMenu, Inventory inventory, Component pTitle) {
        super(pMenu, inventory, pTitle, 178, 106);
        this.inventoryLabelY += 48;
        this.titleLabelY += 66;
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
        int gunX = (this.width - this.menuWidth) / 2;
        int gunY = (this.height - this.imageHeight) / 2;
        RenderSystem.setShaderTexture(0, BACKGROUND);
        this.blit(pPoseStack, gunX, gunY, 0, 0, this.menuWidth, this.menuHeight);
        for (int i = 0; i < 3; i++) {
            this.renderItemBg(pPoseStack, gunX + 9, gunY + i * 18 + 16, this.menuWidth, i * 16, i);
            this.blit(pPoseStack, gunX + i * 18 + 8, gunY + 16 + 67, i * 18, this.menuHeight, 18, 16);
        }

        this.blit(pPoseStack, gunX + 89, gunY + 84, 54, this.menuHeight, 80, 4);
        this.blit(pPoseStack, gunX + 89, gunY + 94, 54, this.menuHeight + 4, 80, 4);

    }

}
