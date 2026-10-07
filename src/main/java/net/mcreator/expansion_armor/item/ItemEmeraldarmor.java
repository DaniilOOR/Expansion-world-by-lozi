
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
public class ItemEmeraldarmor extends ElementsExpansionarmor.ModElement {
	@GameRegistry.ObjectHolder("expansion_armor:emeraldarmorhelmet")
	public static final Item helmet = null;
	@GameRegistry.ObjectHolder("expansion_armor:emeraldarmorbody")
	public static final Item body = null;
	@GameRegistry.ObjectHolder("expansion_armor:emeraldarmorlegs")
	public static final Item legs = null;
	@GameRegistry.ObjectHolder("expansion_armor:emeraldarmorboots")
	public static final Item boots = null;
	public ItemEmeraldarmor(ElementsExpansionarmor instance) {
		super(instance, 178);
	}

	@Override
	public void initElements() {
		ItemArmor.ArmorMaterial enuma = EnumHelper.addArmorMaterial("EMERALDARMOR", "expansion_armor:emerald_", 24, new int[]{3, 6, 8, 3}, 20,
				(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_diamond")),
				1f);
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("emeraldarmorhelmet")
				.setRegistryName("emeraldarmorhelmet").setCreativeTab(TabVanila_armor_plus.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.CHEST).setUnlocalizedName("emeraldarmorbody")
				.setRegistryName("emeraldarmorbody").setCreativeTab(TabVanila_armor_plus.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.LEGS).setUnlocalizedName("emeraldarmorlegs")
				.setRegistryName("emeraldarmorlegs").setCreativeTab(TabVanila_armor_plus.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.FEET).setUnlocalizedName("emeraldarmorboots")
				.setRegistryName("emeraldarmorboots").setCreativeTab(TabVanila_armor_plus.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(helmet, 0, new ModelResourceLocation("expansion_armor:emeraldarmorhelmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(body, 0, new ModelResourceLocation("expansion_armor:emeraldarmorbody", "inventory"));
		ModelLoader.setCustomModelResourceLocation(legs, 0, new ModelResourceLocation("expansion_armor:emeraldarmorlegs", "inventory"));
		ModelLoader.setCustomModelResourceLocation(boots, 0, new ModelResourceLocation("expansion_armor:emeraldarmorboots", "inventory"));
	}
}
