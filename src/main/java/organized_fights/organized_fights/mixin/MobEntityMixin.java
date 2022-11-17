package organized_fights.organized_fights.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import organized_fights.organized_fights.VulnerablePredicate;
import organized_fights.organized_fights.wrap_interface.HasTargetSelector;
import organized_fights.organized_fights.TargetDiffGoal;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements HasTargetSelector {

    @Accessor
    public abstract GoalSelector getTargetSelector();

    @Accessor
    public abstract GoalSelector getGoalSelector();

    // Prevents ghast immunity to targeting, which is weird
    @Inject(at=@At("HEAD"), method = "canTarget", cancellable = true)
    public void ghastsDie(EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    public void init(CallbackInfo ci) {
        MobEntity t = (MobEntity) (Object) this;
        //System.out.println(this.getClass().toString());
        GoalSelector s = this.getTargetSelector();
        s.add(0, new TargetDiffGoal(t, new VulnerablePredicate(t)));
    }


}

