package fuzs.easyanvils.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.easyanvils.EasyAnvils;
import fuzs.easyanvils.network.client.C2SNameTagUpdateMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class NameTagEditScreen extends Screen {
    private static final ResourceLocation EDIT_NAME_TAG_LOCATION = new ResourceLocation(EasyAnvils.MOD_ID, "textures/gui/edit_name_tag.png");

    private final int imageWidth = 176;
    private final int imageHeight = 48;
    private int leftPos;
    private int topPos;
    private final int titleLabelX = 60;
    private final int titleLabelY = 8;
    private final InteractionHand hand;
    private String itemName;
    private EditBox name;

    public NameTagEditScreen(InteractionHand hand, String title) {
        super(Component.translatable("easyanvils.name_tag.edit", Items.NAME_TAG.getDescription()));
        this.hand = hand;
        this.itemName = title;
    }

    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = this.height / 4;
        this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 120, 200, 20, CommonComponents.GUI_DONE, (button) -> {
            EasyAnvils.NETWORK.sendToServer(new C2SNameTagUpdateMessage(this.hand, this.itemName));
            this.onClose();
        }));
        this.name = new EditBox(this.font, this.leftPos + 62, this.topPos + 26, 103, 12, Component.translatable("container.repair"));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(50);
        this.name.setResponder(s -> this.itemName = s);
        this.name.setValue(this.itemName);
        this.addWidget(this.name);
        this.setInitialFocus(this.name);
    }

    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void tick() {
        this.name.tick();
    }

    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.title, this.leftPos + this.titleLabelX, this.topPos + this.titleLabelY, 4210752);
    }

    @Override
    public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
        String s = this.name.getValue();
        this.init(pMinecraft, pWidth, pHeight);
        this.name.setValue(s);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        this.renderBg(poseStack, partialTick, mouseX, mouseY);
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.renderLabels(poseStack, mouseX, mouseY);
        this.name.render(poseStack, mouseX, mouseY, partialTick);
    }

    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, EDIT_NAME_TAG_LOCATION);
        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.itemRenderer.blitOffset = 100.0F;
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.scale(2.0F, 2.0F, 2.0F);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.NAME_TAG), (this.leftPos + 17) / 2, (this.topPos + 8) / 2);
        modelViewStack.popPose();
        this.itemRenderer.blitOffset = 0.0F;
        RenderSystem.applyModelViewMatrix();
    }
}
