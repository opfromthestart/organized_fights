package organized_fights.organized_fights.wrap_interface;

import net.minecraft.entity.ai.goal.GoalSelector;

public interface HasTargetSelector {
    GoalSelector getTargetSelector();

    GoalSelector getGoalSelector();
}
