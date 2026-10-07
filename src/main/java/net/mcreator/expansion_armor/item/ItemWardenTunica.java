
package net.mcreator.expansion_armor.item;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.TextFormatting;
import java.util.List;
import javax.annotation.Nullable;

import net.mcreator.expansion_armor.creativetab.TabStrangearmor;
import net.mcreator.expansion_armor.ElementsExpansionarmor;

@ElementsExpansionarmor.ModElement.Tag
public class ItemWardenTunica extends ElementsExpansionarmor.ModElement {
	@GameRegistry.ObjectHolder("expansion_armor:wardentunicahelmet")
	public static final Item helmet = null;
	@GameRegistry.ObjectHolder("expansion_armor:wardentunicabody")
	public static final Item body = null;
	@GameRegistry.ObjectHolder("expansion_armor:wardentunicalegs")
	public static final Item legs = null;
	@GameRegistry.ObjectHolder("expansion_armor:wardentunicaboots")
	public static final Item boots = null;

	@SideOnly(Side.CLIENT)
	private static class ModelHolder {
		private static ModelBiped INSTANCE_ARMOR = null;
		private static ItemWardenTunica.Modele TUNICA_MODEL = null;
	}
	
	public ItemWardenTunica(ElementsExpansionarmor instance) {
		super(instance, 287);
	}

	@Override
	public void initElements() {
		ItemArmor.ArmorMaterial enuma = EnumHelper.addArmorMaterial("WARDENTUNICA", "expansion_armor:tunica", 25, new int[]{2, 5, 6, 2}, 20,
				(net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_diamond")),
				0f);
		elements.items.add(() -> new ItemArmor(enuma, 0, EntityEquipmentSlot.CHEST){
			@Override
			public void setDamage(ItemStack stack, int damage) {
				super.setDamage(stack, 0);
			}

			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable net.minecraft.world.World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
				super.addInformation(stack, worldIn, tooltip, flagIn);
				
				float storedDamage = 0.0F;
				
				// Проверяем, есть ли NBT у предмета, и вытягиваем значение
				if (stack.hasTagCompound() && stack.getTagCompound().hasKey("tunicaStoredDamage")) {
					storedDamage = stack.getTagCompound().getFloat("tunicaStoredDamage");
				}
				
				tooltip.add(""); // Пустая строка для читаемости
				
				if (storedDamage >= 100.0F) {
					tooltip.add(TextFormatting.WHITE + "Stored Resonance: " + TextFormatting.RED + "100.0 / 100.0");
				} else {
					String damageText = String.format("%.1f", storedDamage);
					tooltip.add(TextFormatting.WHITE + "Stored Resonance: " + TextFormatting.AQUA + damageText + " / 100.0");
				}
			}
			
			@Override
			@SideOnly(Side.CLIENT)
			public ModelBiped getArmorModel(EntityLivingBase living, ItemStack stack, EntityEquipmentSlot slot, ModelBiped defaultModel) {
				// Создаем объекты модели один раз за всю игру, если их еще нет
				if (ModelHolder.INSTANCE_ARMOR == null) {
					ModelHolder.INSTANCE_ARMOR = new ModelBiped();
					ModelHolder.TUNICA_MODEL = new ItemWardenTunica.Modele();
				}

				ModelBiped armorModel = ModelHolder.INSTANCE_ARMOR;
				
				// Заменяем тело на нашу статическую 3D модель из холдера
				armorModel.bipedBody = ModelHolder.TUNICA_MODEL.body;
				
				// Переносим базовые состояния существа (анимации)
				armorModel.isSneak = living.isSneaking();
				armorModel.isRiding = living.isRiding();
				armorModel.isChild = living.isChild();
				
				return armorModel;
			}

			// Указываем путь к текстуре нашей 3D модели
			@Override
			public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
				return "expansion_armor:textures/models/armor/tunica_layer_1.png";
			}
		}.setUnlocalizedName("wardentunicabody").setRegistryName("wardentunicabody").setCreativeTab(TabStrangearmor.tab));
		
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(body, 0, new ModelResourceLocation("expansion_armor:wardentunicabody", "inventory"));
	}

	public static class Modele extends ModelBase {
		public final ModelRenderer body;
		
		public Modele() {
			this.textureWidth = 64;   
			this.textureHeight = 32;  
			
			this.body = new ModelRenderer(this);
			this.body.setRotationPoint(0.0F, 0.0F, 0.0F); 
			
			this.body.cubeList.add(new ModelBox(this.body, 13, 12, -5.0F, 0.0F, -3.0F, 10, 14, 6, 0.0F, true));
		}
		
		@Override
		public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
			this.body.render(f5); 
		}
		
		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x; 
			modelRenderer.rotateAngleY = y; 
			modelRenderer.rotateAngleZ = z; 
		}
		
		@Override
		public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
			super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
		}
	}
}
