package crystalspider.soulfired.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.vertex.PoseStack;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.client.FireClientManager;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent.OverlayType;

/**
 * Injects into {@link ScreenEffectRenderer} to alter Fire behavior for consistency.
 */
@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
  /**
   * Redirects the call to {@link ForgeHooksClient#renderFireOverlay(Player, PoseStack)} inside the method {@link ScreenEffectRenderer#renderScreenEffect(Minecraft, PoseStack)}.
   * <p>
   * If the player is on fire, posts the correct event for the screen overlay kind.
   * 
   * @param player
   * @param poseStack
   * @return the result of the posted event.
   */
  @Redirect(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/ForgeHooksClient;renderFireOverlay(Lnet/minecraft/world/entity/player/Player;Lcom/mojang/blaze3d/vertex/PoseStack;)Z"))
  private static boolean redirectRenderFireOverlay(Player player, PoseStack poseStack) {
    String fireId = ((FireTyped) player).getFireId();
    if (FireManager.isFireId(fireId)) {
      return ForgeHooksClient.renderBlockOverlay(player, poseStack, OverlayType.FIRE, FireManager.getSourceBlock(fireId), player.blockPosition());
    }
    return ForgeHooksClient.renderFireOverlay(player, poseStack);
  }

  /**
   * Modifies the assignment value returned by {@link Material#sprite()} in the method {@link ScreenEffectRenderer#renderFire(Minecraft, PoseStack)}.
   * <p>
   * Assigns the correct sprite for the fire type the player is burning from.
   * 
   * @param value original sprite returned by the modified method.
   * @param minecraft Minecraft client.
   * @param poseStack
   * @return {@link TextureAtlasSprite} to assign.
   */
  @ModifyVariable(method = "renderFire", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
  private static TextureAtlasSprite onRenderFire(TextureAtlasSprite value, Minecraft minecraft, PoseStack poseStack) {
    String fireId = ((FireTyped) minecraft.player).getFireId();
    if (FireManager.isFireId(fireId)) {
      return FireClientManager.getSprite1(fireId);
    }
    return value;
  }
}
