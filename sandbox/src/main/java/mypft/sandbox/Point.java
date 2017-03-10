package mypft.sandbox;

public class Point {

  public double x;
  public double y;
  

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }
  public double distanceTo(Point other) {
    double sum = (this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y);
    return Math.sqrt(sum);
  }

}
