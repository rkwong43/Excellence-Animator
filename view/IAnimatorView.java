package cs3500.animator.view;

import cs3500.animator.model.IROAnimatorModel;

/**
 * Represents the view for an animator. Has the ability to render the animation in a specified form.
 */
public interface IAnimatorView {

  /**
   * Renders the animation according to the particular view's purpose at the particular tick.
   *
   * @param model Read-only model to grab shapes from
   * @param tick The tick to get the shapes at
   * @throws IllegalArgumentException if the given model is null or the tick is negative
   */
  void render(IROAnimatorModel model, int tick) throws IllegalArgumentException;

  /**
   * Returns true if the view has no more items to animate.
   *
   * @return Boolean whether the view has any more items to animate
   */
  boolean finished();
}
