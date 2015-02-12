package amerifrance.guideapi.gui;

import amerifrance.guideapi.ModInformation;
import amerifrance.guideapi.objects.Book;
import amerifrance.guideapi.objects.abstraction.AbstractCategory;
import amerifrance.guideapi.objects.abstraction.AbstractEntry;
import amerifrance.guideapi.objects.abstraction.AbstractPage;
import amerifrance.guideapi.wrappers.PageWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class GuiEntry extends GuiBase {

    public ResourceLocation outlineTexture;
    public ResourceLocation pageTexture = new ResourceLocation(ModInformation.GUITEXLOC + "book_colored.png");
    public Book book;
    public AbstractCategory category;
    public AbstractEntry entry;
    public List<PageWrapper> pageWrapperList = new ArrayList<PageWrapper>();
    private int pageNumber;

    public GuiEntry(GuiCategory categoryGui, Book book, AbstractCategory category, AbstractEntry entry, EntityPlayer player, ItemStack bookStack) {
        super(player, bookStack);
        this.outlineTexture = new ResourceLocation(ModInformation.GUITEXLOC + "book_greyscale.png");
        this.category = category;
        this.book = book;
        this.entry = entry;
        this.pageNumber = 0;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.pageWrapperList.clear();

        guiLeft = (this.width - this.xSize) / 2;
        guiTop = (this.height - this.ySize) / 2;

        for (AbstractPage page : this.entry.pages()) {
            pageWrapperList.add(new PageWrapper(book, category, entry, page, guiLeft, guiTop, player, this.fontRendererObj, bookStack));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
        super.drawScreen(mouseX, mouseY, renderPartialTicks);

        Minecraft.getMinecraft().getTextureManager().bindTexture(pageTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        Minecraft.getMinecraft().getTextureManager().bindTexture(outlineTexture);
        drawTexturedModalRectWithColor(guiLeft, guiTop, 0, 0, xSize, ySize, book.color());

        if (pageNumber < pageWrapperList.size()) {
            if (pageWrapperList.get(pageNumber).canPlayerSee()) {
                pageWrapperList.get(pageNumber).draw(mouseX, mouseY, this);
                pageWrapperList.get(pageNumber).drawExtras(mouseX, mouseY, this);
            }
        }

        drawCenteredString(fontRendererObj, String.valueOf(pageNumber + 1) + "/" + String.valueOf(pageWrapperList.size()), guiLeft + xSize / 2, guiTop + 5 * ySize / 6, 0);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int typeofClick) {
        super.mouseClicked(mouseX, mouseY, typeofClick);
        for (PageWrapper wrapper : this.pageWrapperList) {
            if (wrapper.isMouseOnWrapper(mouseX, mouseY) && wrapper.canPlayerSee()) {
                if (typeofClick == 0) {
                    pageWrapperList.get(pageNumber).page.onLeftClicked(book, category, entry, mouseX, mouseY, player, this);
                }
                if (typeofClick == 1) {
                    pageWrapperList.get(pageNumber).page.onRightClicked(book, category, entry, mouseX, mouseY, player, this);
                }
            }
        }

        if (typeofClick == 1) {
            this.mc.displayGuiScreen(new GuiCategory(book, category, player, bookStack));
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_BACK || keyCode == this.mc.gameSettings.keyBindUseItem.getKeyCode()) {
            this.mc.displayGuiScreen(new GuiCategory(book, category, player, bookStack));
        }

        if ((keyCode == Keyboard.KEY_UP || keyCode == Keyboard.KEY_RIGHT) && pageNumber + 1 < pageWrapperList.size()) {
            this.pageNumber++;
        }
        if ((keyCode == Keyboard.KEY_DOWN || keyCode == Keyboard.KEY_LEFT) && pageNumber > 0) {
            this.pageNumber--;
        }
    }
}
