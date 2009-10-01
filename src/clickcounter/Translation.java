package clickcounter;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Provides the interface behaviour for the interactive
 * Application.
 */

public class Translation implements ActionListener, EventLabels {

  private Application theCounter;
  private ClickCounterState theState;

  /** The list of all types of event listeners. */
  private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

  public Translation() {
    theState = MINIMUM;
  }

  /**
   * This method provides this translation with a reference to the
   * application.
   */
  public void setApplication(Application app) {
    theCounter = app;
  }

  /**
   * This method provides this translation with a visible interface
   * (presentation).
   */
  public void initInterfaces() {
    scheduleUpdate(theCounter.getValue());
    scheduleState(UI_MINIMUM);
  } // end init.

  public void actionPerformed(ActionEvent event) {
    // handle global events first
    String buttonPressed = event.getActionCommand();
    if (RESET.equals(buttonPressed)) {
        theCounter.reset();
        changeState(MINIMUM);
        scheduleUpdate(theCounter.getValue());
        scheduleState(UI_MINIMUM);
    } else {
      // use the State pattern for handling state-specific events
      theState.actionPerformed(event);
    }
  }

  protected void changeState(ClickCounterState state) {
    theState = state;
  }

  /**
   * Interface to support the State pattern.
   */
  private interface ClickCounterState extends ActionListener {
  }

  private final ClickCounterState MINIMUM = new ClickCounterState() {
    public void actionPerformed(ActionEvent event) {
      String buttonPressed = event.getActionCommand();
      if (INCREMENT.equals(buttonPressed)) {
        theCounter.increment();
        if (theCounter.isAtMaximum()) {
          changeState(MAXIMUM);
          scheduleState(UI_MAXIMUM);
        } else {
          changeState(COUNTING);
          scheduleState(UI_COUNTING);
        }
        scheduleUpdate(theCounter.getValue());
      }
    }
  };

  private final ClickCounterState COUNTING = new ClickCounterState() {
    public void actionPerformed(ActionEvent event) {
      String buttonPressed = event.getActionCommand();
      if (INCREMENT.equals(buttonPressed)) {
        theCounter.increment();
        if (theCounter.isAtMaximum()) {
          changeState(MAXIMUM);
          scheduleState(UI_MAXIMUM);
        }
        scheduleUpdate(theCounter.getValue());
      } else if (DECREMENT.equals(buttonPressed)) {
        theCounter.decrement();
        if (theCounter.isAtMinimum()) {
          changeState(MINIMUM);
          scheduleState(UI_MINIMUM);
        } // End if.
        scheduleUpdate(theCounter.getValue());
      } // End if.
    }
  };

  private final ClickCounterState MAXIMUM = new ClickCounterState() {
    public void actionPerformed(ActionEvent event) {
      String buttonPressed = event.getActionCommand();
      if (DECREMENT.equals(buttonPressed)) {
        theCounter.decrement();
        if (theCounter.isAtMinimum()) {
          changeState(MINIMUM);
          scheduleState(UI_MINIMUM);
        } else {
          changeState(COUNTING);
          scheduleState(UI_COUNTING);
        } // End if.
        scheduleUpdate(theCounter.getValue());
      } // End if.
    }
  };

  public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
    listeners.addPropertyChangeListener(l);
  }

  public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
    listeners.removePropertyChangeListener(l);
  }

  protected void fireUpdate(int value) {
    listeners.firePropertyChange(new PropertyChangeEvent(this, COUNTER, null, new Integer(value)));
  }

  protected void fireState(int state) {
    listeners.firePropertyChange(new PropertyChangeEvent(this, STATE, null, new Integer(state)));
  }

  protected void scheduleUpdate(final int value) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        fireUpdate(value);
      }
    });
  }

  protected void scheduleState(final int state) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        fireState(state);
      }
    });
  }
} // end class Translation.
