package organized_fights.organized_fights.mixin.specific;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SlimeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import organized_fights.organized_fights.TargetDiffGoal;
import organized_fights.organized_fights.wrap_interface.HasTarget;

@Mixin(SlimeEntity.class)
public abstract class SlimeMixin {
    @Inject(at=@At("HEAD"), method="pushAwayFrom")
    public void damageAll(Entity entity, CallbackInfo ci) {
        HasTarget thi = (HasTarget) this;

        TargetDiffGoal.DiffNamePredicate pred = new TargetDiffGoal.DiffNamePredicate((SlimeEntity)(Object)this);
        if (this.invokeCanAttack() && pred.test((LivingEntity) entity)) {
            this.invokeDamage((LivingEntity)entity);
        }
    }

    @Invoker
    public abstract boolean invokeCanAttack();

    @Invoker
    public abstract void invokeDamage(LivingEntity le);
}
