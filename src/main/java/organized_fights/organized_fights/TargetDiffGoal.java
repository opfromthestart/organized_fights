package organized_fights.organized_fights;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.world.Difficulty;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;


public class TargetDiffGoal extends TrackTargetGoal {
    boolean isPhantom;
    Predicate<LivingEntity> pred;

    public TargetDiffGoal(MobEntity mob) {
        this(mob, new DiffNamePredicate(mob));
    }
    public TargetDiffGoal(MobEntity mob, Predicate<LivingEntity> pred) {
        super(mob, false, false);
        isPhantom = mob instanceof PhantomEntity;
        this.pred = pred;
        //System.out.println(mob.getClass().toString());
    }

    public static class DiffNamePredicate implements Predicate<LivingEntity> {
        MobEntity mob;

        public DiffNamePredicate(MobEntity mob) {
            this.mob = mob;
        }

        @Override
        public boolean test(LivingEntity target) {
            Text targetName = target.getCustomName();
            Text thisName = mob.getCustomName();

            if (thisName != null && thisName.asTruncatedString(64).contains("*")) {
                return true;
            }
            if (targetName != null && thisName != null) {
                String targetStr = targetName.asTruncatedString(64);
                String thisStr = thisName.asTruncatedString(64);
                //System.out.println("Names are "+targetStr+", "+thisStr);
                //System.out.println("Fight");
                if (thisStr.contains("-") && thisStr.equals(targetStr)) {
                    return true;
                }
                if (!Objects.equals(targetStr, thisStr)) {
                    return true;
                }
            }
            if (thisName != null && thisName.asTruncatedString(64).contains("+")) {
                return targetName == null || !Objects.equals(targetName.asTruncatedString(64), thisName.asTruncatedString(64));
            }
            return false;
        }
    }

    protected void findClosestTarget() {
        if (target != null) {
            if (!target.isAlive()) {
                target = null;
            }
            else if (target.world == null || target.world != mob.world) {
                target = null;
            }
        }
        if (target == null) {
            Box searchBox = this.mob.getBoundingBox().expand(this.getFollowRange());
            if (isPhantom) {
                //System.out.println("Phantom search");
                searchBox = this.mob.getBoundingBox().expand(16D, 64D, 16D);
            }
            List<MobEntity> mobs = this.mob.world.getEntitiesByClass(MobEntity.class, searchBox, (livingEntity) -> {
                return true;
            });
            mobs.remove(this.mob);
            double min_dist = Double.MAX_VALUE;
            for (MobEntity mob : mobs) {
                if (this.mob.distanceTo(mob) < min_dist && pred.test(mob)) {
                    min_dist = this.mob.distanceTo(mob);
                    this.mob.setTarget(mob);
                }
            }
            /*
            if (isPhantom) {
                System.out.println("Test");
            }
            if (isPhantom && this.target != null) {
                System.out.println(this.target.toString());
            }

             */
        }
        //this.target = this.mob.world.getClosestEntity(mobs, pred, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());

    }


    @Override
    public boolean canStart() {
        this.findClosestTarget();
        return true;
    }

    public static boolean test(LivingEntity baseEntity, LivingEntity targetEntity, boolean attackable, boolean respectsVisibility) {
        if (baseEntity == targetEntity) {
            return false;
        } else if (!targetEntity.isPartOfGame()) {
            return false;
        } else {
            if (baseEntity == null) {
                return !attackable || (targetEntity.canTakeDamage() && targetEntity.world.getDifficulty() != Difficulty.PEACEFUL);
            } else {
                if (attackable && (!baseEntity.canTarget(targetEntity) || !baseEntity.canTarget(targetEntity.getType()) || baseEntity.isTeammate(targetEntity))) {
                    return false;
                }

                if (respectsVisibility && baseEntity instanceof MobEntity mobEntity) {
                    return mobEntity.getVisibilityCache().canSee(targetEntity);
                }
            }

            return true;
        }
    }
}
