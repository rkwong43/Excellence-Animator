package cs3500.animator.view;

import cs3500.animator.model.IROAnimatorModel;
import java.io.IOException;

/**
 * Represents an abstract animator view. Accepts an {@code Appendable} object.
 */
public abstract class AAnimatorView implements IAnimatorView {

  protected Appendable ap; // output

  /**
   * Constructs an abstract animator view, taking in an output source.
   *
   * @param ap {@code Appendable} object to represent output
   * @throws IllegalArgumentException if the parameter is null
   */
  protected AAnimatorView(Appendable ap) {
    if (ap == null) {
      throw new IllegalArgumentException("Null Appendable given");
    } else {
      this.ap = ap;
    }
  }

  public abstract void render(IROAnimatorModel model, int tick)
      throws IllegalArgumentException;

  /**
   * Appends a {@code String} onto the {@code Appendable}.
   *
   * @param s String to append
   * @throws IllegalStateException if an IO exception is caught while attempting to append
   */
  protected void append(String s) {
    try {
      ap.append(s);
    } catch (IOException e) {
      throw new IllegalStateException("IOException caught, issue appending onto Appendable");
    }
  }
}
