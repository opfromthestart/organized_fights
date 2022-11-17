package organized_fights.organized_fights.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import organized_fights.organized_fights.TargetDiffGoal;
import organized_fights.organized_fights.wrap_interface.HasTargetSelector;

@Mixin(Entity.class)
public abstract class EntityMixin {
    private static int tick_slow = 0;
    private static final int every = 20;

    @Inject(at=@At("HEAD"), method="isTeammate", cancellable = true)
    public void isTeam(Entity other, CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(false);
    }

    @Inject(at=@At("TAIL"), method="setCustomName")
    public void nameChange(Text name, CallbackInfo ci) {
        Entity thi = (Entity)(Object)this;
        if (thi instanceof MobEntity) {
            HasTargetSelector thi2 = (HasTargetSelector) this;
            thi2.getTargetSelector().clear();
            thi2.getTargetSelector().add(0, new TargetDiffGoal((MobEntity) thi));
        }
    }

    @Inject(at=@At("HEAD"), method="tick")
    public void tick(CallbackInfo ci) {
        tick_slow++;
        if (tick_slow%every != 0) {
            return;
        }
        Entity thi = (Entity)(Object)this;
        if (thi instanceof PhantomEntity) {
            HasTargetSelector thi2 = (HasTargetSelector) this;

                //for (PrioritizedGoal g : thi2.getGoalSelector().getGoals()) {
                    //System.out.print(g.getPriority()+" ");
                    //System.out.print(g.canStart() + " ");
                    //System.out.println(g.shouldContinue());
                //}

            /*
            if (((PhantomEntity) thi).getTarget() != null) {
                System.out.println(TargetDiffGoal.test((LivingEntity) thi, ((PhantomEntity) thi).getTarget(), true, true));
            }
            else {
                System.out.println("Null target");
            }
             */
        }
    }

}
