package cs3500.animator.view;

import cs3500.animator.model.ColoredEllipse;
import cs3500.animator.model.ColoredRectangle;
import cs3500.animator.model.IColoredShape;
import cs3500.animator.model.ShapeType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Represents a menu where the user can add shapes.
 */
public class ShapeMenu extends JPanel implements IAnimatorMenu, ActionListener, ChangeListener {

  private ShapeType currentShape; // Current shape declared
  // X and y fields of the current shape

  private int x = 0;
  private int y = 0;
  private String name; // Current given ID

  private DrawingPanel panel; // Panel to display color selected and size
  private List<IColoredShape> oneShape; // Shape to be passed to panel

  private JFrame shapeFrame; // Extra window to display the current shape in

  // Sliders for colors
  private JSlider rSlider;
  private JSlider gSlider;
  private JSlider bSlider;

  // Labels for the colors:
  private JLabel rLabel;
  private JLabel gLabel;
  private JLabel bLabel;

  // Text fields for x, y, width, height
  private JFormattedTextField xField;
  private JFormattedTextField yField;
  private JFormattedTextField widthField;
  private JFormattedTextField heightField;

  // Text field for the name
  private JTextField nameField;

  // Helper to check if name has been created
  private JButton createShapeHelperButton;

  /**
   * Constructs a menu for constructing shapes.
   */
  ShapeMenu() {
    super();
    setLayout(new FlowLayout());

    // Initializing the panel and the one shape
    oneShape = new ArrayList<>();
    // Default value of red
    oneShape.add(new ColoredRectangle(0, 0, 50, 50,
        new Color(255, 0, 0)));

    // Making the extra window for the shape displayed
    shapeFrame = new JFrame();
    panel = new DrawingPanel();
    panel.setVisible(true);
    shapeFrame.setVisible(true);
    shapeFrame.setLocationRelativeTo(null);
    shapeFrame.setLayout(new FlowLayout());
    shapeFrame.setTitle("Current Shape");
    shapeFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    shapeFrame.add(panel);
    shapeFrame.pack();
    panel.setPreferredSize(new Dimension(100, 150));
    shapeFrame.setPreferredSize(new Dimension(100, 150));
    shapeFrame.setMinimumSize(new Dimension(100, 150));

    // Making the combo box
    Vector<ShapeType> temp = new Vector<>();
    temp.addAll(Arrays.asList(ShapeType.values()));
    JComboBox<ShapeType> shapeChoices = new JComboBox<>(temp);
    shapeChoices.setActionCommand("declareShape");
    shapeChoices.addActionListener(this);
    shapeChoices.setSelectedItem(ShapeType.RECTANGLE);

    // Initializing the sliders for RGB
    rSlider = new JSlider(0, 255, 255);
    gSlider = new JSlider(0, 255, 0);
    bSlider = new JSlider(0, 255, 0);
    rSlider.addChangeListener(this);
    gSlider.addChangeListener(this);
    bSlider.addChangeListener(this);

    // Initializing the labels for the RGB sliders
    rLabel = new JLabel("Red Value: 255");
    gLabel = new JLabel("Green Value: 0");
    bLabel = new JLabel("Blue Value: 0");

    Format numberFormat = NumberFormat.getNumberInstance();
    // Initializing the x, y, width, height text fields
    xField = new JFormattedTextField(numberFormat);
    yField = new JFormattedTextField(numberFormat);
    widthField = new JFormattedTextField(numberFormat);
    heightField = new JFormattedTextField(numberFormat);
    xField.addActionListener(this);
    yField.addActionListener(this);
    widthField.addActionListener(this);
    heightField.addActionListener(this);
    xField.setText("0");
    yField.setText("0");
    widthField.setText("50");
    heightField.setText("50");
    xField.setActionCommand("xChange");
    yField.setActionCommand("yChange");
    widthField.setActionCommand("shapeChange");
    heightField.setActionCommand("shapeChange");

    xField.setColumns(6);
    yField.setColumns(6);
    widthField.setColumns(6);
    heightField.setColumns(6);

    // Initializing the labels for x, y, width, height, and name
    JLabel xLabel = new JLabel("x: ");
    JLabel yLabel = new JLabel("y: ");
    JLabel widthLabel = new JLabel("Width: ");
    JLabel heightLabel = new JLabel("Height: ");
    JLabel nameLabel = new JLabel("ID: ");

    // Text field for name
    nameField = new JTextField(12);
    nameField.setActionCommand("nameChange");
    nameField.addActionListener(this);

    // Button to create the shape
    JButton createShapeButton = new JButton("Create Shape");
    createShapeButton.addActionListener(this);
    createShapeButton.setActionCommand("create");
    createShapeHelperButton = new JButton();
    updateCreateShapeButton();

    add(shapeChoices);
    add(rLabel);
    add(rSlider);
    add(gLabel);
    add(gSlider);
    add(bLabel);
    add(bSlider);
    add(xLabel);
    add(xField);
    add(yLabel);
    add(yField);
    add(widthLabel);
    add(widthField);
    add(heightLabel);
    add(heightField);
    add(nameLabel);
    add(nameField);
    add(createShapeButton);

    setVisible(true);
  }

