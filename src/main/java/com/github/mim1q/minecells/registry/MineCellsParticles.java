package com.github.mim1q.minecells.registry;

import com.github.mim1q.minecells.MineCells;
import com.github.mim1q.minecells.particle.ChargeParticle;
import com.github.mim1q.minecells.particle.ExplosionParticle;
import com.github.mim1q.minecells.particle.ProtectorParticle;
import com.github.mim1q.minecells.particle.SpeckleParticle;
import com.github.mim1q.minecells.particle.colored.ColoredParticle;
import com.github.mim1q.minecells.particle.colored.ColoredParticleType;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.registry.Registry;

public class MineCellsParticles {

  public static final DefaultParticleType AURA = FabricParticleTypes.simple();
  public static final DefaultParticleType EXPLOSION = FabricParticleTypes.simple();
  public static final DefaultParticleType PROTECTOR = FabricParticleTypes.simple();
  public static final DefaultParticleType CHARGE = FabricParticleTypes.simple();
  public static final ColoredParticleType SPECKLE = ColoredParticleType.create();

  public static void init() {
    Registry.register(Registry.PARTICLE_TYPE, MineCells.createId("aura"), AURA);
    Registry.register(Registry.PARTICLE_TYPE, MineCells.createId("explosion"), EXPLOSION);
    Registry.register(Registry.PARTICLE_TYPE, MineCells.createId("protector"), PROTECTOR);
    Registry.register(Registry.PARTICLE_TYPE, MineCells.createId("charge"), CHARGE);
    Registry.register(Registry.PARTICLE_TYPE, MineCells.createId("speckle"), SPECKLE);
  }

  public static void initClient() {
    ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
      registry.register(MineCells.createId("particle/aura"));
      registry.register(MineCells.createId("particle/explosion"));
      registry.register(MineCells.createId("particle/protector"));
      registry.register(MineCells.createId("particle/charge"));
    });

    ParticleFactoryRegistry.getInstance().register(AURA, FlameParticle.Factory::new);
    ParticleFactoryRegistry.getInstance().register(EXPLOSION, ExplosionParticle.Factory::new);
    ParticleFactoryRegistry.getInstance().register(PROTECTOR, ProtectorParticle.Factory::new);
    ParticleFactoryRegistry.getInstance().register(CHARGE, ChargeParticle.Factory::new);
    ParticleFactoryRegistry.getInstance().register(SPECKLE, ColoredParticle.createFactory(SpeckleParticle::new));
  }
}
