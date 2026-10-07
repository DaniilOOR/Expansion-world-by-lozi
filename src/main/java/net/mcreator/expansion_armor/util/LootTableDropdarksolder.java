
package net.mcreator.expansion_armor.util;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;

import net.mcreator.expansion_armor.ElementsExpansionarmor;

@ElementsExpansionarmor.ModElement.Tag
public class LootTableDropdarksolder extends ElementsExpansionarmor.ModElement {
	public LootTableDropdarksolder(ElementsExpansionarmor instance) {
		super(instance, 46);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		LootTableList.register(new ResourceLocation("expansion_armor", "entities/dark_solder"));
	}
}
