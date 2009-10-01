package clickcounter;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.concurrent.Semaphore;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class TestTranslation extends TestCase implements EventLabels {

  private Translation trans;
  
  private StubPresentation pres;
  
  private final static int MIN = 0;

  private final static int MAX = 1;
  
  private ActionEvent INC = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, INCREMENT);

  private ActionEvent DEC = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, DECREMENT);

  private ActionEvent RES = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, RESET);

  protected void setUp() throws Exception {
    super.setUp();
    trans = new Translation();
    trans.setApplication(new Application(MIN, MAX));
    pres = new StubPresentation();
    trans.addPropertyChangeListener(pres);
    trans.initInterfaces();
  }

  protected void tearDown() throws Exception {
    trans.removePropertyChangeListener(pres);
    trans.setApplication(null);
    trans = null;
    pres = null;
    super.tearDown();
  }

  /**
   * Schedules an action event to be received by a listener. 
   * @param l the listener
   * @param e the event
   */
  protected void scheduleActionEvent(final ActionListener l, final ActionEvent e) {
    EventQueue.invokeLater(new Runnable() { public void run() {
      l.actionPerformed(e);
    }});
  }

  /** 
   * A semaphore used to indicate quiescence, meaning 
   * that all pending events on the system event queue
   * have been processed.
   */ 
  private final Semaphore quiescence = new Semaphore(0);

  /**
   * A probe to signal quiescence if there are no more pending
   * events on the system event queue. 
   */
  private Runnable checkQuiescence = new Runnable() {
    public void run() {
      // check whether there are any pending events
      // after the InvocationEvent representing this runnable
      if (Toolkit.getDefaultToolkit().getSystemEventQueue().peekEvent() == null) {
        // if not, indicate quiescence
        quiescence.release();
      } else {
        // otherwise put the probe back on the event queue
        EventQueue.invokeLater(checkQuiescence);
      }
    }
  };

  /**
   * Waits until there are no more pending events on the system
   * event queue. Called within test methods after sending events 
   * to a component and before inspecting the state of the component.
   * @throws InterruptedException if the waiting thread has been interrupted
   */
  protected void awaitQuiescence() throws InterruptedException {
    // schedule the probe runnable on the event queue 
    EventQueue.invokeLater(checkQuiescence);
    // wait until the probe signals quiescence
    quiescence.acquire();
  }
  
  public void testInit() throws Exception {
    awaitQuiescence();
    assertEquals(UI_MINIMUM, pres.getState());
  }
  
  public void testIncrement() throws Exception {
    scheduleActionEvent(trans, INC);
    awaitQuiescence();
    assertEquals(MIN + 1, pres.getValue());
    try {
      assertEquals(UI_COUNTING, pres.getState());
    } catch (AssertionFailedError e) {
      assertEquals(MIN + 1, MAX);
      assertEquals(UI_MAXIMUM, pres.getState());
    }
  }
  
  public void testMaximum() throws Exception {
    for (int i = MIN; i < MAX; i ++) {
      scheduleActionEvent(trans, INC);
    }
    awaitQuiescence();
    assertEquals(MAX, pres.getValue());
    assertEquals(UI_MAXIMUM, pres.getState());
  }
  
  public void testDecrememt() throws Exception {
    testMaximum();
    scheduleActionEvent(trans, DEC);
    awaitQuiescence();
    assertEquals(MAX - 1, pres.getValue());
    try {
      assertEquals(UI_COUNTING, pres.getState());
    } catch (AssertionFailedError e) {
      assertEquals(MIN + 1, MAX);
      assertEquals(UI_MINIMUM, pres.getState());
    }
  }
  
  public void testMinimum() throws Exception {
    testMaximum();
    for (int i = MIN; i < MAX; i ++) {
      scheduleActionEvent(trans, DEC);
    }
    awaitQuiescence();
    assertEquals(MIN, pres.getValue());
    assertEquals(UI_MINIMUM, pres.getState());
  }
  
  public void testReset() throws Exception {
    testMaximum();
    scheduleActionEvent(trans, RES);
    awaitQuiescence();
    assertEquals(MIN, pres.getValue());
  }
  
  /**
   * A stub to simulate the presence of a presentation component. 
   */
  private static class StubPresentation implements Presentation {
    private int value;
    private int state = -1;
    public void addActionListener(ActionListener l) { }
    public void removeActionListener(ActionListener l) { }
    public void propertyChange(PropertyChangeEvent evt) {
      if (COUNTER.equals(evt.getPropertyName())) {
        value = ((Integer) evt.getNewValue()).intValue();
      } else if (STATE.equals(evt.getPropertyName())) {
        state = ((Integer) evt.getNewValue()).intValue();
      }
    }
    public int getValue() { return value; }
    public int getState() { return state; }
  }
}
