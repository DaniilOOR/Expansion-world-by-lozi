
package net.mcreator.expansion_armor.item;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.World;
import java.util.List;
import javax.annotation.Nullable;

import net.mcreator.expansion_armor.creativetab.TabStrangearmor;
import net.mcreator.expansion_armor.ElementsExpansionarmor;

import java.util.Set;
import java.util.HashMap;

import com.google.common.collect.Multimap;

@ElementsExpansionarmor.ModElement.Tag
public class ItemCrimson_sword extends ElementsExpansionarmor.ModElement {
	@GameRegistry.ObjectHolder("expansion_armor:crimson_sword")
	public static final Item block = null;
	public ItemCrimson_sword(ElementsExpansionarmor instance) {
		super(instance, 196);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemSword(EnumHelper.addToolMaterial("CRIMSON_SWORD", 3, 3000, 10f, 6f, 15)) {
			@Override
			public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
				Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);
				if (slot == EntityEquipmentSlot.MAINHAND) {
					multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
							new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.getAttackDamage(), 0));
					multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
							new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4, 0));
				}
				return multimap;
			}

			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
				super.addInformation(stack, worldIn, tooltip, flagIn);

				tooltip.add(TextFormatting.WHITE + "Restores " + TextFormatting.RED + "2 heart " + TextFormatting.WHITE + "upon hitting an enemy.");
			}
			
			@Override
			public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

				if (!attacker.world.isRemote && attacker instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) attacker;
					
					player.heal(4.0F);

					// Спавним красивые ванильные частицы сердечек вокруг игрока
					if (player.world instanceof WorldServer) {
						WorldServer worldServer = (WorldServer) player.world;
						worldServer.spawnParticle(
							EnumParticleTypes.HEART, 
							player.posX, player.posY + player.height / 2, player.posZ, 
							3,          // Количество частиц
							0.3D, 0.3D, 0.3D, // Радиус разлета
							0.0D        // Скорость
						);
					}
				}
				
				// Вызываем базовое поведение (трата 1 единицы прочности меча)
				return super.hitEntity(stack, target, attacker);
			}

			public Set<String> getToolClasses(ItemStack stack) {
				HashMap<String, Integer> ret = new HashMap<String, Integer>();
				ret.put("sword", 3);
				return ret.keySet();
			}
		}.setUnlocalizedName("crimson_sword").setRegistryName("crimson_sword").setCreativeTab(TabStrangearmor.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("expansion_armor:crimson_sword", "inventory"));
	}
}
