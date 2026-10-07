package net.mcreator.expansion_armor.procedure;

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
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;

import net.mcreator.expansion_armor.ElementsExpansionarmor;

import java.util.HashMap;
import java.util.List;

@ElementsExpansionarmor.ModElement.Tag
public class ProcedureBigKatanaPower extends ElementsExpansionarmor.ModElement {

    public ProcedureBigKatanaPower(ElementsExpansionarmor instance) {
        super(instance, 291); // Кастомный ID вашей процедуры
    }

    public static void executeProcedure(java.util.HashMap<String, Object> dependencies) {
        if (dependencies.get("entity") == null) {
            return;
        }

        net.minecraft.entity.Entity entity = (net.minecraft.entity.Entity) dependencies.get("entity");

        // Работаем строго на сервере и только с игроком
        if (!(entity instanceof EntityPlayer) || entity.world.isRemote) {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;
        WorldServer worldServer = (WorldServer) player.world;
        ItemStack heldItem = player.getHeldItemMainhand();

        long currentTime = worldServer.getTotalWorldTime();

        // =========================================================================
        // МЕХАНИКА 2: ИАЙДО (Зажат Shift + ПКМ) — Выпад и Кровотечение
        // =========================================================================
        if (player.isSneaking()) {
            long iaidoCooldownEnd = player.getEntityData().getLong("katanaIaidoCooldownEnd");
            if (currentTime < iaidoCooldownEnd) {
                return; // Способность на перезарядке
            }

            // Ставим КД на 12 секунд (240 тиков)
            player.getEntityData().setLong("katanaIaidoCooldownEnd", currentTime + 240);
            player.getCooldownTracker().setCooldown(heldItem.getItem(), 240);

            player.swingArm(EnumHand.MAIN_HAND);
            
            // Резкий самурайский звук рассекания воздуха
            worldServer.playSound(null, player.posX, player.posY, player.posZ, 
                SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.5F, 0.6F);

            // Толкаем игрока вперед по направлению взгляда
            Vec3d lookVec = player.getLookVec();
            player.motionX = lookVec.x * 2.2D; // Скорость рывка
            player.motionZ = lookVec.z * 2.2D;
            player.velocityChanged = true; // Синхронизируем движение с клиентом

            // Оставляем шлейф из частиц критического удара по пути рывка
            for (double d = 0.0; d <= 6.0; d += 0.5) {
                double pX = player.posX + lookVec.x * d;
                double pY = player.posY + player.getEyeHeight() / 2;
                double pZ = player.posZ + lookVec.z * d;
                worldServer.spawnParticle(EnumParticleTypes.CRIT, pX, pY, pZ, 3, 0.1, 0.1, 0.1, 0.0D);
            }

            // Ищем врагов в прямоугольной зоне рывка
            AxisAlignedBB dashBox = player.getEntityBoundingBox().grow(3.0, 1.0, 3.0);
            List<EntityLivingBase> targets = worldServer.getEntitiesWithinAABB(EntityLivingBase.class, dashBox);

            for (EntityLivingBase target : targets) {
                if (target != player) {
                    // Наносим большой критический урон (16 единиц = 8 сердец)
                    target.attackEntityFrom(DamageSource.causePlayerDamage(player), 16.0F);
                    
                    // Накладываем КРОВОТЕЧЕНИЕ (эффект Иссушения II на 4 секунды)
                    target.addPotionEffect(new PotionEffect(MobEffects.WITHER, 80, 1, false, false));
                    
                    // Спавнить частицы редстоуна (имитация брызг крови) на жертве
                    worldServer.spawnParticle(EnumParticleTypes.REDSTONE, 
                        target.posX, target.posY + target.height / 2, target.posZ, 
                        15, 0.2, 0.4, 0.2, 0.0D);
                }
            }
            return;
        }

        // =========================================================================
        // МЕХАНИКА 1: КРУГОВОЙ СМЕТАЮЩИЙ УДАР (Обычный ПКМ) — Круговое АОЕ
        // =========================================================================
        long sweepCooldownEnd = player.getEntityData().getLong("katanaSweepCooldownEnd");
        if (currentTime < sweepCooldownEnd || player.getCooldownTracker().hasCooldown(heldItem.getItem())) {
            return; 
        }

        // Ставим КД на 5 секунд (100 тиков)
        player.getEntityData().setLong("katanaSweepCooldownEnd", currentTime + 100);
        player.getCooldownTracker().setCooldown(heldItem.getItem(), 100);

        player.swingArm(EnumHand.MAIN_HAND);
        
        // Тяжелый глубокий звук удара
        worldServer.playSound(null, player.posX, player.posY, player.posZ, 
            SoundEvents.ENTITY_IRONGOLEM_ATTACK, SoundCategory.PLAYERS, 0.8F, 1.5F);

        double centerX = player.posX;
        double centerY = player.posY + 1.25;
        double centerZ = player.posZ;
        double radius = 4.5; // Огромный радиус поражения большой катаны
        int particleCount = 40;
        double angleStep = 2 * Math.PI / particleCount;

        // Красивое кольцо из частиц широкого взмаха (SWEEP_ATTACK) вокруг самурая
        for (int i = 0; i < particleCount; i++) {
            double angle = i * angleStep;
            double spawnX = centerX + radius * Math.cos(angle) * 0.7;
            double spawnZ = (centerZ + 0.4) + radius * Math.sin(angle) * 0.7;

            worldServer.spawnParticle(
                EnumParticleTypes.SWEEP_ATTACK, 
                spawnX, centerY, spawnZ, 
                1, 0, 0, 0, 0.0D
            );
        }

        // Ищем цели в большом радиусе 4.5 блоков вокруг игрока
        AxisAlignedBB sweepBox = player.getEntityBoundingBox().grow(radius, 1.5, radius);
        List<EntityLivingBase> targets = worldServer.getEntitiesWithinAABB(EntityLivingBase.class, sweepBox);

        for (EntityLivingBase target : targets) {
            if (target != player) {
                // Наносим урон
                target.attackEntityFrom(DamageSource.causePlayerDamage(player), 12.0F);
                
                // Сильное круговое расталкивание (отбрасываем врагов назад от игрока)
                double dx = target.posX - player.posX;
                double dz = target.posZ - player.posZ;
                target.knockBack(player, 1.5F, -dx, -dz);
            }
        }
    }
}
