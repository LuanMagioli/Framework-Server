package tads.dipas.server.softwares.sapl;


import org.springframework.hateoas.RepresentationModel;
import tads.dipas.server.model.ImageResponse;
import tads.dipas.server.softwares.img.Image;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany
    private List<Point> aerial;
    @OneToMany
    private List<Point> root;
    private boolean isRegular;

    private Long processTime;

    public Seedling() {
        this.aerial = new ArrayList<Point>();
        this.root = new ArrayList<Point>();
    }

    public Seedling(int index, int[][][] image) {
        this.index = index;
        this.image = image;
        this.processTime = System.nanoTime();
        this.aerial = new ArrayList<Point>();
        this.root = new ArrayList<Point>();
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

    public double getAerialLength(double metric) {
        return aerial.size() / metric;
    }

    public double getRootLength(double metric) {
        return root.size() / metric;
    }

    public double getTotal(double metric) {
        return aerial.size() / metric + root.size() / metric;
    }

    public double getAerialRoot(double metric) {
        return (root.size() / metric) / (aerial.size() / metric);
    }

    public void measureRegular(float regularAerial, float regularRoot, double metric) {
        this.isRegular = !(getAerialLength(metric) < regularAerial || getRootLength(metric) < regularRoot);
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

    public double getTotalLength(double metric) {
        return this.getAerialLength(metric) + this.getRootLength(metric);
    }

    public double getProcessTime() {
        return ((double) this.processTime) / 1_000_000_000;
    }
}

public class SeedlingResponse extends RepresentationModel<ImageResponse> {
    private Long id;
    private int index;

    private int[][][] image;

    private Point middlePoint;

    private List<Point> aerial;

    private List<Point> root;
    private boolean isRegular;

    private Long processTime;

    SeedlingResponse(Image image) {
        id = image.id;
        date = image.date.toString();
        creator = image.user.username;

    // add(linkTo<UserController> {methodOn(UserController::class.java).index()}.withRel("users"))
    // add(linkTo<UserController> {methodOn(UserController::class.java).get(user.id)}.withRel("user"))
    }
}