  // Updates the action command of the create shape button
  private void updateCreateShapeButton() {
    IColoredShape current = oneShape.get(0);
    Color color = current.getColor();
    createShapeHelperButton.setActionCommand("addShape " + currentShape.toString()
        + " " + name + " " + x + " " + y + " " + current.getWidth() + " " + current.getHeight()
        + " "
        + color.getRed() + " " + color.getGreen() + " " + color.getBlue());
  }

  @Override
  public void acceptListener(ActionListener listener) {
    createShapeHelperButton.addActionListener(listener);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    switch (command) {
      case "declareShape":
        JComboBox<ShapeType> temp = (JComboBox<ShapeType>) e.getSource();
        currentShape = (ShapeType) temp.getSelectedItem();
        updateOneShape(currentShape);
        break;
      case "shapeChange":
        commitEditAll();
        int width = Math.toIntExact((long) widthField.getValue());
        int height = Math.toIntExact((long) heightField.getValue());
        shapeFrame.setPreferredSize(new Dimension(width, height));
        panel.setPreferredSize(new Dimension(width, height));
        shapeFrame.setMinimumSize(new Dimension(width, height));
        panel.setMinimumSize(new Dimension(width, height));
        oneShape.get(0).setShape(0, 0, width, height);
        updateCreateShapeButton();
        break;
      case "xChange":
        commitEditAll();
        x = Math.toIntExact((long) xField.getValue());
        updateCreateShapeButton();
        break;
      case "yChange":
        commitEditAll();
        y = Math.toIntExact((long) yField.getValue());
        updateCreateShapeButton();
        break;
      case "nameChange":
        name = nameField.getText();
        shapeFrame.setTitle("Current Shape: " + name);
        updateCreateShapeButton();
        break;
      case "create":
        if (name == null) {
          JOptionPane.showMessageDialog(this,
              "Please give your shape a name");
        } else {
          createShapeHelperButton.doClick();
        }
        break;
      default:
        throw new UnsupportedOperationException("Unsupported command");
    }
    panel.draw(oneShape);
  }

  // Commits edits for all the formatted text fields
  private void commitEditAll() {
    try {
      xField.commitEdit();
      yField.commitEdit();
      widthField.commitEdit();
      heightField.commitEdit();
    } catch (ParseException e) {
      JOptionPane.showMessageDialog(this, "Error updating shape.");
    }
  }

  // Updates the one shape displayed in case the user selects another
  private void updateOneShape(ShapeType shape) {
    if (shape == null) {
      return;
    }
    IColoredShape old = oneShape.remove(0);
    switch (shape) {
      case RECTANGLE:
        oneShape.add(new ColoredRectangle(0, 0, old.getWidth(), old.getHeight(), old.getColor()));
        break;
      case ELLIPSE:
        oneShape.add(new ColoredEllipse(0, 0, old.getWidth(), old.getHeight(), old.getColor()));
        break;
      default:
        throw new UnsupportedOperationException("Unsupported shape: " + shape);
    }
    panel.draw(oneShape);
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    // Updates the one shape's color
    int r = rSlider.getValue();
    int g = gSlider.getValue();
    int b = bSlider.getValue();
    oneShape.get(0).setColor(new Color(r, g, b));
    // Updates the labels
    rLabel.setText("Red Value: " + r);
    gLabel.setText("Green Value: " + g);
    bLabel.setText("Blue Value: " + b);
    panel.draw(oneShape);
  }
}
