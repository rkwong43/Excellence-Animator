package cs3500.animator.view;

import cs3500.animator.model.IColoredShape;
import cs3500.animator.model.IROAnimatorModel;
import cs3500.animator.model.IROMotion;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Represents a menu where a user can add or remove key frames. Can also remove shapes.
 */
public class KeyframeMenu extends JPanel implements IAnimatorMenu, ActionListener {

  private IROAnimatorModel model;
  private List<String> ids; // List of IDs
  // Helpers that actually communicates with the controller
  private JButton removeShapeHelperButton;
  private JButton removeKeyframeHelperButton;
  private JButton addKeyframeHelperButton;

  // List of labels and panels for the list of shapes
  private List<Component> labels;
  private List<Component> panels;

  private boolean initialized = false; // If the list of shapes have been initialized yet

  private KeyframeBuilder builder; // Builds a keyframe using user input

  /**
   * Constructs a menu for viewing, adding, and deleting keyframes.
   */
  KeyframeMenu() {
    super();
    setPreferredSize(new Dimension(600, 100));
    setLayout(new FlowLayout());
    labels = new ArrayList<>();
    panels = new ArrayList<>();

    builder = new KeyframeBuilder();
    builder.setVisible(false);

    // Button to remove a shape
    JButton removeShapeButton = new JButton("Remove Shape");
    removeShapeButton.setActionCommand("removeShape");
    removeShapeButton.addActionListener(this);
    // Its helper, invisible
    removeShapeHelperButton = new JButton();

    // Button to remove a keyframe
    JButton removeKeyframeButton = new JButton("Remove Keyframe");
    removeKeyframeButton.setActionCommand("removeKeyframe");
    removeKeyframeButton.addActionListener(this);
    // Helper, invisible
    removeKeyframeHelperButton = new JButton();

    // Button to add a keyframe
    JButton addKeyframeButton = new JButton("Add Keyframe");
    addKeyframeButton.setActionCommand("addKeyframe");
    addKeyframeButton.addActionListener(this);
    // Helper, invisible
    addKeyframeHelperButton = new JButton();

    // Header for list of shapes
    JLabel currentShapesLabel = new JLabel("Current Shapes:");

    // Keyframe building window

    add(removeShapeButton);
    add(removeKeyframeButton);
    add(addKeyframeButton);
    add(currentShapesLabel);

    setVisible(true);
  }

  @Override
  public void acceptListener(ActionListener listener) {
    removeShapeHelperButton.addActionListener(listener);
    removeKeyframeHelperButton.addActionListener(listener);
    addKeyframeHelperButton.addActionListener(listener);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Scanner scan = new Scanner(e.getActionCommand());
    String command = scan.next();
    switch (command) {
      case "removeShape":
        removeShapePopup();
        break;
      case "removeKeyframe":
        removeKeyframePopup();
        break;
      case "addKeyframe":
        addKeyframePopup();
        break;
      case "keyframeBuilt":
        builder.setVisible(false);
        addKeyframeHelperButton.setActionCommand("addKeyframe " + scan.nextLine());
        addKeyframeHelperButton.doClick();
        break;
      default:
        throw new UnsupportedOperationException("Unsupported command: " + command);
    }
  }

  private void addKeyframePopup() {
    String id = selectShape("add a keyframe for");
    List<String> frames = getKeyframes(id);
    builder.setVisible(true);
    builder.buildKeyframe(id, frames);
  }

  // Creates a dialog box that allows user to select a shape to remove
  private void removeShapePopup() {
    String shape = selectShape("remove");
    if (shape != null) {
      removeShapeHelperButton.setActionCommand("removeShape " + shape);
      removeShapeHelperButton.doClick();
    }
  }

