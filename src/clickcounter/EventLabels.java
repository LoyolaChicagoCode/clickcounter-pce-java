package clickcounter;

/**
 * Labels of events known to the UI layer.
 */

interface EventLabels {

  // action commands

  String INCREMENT = "increment";
  String DECREMENT = "decrement";
  String RESET     = "reset";

  // properties

  String STATE   = "State";
  String COUNTER = "Counter";

  // states
  int UI_MINIMUM = 777;
  int UI_COUNTING = 778;
  int UI_MAXIMUM = 779;
}