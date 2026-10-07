
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
public class ItemS0 extends ElementsExpansionarmor.ModElement {
	@GameRegistry.ObjectHolder("expansion_armor:s0helmet")
	public static final Item helmet = null;
	@GameRegistry.ObjectHolder("expansion_armor:s0body")
	public static final Item body = null;
	@GameRegistry.ObjectHolder("expansion_armor:s0legs")
	public static final Item legs = null;
	@GameRegistry.ObjectHolder("expansion_armor:s0boots")
	public static final Item boots = null;
	public ItemS0(ElementsExpansionarmor instance) {
		super(instance, 26);
	}

	@Override
	public void initElements() {
		ItemArmor.ArmorMaterial enuma = EnumHelper.addArmorMaterial("S0", "expansion_armor:loodstone__", 44, new int[]{4, 7, 10, 4}, 15,
				(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_diamond")),
				4f);
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("s0helmet").setRegistryName("s0helmet")
				.setCreativeTab(TabStrangearmor.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.CHEST).setUnlocalizedName("s0body").setRegistryName("s0body")
				.setCreativeTab(TabStrangearmor.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.LEGS).setUnlocalizedName("s0legs").setRegistryName("s0legs")
				.setCreativeTab(TabStrangearmor.tab));
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.FEET).setUnlocalizedName("s0boots").setRegistryName("s0boots")
				.setCreativeTab(TabStrangearmor.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(helmet, 0, new ModelResourceLocation("expansion_armor:s0helmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(body, 0, new ModelResourceLocation("expansion_armor:s0body", "inventory"));
		ModelLoader.setCustomModelResourceLocation(legs, 0, new ModelResourceLocation("expansion_armor:s0legs", "inventory"));
		ModelLoader.setCustomModelResourceLocation(boots, 0, new ModelResourceLocation("expansion_armor:s0boots", "inventory"));
	}
}
