package cs3500.animator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

/**
 * Represents a window to create a keyframe in. Has a combo box for the type of action and fields
 * for the values.
 */
public class KeyframeBuilder extends JFrame implements IAnimatorMenu, ActionListener {

  private JButton sendButton; // Button to send the final parameters to the menu
  private JFormattedTextField tickTextField; // Text field for the tick
  private int tick = -1; // Tick to add keyframe at
  private String id; // ID of the shape to add keyframe for

  /**
   * Constructs the keyframe builder.
   */
  KeyframeBuilder() {
    super();
    setLayout(new FlowLayout());
    setPreferredSize(new Dimension(400, 500));
    setMinimumSize(new Dimension(400, 500));
    setLocationRelativeTo(null);
    sendButton = new JButton();
    // Tick text label
    JLabel tickLabel = new JLabel("Tick:");
    add(tickLabel);
    // Tick text field
    Format numberFormat = NumberFormat.getNumberInstance();
    tickTextField = new JFormattedTextField(numberFormat);
    tickTextField.addActionListener(this);
    tickTextField.setActionCommand("tickSet");
    tickTextField.setText("-1");
    tickTextField.setColumns(9);
    add(tickTextField);

    JComboBox<String> stateChoices = new JComboBox<>(
        new String[]{"Change Color", "Change Size", "Change Position"});
    stateChoices.setActionCommand("declareState");
    stateChoices.addActionListener(this);
    add(stateChoices);

    setTitle("Keyframe Builder");
  }

  @Override
  public void acceptListener(ActionListener listener) {
    sendButton.addActionListener(listener);
  }

  /**
   * Builds a keyframe off of the selected values.
   *
   * @param id The ID of the shape
   * @param frames The list of Strings representing existing keyframes
   */
  void buildKeyframe(String id, List<String> frames) {
    Object[] listFrames = frames.toArray();
    JList<Object> list = new JList<>(listFrames);
    list.setLayoutOrientation(JList.VERTICAL);
    list.setVisibleRowCount(-1);
    JScrollPane scrollPane = new JScrollPane(list);
    scrollPane.setPreferredSize(new Dimension(300, frames.size() * 4));
    add(scrollPane);
    this.id = id;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    switch (command) {
      case "declareState":
        JComboBox temp = (JComboBox<String>) e.getSource();
        String message = (String) temp.getSelectedItem();
        getParams(message);
        break;
      case "tickSet":
        commitAll();
        tick = Math.toIntExact((long) tickTextField.getValue());
        break;
      default:
        throw new UnsupportedOperationException("Unsupported command: " + command);
    }
  }

  // Creates popups to determine what to set the parameters
  private void getParams(String message) {
    if (tick == -1) {
      JOptionPane.showMessageDialog(this, "Please set a tick first.");
      return;
    }
    List<Integer> params = new ArrayList<>();
    String state;
    switch (message) {
      case "Change Color":
        Integer r = createPopup("red");
        // User cancelled
        if (r == null) {
          return;
        }
        Integer g = createPopup("green");
        if (g == null) {
          return;
        }
        Integer b = createPopup("blue");
        if (b == null) {
          return;
        }
        params.add(r);
        params.add(g);
        params.add(b);
        state = "color";
        break;
      case "Change Position":
        Integer x = createPopup("x");
        if (x == null) {
          return;
        }
        Integer y = createPopup("y");
        if (y == null) {
          return;
        }
        params.add(x);
        params.add(y);
        state = "move";
        break;
      case "Change Size":
        Integer width = createPopup("width");
        if (width == null) {
          return;
        }
        Integer height = createPopup("height");
        if (height == null) {
          return;
        }
        params.add(width);
        params.add(height);
        state = "size";
        break;
      default:
        throw new UnsupportedOperationException("Unsupported command: " + message);
    }
    StringBuilder temp = new StringBuilder();
    for (Integer i : params) {
      temp.append(i).append(" ");
    }
    sendButton
        .setActionCommand("keyframeBuilt " + id + " " + state + " " + tick + " " + temp.toString());
    sendButton.doClick();
  }

  // Creates a popup field that lets the user enter text
  private Integer createPopup(String param) {
    boolean complete = false;
    Integer result = null;
    while (!complete) {
      String s = (String) JOptionPane.showInputDialog(
          this,
          "Set " + param + ":",
          "Parameters",
          JOptionPane.PLAIN_MESSAGE,
          null,
          null,
          "-1");
      if (s == null) {
        break;
      }
      try {
        result = Integer.valueOf(s);
        complete = true;
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Please enter an integer.");
      }
    }
    return result;
  }

  // Commits the edit
  private void commitAll() {
    try {
      tickTextField.commitEdit();
    } catch (ParseException e) {
      throw new IllegalStateException("Unable to save tick");
    }
  }
}
