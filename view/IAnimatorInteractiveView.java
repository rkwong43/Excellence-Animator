package cs3500.animator.view;

import java.awt.event.ActionListener;

/**
 * Represents an interactive animator. Has the capability to accept new {@code ActionListener}s
 * and fire events towards said listeners.
 */
public interface IAnimatorInteractiveView extends IAnimatorView {

  /**
   * Sets the listener for the view.
   *
   * @param listener The listener to accept
   * @throws IllegalArgumentException if the listener is null
   */
  void acceptListener(ActionListener listener);

  /**
   * Shows an error popup for the user.
   *
   * @param error String of message to display
   * @throws IllegalArgumentException if the error given is null
   */
  void throwError(String error);

  /**
   * Sets the initial speed of the decorations in the view. If less than 1, sets the speed to 1.
   *
   * @param delay The delay given by the controller
   */
  void setInitialSpeed(int delay);
}
