package mypft.sandbox;

public class MyFirstProgram {

  public static void main(String[] args) {
    System.out.println("Hello, world!");
    Point p = new Point(1, 3);
    Point other = new Point(5, 10);

    System.out.println("Расстояние между двумя точками с координатами (x1 = " + 1.0 + ", y1 = " + 4.0 + "; x2 = " + 12.0 + ", y2 = " + 5.0 + ") = " + p.distanceTo(other));
  }

}
