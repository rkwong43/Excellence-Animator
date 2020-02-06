package cs3500.animator.view;

import cs3500.animator.model.IROAnimatorModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * Represents a visual view for {@code IAnimatorModel}. Utilizes {@code JFrame} to display and play
 * an animation.
 */
public class AnimatorVisualView extends JFrame implements IAnimatorView {

  private final DrawingPanel panel;
  private boolean boundsCreated = false;
  private boolean finished = false;

  /**
   * Constructs a visual view for running animations.
   *
   */
  public AnimatorVisualView() {
    super();
    this.panel = new DrawingPanel();
    setTitle("Excellence Animator");
    setLayout(new BorderLayout());
    // Sets it at the corner of the screen
    setLocation(0, 0);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(panel);
    setVisible(true);
  }

  /**
   * Renders the animation visually, allowing it to play.
   *
   * @param model Read-only model to render shapes from
   * @param tick The given tick to get the shapes at
   * @throws IllegalArgumentException if the given model is null or the tick is negative
   */
  @Override
  public void render(IROAnimatorModel model, int tick) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Null read-only model given");
    }
    if (!boundsCreated) {
      int[] bounds = model.getBounds();
      setPreferredSize(new Dimension(bounds[2], bounds[3]));
      panel.setPreferredSize(new Dimension(bounds[2], bounds[3]));
      panel.setMinimumSize(new Dimension(bounds[2], bounds[3]));
      setMinimumSize(new Dimension(bounds[2], bounds[3]));
      boundsCreated = true;
    }
    // getShapesAt(int) will throw exception if tick < 0
    panel.draw(model.getShapesAt(tick));
    if (tick > model.getFinalTick()) {
      this.finished = true;
    }
    repaint();
  }

  @Override
  public boolean finished() {
    return this.finished;
  }
}
