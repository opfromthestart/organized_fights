package organized_fights.organized_fights.wrap_interface;

import net.minecraft.entity.LivingEntity;

public interface HasTarget {
    boolean invokeCanTarget(LivingEntity le);
}
