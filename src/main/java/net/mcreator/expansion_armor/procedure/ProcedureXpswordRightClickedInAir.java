package net.mcreator.expansion_armor.procedure;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;

import net.mcreator.expansion_armor.ElementsExpansionarmor;

import java.util.HashMap;

@ElementsExpansionarmor.ModElement.Tag
public class ProcedureXpswordRightClickedInAir extends ElementsExpansionarmor.ModElement {
	public ProcedureXpswordRightClickedInAir(ElementsExpansionarmor instance) {
		super(instance, 253);
	}

	public static void executeProcedure(java.util.HashMap<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			return;
		}
		if (dependencies.get("itemstack") == null) {
			return;
		}
		
		Entity entity = (Entity) dependencies.get("entity");
		ItemStack itemstack = (ItemStack) dependencies.get("itemstack");

		// Работаем строго на сервере и только с игроком
		if (!(entity instanceof EntityPlayer) || entity.world.isRemote) {
			return;
		}

		EntityPlayer player = (EntityPlayer) entity;

		if (!player.isSneaking()) {
			return;
		}

		if (!itemstack.isEmpty()) {
			int maxDamage = itemstack.getMaxDamage(); 
			int currentDamage = itemstack.getItemDamage(); 
			
			// Находим, сколько единиц прочности осталось у меча
			int remainingDurability = Math.max(0, maxDamage - currentDamage);
			
			// Пропорциональный расчет опыта: (Оставшаяся прочность / 30) * 1395
			// Используем double во время деления, чтобы Java не округлил дробь до нуля
			double durabilityPercent = (double) remainingDurability / (double) maxDamage;
			int experienceToGive = (int) Math.round(durabilityPercent * 1395.0D);

			// Выдаем рассчитанный опыт игроку (только если значение больше нуля)
			if (experienceToGive > 0) {
				player.addExperience(experienceToGive);
			}

			// Проигрываем ванильный звук успешного поглощения опыта
			player.world.playSound(null, player.posX, player.posY, player.posZ, 
				SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.7F, 1.0F);

			// Моментально ломаем меч: уменьшаем количество в стаке на 1
			itemstack.shrink(1);
			
			// Воспроизводим звук поломки предмета, если игрок находится на сервере
			if (player.world instanceof WorldServer) {
				player.world.playSound(null, player.posX, player.posY, player.posZ, 
					SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F);
			}
		}
	}
}
