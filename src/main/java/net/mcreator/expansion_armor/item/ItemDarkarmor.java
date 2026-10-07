
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

import net.mcreator.expansion_armor.creativetab.TabStrangearmor;
import net.mcreator.expansion_armor.ElementsExpansionarmor;

@ElementsExpansionarmor.ModElement.Tag
public class ItemDarkarmor extends ElementsExpansionarmor.ModElement {
	@GameRegistry.ObjectHolder("expansion_armor:darkarmorhelmet")
	public static final Item helmet = null;
	@GameRegistry.ObjectHolder("expansion_armor:darkarmorbody")
	public static final Item body = null;
	@GameRegistry.ObjectHolder("expansion_armor:darkarmorlegs")
	public static final Item legs = null;
	@GameRegistry.ObjectHolder("expansion_armor:darkarmorboots")
	public static final Item boots = null;
	public ItemDarkarmor(ElementsExpansionarmor instance) {
		super(instance, 47);
	}

	@Override
	public void initElements() {
		ItemArmor.ArmorMaterial enuma = EnumHelper.addArmorMaterial("DARKARMOR", "expansion_armor:dusksteel__", 40, new int[]{3, 7, 9, 4}, 20,
				(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_diamond")),
				4f);
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("darkarmorhelmet")
				.setRegistryName("darkarmorhelmet").setCreativeTab(TabStrangearmor.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.CHEST).setUnlocalizedName("darkarmorbody")
				.setRegistryName("darkarmorbody").setCreativeTab(TabStrangearmor.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.LEGS).setUnlocalizedName("darkarmorlegs")
				.setRegistryName("darkarmorlegs").setCreativeTab(TabStrangearmor.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.FEET).setUnlocalizedName("darkarmorboots")
				.setRegistryName("darkarmorboots").setCreativeTab(TabStrangearmor.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(helmet, 0, new ModelResourceLocation("expansion_armor:darkarmorhelmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(body, 0, new ModelResourceLocation("expansion_armor:darkarmorbody", "inventory"));
		ModelLoader.setCustomModelResourceLocation(legs, 0, new ModelResourceLocation("expansion_armor:darkarmorlegs", "inventory"));
		ModelLoader.setCustomModelResourceLocation(boots, 0, new ModelResourceLocation("expansion_armor:darkarmorboots", "inventory"));
	}
}
