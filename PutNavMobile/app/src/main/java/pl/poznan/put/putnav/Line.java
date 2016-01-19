package pl.poznan.put.putnav;

public class Line{
    private float startX, startY, stopX, stopY;

    public Line(float startX, float startY, float stopX, float stopY) {
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
    }

    //point
    public Line(float startX, float startY) {
        this(startX, startY, startX, startY);
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getStopX() {
        return stopX;
    }

    public float getStopY() {
        return stopY;
    }
}
