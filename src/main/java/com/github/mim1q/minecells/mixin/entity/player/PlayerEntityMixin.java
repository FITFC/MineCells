package com.github.mim1q.minecells.mixin.entity.player;

import com.github.mim1q.minecells.accessor.PlayerEntityAccessor;
import com.github.mim1q.minecells.entity.nonliving.CellEntity;
import com.github.mim1q.minecells.entity.player.MineCellsPortalData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("WrongEntityDataParameterClass")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccessor {

  private static final TrackedData<Integer> CELL_AMOUNT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
  private static final TrackedData<String> LAST_DIMENSION_TRANSLATION_KEY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
  private int kingdomPortalCooldown = 0;
  private final MineCellsPortalData mineCellsPortalData = new MineCellsPortalData(this);


  protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
    super(entityType, world);
  }

  @Inject(method = "initDataTracker", at = @At("TAIL"))
  public void initDataTracker(CallbackInfo ci) {
    this.dataTracker.startTracking(CELL_AMOUNT, 0);
    this.dataTracker.startTracking(LAST_DIMENSION_TRANSLATION_KEY, "dimension.minecraft.overworld");
  }

  public int getCells() {
    return this.dataTracker.get(CELL_AMOUNT);
  }

  public void setCells(int amount) {
    this.dataTracker.set(CELL_AMOUNT, amount);
  }

  @Inject(method = "tick", at = @At("TAIL"))
  public void tick(CallbackInfo ci) {
    if (kingdomPortalCooldown > 0) {
      kingdomPortalCooldown--;
    }
  }

  @Inject(method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
  protected void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
    nbt.putInt("cells", this.getCells());
    nbt.putInt("kingdomPortalCooldown", kingdomPortalCooldown);
    nbt.put("mineCellsPortalData", this.mineCellsPortalData.toNbt());

  }

  @Inject(method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
  protected void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
    this.setCells(nbt.getInt("cells"));
    kingdomPortalCooldown = nbt.getInt("kingdomPortalCooldown");
    this.mineCellsPortalData.fromNbt(nbt.getCompound("mineCellsPortalData"));
  }

  public void setKingdomPortalCooldown(int cooldown) {
    kingdomPortalCooldown = cooldown;
  }

  public int getKingdomPortalCooldown() {
    return kingdomPortalCooldown;
  }

  public boolean canUseKingdomPortal() {
    return kingdomPortalCooldown == 0;
  }

  @Override
  protected void drop(DamageSource source) {
    super.drop(source);
    int amount = this.getCells() / 2;
    if (amount > 0) {
      CellEntity.spawn(this.world, this.getPos(), amount);
    }
  }

  @Override
  public MineCellsPortalData getMineCellsPortalData() {
    return this.mineCellsPortalData;
  }

  @Override
  public void setLastDimensionTranslationKey(String key) {
    this.dataTracker.set(LAST_DIMENSION_TRANSLATION_KEY, key);
  }

  @Override
  public String getLastDimensionTranslationKey() {
    return this.dataTracker.get(LAST_DIMENSION_TRANSLATION_KEY);
  }
}
