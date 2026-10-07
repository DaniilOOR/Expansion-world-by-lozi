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

import net.mcreator.expansion_armor.ElementsExpansionarmor;

import java.util.HashMap;
import java.util.List;

@ElementsExpansionarmor.ModElement.Tag
public class ProcedureNaginataPower extends ElementsExpansionarmor.ModElement {

    public ProcedureNaginataPower(ElementsExpansionarmor instance) {
        super(instance, 290); // Кастомный ID вашей процедуры
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
        //ItemStack heldItem = player.getHeldItemMainhand();
        ItemStack heldItem = (ItemStack) dependencies.get("itemstack");

        long currentTime = worldServer.getTotalWorldTime();

        // =========================================================================
        // МЕХАНИКА 3: ЦУНАМИ ВОЗМЕЗДИЯ (Зажат Shift + ПКМ)
        // =========================================================================
        if (player.isSneaking()) {
            long tsunamiCooldownEnd = player.getEntityData().getLong("naginataTsunamiCooldownEnd");
            if (currentTime < tsunamiCooldownEnd) {
                return; // Способность еще перезаряжается
            }

            // Ставим КД на 20 секунд (400 тиков)
            player.getEntityData().setLong("naginataTsunamiCooldownEnd", currentTime + 400);
            player.getCooldownTracker().setCooldown(heldItem.getItem(), 400);

            // Анимация взмаха и звук грохота земли
            player.swingArm(EnumHand.MAIN_HAND);
            worldServer.playSound(null, player.posX, player.posY, player.posZ, 
                SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.PLAYERS, 1.2F, 0.5F);

            Vec3d lookVec = player.getLookVec();
            double yaw = Math.atan2(lookVec.z, lookVec.x);

            // Пускаем 3 волны веером по земле (центральная, левее и правее)
            double[] angles = {0.0, -0.3, 0.3};

            for (double angleOffset : angles) {
                double currentYaw = yaw + angleOffset;
                double dx = Math.cos(currentYaw);
                double dz = Math.sin(currentYaw);

                // Волна идет вперед на 14 блоков
                for (double d = 2.0; d <= 14.0; d += 1.0) {
                    double waveX = player.posX + dx * d;
                    double waveY = player.posY;
                    double waveZ = player.posZ + dz * d;

                    // Спавним частицы летящих камней и дыма из-под земли
                    worldServer.spawnParticle(EnumParticleTypes.BLOCK_DUST, waveX, waveY + 0.1, waveZ, 8, 0.2, 0.1, 0.2, 0.0D, 
                        net.minecraft.block.Block.getStateId(net.minecraft.init.Blocks.STONE.getDefaultState()));
                    worldServer.spawnParticle(EnumParticleTypes.SMOKE_LARGE, waveX, waveY + 0.3, waveZ, 2, 0.1, 0.1, 0.1, 0.01D);

                    // Зона поражения вокруг текущей точки волны
                    AxisAlignedBB waveBox = new AxisAlignedBB(waveX - 1.5, waveY - 1.0, waveZ - 1.5, waveX + 1.5, waveY + 3.0, waveZ + 1.5);
                    List<EntityLivingBase> targets = worldServer.getEntitiesWithinAABB(EntityLivingBase.class, waveBox);

                    for (EntityLivingBase target : targets) {
                        if (target != player) {
                            // Наносим тяжелый физический урон (20 единиц = 10 сердец)
                            target.attackEntityFrom(DamageSource.causePlayerDamage(player).setDamageBypassesArmor(), 20.0F);
                            
                            // ПОДБРАСЫВАЕМ врагов высоко вверх, как от подземного толчка!
                            target.motionY = 0.9D;
                            target.velocityChanged = true; // Принудительно обновляем скорость у клиентов
                        }
                    }
                }
            }
            return; // Завершаем выполнение, чтобы не сработала обычная атака
        }

        // =========================================================================
        // МЕХАНИКА 1: РАСКОЛ ПРОСТРАНСТВА (Обычный ПКМ)
        // =========================================================================
        long crackCooldownEnd = player.getEntityData().getLong("naginataCrackCooldownEnd");
        // Также проверяем, чтобы КД ульты (цунами) не наложилось на шторку
        if (currentTime < crackCooldownEnd || player.getCooldownTracker().hasCooldown(heldItem.getItem())) {
            return; 
        }

        // Ставим КД на 8 секунд (160 тиков)
        player.getEntityData().setLong("naginataCrackCooldownEnd", currentTime + 160);
        player.getCooldownTracker().setCooldown(heldItem.getItem(), 160);

        player.swingArm(EnumHand.MAIN_HAND);
        
        // Звук раскалывающегося стекла/воздуха
        worldServer.playSound(null, player.posX, player.posY, player.posZ, 
            SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.5F, 0.4F);

        // Рассчитываем эпицентр взрыва воздуха (на 4 блока перед лицом игрока)
        Vec3d lookVec = player.getLookVec();
        double centerX = player.posX + lookVec.x * 4.0;
        double centerY = player.posY + player.getEyeHeight() + lookVec.y * 4.0;
        double centerZ = player.posZ + lookVec.z * 4.0;

        // Спавним огромные взрывные частицы в эпицентре "раскола"
        worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, centerX, centerY, centerZ, 5, 0.5, 0.5, 0.5, 0.0D);
        for (int i = 0; i < 15; i++) {
            // Маленькие "трещины" вокруг взрыва
            worldServer.spawnParticle(EnumParticleTypes.CRIT_MAGIC, 
                centerX + (worldServer.rand.nextDouble() - 0.5) * 3.0,
                centerY + (worldServer.rand.nextDouble() - 0.5) * 3.0,
                centerZ + (worldServer.rand.nextDouble() - 0.5) * 3.0,
                1, 0, 0, 0, 0.0D
            );
        }

        // Ищем цели в радиусе 5 блоков вокруг эпицентра раскола
        AxisAlignedBB crackBox = new AxisAlignedBB(centerX - 5.0, centerY - 4.0, centerZ - 5.0, centerX + 5.0, centerY + 4.0, centerZ + 5.0);
        List<EntityLivingBase> targets = worldServer.getEntitiesWithinAABB(EntityLivingBase.class, crackBox);

        for (EntityLivingBase target : targets) {
            if (target != player) {
                // Магический урон (раскол воздуха прошивает щиты и игнорирует броню!)
                target.attackEntityFrom(DamageSource.causePlayerDamage(player).setMagicDamage().setDamageBypassesArmor(), 14.0F);
                
                // С силой расталкиваем врагов в стороны от центра раскола
                double targetDx = target.posX - centerX;
                double targetDz = target.posZ - centerZ;
                target.knockBack(player, 1.4F, -targetDx, -targetDz);
            }
        }
    }
}
