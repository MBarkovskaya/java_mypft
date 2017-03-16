package mypft.sandbox;

public class MyFirstProgram {

  public static void main(String[] args) {
    System.out.println("Hello, world!");
    Point p = new Point(1.0, 5.0);
    Point other = new Point(2.0,4.0);
    System.out.println("Расстояние между двумя точками с координатами (x1 = " + p.getX() + ", y1 = " + p.getY() + "; x2 = " + other.getX() +
            ", y2 = " + other.getY() + ") = " + p.distanceTo(other));
  }

}
