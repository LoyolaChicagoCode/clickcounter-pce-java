package clickcounter;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

/**
 * An interface for Application presentation classes.
 */

public interface Presentation extends PropertyChangeListener {

  void addActionListener(ActionListener l);
  void removeActionListener(ActionListener l);

} // end class Presentation.
