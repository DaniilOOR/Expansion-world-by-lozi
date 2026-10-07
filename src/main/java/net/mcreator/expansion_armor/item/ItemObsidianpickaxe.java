
package net.mcreator.expansion_armor.item;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import net.mcreator.expansion_armor.creativetab.TabVanila_armor_plus;
import net.mcreator.expansion_armor.ElementsExpansionarmor;

import java.util.Set;
import java.util.HashMap;

@ElementsExpansionarmor.ModElement.Tag
public class ItemObsidianpickaxe extends ElementsExpansionarmor.ModElement {
	@GameRegistry.ObjectHolder("expansion_armor:obsidianpickaxe")
	public static final Item block = null;
	public ItemObsidianpickaxe(ElementsExpansionarmor instance) {
		super(instance, 191);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemPickaxe(EnumHelper.addToolMaterial("OBSIDIANPICKAXE", 3, 2048, 5f, 3f, 5)) {
			{
				this.attackSpeed = -2.7999999999999998f;
			}
			public Set<String> getToolClasses(ItemStack stack) {
				HashMap<String, Integer> ret = new HashMap<String, Integer>();
				ret.put("pickaxe", 3);
				return ret.keySet();
			}
		}.setUnlocalizedName("obsidianpickaxe").setRegistryName("obsidianpickaxe").setCreativeTab(TabVanila_armor_plus.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("expansion_armor:obsidianpickaxe", "inventory"));
	}
}
