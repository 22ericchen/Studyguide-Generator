package cs3500.pa02.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DriverTest {
  /**
   * Tests if Driver catches the case that there not either 3 or 0
   * command line arguments
   */
  @Test
  public void testMainException() {
    //String array of one argument
    String[] arg = new String[]{"one argument"};
    //String array of 4 arguments
    String[] args = new String[]{"four arguments", "2th", "3th.md", "4th"};

    assertThrows(IllegalArgumentException.class, ()-> {Driver.main(arg);});
    assertThrows(IllegalArgumentException.class, ()-> {Driver.main(args);});
  }

}