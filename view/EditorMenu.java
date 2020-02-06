package cs3500.animator.view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

/**
 * Represents an editor menu to be used in {@code IAnimatorInteractiveView}. Contains button to
 * pause or play the animation, slider for speed, and slider for the point to rewind or go ahead in
 * the animation. Also contains a checkbox for looping.
 */
public class EditorMenu extends JPanel implements IAnimatorMenu {

  private ActionListener listener; // action listener
  private JButton pauseButton; // Pause or play button
  private JCheckBox loopBox; // Checkbox to loop or not
  private JSlider timeSlider; // Controls the point to play the animation at, is represented in
  // seconds, not ticks
  private JSlider speedSlider; // Controls the speed of the animation

  private int finalTick = 0;

  private JButton speedButton; // Fake button to toss events at the controller, to represent
  // changes in the slider, for speed

  private JButton timeButton; // Fake button to toss events at the controller, to represent
  // changes in the slider. This one is for time

  // Labels for the sliders
  private JLabel speedLabel;
  private JLabel timeLabel;

  /**
   * Constructs an editor menu with a pause/play button, checkbox and sliders.
   */
  EditorMenu() {
    super();
    setLayout(new FlowLayout());
    // Adds a pause button
    pauseButton = new JButton("Pause");
    pauseButton.setActionCommand("pause");
    add(pauseButton);

    // Adds the checkbox to set looping
    loopBox = new JCheckBox("Loop");
    loopBox.setActionCommand("loop");
    add(loopBox);

    // Adds the speed slider
    speedSlider = new JSlider(1, 999, 1);
    speedSlider.addChangeListener((ChangeEvent e) -> changeSpeed());
    speedButton = new JButton();
    speedLabel = new JLabel("Speed: " + speedSlider.getValue());
    add(speedLabel);
    add(speedSlider);

    // Adds the time slider
    timeSlider = new JSlider(0, finalTick, 0);
    timeButton = new JButton();
    timeSlider.addChangeListener((ChangeEvent e) -> changeTime());
    timeLabel = new JLabel("Tick: " + timeSlider.getValue());
    add(timeLabel);
    add(timeSlider);

    setVisible(true);
  }

  // Updates the speed inside the controller, which is the listener, by simulating a click with
  // an invisible button
  private void changeSpeed() {
    speedButton.setActionCommand("setDelay " + speedSlider.getValue());
    speedButton.doClick();
    speedLabel.setText("Speed: " + speedSlider.getValue());
  }

  // Updates the time inside the controller, which is the listener, by simulating a click with
  // an invisible button
  private void changeTime() {
    timeButton.setActionCommand("setTick " + timeSlider.getValue());
    timeButton.doClick();
    timeLabel.setText("Tick: " + timeSlider.getValue());
  }

  @Override
  public void acceptListener(ActionListener listener) {
    this.listener = listener;
    pauseButton.addActionListener(listener);
    loopBox.addActionListener(listener);
    speedButton.addActionListener(listener);
    timeButton.addActionListener(listener);
  }

  /**
   * Updates the current position of the knob on the time slider.
   *
   * @param tick The tick to update the knob to
   * @throws IllegalArgumentException if the tick is less than zero or greater than the final tick
   */
  void updateTime(int tick) {
    // So the controller isn't thrown an event
    timeButton.removeActionListener(listener);
    timeSlider.setValue(tick);
    timeButton.addActionListener(listener);
  }

  /**
   * Accepts the final tick given to be used as the max bound in the slider for time.
   *
   * @param tick The final tick
   * @throws IllegalArgumentException if the tick given is less than 0
   */
  void acceptFinalTick(int tick) {
    finalTick = tick;
    // So the controller isn't thrown an event
    timeButton.removeActionListener(listener);
    timeSlider.setMaximum(tick);
    timeButton.addActionListener(listener);
  }

  /**
   * Sets the initial speed of the speed slider. If less than 1, sets it to 1.
   *
   * @param delay The delay given by the controller
   */
  void setInitialSpeed(int delay) {
    if (delay == 1000) {
      speedSlider.setValue(1);
    } else {
      speedSlider.setValue(1000 - delay);
    }
  }
}
