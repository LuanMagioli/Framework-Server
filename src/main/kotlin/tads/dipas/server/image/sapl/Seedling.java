package tads.dipas.server.image.sapl;


import tads.dipas.server.image.img.Image;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "seedling")
public class Seedling {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private int index;
    @Transient
    private int[][][] image;
    @OneToOne
    @JoinColumn(name = "middlepoint_id")
    private Point middlePoint;

    @OneToMany(mappedBy = "seedling")
    private List<Point> aerial;
    @OneToMany(mappedBy = "seedling")
    private List<Point> root;
    private boolean isRegular;

    @ManyToOne
    @JoinColumn(name = "sample_id")
    private Sample sample;
    private Long processTime;

    public Seedling() {
    }

    public Seedling(int index, int[][][] image) {
        this.index = index;
        this.image = image;
        this.processTime = System.nanoTime();
    }

    @Override
    public String toString() {
        return "plantula_" + index;
    }

    public int[][][] drawImage() {
        int[][][] im = Image.copy(this.image);

        for (int j = 0; j < aerial.size(); j++) {
            im[aerial.get(j).getX()][aerial.get(j).getY()][0] = 255;
            im[aerial.get(j).getX()][aerial.get(j).getY()][1] = 0;
            im[aerial.get(j).getX()][aerial.get(j).getY()][2] = 0;
        }

        for (int j = 0; j < root.size(); j++) {
            im[root.get(j).getX()][root.get(j).getY()][0] = 0;
            im[root.get(j).getX()][root.get(j).getY()][1] = 255;
            im[root.get(j).getX()][root.get(j).getY()][2] = 0;
        }

        return im;
    }

    public void updateTime() {
        this.processTime = System.nanoTime() - this.processTime;
    }

    public double getAerialLength() {
        return aerial.size() / sample.getMetric();
    }

    public double getRootLength() {
        return root.size() / sample.getMetric();
    }

    public double getTotal() {
        return aerial.size() / sample.getMetric() + root.size() / sample.getMetric();
    }

    public double getAerialRoot() {
        return (root.size() / sample.getMetric()) / (aerial.size() / sample.getMetric());
    }

    public void measureRegular(float regularAerial, float regularRoot) {
        this.isRegular = !(getAerialLength() < regularAerial || getRootLength() < regularRoot);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Point> getAerial() {
        return aerial;
    }

    public void setAerial(List<int[]> aerial) {
        for (int[] p : aerial) {
            this.aerial.add(new Point(p[0], p[1]));
        }
    }

    public List<Point> getRoot() {
        return root;
    }

    public void setRoot(List<int[]> root) {
        for (int[] p : root) {
            this.root.add(new Point(p[0], p[1]));
        }
    }

    public int[][][] getImage() {
        return image;
    }

    public void setImage(int[][][] image) {
        this.image = image;
    }

    public Point getMiddlePoint() {
        return middlePoint;
    }

    public void setMiddlePoint(int[] middlePoint) {
        this.middlePoint = new Point(middlePoint[0], middlePoint[1]);
    }

    public boolean isIsRegular() {
        return isRegular;
    }

    public void setIsRegular(boolean normal) {
        this.isRegular = normal;
    }

    public double getTotalLength() {
        return this.getAerialLength() + this.getRootLength();
    }

    public double getProcessTime() {
        return ((double) this.processTime) / 1_000_000_000;
    }
}
