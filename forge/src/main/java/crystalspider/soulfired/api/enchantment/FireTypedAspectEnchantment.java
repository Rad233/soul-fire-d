package crystalspider.soulfired.api.enchantment;

import crystalspider.soulfired.api.FireManager;
import crystalspider.soulfired.api.type.FireTypeChanger;
import crystalspider.soulfired.api.type.FireTyped;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.FireAspectEnchantment;

/**
 * Fire Aspect Enchantment sensitive to the fire type.
 */
public class FireTypedAspectEnchantment extends FireAspectEnchantment implements FireTyped {
  /**
   * Mod Id.
   */
  private final String modId;
  /**
   * Fire Id.
   */
  private final String fireId;

  private final boolean isTreasure;
  private final boolean isCurse;
  private final boolean isTradeable;
  private final boolean isDiscoverable;

  /**
   * @param modId
   * @param fireId
   * @param rarity
   * @param isTreasure
   * @param isCurse
   * @param isTradeable
   * @param isDiscoverable
   */
  public FireTypedAspectEnchantment(String modId, String fireId, Rarity rarity, boolean isTreasure, boolean isCurse, boolean isTradeable, boolean isDiscoverable) {
    super(rarity, EquipmentSlot.MAINHAND);
    this.modId = modId;
    this.fireId = FireManager.sanitizeFireId(fireId);
    this.isTreasure = isTreasure;
    this.isCurse = isCurse;
    this.isTradeable = isTradeable;
    this.isDiscoverable = isDiscoverable;
  }

  /**
   * @param modId
   * @param fireId
   * @param rarity
   */
  public FireTypedAspectEnchantment(String modId, String fireId, Rarity rarity) {
    this(modId, fireId, rarity, true, false, true, true);
  }

  @Override
  public final void doPostAttack(LivingEntity attacker, Entity target, int level) {
    if (!attacker.level.isClientSide) {
      target.setSecondsOnFire(level * 4);
    }
    ((FireTypeChanger) target).setFireId(FireManager.ensureFireId(fireId));
  }

  @Override
  public final boolean checkCompatibility(Enchantment enchantment) {
    return super.checkCompatibility(enchantment) && !(enchantment instanceof FireAspectEnchantment) && !FireManager.getFireAspects().contains(enchantment);
  }

  @Override
  public final boolean isTreasureOnly() {
    return isTreasure;
  }

  @Override
  public final boolean isCurse() {
    return isCurse;
  }

  @Override
  public final boolean isTradeable() {
    return isTradeable;
  }

  @Override
  public final boolean isDiscoverable() {
    return isDiscoverable;
  }

  @Override
  public final String getFireId() {
    return fireId;
  }
  
  /**
   * Returns this {@link #modId}.
   * 
   * @return this {@link #modId}.
   */
  public final String getModId() {
    return modId;
  }
}
