package pl.poznan.put.putnav;

import android.graphics.Path;
import android.graphics.Point;

public class Arrow {
    private double[] quadraticEquationRoot(double a, double b, double c) {
        double root1, root2;
        root1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        root2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        return new double[]{root1, root2};
    }

    public double[] getFactors(MapPoint mp1, MapPoint mp2) {
        double x1 = (double) mp1.getX();
        double y1 = (double) mp1.getY();
        double x2 = (double) mp2.getX();
        double y2 = (double) mp2.getY();
        double tmpY = y2 - y1;
        double tmpX = x2 - x1;
        double a = tmpY / tmpX;
        double b = y1 - x1 * a;

        return new double[]{a, b};
    }

    public Path buildTriangleStart(MapPoint mp1, MapPoint mp2) {

        Point a, b, c;
        Path path = new Path();
        if (mp1.getX() == mp2.getX()) {
            if (mp2.getY() > mp1.getY()) {
                a = new Point(mp2.getX(), mp1.getY() + 55);
                b = new Point(mp2.getX() - 22, mp1.getY());
                c = new Point(mp2.getX() + 22, mp1.getY());
            } else {
                a = new Point(mp2.getX(), mp1.getY() - 55);
                b = new Point(mp2.getX() - 22, mp1.getY());
                c = new Point(mp2.getX() + 22, mp1.getY());
            }

        } else {

            double[] factors1 = getFactors(mp1, mp2);
            double x1 = (double) mp1.getX();
            double y1 = (double) mp1.getY();
            double[] roots1 = quadraticEquationRoot(
                    1.0 + factors1[0] * factors1[0],
                    -2 * x1 + 2 * factors1[0] * factors1[1] - 2 * factors1[0] * y1,
                    x1 * x1 + y1 * y1 + factors1[1] * factors1[1] - 2 * y1 * factors1[1] - 3000);
            double xt0 = 0;
            if (mp1.getX() > mp2.getX()) {
                xt0 = Math.min(roots1[0], roots1[1]);
            } else {
                xt0 = Math.max(roots1[0], roots1[1]);
            }
            double yt0 = xt0 * factors1[0] + factors1[1];
            if (factors1[0] == 0) {
                factors1[0] += 0.00000001;
            }
            double[] factors2 = {-1 / factors1[0], y1 + 1 / factors1[0] * x1};
            double[] roots2 = quadraticEquationRoot(
                    1.0 + factors2[0] * factors2[0],
                    -2 * x1 + 2 * factors2[0] * factors2[1] - 2 * factors2[0] * y1,
                    x1 * x1 + y1 * y1 + factors2[1] * factors2[1] - 2 * y1 * factors2[1] - 500);
            double xt1 = Math.max(roots2[0], roots2[1]);
            double xt2 = Math.min(roots2[0], roots2[1]);
            double yt1 = xt1 * factors2[0] + factors2[1];
            double yt2 = xt2 * factors2[0] + factors2[1];

            a = new Point((int) xt0, (int) yt0);
            b = new Point((int) xt1, (int) yt1);
            c = new Point((int) xt2, (int) yt2);
        }
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();
        return path;
    }


    public Path buildTriangleStop(MapPoint mp1, MapPoint mp2) {

        Point a;
        Point b;
        Point c;
        Path triangle = new Path();

        if (mp1.getX() == mp2.getX()) {
            if (mp2.getY() > mp1.getY()) {
                a = new Point(mp2.getX(), mp2.getY() + 33);
                b = new Point(mp2.getX() - 22, mp2.getY());
                c = new Point(mp2.getX() + 22, mp2.getY());
            } else {
                a = new Point(mp2.getX(), mp2.getY() - 33);
                b = new Point(mp2.getX() - 22, mp2.getY());
                c = new Point(mp2.getX() + 22, mp2.getY());
            }

        } else {

            double[] factors1 = getFactors(mp2, mp1);
            double x1 = (double) mp2.getX();
            double y1 = (double) mp2.getY();
            double[] roots1 = quadraticEquationRoot(
                    1.0 + factors1[0] * factors1[0],
                    -2 * x1 + 2 * factors1[0] * factors1[1] - 2 * factors1[0] * y1,
                    x1 * x1 + y1 * y1 + factors1[1] * factors1[1] - 2 * y1 * factors1[1] - 3000);
            double xt0 = 0;
            if (mp1.getX() < mp2.getX()) {
                xt0 = Math.max(roots1[0], roots1[1]);
            } else {
                xt0 = Math.min(roots1[0], roots1[1]);
            }
            double yt0 = xt0 * factors1[0] + factors1[1];
            if (factors1[0] == 0) {
                factors1[0] += 0.00000001;
            }
            double[] factors2 = {-1 / factors1[0], y1 + 1 / factors1[0] * x1};   // - +
            double[] roots2 = quadraticEquationRoot(
                    1.0 + factors2[0] * factors2[0],
                    -2 * x1 + 2 * factors2[0] * factors2[1] - 2 * factors2[0] * y1,
                    x1 * x1 + y1 * y1 + factors2[1] * factors2[1] - 2 * y1 * factors2[1] - 500);
            double xt1 = Math.max(roots2[0], roots2[1]);
            double xt2 = Math.min(roots2[0], roots2[1]);
            double yt1 = xt1 * factors2[0] + factors2[1];
            double yt2 = xt2 * factors2[0] + factors2[1];
            a = new Point((int) xt0, (int) yt0);
            b = new Point((int) xt1, (int) yt1);
            c = new Point((int) xt2, (int) yt2);
        }

        triangle.setFillType(Path.FillType.EVEN_ODD);
        triangle.moveTo(a.x, a.y);
        triangle.lineTo(b.x, b.y);
        triangle.lineTo(c.x, c.y);
        triangle.lineTo(a.x, a.y);
        triangle.close();
        return triangle;
    }
}
