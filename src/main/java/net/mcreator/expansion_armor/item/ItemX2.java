package net.mcreator.expansion_armor.item;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import net.mcreator.expansion_armor.creativetab.TabStrangearmor;
import net.mcreator.expansion_armor.ElementsExpansionarmor;

import java.util.Set;
import java.util.List;


@ElementsExpansionarmor.ModElement.Tag
public class ItemX2 extends ElementsExpansionarmor.ModElement {
	@GameRegistry.ObjectHolder("expansion_armor:x2")
	public static final Item block = null;
	public ItemX2(ElementsExpansionarmor instance) {
		super(instance, 51);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemToolCustom() {
		}.setUnlocalizedName("x2").setRegistryName("x2").setCreativeTab(TabStrangearmor.tab));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("expansion_armor:x2", "inventory"));
	}
	private static class ItemToolCustom extends ItemTool {
		private static final Set<Block> effective_items_set = com.google.common.collect.Sets
				.newHashSet(new Block[]{Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN,
						Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE});
		protected ItemToolCustom() {
			super(EnumHelper.addToolMaterial("X2", 2, 400, 6f, 7f, 14), effective_items_set);
			this.attackDamage = 7f;
			this.attackSpeed = -2.7999999999999998f;
		}

		@Override
		public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
			ItemStack itemstack = player.getHeldItem(hand);

			// Делаем бросок только на сервере
			if (!world.isRemote && world instanceof WorldServer) {
				WorldServer worldServer = (WorldServer) world;

				// 1. Анимация взмаха руки и резкий звук броска
				player.swingArm(hand);
				
				
				worldServer.playSound(null, player.posX, player.posY, player.posZ, 
					SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0F, 0.7F);

				// 2. Расчет траектории полета топора вперед по взгляду
				Vec3d lookVec = player.getLookVec();
				double startX = player.posX;
				double startY = player.posY + player.getEyeHeight() - 0.2;
				double startZ = player.posZ;

				int maxRange = 15; // Дальность броска в блоках
				boolean hitTarget = false;

				// Шагаем по лучу каждые 0.5 блока
				for (double d = 1.0; d <= maxRange; d += 0.5) {
					double checkX = startX + lookVec.x * d;
					double checkY = startY + lookVec.y * d;
					double checkZ = startZ + lookVec.z * d;

					// Если на пути плотный блок — топор «ударился» о стену, прекращаем полет
					if (worldServer.getBlockState(new net.minecraft.util.math.BlockPos(checkX, checkY, checkZ)).getMaterial().isSolid()) {
						worldServer.playSound(null, checkX, checkY, checkZ, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.4F, 2.0F);
						break;
					}

					// Эффект крутящегося топора (частицы критического удара и облако дыма)
					worldServer.spawnParticle(EnumParticleTypes.CRIT, checkX, checkY, checkZ, 1, 0, 0, 0, 0.0D);
					if (d % 2 == 0) {
						worldServer.spawnParticle(EnumParticleTypes.SWEEP_ATTACK, checkX, checkY, checkZ, 1, 0, 0, 0, 0.0D);
					}

					// Ищем существ вокруг текущей точки летящего топора
					AxisAlignedBB scanBox = new AxisAlignedBB(
						checkX - 0.5, checkY - 0.5, checkZ - 0.5,
						checkX + 0.5, checkY + 0.5, checkZ + 0.5
					);
					List<EntityLivingBase> targets = worldServer.getEntitiesWithinAABB(EntityLivingBase.class, scanBox);

					for (EntityLivingBase target : targets) {
						if (target != player) {
							// Наносим урон от имени игрока (равный урону топора = 7 единиц)
							target.attackEntityFrom(DamageSource.causePlayerDamage(player), 7.0F);
							
							// Отбрасываем цель по направлению броска
							target.knockBack(player, 0.6F, -lookVec.x, -lookVec.z);
							
							// Звук успешного попадания металла по плоти
							worldServer.playSound(null, target.posX, target.posY, target.posZ, 
								SoundEvents.ENTITY_IRONGOLEM_HURT, SoundCategory.PLAYERS, 0.7F, 1.3F);
							
							hitTarget = true;
							break;
						}
					}

					// Если топор попал во врага — прерываем цикл полета
					if (hitTarget) {
						break;
					}
				}

				// 3. Тратим 1 единицу прочности топора за бросок
				itemstack.damageItem(1, player);

				// 4. Включаем кулдаун (шторку) на 1.5 секунды (30 тиков), чтобы нельзя было спамить
				player.getCooldownTracker().setCooldown(this, 30);
			}

			return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
		}

		@Override
		public float getDestroySpeed(ItemStack stack, IBlockState state) {
			Material material = state.getMaterial();
			return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE
					? super.getDestroySpeed(stack, state)
					: this.efficiency;
		}
	}
}
