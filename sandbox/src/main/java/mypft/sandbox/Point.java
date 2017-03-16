package mypft.sandbox;

public class Point {

  private double x;
  private double y;

  public Point() {
  }

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double distanceTo(Point other) {
    double sum = (this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y);
    return Math.sqrt(sum);
  }

}
