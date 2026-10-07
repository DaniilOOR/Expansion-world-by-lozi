package net.mcreator.expansion_armor.procedure;

import net.minecraft.util.DamageSource;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.init.MobEffects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;

import net.mcreator.expansion_armor.ElementsExpansionarmor;

import java.util.HashMap;

@ElementsExpansionarmor.ModElement.Tag
public class ProcedureStrenght_1_katana extends ElementsExpansionarmor.ModElement {
	
	public ProcedureStrenght_1_katana(ElementsExpansionarmor instance) {
		super(instance, 176);
	}

	public static void executeProcedure(java.util.HashMap<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			System.err.println("Failed to load dependency entity for procedure Strenght_1_katana!");
			return;
		}
		if (dependencies.get("itemstack") == null) {
			System.err.println("Failed to load dependency itemstack for procedure Strenght_1_katana!");
			return;
		}

		Entity entity = (Entity) dependencies.get("entity");

		// Выполняем строго на сервере и только для живых существ
		if (!(entity instanceof EntityLivingBase) || entity.world.isRemote) {
			return;
		}

		EntityLivingBase livingEntity = (EntityLivingBase) entity;
		ItemStack itemstack = (ItemStack) dependencies.get("itemstack");

		if (!itemstack.isEmpty()) {
			itemstack.damageItem(1, livingEntity);
		}
		
		// 1. Наносим урон магией самому себе (6 единиц = 3 сердца), пробивая броню
		livingEntity.attackEntityFrom(DamageSource.MAGIC, 6.0F);

		// 2. Логика накопительного эффекта Силы без сброса времени
		int nextAmplifier = 0; // По умолчанию Сила I (индекс 0)
		int remainingDuration = 200; // Если эффекта нет, даем 10 секунд (200 тиков)

		// Проверяем текущий активный эффект силы
		PotionEffect activeStrength = livingEntity.getActivePotionEffect(MobEffects.STRENGTH);
		
		if (activeStrength != null) {
			// Если эффект есть, повышаем уровень на +1
			nextAmplifier = activeStrength.getAmplifier() + 1;
			// ВАЖНО: Запоминаем ОСТАВШЕЕСЯ время действия эффекта, чтобы оно не обновлялось на 10 сек
			remainingDuration = activeStrength.getDuration();
		}

		// Перенакладываем эффект с повышенным уровнем, сохраняя старый таймер
		livingEntity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, remainingDuration, nextAmplifier, false, false));
	}
}