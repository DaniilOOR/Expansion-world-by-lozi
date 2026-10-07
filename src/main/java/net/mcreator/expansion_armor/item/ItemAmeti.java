
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
public class ItemAmeti extends ElementsExpansionarmor.ModElement {
	@GameRegistry.ObjectHolder("expansion_armor:ametihelmet")
	public static final Item helmet = null;
	@GameRegistry.ObjectHolder("expansion_armor:ametibody")
	public static final Item body = null;
	@GameRegistry.ObjectHolder("expansion_armor:ametilegs")
	public static final Item legs = null;
	@GameRegistry.ObjectHolder("expansion_armor:ametiboots")
	public static final Item boots = null;
	public ItemAmeti(ElementsExpansionarmor instance) {
		super(instance, 135);
	}

	@Override
	public void initElements() {
		ItemArmor.ArmorMaterial enuma = EnumHelper.addArmorMaterial("AMETI", "expansion_armor:phantom_main_", 100, new int[]{5, 8, 12, 5}, 30,
				(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_diamond")),
				4f);
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("ametihelmet").setRegistryName("ametihelmet")
				.setCreativeTab(TabStrangearmor.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.CHEST).setUnlocalizedName("ametibody").setRegistryName("ametibody")
				.setCreativeTab(TabStrangearmor.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.LEGS).setUnlocalizedName("ametilegs").setRegistryName("ametilegs")
				.setCreativeTab(TabStrangearmor.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.FEET).setUnlocalizedName("ametiboots").setRegistryName("ametiboots")
				.setCreativeTab(TabStrangearmor.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(helmet, 0, new ModelResourceLocation("expansion_armor:ametihelmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(body, 0, new ModelResourceLocation("expansion_armor:ametibody", "inventory"));
		ModelLoader.setCustomModelResourceLocation(legs, 0, new ModelResourceLocation("expansion_armor:ametilegs", "inventory"));
		ModelLoader.setCustomModelResourceLocation(boots, 0, new ModelResourceLocation("expansion_armor:ametiboots", "inventory"));
	}
}
