package clickcounter;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.TestHelper;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import junit.extensions.jfcunit.finder.AbstractButtonFinder;
import junit.extensions.jfcunit.finder.JLabelFinder;

public class TestPresentation extends JFCTestCase implements EventLabels {

  private Presentation pres;
  
  private Translation trans;
  
  private JFrame frame;
  
  private final static int MIN = 0;
  
  private final static int MAX = 5;
  
  protected void setUp() throws Exception {
    super.setUp();
    setHelper(new JFCTestHelper());

    trans = new Translation();
    trans.setApplication(new Application(0, 5));
    pres = new DefaultPresentation();
    trans.addPropertyChangeListener(pres);
    pres.addActionListener(trans);
    trans.initInterfaces();
    frame = new JFrame("ClickCounter jfcUnit Test");
    frame.setContentPane((Container) pres);
    frame.pack();
    frame.setVisible(true);
}

  protected void tearDown() throws Exception {
    pres.removeActionListener(trans);
    trans.removePropertyChangeListener(pres);
    trans.setApplication(null);
    trans = null;
    pres = null;
    frame = null;

    TestHelper.cleanUp(this);
    super.tearDown();
  }

  public void testInit() throws Exception {
    assertEquals(MIN, getValue());
  }
  
  public void testIncrement() throws Exception {
    getHelper().enterClickAndLeave(new MouseEventData(this, getIncrement()));
    assertEquals(MIN + 1, getValue());
  }
  
  public void testMaximum() throws Exception {
    for (int i = MIN; i < MAX; i ++) {
      getHelper().enterClickAndLeave(new MouseEventData(this, getIncrement()));
    }
    assertEquals(MAX, getValue());
  }
  
  public void testDecrement() throws Exception {
    testMaximum();
    getHelper().enterClickAndLeave(new MouseEventData(this, getDecrement()));
    assertEquals(MAX - 1, getValue());
  }
  
  public void testMinimum() throws Exception {
    testMaximum();
    for (int i = MIN; i < MAX; i ++) {
      getHelper().enterClickAndLeave(new MouseEventData(this, getDecrement()));
    }
    assertEquals(MIN, getValue());
  }
  
  public void testReset() throws Exception {
    testMaximum();
    getHelper().enterClickAndLeave(new MouseEventData(this, getReset()));
    assertEquals(MIN, getValue());
  }
  
  protected int getValue() throws Exception {
    JLabelFinder finder = new JLabelFinder("[0-9]*");
    JLabel display = (JLabel) finder.find(frame, 0);
    return Integer.parseInt(display.getText());
  }
  
  protected JButton getIncrement() {
    AbstractButtonFinder finder = new AbstractButtonFinder("\\+");
    return (JButton) finder.find(frame, 0);
  }
  
  protected JButton getReset() {
    AbstractButtonFinder finder = new AbstractButtonFinder("0");
    return (JButton) finder.find(frame, 0);
  }
  
  protected JButton getDecrement() {
    AbstractButtonFinder finder = new AbstractButtonFinder("-");
    return (JButton) finder.find(frame, 0);
  }
}
