package com.github.mim1q.minecells.client.render.conjunctivius;

import com.github.mim1q.minecells.MineCells;
import com.github.mim1q.minecells.client.render.model.conjunctivius.ConjunctiviusEntityModel;
import com.github.mim1q.minecells.entity.boss.ConjunctiviusEntity;
import com.github.mim1q.minecells.util.MathUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ConjunctiviusEyeFeatureRenderer extends FeatureRenderer<ConjunctiviusEntity, ConjunctiviusEntityModel>  {

  private final Identifier EYE_PINK = MineCells.createId("textures/entity/conjunctivius/eye_pink.png");

  private final ConjunctiviusEyeModel model;

  public ConjunctiviusEyeFeatureRenderer(FeatureRendererContext<ConjunctiviusEntity, ConjunctiviusEntityModel> context, ModelPart eyeRoot) {
    super(context);
    model = new ConjunctiviusEyeModel(eyeRoot);
  }

  @Override
  public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ConjunctiviusEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
    matrices.push();
    Entity player = MinecraftClient.getInstance().getCameraEntity();
    if (player != null) {
      Vec3d playerPos = player.getPos().add(0.0D, 1.5D, 0.0D);
      Vec3d entityPos = entity.getPos().add(0.0D, 1.25D, 0.0D);
      Vec3d diff = playerPos.subtract(entityPos);
      float rotation = entity.getRotationClient().y;
      Vec3d rotatedDiff = MathUtils.vectorRotateY(diff, rotation * MathHelper.RADIANS_PER_DEGREE + MathHelper.HALF_PI);

      float xOffset = (float) -rotatedDiff.x;
      float yOffset = (float) -rotatedDiff.y;

      xOffset = MathHelper.clamp(xOffset * 0.1F, -0.3F, 0.3F);
      yOffset = MathHelper.clamp(yOffset * 0.1F, -0.3F, 0.3F);

      matrices.translate(0.0F + xOffset, 0.2F + yOffset, -15.75F / 16.0F);
      RenderLayer renderLayer = this.model.getLayer(EYE_PINK);
      VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
      this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
      matrices.pop();
    }
  }

  public static class ConjunctiviusEyeModel extends EntityModel<ConjunctiviusEntity> {

    private final ModelPart eye;

    public ConjunctiviusEyeModel(ModelPart root) {
      this.eye = root.getChild("eye");
    }

    public static TexturedModelData getTexturedModelData() {
      ModelData modelData = new ModelData();
      ModelPartData modelPartData = modelData.getRoot();

      modelPartData.addChild("eye",
        ModelPartBuilder.create()
          .uv(0, 0)
          .cuboid(-5.5F, -5.5F, 0.0F, 11, 11, 0, new Dilation(0.01F)),
        ModelTransform.NONE
      );

      return TexturedModelData.of(modelData, 32, 16);
    }

    @Override
    public void setAngles(ConjunctiviusEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
      this.eye.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
  }
}
