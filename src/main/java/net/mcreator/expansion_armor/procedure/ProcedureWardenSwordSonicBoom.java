package net.mcreator.expansion_armor.procedure;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.EnumHand;
import net.minecraft.item.ItemStack;

import net.mcreator.expansion_armor.ElementsExpansionarmor;

import java.util.HashMap;
import java.util.List;

@ElementsExpansionarmor.ModElement.Tag
public class ProcedureWardenSwordSonicBoom extends ElementsExpansionarmor.ModElement {
	public ProcedureWardenSwordSonicBoom(ElementsExpansionarmor instance) {
		super(instance, 285);
	}

	public static void executeProcedure(java.util.HashMap<String, Object> dependencies) {
        if (dependencies.get("entity") == null) {
            return;
        }

        Entity entity = (Entity) dependencies.get("entity");

        if (!(entity instanceof EntityPlayer) || entity.world.isRemote) {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;
        WorldServer worldServer = (WorldServer) player.world;
        ItemStack heldItem = (ItemStack) dependencies.get("itemstack");
        
        long currentTime = player.world.getTotalWorldTime();
        long cooldownEnd = player.getEntityData().getLong("wardenSwordCooldownEnd");

        if (currentTime < cooldownEnd) {
            return;
        }

        // 2. Устанавливаем кулдаун на 120 тиков (6 секунд) вперед
        player.getEntityData().setLong("wardenSwordCooldownEnd", currentTime + 120);
        player.getCooldownTracker().setCooldown(heldItem.getItem(), 120);

        // Включаем ванильную анимацию взмаха руки
        //player.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);

        // Воспроизводим мощный звук (звук взрыва эндер-кристалла идеально подходит под звуковой удар)
        worldServer.playSound(null, player.posX, player.posY, player.posZ, 
            SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1.0F, 1.5F);

        // 3. Рассчитываем луч звукового удара вперед
        Vec3d lookVec = player.getLookVec(); // Направление взгляда игрока
        double startX = player.posX;
        // Начинаем на уровне глаз игрока
        double startY = player.posY + player.getEyeHeight() - 0.2; 
        double startZ = player.posZ;

        int maxDistance = 12; // Дальность удара в блоках

        // Просчитываем линию пошагово (каждые 0.5 блока для точности партиклов)
        for (double d = 1.0; d <= maxDistance; d += 0.5) {
            double checkX = startX + lookVec.x * d;
            double checkY = startY + lookVec.y * d;
            double checkZ = startZ + lookVec.z * d;

            // Спавним партиклы по линии луча
            worldServer.spawnParticle(
                EnumParticleTypes.SWEEP_ATTACK, // Красивая круговая волна взмаха
                checkX, checkY, checkZ, 
                1,       // Количество
                0, 0, 0, // Смещение (в линию)
                0.0D     // Скорость
            );
            
            // Также добавляем немного дыма для плотности эффекта
            if (d % 1 == 0) {
                worldServer.spawnParticle(EnumParticleTypes.SMOKE_LARGE, checkX, checkY, checkZ, 1, 0, 0, 0, 0.0D);
            }

            // Ищем существ в маленьком кубе вокруг текущей точки луча
            AxisAlignedBB damageBox = new AxisAlignedBB(
                checkX - 0.6, checkY - 0.6, checkZ - 0.6,
                checkX + 0.6, checkY + 0.6, checkZ + 0.6
            );

            List<EntityLivingBase> targets = worldServer.getEntitiesWithinAABB(EntityLivingBase.class, damageBox);

            for (EntityLivingBase target : targets) {
                // Звуковая волна бьет всех, кроме самого игрока
                if (target != player) {
                    // Наносим МАГИЧЕСКИЙ урон (пробивает обычную броню)
                    target.attackEntityFrom(DamageSource.MAGIC, 15.0F);

                    // Отбрасываем цель НАЗАД по вектору взгляда игрока (сметающий удар)
                    target.knockBack(player, 1.2F, -lookVec.x, -lookVec.z);
                }
            }
        }
    }
}