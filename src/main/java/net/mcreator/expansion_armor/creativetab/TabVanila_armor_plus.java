
package net.mcreator.expansion_armor.creativetab;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;

import net.mcreator.expansion_armor.item.ItemApplesword;
import net.mcreator.expansion_armor.ElementsExpansionarmor;

@ElementsExpansionarmor.ModElement.Tag
public class TabVanila_armor_plus extends ElementsExpansionarmor.ModElement {
	public TabVanila_armor_plus(ElementsExpansionarmor instance) {
		super(instance, 185);
	}

	@Override
	public void initElements() {
		tab = new CreativeTabs("tabvanila_armor_plus") {
			@SideOnly(Side.CLIENT)
			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(ItemApplesword.block, (int) (1));
			}

			@SideOnly(Side.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
	}
	public static CreativeTabs tab;
}
