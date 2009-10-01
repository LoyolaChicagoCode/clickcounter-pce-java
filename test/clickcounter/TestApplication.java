package clickcounter;

import junit.framework.TestCase;

public class TestApplication extends TestCase {
  
  private Application app;
  
  private final static int MIN = 0;

  private final static int MAX = 5;

  protected void setUp() throws Exception {
    super.setUp();
    app = new Application(MIN, MAX);
  }

  protected void tearDown() throws Exception {
    app = null;
    super.tearDown();
  }
  
  public void testInit() {
    assertTrue(app.isAtMinimum());
    assertEquals(MIN, app.getValue());
  }
  
  public void testIncrement() {
    assertEquals(MIN, app.getValue());
    app.increment();
    assertFalse(app.isAtMinimum());
    assertEquals(MIN + 1, app.getValue());
  }
  
  public void testMaximum() {
    while (! app.isAtMaximum()) {
      app.increment();
    }
    assertEquals(MAX, app.getValue());
  }
  
  public void testDecrement() {
    testMaximum();
    app.decrement();
    assertEquals(MAX - 1, app.getValue());
  }
  
  public void testMinimum() {
    testMaximum();
    while (! app.isAtMinimum()) {
      app.decrement();
    }
    assertEquals(MIN, app.getValue());
  }
  
  public void testReset() {
    testMaximum();
    app.reset();
    assertEquals(MIN, app.getValue());
  }
}
