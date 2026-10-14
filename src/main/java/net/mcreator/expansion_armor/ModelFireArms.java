package net.mcreator.expansion_armor.client;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ModelFireArms extends ModelBase {
	private static final ResourceLocation BLAZE_TEXTURE = new ResourceLocation("expansion_armor:textures/blocks/custom_blaze.png");
    private final ModelRenderer[] blazeSticks = new ModelRenderer[12];
    private float r3 = 7.0F;

    public ModelFireArms() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        
        for (int i = 0; i < this.blazeSticks.length; i++) {
		    this.blazeSticks[i] = new ModelRenderer(this, 0, 16);
		    this.blazeSticks[i].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
        }
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    	
        if (!(entityIn instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entityIn;

        float ySpeed = 1.0F;
        if (player.world.isRainingAt(player.getPosition())) {
            float rainStrength = player.world.getRainStrength(1.0F);
            ySpeed += 1.5F * rainStrength;
        }

        float f1 = ageInTicks * (float)Math.PI * -0.1F;
        float f2 = 0.7853982F + ageInTicks * (float)Math.PI * 0.03F;
        float f3 = 0.47123894F + ageInTicks * (float)Math.PI * -0.05F;

        float y1 = -5.0F, y2 = 0.0F, y3 = 13.1F;
        float r1 = 14.0F, r2 = 12.0F;

        if (player.isElytraFlying()) {
            y1 += 3.0F; 
            y2 += 4.0F; 
            y3 += 0.1F;
            if (this.r3 <= 9.0F) this.r3 += 0.1F;
        } else if (this.r3 >= 7.0F) {
            this.r3 -= 0.1F; 
		}

    	for (int i = 0; i < 4; i++) {
            this.blazeSticks[i].rotationPointY = y1 + MathHelper.cos(((i * 2) + ageInTicks) * 0.25F) * ySpeed;
            this.blazeSticks[i].rotationPointX = MathHelper.cos(f1) * r1 - 1.0F;
            this.blazeSticks[i].rotationPointZ = MathHelper.sin(f1) * r1;
            f1++;
        }
        
        // Орбита 2 (палочки 4-7)
        for (int j = 4; j < 8; j++) {
            this.blazeSticks[j].rotationPointY = y2 + MathHelper.cos(((j * 2) + ageInTicks) * 0.25F) * ySpeed;
            this.blazeSticks[j].rotationPointX = MathHelper.cos(f2) * r2 - 1.0F;
            this.blazeSticks[j].rotationPointZ = MathHelper.sin(f2) * r2;
            f2++;
        }
        
        // Орбита 3 (палочки 8-11)
        for (int k = 8; k < 12; k++) {
            this.blazeSticks[k].rotationPointY = y3 + MathHelper.cos((k * 1.5F + ageInTicks) * 0.5F) * ySpeed;
            this.blazeSticks[k].rotationPointX = MathHelper.cos(f3) * this.r3 - 1.0F;
            this.blazeSticks[k].rotationPointZ = MathHelper.sin(f3) * this.r3;
            f3++;
        }

       // Отрисовка модели в мире
        GlStateManager.pushMatrix();
        
        Minecraft.getMinecraft().getTextureManager().bindTexture(BLAZE_TEXTURE);
        
        for (ModelRenderer stick : this.blazeSticks) {
            stick.render(scale);
        }
        
        GlStateManager.popMatrix();
    }
}
