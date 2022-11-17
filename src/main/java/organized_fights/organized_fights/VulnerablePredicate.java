package organized_fights.organized_fights;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.text.Text;

import java.util.function.Predicate;

public class VulnerablePredicate implements Predicate<LivingEntity> {
    MobEntity mob;

    public VulnerablePredicate(MobEntity mob) {
        this.mob = mob;
    }
    @Override
    public boolean test(LivingEntity target) {
        Text targetName = target.getCustomName();
        Text thisName = mob.getCustomName();

        return targetName != null && targetName.asTruncatedString(64).contains("_");
    }
}
