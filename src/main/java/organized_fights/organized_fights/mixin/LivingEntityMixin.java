package organized_fights.organized_fights.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//This pretty much does nothing
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at=@At("HEAD"), method="canTarget(Lnet/minecraft/entity/EntityType;)Z", cancellable = true)
    public void allTypes(EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(at=@At("HEAD"), method="canTarget(Lnet/minecraft/entity/LivingEntity;)Z", cancellable = true)
    public void allTypes(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!((target instanceof PlayerEntity) && ((PlayerEntity)target).isCreative()) && target.isAlive());
    }
}
