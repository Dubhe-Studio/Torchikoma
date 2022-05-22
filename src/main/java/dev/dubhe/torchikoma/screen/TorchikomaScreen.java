package dev.dubhe.torchikoma.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.menu.TorchikomaMenu;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
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
        int bgX = (this.width - this.menuWidth) / 2;
        int bgY = (this.height - this.imageHeight) / 2;
        byte status = this.menu.getEntity().getStatus();
        int healLength = (int) (80 * this.menu.getEntity().getHealth() / this.menu.getEntity().getMaxHealth());
        int energyLength = (int) (80 * this.menu.getEntity().getEnergy() / 200.0F);
        if (status < 0 || status > 2) throw new IllegalArgumentException("Invalid status index: " + status);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        this.blit(pPoseStack, bgX, bgY, 0, 0, this.menuWidth, this.menuHeight); // 背景板
        for (int i = 0; i < 3; i++) {
            this.renderItemBg(pPoseStack, bgX + 9, bgY + i * 18 + 16, this.menuWidth, i * 16, i); // 物品背景
        }
        this.blit(pPoseStack, bgX + status * 18 + 8, bgY + 16 + 67, status * 18, this.menuHeight, 18, 16); // 三个按钮

        this.blit(pPoseStack, bgX + 89, bgY + 84, 54, this.menuHeight, energyLength, 4); // 血条
        this.blit(pPoseStack, bgX + 89, bgY + 94, 54, this.menuHeight + 4, healLength, 4); // 能量条
        // 51 60
        InventoryScreen.renderEntityInInventory(bgX + 62, bgY + 57, 17, bgX + 62 - pMouseX, bgY + 42 - pMouseY, this.menu.getEntity());
    }

    protected void renderItemBg(PoseStack pPoseStack, int x, int y, int u, int v, int index) {
        if (this.menu.isEmpty(index + 12)) {
            this.blit(pPoseStack, x, y, u, v, 16, 16);
        }
    }
}
