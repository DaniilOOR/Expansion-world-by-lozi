
package net.mcreator.expansion_armor.item;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.Item;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import net.mcreator.expansion_armor.creativetab.TabVanila_armor_plus;
import net.mcreator.expansion_armor.ElementsExpansionarmor;

@ElementsExpansionarmor.ModElement.Tag
public class ItemObsidian_armor extends ElementsExpansionarmor.ModElement {
	@GameRegistry.ObjectHolder("expansion_armor:obsidian_armorhelmet")
	public static final Item helmet = null;
	@GameRegistry.ObjectHolder("expansion_armor:obsidian_armorbody")
	public static final Item body = null;
	@GameRegistry.ObjectHolder("expansion_armor:obsidian_armorlegs")
	public static final Item legs = null;
	@GameRegistry.ObjectHolder("expansion_armor:obsidian_armorboots")
	public static final Item boots = null;
	public ItemObsidian_armor(ElementsExpansionarmor instance) {
		super(instance, 180);
	}

	@Override
	public void initElements() {
		ItemArmor.ArmorMaterial enuma = EnumHelper.addArmorMaterial("OBSIDIAN_ARMOR", "expansion_armor:obsidian_", 40, new int[]{3, 6, 8, 3}, 5,
				(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_diamond")),
				4f);
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("obsidian_armorhelmet")
				.setRegistryName("obsidian_armorhelmet").setCreativeTab(TabVanila_armor_plus.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.CHEST).setUnlocalizedName("obsidian_armorbody")
				.setRegistryName("obsidian_armorbody").setCreativeTab(TabVanila_armor_plus.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.LEGS).setUnlocalizedName("obsidian_armorlegs")
				.setRegistryName("obsidian_armorlegs").setCreativeTab(TabVanila_armor_plus.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.FEET).setUnlocalizedName("obsidian_armorboots")
				.setRegistryName("obsidian_armorboots").setCreativeTab(TabVanila_armor_plus.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(helmet, 0, new ModelResourceLocation("expansion_armor:obsidian_armorhelmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(body, 0, new ModelResourceLocation("expansion_armor:obsidian_armorbody", "inventory"));
		ModelLoader.setCustomModelResourceLocation(legs, 0, new ModelResourceLocation("expansion_armor:obsidian_armorlegs", "inventory"));
		ModelLoader.setCustomModelResourceLocation(boots, 0, new ModelResourceLocation("expansion_armor:obsidian_armorboots", "inventory"));
	}
}
