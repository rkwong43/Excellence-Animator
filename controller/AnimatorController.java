package cs3500.animator.controller;

import cs3500.animator.model.IAnimatorModel;
import cs3500.animator.model.IROAnimatorModel;
import cs3500.animator.model.ROAnimatorModelImpl;
import cs3500.animator.view.IAnimatorView;
import java.awt.event.ActionEvent;
import javax.swing.Timer;


/**
 * Represents an animator controller that runs the animation based on the given time.
 */
public class AnimatorController implements IAnimatorController {

  private Timer timer;
  private IAnimatorView view;
  private IROAnimatorModel model;
  private int tick = 0;

  /**
   * Constructs an {@code AnimatorController} that runs the given model on the given view, with the
   * speed set to the given delay.
   *
   * @param model Read-only model to get shapes from
   * @param view View to draw the shapes on
   * @param delay Delay in milliseconds the timer is run on
   * @throws IllegalArgumentException if the model or view are null or the delay is less than 0
   */
  public AnimatorController(IAnimatorModel model, IAnimatorView view, int delay) {
    if (model == null || view == null || delay < 0) {
      throw new IllegalArgumentException("Illegal parameters for controller");
    }
    this.model = new ROAnimatorModelImpl(model);
    this.view = view;
    timer = new Timer(delay, (ActionEvent e) ->
        getNewShapes());
  }

  // Increments timer and updates shapes
  private void getNewShapes() {
    view.render(model, tick++);
  }

  @Override
  public void startTimer() {
    timer.start();
  }

}
