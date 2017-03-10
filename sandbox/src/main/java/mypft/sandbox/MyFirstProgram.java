package mypft.sandbox;

public class MyFirstProgram {

  public static void main(String[] args) {
    System.out.println("Hello, world!");
    Point p = new Point(1, 3);
    Point other = new Point(5, 10);

    System.out.println("Расстояние между двумя точками с координатами (x1 = " + p.x + ", y1 = " + p.y + "; x2 = " + other.x + ", y2 = " + other.y + ") = " + p.distanceTo(other));
  }

}
