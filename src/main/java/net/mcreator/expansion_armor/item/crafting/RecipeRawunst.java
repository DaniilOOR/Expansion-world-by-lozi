
package net.mcreator.expansion_armor.item.crafting;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import net.minecraft.item.ItemStack;

import net.mcreator.expansion_armor.item.ItemUnstablesteel;
import net.mcreator.expansion_armor.item.ItemRaw_Unstable_steel;
import net.mcreator.expansion_armor.ElementsExpansionarmor;

@ElementsExpansionarmor.ModElement.Tag
public class RecipeRawunst extends ElementsExpansionarmor.ModElement {
	public RecipeRawunst(ElementsExpansionarmor instance) {
		super(instance, 311);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		GameRegistry.addSmelting(new ItemStack(ItemRaw_Unstable_steel.block, (int) (1)), new ItemStack(ItemUnstablesteel.block, (int) (1)), 1F);
	}
}
