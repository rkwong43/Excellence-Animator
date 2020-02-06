package cs3500.animator.view;

import cs3500.animator.model.IROAnimatorModel;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

/**
 * Represents an interactive animator view with editor controls. Can add shapes, remove them,
 * pause/un-pause the animation, rewind, and restart. Also allows the user to decide if the
 * animation loops or not.
 */
public class AnimatorInteractiveView extends JFrame implements IAnimatorInteractiveView,
    IAnimatorView {

  private List<ActionListener> listeners; // List of action listeners to fire events toward
  private IAnimatorView viewingWindow; // Separate window to view animator in
  private EditorMenu editor; // Editor page for editing the general playback features
  private ShapeMenu shapes; // Page for shapes, adding and removing them
  private KeyframeMenu keyframes; // Page for keyframes, adding and removing them


  /**
   * Constructs an instance of the interactive view.
   */
  public AnimatorInteractiveView() {
    super();
    listeners = new ArrayList<>();
    // Creates a separate window for the animation to play in
    setLayout(new FlowLayout());
    viewingWindow = new AnimatorVisualView();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Excellence Editor");
    editor = new EditorMenu();
    shapes = new ShapeMenu();
    keyframes = new KeyframeMenu();
    // Creating a set of tabs
    JTabbedPane tabs = new JTabbedPane();
    tabs.addTab("Playback Editor", null, editor, "Playback Editor");
    tabs.addTab("Shape Creator", null, shapes, "For adding and removing shapes");
    tabs.addTab("Animation Editor", null, keyframes,
        "Adding keyframes or removing keyframes or shapes");
    add(tabs);
    setLocation(100, 800);

    pack();
    setVisible(true);
  }

  @Override
  public void acceptListener(ActionListener listener) {
    if (listener != null) {
      listeners.add(listener);
      editor.acceptListener(listener);
      shapes.acceptListener(listener);
      keyframes.acceptListener(listener);
    } else {
      throw new IllegalArgumentException("Null listener given");
    }
  }

  @Override
  public void throwError(String error) {
    JOptionPane.showMessageDialog(this, error);
  }

  @Override
  public void render(IROAnimatorModel model, int tick) throws IllegalArgumentException {
    viewingWindow.render(model, tick);
    keyframes.acceptModel(model);
    editor.acceptFinalTick(model.getFinalTick());
    editor.updateTime(tick);
  }

  @Override
  public void setInitialSpeed(int delay) {
    editor.setInitialSpeed(delay);
  }

  @Override
  public boolean finished() {
    return viewingWindow.finished();
  }

}
