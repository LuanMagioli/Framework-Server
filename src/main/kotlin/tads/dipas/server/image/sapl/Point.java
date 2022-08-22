package tads.dipas.server.image.sapl;

import javax.persistence.*;

@Entity
@Table(name = "point")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private int x;
    private int y;
    @ManyToOne
    @JoinColumn(name = "seedling_id")
    private Seedling seedling;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
