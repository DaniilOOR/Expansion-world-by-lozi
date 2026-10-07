
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
public class ItemStone_armor extends ElementsExpansionarmor.ModElement {
	@GameRegistry.ObjectHolder("expansion_armor:stone_armorhelmet")
	public static final Item helmet = null;
	@GameRegistry.ObjectHolder("expansion_armor:stone_armorbody")
	public static final Item body = null;
	@GameRegistry.ObjectHolder("expansion_armor:stone_armorlegs")
	public static final Item legs = null;
	@GameRegistry.ObjectHolder("expansion_armor:stone_armorboots")
	public static final Item boots = null;
	public ItemStone_armor(ElementsExpansionarmor instance) {
		super(instance, 201);
	}

	@Override
	public void initElements() {
		ItemArmor.ArmorMaterial enuma = EnumHelper.addArmorMaterial("STONE_ARMOR", "expansion_armor:bone_", 9, new int[]{1, 3, 5, 1}, 14,
				(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_iron")), 0f);
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("stone_armorhelmet")
				.setRegistryName("stone_armorhelmet").setCreativeTab(TabVanila_armor_plus.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.CHEST).setUnlocalizedName("stone_armorbody")
				.setRegistryName("stone_armorbody").setCreativeTab(TabVanila_armor_plus.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.LEGS).setUnlocalizedName("stone_armorlegs")
				.setRegistryName("stone_armorlegs").setCreativeTab(TabVanila_armor_plus.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.FEET).setUnlocalizedName("stone_armorboots")
				.setRegistryName("stone_armorboots").setCreativeTab(TabVanila_armor_plus.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(helmet, 0, new ModelResourceLocation("expansion_armor:stone_armorhelmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(body, 0, new ModelResourceLocation("expansion_armor:stone_armorbody", "inventory"));
		ModelLoader.setCustomModelResourceLocation(legs, 0, new ModelResourceLocation("expansion_armor:stone_armorlegs", "inventory"));
		ModelLoader.setCustomModelResourceLocation(boots, 0, new ModelResourceLocation("expansion_armor:stone_armorboots", "inventory"));
	}
}
