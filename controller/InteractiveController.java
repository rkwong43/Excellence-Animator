package cs3500.animator.controller;

import cs3500.animator.model.IAnimatorModel;
import cs3500.animator.model.IROAnimatorModel;
import cs3500.animator.model.ROAnimatorModelImpl;
import cs3500.animator.model.ShapeType;
import cs3500.animator.view.IAnimatorInteractiveView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.Timer;

/**
 * An interactive controller compatible with {@code IAnimatorInteractiveView}. Has the ability to
 * accept events from the view. Can add shapes, keyframes, remove them, pause/un-pause the
 * animation, and set the tick. Also allows the user to decide if the animation loops or not.
 */
public class InteractiveController implements IAnimatorController, ActionListener {

  private Timer timer;
  private IAnimatorInteractiveView view;
  private IAnimatorModel model;
  private boolean paused = false; // If the animation is paused or not
  private boolean looping = false; // If the animation is looping or not
  private int finalTick;
  private int tick = 0;
  private IROAnimatorModel readOnly;

  /**
   * Constructs an instance of the interactive controller, adding itself as the listener for the
   * view.
   *
   * @param model The model to use
   * @param view The view to work off of
   * @param delay The timer delay to initially use
   * @throws IllegalArgumentException if the model or view are null, or the delay is less than 0
   */
  public InteractiveController(IAnimatorModel model, IAnimatorInteractiveView view, int delay) {
    // Initial setup
    if (model == null || view == null || delay < 0) {
      throw new IllegalArgumentException("Illegal parameters for controller");
    }
    this.model = model;
    this.view = view;
    readOnly = new ROAnimatorModelImpl(model);
    finalTick = model.getFinalTick();
    view.acceptListener(this);
    timer = new Timer(delay, (ActionEvent e) ->
        getNewShapes());
    view.setInitialSpeed(delay);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Scanner scan = new Scanner(e.getActionCommand());
    String command = scan.next();
    switch (command) {
      case "loop":
        looping = !looping;
        break;
      case "pause":
        pause();
        JButton source = (JButton) e.getSource();
        switch (source.getText()) {
          case "Pause":
            source.setText("Play");
            break;
          case "Play":
            source.setText("Pause");
            break;
          default:
            // Do nothing
        }
        break;
      case "addShape":
        addShape(scan.nextLine());
        break;
      case "addKeyFrame":
        addKeyFrame(scan.nextLine());
        break;
      case "removeShape":
        removeShape(scan.nextLine());
        break;
      case "removeKeyFrame":
        removeKeyFrame(scan.nextLine());
        break;
      case "setDelay":
        int delay = scan.nextInt();
        timer.setDelay(1000 - delay);
        break;
      case "setTick":
        tick = scan.nextInt();
        break;
      default:
        throw new UnsupportedOperationException(
            "Unsupported event: " + command);
    }
  }

  // Removes a keyframe
  private void removeKeyFrame(String frame) {
    Scanner scan = new Scanner(frame);
    // ID and tick given
    String id = scan.next().trim();
    int tick = scan.nextInt();
    model.removeKeyframe(id, tick);
  }


  // Adds a keyframe
  private void addKeyFrame(String frame) {
    Scanner scan = new Scanner(frame);
    // ID, type of state, tick, and rest of parameters
    String id = scan.next().trim();
    String state = scan.next();
    int tick = scan.nextInt();
    int param1 = scan.nextInt();
    int param2 = scan.nextInt();
    try {
      if (state.equals("color")) {
        model.addKeyframe(id, state, tick, param1, param2, scan.nextInt());
      } else {
        model.addKeyframe(id, state, tick, param1, param2);
      }
    } catch (IllegalArgumentException e) {
      view.throwError(e.getMessage());
    }
  }

  // Increments timer and updates shapes
  private void getNewShapes() {
    if (looping && tick > finalTick) {
      tick = 0;
      view.render(readOnly.loop(), tick++);
    } else {
      view.render(readOnly, tick++);
    }
  }

  // Adds a shape to the animation
  private void addShape(String shape) {
    // Format of given String:
    // shapeType ID x y width height r g b
    Scanner scan = new Scanner(shape);

    try {
      switch (scan.next()) {
        case "RECTANGLE":
          model.addShape(ShapeType.RECTANGLE, scan.next(), scan.nextInt(), scan.nextInt(),
              scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt());
          break;
        case "ELLIPSE":
          model.addShape(ShapeType.ELLIPSE, scan.next(), scan.nextInt(), scan.nextInt(),
              scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt(), scan.nextInt());
          break;
        default:
          throw new UnsupportedOperationException("Unsupported shape attempted to be added");
      }
    } catch (IllegalArgumentException e) {
      view.throwError(e.getMessage());
    }
  }

  // Removes the shape
  private void removeShape(String shape) {
    // Format of given String:
    // ID
    try {
      model.removeShape(shape.trim());
    } catch (IllegalArgumentException e) {
      view.throwError(e.getMessage());
    }
  }

  // Pauses and un-pauses the animation
  private void pause() {
    if (!paused) {
      timer.stop();
      paused = !paused;
    } else {
      timer.start();
      paused = !paused;
    }
  }

  @Override
  public void startTimer() {
    timer.start();
  }
}
