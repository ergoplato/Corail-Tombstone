package ovh.corail.tombstone.gui;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.OptionButton;
import net.minecraft.client.settings.AbstractOption;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

@OnlyIn(Dist.CLIENT)
public abstract class IntegerConfigOption extends AbstractOption {
    private final IntSupplier supplier;
    private final Consumer<Integer> consumer;
    private int max;
    private final Consumer<Boolean> dirty;

    public IntegerConfigOption(String title, IntSupplier supplier, Consumer<Integer> consumer, int max, Consumer<Boolean> dirty) {
        super(title);
        this.supplier = supplier;
        this.consumer = consumer;
        this.max = max;
        this.dirty = dirty;
    }

    private void set(int value) {
        this.consumer.accept(value);
    }

    public int get() {
        return this.supplier.getAsInt();
    }

    @Override
    public Widget createWidget(GameSettings options, int x, int y, int width) {
        return new OptionButton(x, y, width, 14, this, getOptionName(), pressable -> {
            set(this.supplier.getAsInt() >= this.max ? 0 : this.supplier.getAsInt() + 1);
            pressable.setMessage(getOptionName());
            this.dirty.accept(true);
        }) {
            @Override
            public void renderButton(int mouseX, int mouseY, float partialTicks) {
                Minecraft minecraft = Minecraft.getInstance();
                renderBg(minecraft, mouseX, mouseY);
                int j = isHovered() ? 0xff897235 : 0xffffffff;
                drawCenteredString(minecraft.fontRenderer, getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255f) << 24);
            }
        };
    }

    protected abstract String getOptionName();
}
