package tads.dipas.server.image.img;

public class Properties {
    public int area;
    public int[] boundingBox;
    public boolean[][] image;

    public Properties() {
        boundingBox = new int[4];
    }
}