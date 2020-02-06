package cs3500.animator.view;

import cs3500.animator.model.IROAnimatorModel;

/**
 * Represents an animation in text form.
 */
public class AnimatorTextView extends AAnimatorView implements IAnimatorView {

  private boolean finished = false;

  /**
   * Constructs an animator that will output animation in the form of text.
   *
   * @param ap {@code Appendable} to represent output
   * @throws IllegalArgumentException if the given parameter is null
   */
  public AnimatorTextView(Appendable ap) {
    super(ap);
  }

  /**
   * Renders the animation in text form.
   *
   * @param model The model to grab shapes from
   * @param tick The tick in which the shapes should be grabbed from
   * @throws IllegalArgumentException if the model is null
   */
  @Override
  public void render(IROAnimatorModel model, int tick) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Null read-only model given");
    }
    if (!finished) {
      int[] bounds = model.getBounds();

      append("canvas " + bounds[0] + " " + bounds[1] + " " + bounds[2] + " " + bounds[3] + "\n");
      append(model.getDescription());
      finished = true;
    }
  }

  @Override
  public boolean finished() {
    return this.finished;
  }
}
