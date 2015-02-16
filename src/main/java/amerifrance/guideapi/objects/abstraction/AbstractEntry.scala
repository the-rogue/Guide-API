package amerifrance.guideapi.objects.abstraction

import java.util

import amerifrance.guideapi.gui.{GuiBase, GuiCategory}
import amerifrance.guideapi.objects.Book
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.gui.FontRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector

abstract class AbstractEntry(pageList: util.List[AbstractPage] = new util.ArrayList[AbstractPage], unlocEntryName: String) {

  var pages: util.List[AbstractPage] = pageList
  var unlocalizedEntryName: String = unlocEntryName

  def addPage(page: AbstractPage) = {
    this.pages.add(page)
  }

  def addPageList(list: util.List[AbstractPage]) = {
    this.pages.addAll(list)
  }

  def removePage(page: AbstractPage) = {
    this.pages.remove(page)
  }

  def removePageList(list: util.List[AbstractPage]) = {
    this.pages.removeAll(list)
  }

  def getLocalizedName(): String = {
    return StatCollector.translateToLocal(unlocalizedEntryName)
  }

  @SideOnly(Side.CLIENT)
  def draw(book: Book, category: AbstractCategory, entryX: Int, entryY: Int, entryWidth: Int, entryHeight: Int, mouseX: Int, mouseY: Int, guiBase: GuiBase, fontRenderer: FontRenderer)

  @SideOnly(Side.CLIENT)
  def drawExtras(book: Book, category: AbstractCategory, entryX: Int, entryY: Int, entryWidth: Int, entryHeight: Int, mouseX: Int, mouseY: Int, guiBase: GuiBase, fontRenderer: FontRenderer)

  def canSee(player: EntityPlayer, bookStack: ItemStack): Boolean

  def onLeftClicked(book: Book, category: AbstractCategory, mouseX: Int, mouseY: Int, player: EntityPlayer, guiCategory: GuiCategory)

  def onRightClicked(book: Book, category: AbstractCategory, mouseX: Int, mouseY: Int, player: EntityPlayer, guiCategory: GuiCategory)

  def canEqual(other: Any): Boolean = other.isInstanceOf[AbstractEntry]

  override def equals(other: Any): Boolean = other match {
    case that: AbstractEntry =>
      (that canEqual this) &&
        pages == that.pages &&
        unlocalizedEntryName == that.unlocalizedEntryName
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(pages, unlocalizedEntryName)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
