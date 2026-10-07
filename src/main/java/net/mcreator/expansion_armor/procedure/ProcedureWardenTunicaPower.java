package net.mcreator.expansion_armor.procedure;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.WorldServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.mcreator.expansion_armor.item.ItemWardenTunica; // Проверьте имя класса вашей брони
import net.mcreator.expansion_armor.ElementsExpansionarmor;

@ElementsExpansionarmor.ModElement.Tag
public class ProcedureWardenTunicaPower extends ElementsExpansionarmor.ModElement {

    public ProcedureWardenTunicaPower(ElementsExpansionarmor instance) {
        super(instance, 285);
    }

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {

        if (!(event.getEntityLiving() instanceof EntityPlayer) || event.getEntityLiving().world.isRemote) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        ItemStack chestplate = player.inventory.armorInventory.get(2);

        if (player.inventory.armorInventory.get(2).getItem() != ItemWardenTunica.body) {
            return;
        }

		if (event.getSource().getTrueSource() == null || !(event.getSource().getTrueSource() instanceof EntityLivingBase)) {
            return;
        }
		
        float incomingDamage = event.getAmount();
        
        // Поглощаем 30% урона (игрок получит только 70%)
        float absorbedDamage = incomingDamage * 0.3F;


        // Записываем поглощенный урон в память NBT игрока (суммируем со старым)
        float currentStored = 0.0F;
        if (chestplate.hasTagCompound() && chestplate.getTagCompound().hasKey("tunicaStoredDamage")) {
            currentStored = chestplate.getTagCompound().getFloat("tunicaStoredDamage");
        }

		if (currentStored >= 100.0F) {return;}

		event.setAmount(incomingDamage - absorbedDamage);

        if (chestplate.getTagCompound() == null) {
            chestplate.setTagCompound(new NBTTagCompound());
        }
        chestplate.getTagCompound().setFloat("tunicaStoredDamage", Math.min(currentStored + absorbedDamage, 100.0F));

        // Эффект поглощения: тихий вибрирующий звук на игроке
        if (player.world instanceof WorldServer) {
            ((WorldServer) player.world).playSound(null, player.posX, player.posY, player.posZ, 
                SoundEvents.BLOCK_NOTE_CHIME, SoundCategory.PLAYERS, 0.5F, 0.5F);
        }
    }

    // 2. СОБЫТИЕ: Игрок бьет врага — высвобождаем весь накопленный урон
    @SubscribeEvent
    public void onPlayerAttack(LivingHurtEvent event) { // Используем Hurt для атакующего, чтобы изменить урон

        if (event.getSource().getTrueSource() == null || !(event.getSource().getTrueSource() instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
        ItemStack chestplate = player.inventory.armorInventory.get(2);

        if (chestplate.isEmpty() || chestplate.getItem() != ItemWardenTunica.body) {
            return;
        }
        
        float storedDamage = 0.0F;
        if (chestplate.hasTagCompound() && chestplate.getTagCompound().hasKey("tunicaStoredDamage")) {
            storedDamage = chestplate.getTagCompound().getFloat("tunicaStoredDamage");
        }

        if (storedDamage <= 0.0F) {
            return;
        }

        // Добавляем весь накопленный урон к текущей атаке игрока
        float currentDamage = event.getAmount();
        event.setAmount(currentDamage + storedDamage);


        chestplate.getTagCompound().setFloat("tunicaStoredDamage", 0.0F);

        // Эффекты взрыва накопленного звука при ударе
        if (!player.world.isRemote && player.world instanceof WorldServer) {
            WorldServer worldServer = (WorldServer) player.world;
            EntityLivingBase target = event.getEntityLiving();

            // Громкий пафосный звук высвобождения энергии Вардена
            worldServer.playSound(null, target.posX, target.posY, target.posZ, 
                SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.PLAYERS, 0.9F, 0.7F);

            // Спавним много магических критических частиц на цели
            worldServer.spawnParticle(
                EnumParticleTypes.CRIT_MAGIC, 
                target.posX, target.posY + (target.height / 2), target.posZ, 
                30,          // Количество частиц
                0.3D, 0.3D, 0.3D, // Радиус разлета
                0.1D         // Скорость
            );
        }
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }
}