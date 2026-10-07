package net.mcreator.expansion_armor.procedure;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;

import net.mcreator.expansion_armor.item.ItemWarden;
import net.mcreator.expansion_armor.ExpansionarmorVariables;
import net.mcreator.expansion_armor.ElementsExpansionarmor;

@ElementsExpansionarmor.ModElement.Tag
public class ProcedureWardenpower extends ElementsExpansionarmor.ModElement {
    
    public ProcedureWardenpower(ElementsExpansionarmor instance) {
        super(instance, 282);
    }

    public static void executeProcedure(EntityPlayer player, LivingAttackEvent event) {
    	
        // 1. Проверяем истинный источник урона (True Source должен быть живым существом)
        if (event.getSource().getTrueSource() == null || !(event.getSource().getTrueSource() instanceof net.minecraft.entity.EntityLivingBase)) {
            return;
        }

		long currentTime = player.world.getTotalWorldTime();
        long cooldownEnd = player.getEntityData().getLong("wardenCooldownEnd");
		
        // 2. Проверяем кулдаун способности
        if (currentTime < cooldownEnd) {
            return;
        }

		
        boolean hasHelmet = player.inventory.armorInventory.get(3).getItem() == ItemWarden.helmet;
        boolean hasBody   = player.inventory.armorInventory.get(2).getItem() == ItemWarden.body;
        boolean hasLegs   = player.inventory.armorInventory.get(1).getItem() == ItemWarden.legs;
        boolean hasBoots  = player.inventory.armorInventory.get(0).getItem() == ItemWarden.boots;

        if (hasHelmet && hasBody && hasLegs && hasBoots) {

        	World world = player.world;
        	
			player.getEntityData().setLong("wardenCooldownEnd", currentTime + 200);
			
			if (!world.isRemote && world instanceof net.minecraft.world.WorldServer) {
				
			    net.minecraft.world.WorldServer wardenWorldServer = (net.minecraft.world.WorldServer) world;
			    
			    double centerX = player.posX;
                double centerY = player.posY;
                double centerZ = player.posZ;
				double radius = 1.0; 		  // радиус кольца частиц
				double radius_mobs = 4.0;     // радиус зоны поражения
				int count = 128;              // количетсво партиклов 
				double angleStep = 2 * Math.PI / count;

				
				for (int i = 0; i < count; i++) {
					double angle = i * angleStep;
					double spawnX = centerX + radius * Math.cos(angle);
					double spawnZ = centerZ + radius * Math.sin(angle);

					wardenWorldServer.spawnParticle(
					    net.minecraft.util.EnumParticleTypes.REDSTONE, 
					    spawnX, centerY + 0.2, spawnZ, 
					    0,        // count = 0 (обязательно для цветных частиц!)
					    0.0392D,  // Red (0.0)
					    0.3137D,  // Green (1.0)
					    0.3765D,  // Blue (1.0)
					    1.0D      // speed/brightness divider
					);
				}

				// 2. Найти цели вокруг в радиусе и нанести урон
				java.util.List<net.minecraft.entity.EntityLivingBase> targets =
						wardenWorldServer.getEntitiesWithinAABB(
							net.minecraft.entity.EntityLivingBase.class,
							player.getEntityBoundingBox().grow(radius_mobs)
						);

				for (net.minecraft.entity.EntityLivingBase target : targets) {
					// Проверяем, что цель не сам защищающийся игрок
					if (target != player) {
						// Наносим магический урон
						target.attackEntityFrom(net.minecraft.util.DamageSource.MAGIC, 10.0F);

						// Отбрасываем врагов от игрока назад (ударная волна)
						double dx = player.posX - target.posX;
						double dz = player.posZ - target.posZ;
						target.knockBack(player, 0.8F, dx, dz);
					}
				}
			}
		}
	}

	@SubscribeEvent
    public void onEntityAttacked(LivingAttackEvent event) {
        if (event != null && event.getEntity() instanceof EntityPlayer) {
            executeProcedure((EntityPlayer) event.getEntity(), event);
        }
    }

    // НОВОЕ СОБЫТИЕ: Перехватываем отбрасывание игрока строго на сервере
	@SubscribeEvent
	public void onEntityKnockback(LivingKnockBackEvent event) {
		if (event.getEntity() instanceof EntityPlayer && !event.getEntity().world.isRemote) {
			EntityPlayer player = (EntityPlayer) event.getEntity();

			boolean hasHelmet = player.inventory.armorInventory.get(3).getItem() == ItemWarden.helmet;
			boolean hasBody   = player.inventory.armorInventory.get(2).getItem() == ItemWarden.body;
			boolean hasLegs   = player.inventory.armorInventory.get(1).getItem() == ItemWarden.legs;
			boolean hasBoots  = player.inventory.armorInventory.get(0).getItem() == ItemWarden.boots;

			if (!(hasHelmet && hasBody && hasLegs && hasBoots)) {
				return; 
			}
			
			// Если активен флаг анти-отброса Вардена
			long currentTime = player.world.getTotalWorldTime();
            long cooldownEnd = player.getEntityData().getLong("wardenCooldownEnd");

            if (cooldownEnd - currentTime >= 200) {
                event.setCanceled(true);
            }
		}
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}
}
