
package net.mcreator.expansion_armor.item;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import net.mcreator.expansion_armor.creativetab.TabVanila_armor_plus;
import net.mcreator.expansion_armor.ElementsExpansionarmor;

import java.util.Set;
import java.util.HashMap;

@ElementsExpansionarmor.ModElement.Tag
public class ItemObsidianhoe extends ElementsExpansionarmor.ModElement {
	@GameRegistry.ObjectHolder("expansion_armor:obsidianhoe")
	public static final Item block = null;
	public ItemObsidianhoe(ElementsExpansionarmor instance) {
		super(instance, 194);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemHoe(EnumHelper.addToolMaterial("OBSIDIANHOE", 3, 2048, 5f, 3f, 5)) {
			public Set<String> getToolClasses(ItemStack stack) {
				HashMap<String, Integer> ret = new HashMap<String, Integer>();
				ret.put("hoe", 3);
				return ret.keySet();
			}
		}.setUnlocalizedName("obsidianhoe").setRegistryName("obsidianhoe").setCreativeTab(TabVanila_armor_plus.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("expansion_armor:obsidianhoe", "inventory"));
	}
}
