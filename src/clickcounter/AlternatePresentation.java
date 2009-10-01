package clickcounter;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.EventListener;

/**
 * An interface whose presentation is built entirely using JBuilder.
 * All commented code was added manually.
 */

public class AlternatePresentation extends JPanel implements Presentation, EventLabels {
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel valueDisplay = new JLabel();
  JPanel jPanel1 = new JPanel();
  JButton incrementButton = new JButton();
  GridLayout gridLayout1 = new GridLayout();
  JButton resetButton = new JButton();
  JButton decrementButton = new JButton();

  /**
   * List of listeners for making this presentation object an event source.
   * In this way, the presentation knows nothing about the translation.  The
   * translation is simply added as one of the listeners.
   */
  private EventListenerList listeners = new EventListenerList();

  /**
   * Default constructor.
   */
  public AlternatePresentation() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    valueDisplay.setHorizontalAlignment(SwingConstants.CENTER);
    valueDisplay.setText("##");
    this.setLayout(borderLayout1);
    incrementButton.setBackground(Color.green);
    incrementButton.setActionCommand("increment");
    incrementButton.setText("+");
    incrementButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        incrementButton_actionPerformed(e);
      }
    });
    jPanel1.setLayout(gridLayout1);
    resetButton.setBackground(Color.green);
    resetButton.setForeground(Color.red);
    resetButton.setActionCommand("reset");
    resetButton.setText("0");
    resetButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        resetButton_actionPerformed(e);
      }
    });
    decrementButton.setBackground(Color.green);
    decrementButton.setActionCommand("decrement");
    decrementButton.setText("-");
    decrementButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        decrementButton_actionPerformed(e);
      }
    });
    this.setBackground(Color.orange);
    jPanel1.setBackground(Color.red);
    this.add(valueDisplay,  BorderLayout.CENTER);
    this.add(jPanel1,  BorderLayout.SOUTH);
    jPanel1.add(incrementButton, null);
    jPanel1.add(resetButton, null);
    jPanel1.add(decrementButton, null);
  }

  /**
   * Forwards this event to the translation object.
   */
  void incrementButton_actionPerformed(ActionEvent e) {
    fireAction(EventLabels.INCREMENT);
  }

  /**
   * Forwards this event to the translation object.
   */
  void resetButton_actionPerformed(ActionEvent e) {
    fireAction(EventLabels.RESET);
  }

  /**
   * Forwards this event to the translation object.
   */
  void decrementButton_actionPerformed(ActionEvent e) {
    fireAction(EventLabels.DECREMENT);
  }

  /**
   * Makes the presentation correspond to the logical minimum state
   * in the translation.
   */
  protected void setMinimumState() {
    incrementButton.setEnabled(true);
    resetButton.setEnabled(false);
    decrementButton.setEnabled(false);
  } // End setMinimumState.

  /**
   * Makes the presentation correspond to the logical counting state
   * in the translation.
   */
  protected void setCountingState() {
    incrementButton.setEnabled(true);
    resetButton.setEnabled(true);
    decrementButton.setEnabled(true);
  } // End setCountingState.

  /**
   * Makes the presentation correspond to the logical maximum state
   * in the translation.
   */
  protected void setMaximumState() {
    incrementButton.setEnabled(false);
    resetButton.setEnabled(true);
    decrementButton.setEnabled(true);
  } // End setMaximumState.

  /**
   * Adds an ActionListener to this presentation object.
   */
  public synchronized void addActionListener(ActionListener l) {
    listeners.add(ActionListener.class, l);
  }

  /**
   * Removes an ActionListener from this presentation object.
   */
  public synchronized void removeActionListener(ActionListener l) {
    listeners.remove(ActionListener.class, l);
  }

  protected void fireAction(String command) {
    ActionEvent e = null;
    EventListener[] ls = listeners.getListeners(ActionListener.class);
    for (int i = ls.length - 1; i >= 0; i -= 1) {
      if (e == null) {
        e = new ActionEvent(this, 0, command);
      }
      ((ActionListener) ls[i]).actionPerformed(e);
    }
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if (COUNTER.equals(evt.getPropertyName())) {
      valueDisplay.setText(evt.getNewValue().toString());
    } else if (STATE.equals(evt.getPropertyName())) {
      int state = ((Integer) evt.getNewValue()).intValue();
      switch (state) {
        case UI_MINIMUM:  setMinimumState();  break;
        case UI_COUNTING: setCountingState(); break;
        case UI_MAXIMUM:  setMaximumState();  break;
      }
    }
  }
}