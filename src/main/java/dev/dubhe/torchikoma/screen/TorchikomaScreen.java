package dev.dubhe.torchikoma.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.menu.TorchikomaMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.function.Supplier;

public class TorchikomaScreen extends AbstractDepartInvScreen<TorchikomaMenu<?>> {
    private static final ResourceLocation BACKGROUND = Torchikoma.of("textures/gui/torchikoma.png");
    private static final DecimalFormat TRANS_FORMAT = new DecimalFormat("0.00#");

    private final TorchikomaButton[] buttons = new TorchikomaButton[3];

    public TorchikomaScreen(TorchikomaMenu pMenu, Inventory inventory, Component pTitle) {
        super(pMenu, inventory, pTitle, 178, 106);
        this.inventoryLabelY += 48;
        this.titleLabelY += 66;
    }

    @Override
    protected void init() {
        super.init();
        this.addButton(8, 83, Component.translatable("btn.torchikoma.mode.following"), 0);
        this.addButton(26, 83, Component.translatable("btn.torchikoma.mode.situ"), 1);
        this.addButton(44, 83, Component.translatable("btn.torchikoma.mode.standby"), 2, this.menu::canStandby);
        this.updateButtons();
    }

    @SuppressWarnings("SameParameterValue")
    private void addButton(int pX, int pY, Component tooltip, int index) {
        this.addButton(pX, pY, tooltip, index, () -> true);
    }

    private void addButton(int pX, int pY, Component tooltip, int index, Supplier<Boolean> canActive) {
        TorchikomaButton button = new TorchikomaButton(this.leftPos + pX, this.topPos + pY, tooltip, index, canActive);
        this.addRenderableWidget(button);
        this.buttons[index] = button;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.updateButtons();
    }

    private void updateButtons() {
        int index = this.menu.getStatus();
        for (TorchikomaButton button : this.buttons) {
            button.setSelected(button.index == index);
            button.active();
        }
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics graphics, int pX, int pY) {
        super.renderTooltip(graphics, pX, pY);
        if (pX >= this.leftPos + 89 && pX <= this.leftPos + 169 && pY >= this.topPos + 84 && pY <= this.topPos + 88) {
            graphics.renderTooltip(this.font, Component.translatable("gui.torchikoma.energy", TRANS_FORMAT.format(this.menu.getEnergy() / 100F)), pX, pY);
        }
        if (pX >= this.leftPos + 89 && pX <= this.leftPos + 169 && pY >= this.topPos + 94 && pY <= this.topPos + 98) {
            graphics.renderTooltip(this.font, Component.translatable("gui.torchikoma.health", this.menu.getHealth()), pX, pY);
        }
        for (TorchikomaButton button : buttons) {
            if (button.isHoveredOrFocused()) graphics.renderTooltip(this.font, button.tooltip, pX, pY);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
        byte status = this.menu.getStatus();
        int healLength = (int) (80 * this.menu.getHealth() / 80.0F);
        int energyLength = (int) (80 * this.menu.getEnergy() / 20000F);
        if (status < 0 || status > 2) throw new IllegalArgumentException("Invalid status index: " + status);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.menuWidth, this.menuHeight); // 背景板
        for (int i = 0; i < 3; i++) {
            this.renderItemBg(graphics, this.leftPos + 9, this.topPos + i * 18 + 16, this.menuWidth, i * 16, i); // 物品背景
        }
        graphics.blit(BACKGROUND, this.leftPos + 89, this.topPos + 84, 54, this.menuHeight, energyLength, 4); // 能量条
        graphics.blit(BACKGROUND, this.leftPos + 89, this.topPos + 94, 54, this.menuHeight + 4, healLength, 4); // 血条
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, this.leftPos + 62, this.topPos + 57, 17, this.leftPos + 62 - pMouseX, this.topPos + 42 - pMouseY, this.menu.getEntity());
    }

    protected void renderItemBg(@NotNull GuiGraphics graphics, int x, int y, int u, int v, int index) {
        if (this.menu.isEmpty(index + 12)) {
            graphics.blit(BACKGROUND, x, y, u, v, 16, 16);
        }
    }

    class TorchikomaButton extends AbstractButton {
        private final Supplier<Boolean> canActive;
        private final Component tooltip;
        private final int index;
        private boolean isSelected;
        public TorchikomaButton(int pX, int pY, Component tooltip, int index, Supplier<Boolean> canActive) {
            super(pX, pY, 18, 16, Component.empty());
            this.canActive = canActive;
            this.tooltip = tooltip;
            this.index = index;
        }

        @Override
        public void onPress() {
            if (!this.isSelected) TorchikomaScreen.this.menu.setStatus((byte) this.index);
        }

        @Override
        public void renderWidget(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
            super.renderWidget(graphics, pMouseX, pMouseY, pPartialTick);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, BACKGROUND);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.blit(BACKGROUND, this.getX(), this.getY(), this.index * 18, this.isSelected ? 106 : this.active && this.isHoveredOrFocused() ? 138 : 122, this.width, this.height);
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
            this.defaultButtonNarrationText(pNarrationElementOutput);
        }

        public void setSelected(boolean selected) {
            this.isSelected = selected;
        }

        public void active() {
            this.active = canActive.get();
        }
    }

}
