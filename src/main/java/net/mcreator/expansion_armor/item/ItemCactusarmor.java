
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
public class ItemCactusarmor extends ElementsExpansionarmor.ModElement {
	@GameRegistry.ObjectHolder("expansion_armor:cactusarmorhelmet")
	public static final Item helmet = null;
	@GameRegistry.ObjectHolder("expansion_armor:cactusarmorbody")
	public static final Item body = null;
	@GameRegistry.ObjectHolder("expansion_armor:cactusarmorlegs")
	public static final Item legs = null;
	@GameRegistry.ObjectHolder("expansion_armor:cactusarmorboots")
	public static final Item boots = null;
	public ItemCactusarmor(ElementsExpansionarmor instance) {
		super(instance, 199);
	}

	@Override
	public void initElements() {
		ItemArmor.ArmorMaterial enuma = EnumHelper.addArmorMaterial("CACTUSARMOR", "expansion_armor:cactus_", 4, new int[]{1, 2, 3, 1}, 7,
				(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_leather")),
				0f);
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("cactusarmorhelmet")
				.setRegistryName("cactusarmorhelmet").setCreativeTab(TabVanila_armor_plus.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.CHEST).setUnlocalizedName("cactusarmorbody")
				.setRegistryName("cactusarmorbody").setCreativeTab(TabVanila_armor_plus.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.LEGS).setUnlocalizedName("cactusarmorlegs")
				.setRegistryName("cactusarmorlegs").setCreativeTab(TabVanila_armor_plus.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.FEET).setUnlocalizedName("cactusarmorboots")
				.setRegistryName("cactusarmorboots").setCreativeTab(TabVanila_armor_plus.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(helmet, 0, new ModelResourceLocation("expansion_armor:cactusarmorhelmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(body, 0, new ModelResourceLocation("expansion_armor:cactusarmorbody", "inventory"));
		ModelLoader.setCustomModelResourceLocation(legs, 0, new ModelResourceLocation("expansion_armor:cactusarmorlegs", "inventory"));
		ModelLoader.setCustomModelResourceLocation(boots, 0, new ModelResourceLocation("expansion_armor:cactusarmorboots", "inventory"));
	}
}
