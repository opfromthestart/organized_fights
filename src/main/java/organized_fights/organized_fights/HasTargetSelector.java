package organized_fights.organized_fights;

import net.minecraft.entity.ai.goal.GoalSelector;

public interface HasTargetSelector {
    public abstract GoalSelector getTargetSelector();

    public abstract GoalSelector getGoalSelector();
}
