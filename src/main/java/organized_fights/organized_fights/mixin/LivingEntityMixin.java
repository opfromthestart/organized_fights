package organized_fights.organized_fights.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import organized_fights.organized_fights.wrap_interface.HasTarget;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements HasTarget {
    @Inject(at=@At("HEAD"), method="canTarget(Lnet/minecraft/entity/EntityType;)Z", cancellable = true)
    public void allTypes(EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    // Cannot attack creative players and dead mobs
    @Inject(at=@At("HEAD"), method="canTarget(Lnet/minecraft/entity/LivingEntity;)Z", cancellable = true)
    public void allTypes(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!((target instanceof PlayerEntity) && ((PlayerEntity)target).isCreative()) && target.isAlive());
    }

    // Resets the current target if a mob takes damage
    // Not the same as normal revenge, as it just retargets the closest mob, not the one that attacked it
    @Inject(at=@At("HEAD"),method="damage")
    public void resetTargetOnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity thi = (LivingEntity) (Object) this;
        if (thi instanceof MobEntity) {
            ((MobEntity) thi).setTarget(null);
        }
    }

    @Invoker
    public abstract boolean invokeCanTarget(LivingEntity e);
}
