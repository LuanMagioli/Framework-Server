package tads.dipas.image_processing;

public class Properties {
    public int area;
    public int[] boundingBox;
    public boolean[][] image;

    public Properties() {
        boundingBox = new int[4];
    }
}