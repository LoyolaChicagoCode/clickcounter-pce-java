package clickcounter;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * Provides the interface behaviour for the interactive Application.
 */

public class Main extends JFrame {

  private final static int DEFAULT_NUMBER_OF_FRAMES = 5;

  private static int num = 0;

  private JComponent theInterface;

  public Main(JComponent theInterface) {
    num ++;
    this.theInterface = theInterface;
    getContentPane().add(theInterface);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocation(200, 100 * num);
//    setVisible(true);
  }

  public static void main(String args[]) {
    // use the command-line argument for the number of frames if present
    int numberOfFrames = DEFAULT_NUMBER_OF_FRAMES;
    try {
      numberOfFrames = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      System.err.println("usage: clickcounter.Main [ numberOfFrames ]");
    }

    // create a translation independently from the presentation
    Translation translation = new Translation();
    translation.setApplication(new Application(0, 5));

    JFrame[] frames = new JFrame[numberOfFrames];

    for (int i = 0; i < numberOfFrames; i ++) {
      Presentation visibleInterface = new AlternatePresentation();
      translation.addPropertyChangeListener(visibleInterface);
      visibleInterface.addActionListener(translation);
      frames[i] = new Main((JComponent) visibleInterface);
    }

    // initialize the presentations before making them visible
    translation.initInterfaces();

    for (int i = 0; i < numberOfFrames; i ++) {
      frames[i].setVisible(true);
    }
  } // end main.
} // end class Translation.

