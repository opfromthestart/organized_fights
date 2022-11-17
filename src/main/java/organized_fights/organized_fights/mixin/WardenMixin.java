package organized_fights.organized_fights.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.WardenAngerManager;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WardenEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import organized_fights.organized_fights.TargetDiffGoal;

@Mixin(WardenEntity.class)
public class WardenMixin {
    @Inject(at=@At("RETURN"), method = "isValidTarget", cancellable = true)
    public void main_angry(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        WardenEntity self = (WardenEntity) (Object) this;
        if (entity instanceof MobEntity target) {
            TargetDiffGoal.DiffNamePredicate pred = new TargetDiffGoal.DiffNamePredicate(self);
            if (pred.test(target)) {
                cir.setReturnValue(true);
            }
        }

    }
}
