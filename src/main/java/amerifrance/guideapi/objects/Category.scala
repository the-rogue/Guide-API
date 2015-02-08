package amerifrance.guideapi.objects

import java.util

import net.minecraft.util.StatCollector

class Category(entryList: util.List[Entry] = new util.ArrayList[Entry](), unlocCategoryName: String) {

  var entries: util.List[Entry] = entryList
  var unlocalizedCategoryName: String = unlocCategoryName

  def addEntry(entry: Entry) = {
    this.entries.add(entry)
  }

  def addEntryList(list: util.List[Entry]) = {
    this.entries.addAll(list)
  }

  def removeEntry(entry: Entry) = {
    this.entries.remove(entry)
  }

  def removeCategoryList(list: util.List[Entry]) = {
    this.entries.remove(list)
  }

  def getLocalizedName(): String = {
    return StatCollector.translateToLocal(unlocalizedCategoryName)
  }
}