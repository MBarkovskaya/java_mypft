package mypft.sandbox;

public class MyFirstProgram {

  public static void main(String[] args) {
    System.out.println("Hello, world!");
    Point p1 = new Point(1, 4);
    Point p2 = new Point(5, 10);
    System.out.println("Расстояние между двумя точками p1 и p2" + " = " + distance(p1, p2));
  }
  public static double distance(Point p1, Point p2) {
    double sum = (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
    return Math.sqrt(sum);
  }

}