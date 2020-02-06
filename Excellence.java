package cs3500.animator;

import cs3500.animator.controller.AnimatorController;
import cs3500.animator.controller.IAnimatorController;
import cs3500.animator.controller.InteractiveController;
import cs3500.animator.model.IAnimatorModel;
import cs3500.animator.model.IAnimatorModelImpl.Builder;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.AnimatorInteractiveView;
import cs3500.animator.view.AnimatorSVGView;
import cs3500.animator.view.AnimatorTextView;
import cs3500.animator.view.AnimatorVisualView;
import cs3500.animator.view.IAnimatorInteractiveView;
import cs3500.animator.view.IAnimatorView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main class to run our Excellence animator.
 */
public final class Excellence {

  private static FileWriter file;
  private static Timer timer;

  /**
   * Main method to run the animations in.
   */
  public static void main(String[] args) {
    // Adds all the commands to a single StringBuilder
    StringBuilder commands = new StringBuilder();
    for (String arg : args) {
      commands.append(arg).append(" ");
    }
    Scanner scan = new Scanner(commands.toString());
    processCommands(scan);
  }

  /**
   * Processes the scanner to create the specified view.
   *
   * @param scan Scanner that contains the command line
   */
  private static void processCommands(Scanner scan) {
    Map<String, String> params = new HashMap<>();
    IAnimatorView view;
    int speed = 1;
    while (scan.hasNext()) {
      String command = scan.next();
      switch (command) {
        case "-in":
          params.put("in", scan.next());
          break;
        case "-out":
          params.put("out", scan.next());
          break;
        case "-view":
          params.put("view", scan.next());
          break;
        case "-speed":
          speed = scan.nextInt();
          break;
        default:
          // Do nothing
      }
    }
    // Parameters initialized at this point:
    view = viewFactory(params.get("view"), params.getOrDefault("out", null));
    Readable inputFile;
    try {
      inputFile = new FileReader(new File(params.get("in")));
    } catch (FileNotFoundException e) {
      throw new IllegalStateException("File not found");
    }
    IAnimatorModel model = AnimationReader.parseFile(inputFile, new Builder());
    // Delay is 1000 ms divided by the given speed (or 1 by default)
    IAnimatorController controller;
    if (params.get("view").equals("edit")) {
      controller = new InteractiveController(model, (IAnimatorInteractiveView) view,
          (1000 / speed));
    } else {
      controller = new AnimatorController(model, view, (1000 / speed));
    }
    controller.startTimer();

    // Checks if the file is done writing to, if it is, flushes and closes file
    if (file != null) {
      timer = new Timer();
      // Waits a second then checks every delay (1000 / speed ms)
      timer.schedule(new CheckTask(view), 1000, (1000 / speed));
    }
  }

  // Checks if the view is done writing
  private static class CheckTask extends TimerTask {

    private IAnimatorView view;

    private CheckTask(IAnimatorView controller) {
      this.view = controller;
    }

    @Override
    public void run() {
      // If view is done:
      if (view.finished()) {
        try {
          // Closes file, stops timer
          file.flush();
          file.close();
          timer.cancel();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Processes the type of view wanted and creates and returns it.
   *
   * @param view String of the view to create
   * @param output Document or null to output the animation onto. If null, System.out
   * @return the new view object
   * @throws IllegalStateException if the given view String is not supported or file errors
   */
  private static IAnimatorView viewFactory(String view, String output)
      throws IllegalStateException {
    IAnimatorView finalView;
    Appendable out;
    // Determining the output
    if (output == null) {
      out = new OutputStreamWriter(System.out);
    } else {
      try {
        file = new FileWriter(output);
        out = file;
      } catch (IOException e) {
        throw new IllegalStateException("File not found");
      }
    }
    switch (view) {
      case "visual":
        finalView = new AnimatorVisualView();
        break;
      case "svg":
        if (file != null) {
          finalView = new AnimatorSVGView(file);
        } else {
          finalView = new AnimatorSVGView(out);
        }
        break;
      case "text":
        if (file != null) {
          finalView = new AnimatorTextView(file);
        } else {
          finalView = new AnimatorTextView(out);
        }
        break;
      case "edit":
        finalView = new AnimatorInteractiveView();
        break;
      default:
        throw new IllegalStateException("Type of view not supported");
    }
    return finalView;
  }
}
