package cs3500.animator.view;

import java.awt.event.ActionListener;

/**
 * Represents a menu inside {@code IAnimatorInteractiveView}. Has the ability to accept listeners
 * for the components inside the menu.
 */
public interface IAnimatorMenu {

  /**
   * Sets the listener for the view.
   *
   * @param listener The listener to accept
   * @throws IllegalArgumentException if the listener is null
   */
  void acceptListener(ActionListener listener);
}
