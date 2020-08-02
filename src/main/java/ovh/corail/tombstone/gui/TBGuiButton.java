package ovh.corail.tombstone.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TBGuiButton extends Button {
    public boolean forceHighlight = false;

    TBGuiButton(int x, int y, int widthIn, int heightIn, String buttonText, Button.IPressable onPress) {
        super(x, y, widthIn, heightIn, buttonText, onPress);
        setFGColor(0xffffffff);
    }

    @Override
    protected void renderBg(Minecraft minecraft, int i, int j) {
        if (isHovered()) {
            fillGradient(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, 0xff202D5F, 0xff202D5F);
        }
        fillGradient(this.x, this.y, this.x + this.width, this.y + this.height, 0x70000000, 0x30000000);
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        renderBg(minecraft, mouseX, mouseY);
        int j = isHovered() || this.forceHighlight ? 0xff897235 : getFGColor();
        this.drawCenteredString(minecraft.fontRenderer, getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255f) << 24);
    }
}