  // Creates a list of Strings representing keyframe representations
  private List<String> getKeyframes(String id) {
    if (id == null) {
      return null;
    }
    List<IROMotion> motions = model.getMotionsFor(id);
    List<String> frames = new ArrayList<>();
    // When the shape is initialized
    int start;
    try {
      start = motions.get(0).getStart();
    } catch (NullPointerException e) {
      start = 0;
    }
    frames.add("Initialized " + id + " at tick = " + start);

    for (int k = 0; k < motions.size(); k++) {
      // Current motion
      IROMotion motion = motions.get(k);
      // List of states
      List<String> states = motion.getStates();
      // If there are states
      if (!states.isEmpty()) {

        IROMotion prev;
        try {
          prev = motions.get(k - 1);
        } catch (IndexOutOfBoundsException e) {
          // This is the first motion
          prev = motions.get(0);
        }

        if ((prev.getEnd() == motion.getStart() || prev.getEnd() == motion.getStart() - 1)
            || k == 0) {
          // Consecutive motions
          processState(states, motion, frames);
        } else if (motion.getStart() != 0) {
          // Non-consecutive motions
          frames.add("No changes until tick " + motion.getStart());
          processState(states, motion, frames);
        }
        // If there are no states
      } else {
        frames.add("No changes until tick " + motion.getEnd());
      }

    }
    return frames;
  }

  // Creates a dialog box to remove a keyframe
  private void removeKeyframePopup() {
    String id = selectShape("remove a keyframe");
    List<String> frames = getKeyframes(id);
    if (frames == null) {
      return;
    }
    Object[] options = frames.toArray();
    String choice = null;
    if (options.length != 0) {
      choice = (String) JOptionPane.showInputDialog(
          this,
          "Select a keyframe to remove: ",
          "Keyframe Deletion",
          JOptionPane.PLAIN_MESSAGE,
          null,
          options,
          options[0]);
    }

    if (choice != null) {
      Scanner scan = new Scanner(choice);
      int tick = -1;
      while (scan.hasNext()) {
        try {
          tick = Integer.valueOf(scan.next());
        } catch (NumberFormatException e) {
          // Do nothing
        }
      }
      removeKeyframeHelperButton.setActionCommand("removeKeyFrame " + id + " " + tick);
      removeKeyframeHelperButton.doClick();
    }
  }

  // Processes the states and motions into a String
  private void processState(List<String> states, IROMotion motion, List<String> frames) {
    for (String state : states) {
      Scanner scan = new Scanner(state);
      String command = scan.next();
      switch (command) {
        case "move":
          frames.add(
              "Moves to x:" + scan.nextInt() + " y:" + scan.nextInt() + " until tick = " + motion
                  .getEnd());
          break;
        case "color":
          frames.add(
              "Changes color to rgb(" + scan.nextInt() + "," + scan.nextInt() + "," + scan.nextInt()
                  + ") until tick = " + motion.getEnd());
          break;
        case "size":
          frames.add(
              "Changes size to width:" + scan.nextInt() + " height:" + scan.nextInt()
                  + " until tick = " + motion
                  .getEnd());
          break;
        default:
          throw new UnsupportedOperationException("Unsupported command: " + command);
      }
    }
  }

  // Creates a popup to select a shape
  private String selectShape(String param) {
    Object[] options = ids.toArray();
    String result = null;
    if (options.length != 0) {
      result = (String) JOptionPane.showInputDialog(
          this,
          "Select a shape to " + param + ":",
          "Shape Selection",
          JOptionPane.PLAIN_MESSAGE,
          null,
          options,
          options[0]);
    }
    return result;
  }

  /**
   * Accepts a read-only model.
   *
   * @param model Read-only model to take in.
   */
  void acceptModel(IROAnimatorModel model) {
    this.model = model;
    this.ids = model.getNames();
    initShapeList();
  }

  /**
   * Initializes and draws out the list of shape IDs.
   */
  private void initShapeList() {
    if (initialized) {
      clearList();
    }
    initialized = true;

    // Adds the new shapes
    for (String id : ids) {
      JLabel tempLabel = new JLabel(id);
      labels.add(tempLabel);
      add(tempLabel);
      if (ids.size() < 20) {
        DrawingPanel panel = new DrawingPanel();
        panels.add(panel);
        add(panel);
        panel.setMinimumSize(new Dimension(5, 5));
        IColoredShape shape = model.getShape(id);
        shape.setShape(0, 0, 10, 10);
        panel.draw(new ArrayList<>(Collections.singletonList(shape)));
      }
      validate();
      repaint();
    }
  }

  // Clears the labels and panels of shapes
  private void clearList() {
    // Removes the old shapes
    if (initialized) {
      // Clears the panels and labels, along with the displayed list
      for (Component c : labels) {
        remove(c);
        validate();
        repaint();
      }
      for (Component c : panels) {
        remove(c);
        validate();
        repaint();
      }
      labels.clear();
      panels.clear();
    }
  }
}
