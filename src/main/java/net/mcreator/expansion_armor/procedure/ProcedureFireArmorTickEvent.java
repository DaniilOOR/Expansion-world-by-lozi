package net.mcreator.expansion_armor.procedure;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;

import net.mcreator.expansion_armor.item.ItemFire;
import net.mcreator.expansion_armor.ElementsExpansionarmor;

import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;

@ElementsExpansionarmor.ModElement.Tag
public class ProcedureFireArmorTickEvent extends ElementsExpansionarmor.ModElement {

	public ProcedureFireArmorTickEvent(ElementsExpansionarmor instance) {
		super(instance, 283);
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

		boolean hasHelmet = player.inventory.armorInventory.get(3).getItem() == ItemFire.helmet;
        boolean hasBody   = player.inventory.armorInventory.get(2).getItem() == ItemFire.body;
        boolean hasLegs   = player.inventory.armorInventory.get(1).getItem() == ItemFire.legs;
        boolean hasBoots  = player.inventory.armorInventory.get(0).getItem() == ItemFire.boots;

        if (hasHelmet && hasBody && hasLegs && hasBoots) {
        	
            PotionEffect activeEffect = player.getActivePotionEffect(MobEffects.FIRE_RESISTANCE);

            if (activeEffect == null || activeEffect.getAmplifier() < 1 || activeEffect.getDuration() <= 20) {
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 100, 1, false, false));
            }
        }
	}
}
