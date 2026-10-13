
package net.mcreator.expansion_armor.item.crafting;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import net.minecraft.item.ItemStack;

import net.mcreator.expansion_armor.item.ItemShardofblood;
import net.mcreator.expansion_armor.item.ItemRaw_shard_of_blood;
import net.mcreator.expansion_armor.ElementsExpansionarmor;

@ElementsExpansionarmor.ModElement.Tag
public class RecipeRaw_blood_furn extends ElementsExpansionarmor.ModElement {
	public RecipeRaw_blood_furn(ElementsExpansionarmor instance) {
		super(instance, 312);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		GameRegistry.addSmelting(new ItemStack(ItemRaw_shard_of_blood.block, (int) (1)), new ItemStack(ItemShardofblood.block, (int) (1)), 1F);
	}
}
