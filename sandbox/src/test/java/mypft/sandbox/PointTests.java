package mypft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PointTests {

  @Test
  public void testDistanceTo() {
    Point p = new Point(1.0, 2.0);
    Point other = new Point(17.0, 19.0);
    Assert.assertEquals(p.distanceTo(other), 23.345235059857504);
  }
}
