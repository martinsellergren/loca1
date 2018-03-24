package map;

import java.util.LinkedList;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.util.Arrays;

/**
 * A shape defined by coordinate-segments.
 */
public class Shape {

    /**
     * One segment in the shape.
     */
    private class Segment {
        public final double[] p;
        public final double[] q;
        Segment(double[] p, double[] q) {
            this.p = p;
            this.q = q;
        }
    }

    private LinkedList<Segment> segs = new LinkedList<Segment>();

    /**
     * Constructs a shape by connecting lon-lat points, one after
     * the other. Finally closes the path.
     *
     * @param lls LonLat-list.
     */
    public Shape(LinkedList<double[]> lls) {
        for (int i = 0; i < lls.size()-1; i++)
            this.segs.add( new Segment(lls.get(i), lls.get(i+1)) );

        segs.add( new Segment(lls.getLast(), lls.getFirst()) );
    }

    /**
     * Constructs a square-shape.
     *
     * @param bs [wsen] of square.
     */
    public Shape(double[] bs) {
        this.segs.add(new Segment(new double[]{bs[0], bs[1]},
                                  new double[]{bs[2], bs[1]}));
        this.segs.add(new Segment(new double[]{bs[2], bs[1]},
                                  new double[]{bs[2], bs[3]}));
        this.segs.add(new Segment(new double[]{bs[2], bs[3]},
                                  new double[]{bs[0], bs[3]}));
        this.segs.add(new Segment(new double[]{bs[0], bs[3]},
                                  new double[]{bs[0], bs[1]}));
    }

    /**
     * @param p Lonlat.
     * @return True if p is inside shape.
     */
    public boolean isInside(double[] p) {
        if (isEdgePoint(p)) return true;

        // System.out.println(Arrays.toString(p));
        // System.out.println(Arrays.toString(getBounds()));
        // System.out.println("");

        while (true) {
            double[] out = getRandomOutsidePoint();
            int res = isInside(p, out);

            if (res == 0) return false;
            if (res == 1) return true;
        }
    }

    /**
     * @return True if p is ON a segment.
     */
    private boolean isEdgePoint(double[] p) {
        for (Segment seg : this.segs) {
            double[] pv = seg.p;
            double[] v = Math2.minus(seg.q, seg.p);
            if (Math2.same( Math2.distance(p, pv, v), 0 ))
                return true;
        }
        return false;
    }

    /**
     * @return Any point outside this shape.
     */
    private double[] getRandomOutsidePoint() {
        double[] bs = getBounds();
        return new double[]{bs[0] - Math2.randInt(1, 1000),
                            bs[1] - Math2.randInt(1, 1000)};
    }

    /**
     * @param out A point outside this shape.
     * @return 0 = not inside, 1 = inside, 2 = undetermined.
     *
     * Undetermined if line from out to p hits any segment-edge.
     */
    private int isInside(double[] p, double[] out) {
        int crossCount = 0;

        for (Segment seg : this.segs) {
            double[] p0 = seg.p;
            double[] v0 = Math2.minus(seg.q, seg.p);
            double length0 = Math2.distance(seg.p, seg.q);

            double[] p1 = out;
            double[] v1 = Math2.minus(p, out);
            double length1 = Math2.distance(p, out);

            double[] d = Math2.intersectDistance(p0, v0, p1, v1);
            if (Double.isNaN(d[0]) || Double.isNaN(d[1])) return 2;

            if (Math2.same(d[0], 0) || Math2.same(d[1], 0) ||
                Math2.same(d[0], length0) || Math2.same(d[1], length1))
                return 2;

            if (d[0] > 0 && d[0] < length0 &&
                d[1] > 0 && d[1] < length1)
                crossCount++;
        }

        if (crossCount % 2 == 0) return 0;
        else return 1;
    }

    /**
     * @return [wsen]-bounds.
     */
    public double[] getBounds() {
        double lonmin = Double.POSITIVE_INFINITY;
        double latmin = Double.POSITIVE_INFINITY;
        double lonmax = Double.NEGATIVE_INFINITY;
        double latmax = Double.NEGATIVE_INFINITY;

        for (Segment seg : this.segs) {
            if (seg.p[0] < lonmin) lonmin = seg.p[0];
            if (seg.p[1] < latmin) latmin = seg.p[1];
            if (seg.p[0] > lonmax) lonmax = seg.p[0];
            if (seg.p[1] > latmax) latmax = seg.p[1];
        }

        return new double[]{lonmin, latmin, lonmax, latmax};
    }

    //------------------------------------------------TESTING

    public void drawYourself(BasicImage img, MapImageView v) {
        Graphics2D g = img.createGraphics();

        for (Segment seg : this.segs) {
            int[] a = Math2.toInt(v.getPixelCoordinates(seg.p));
            int[] b = Math2.toInt(v.getPixelCoordinates(seg.q));

            g.setStroke(new BasicStroke(5));
            g.setPaint(Color.BLUE);

            g.drawLine(a[0], a[1], b[0], b[1]);
        }
    }
}
