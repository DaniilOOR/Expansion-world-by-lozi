
package net.mcreator.expansion_armor.item;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.world.World;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.EnumAction;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;

import net.mcreator.expansion_armor.creativetab.TabVanila_armor_plus;
import net.mcreator.expansion_armor.ElementsExpansionarmor;

import java.util.Set;
import java.util.HashMap;

import com.google.common.collect.Multimap;

@ElementsExpansionarmor.ModElement.Tag
public class ItemApplesword extends ElementsExpansionarmor.ModElement {
	@GameRegistry.ObjectHolder("expansion_armor:applesword")
	public static final Item block = null;
	public ItemApplesword(ElementsExpansionarmor instance) {
		super(instance, 195);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemSword(EnumHelper.addToolMaterial("APPLESWORD", 0, 32, 1f, 0f, 18)) {
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

			public Set<String> getToolClasses(ItemStack stack) {
				HashMap<String, Integer> ret = new HashMap<String, Integer>();
				ret.put("sword", 0);
				return ret.keySet();
			}
			
			@Override
			public EnumAction getItemUseAction(ItemStack stack) {
				return EnumAction.EAT;
			}

			// 2. Время поедания (32 тика = 1.6 секунды, как у обычного яблока)
			@Override
			public int getMaxItemUseDuration(ItemStack stack) {
				return 32;
			}

			// 3. Запуск процесса «поедания» при нажатии ПКМ
			@Override
			public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
				ItemStack itemstack = player.getHeldItem(hand);
				// Разрешаем есть меч, только если игрок голоден (как обычную еду)
				if (player.canEat(false)) {
					player.setActiveHand(hand);
					return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
				}
				return new ActionResult<>(EnumActionResult.FAIL, itemstack);
			}

			// 4. Что происходит, когда игрок ДОЕЛ меч до конца
			@Override
			public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
				if (entityLiving instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) entityLiving;
					
					// Восстанавливаем еду: 4 единицы (2 деления сытости) и 0.3F насыщения (как яблоко)
					player.getFoodStats().addStats(4, 0.3F);
					
					// Проигрываем ванильный звук отрыжки/успешного съедения
					world.playSound(null, player.posX, player.posY, player.posZ, 
						net.minecraft.init.SoundEvents.ENTITY_PLAYER_BURP, net.minecraft.util.SoundCategory.PLAYERS, 
						0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

					// Тратим 1 единицу прочности меча (на сервере)
					if (!world.isRemote) {
						stack.damageItem(1, player);
					}
				}
				return stack;
			}
			
		}.setUnlocalizedName("applesword").setRegistryName("applesword").setCreativeTab(TabVanila_armor_plus.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("expansion_armor:applesword", "inventory"));
	}
}
