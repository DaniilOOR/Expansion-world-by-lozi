package net.mcreator.expansion_armor.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mcreator.expansion_armor.item.ItemFire;
import net.minecraft.client.renderer.GlStateManager;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = "expansion_armor", value = Side.CLIENT)
public class ClientStuf {
    private static boolean alreadyAttached = false;
    private static final ModelFireArms armsModel = new ModelFireArms();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !alreadyAttached) {
            java.util.Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
            
            if (skinMap != null && skinMap.get("default") != null) {
                for (RenderPlayer playerRenderer : skinMap.values()) {
                    playerRenderer.addLayer(new LayerRenderer<AbstractClientPlayer>() {
                        @Override
                        public void doRenderLayer(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
                            
                            // Ультра-скорость: Если шлема нет, Java не тратит ресурсы на проверку остальных 3-х слотов!
                            if (player.inventory.armorInventory.get(3).getItem() == ItemFire.helmet &&
                                player.inventory.armorInventory.get(2).getItem() == ItemFire.body &&
                                player.inventory.armorInventory.get(1).getItem() == ItemFire.legs &&
                                player.inventory.armorInventory.get(0).getItem() == ItemFire.boots) {
                                
                                GlStateManager.pushMatrix();
                                if (player.isSneaking()) {
                                    GlStateManager.translate(0.0F, 0.125F, 0.0F);
                                }
                                
                                armsModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                                GlStateManager.popMatrix();
                            }
                        }

                        @Override
                        public boolean shouldCombineTextures() {
                            return false;
                        }
                    });
                }
                alreadyAttached = true;
            }
        }
    }
}
