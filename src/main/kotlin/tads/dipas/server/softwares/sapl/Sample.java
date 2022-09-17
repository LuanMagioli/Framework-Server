package tads.dipas.server.softwares.sapl;

import tads.dipas.server.softwares.img.Image;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sample")
public class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Long id;
    @Transient
    private int[][][] image;
    @OneToMany
    private List<Seedling> seedlings;
    private String path;
    private double metric;

    public Sample(String path) {
        this.image = Image.imRead(path);
        this.path = path;
        seedlings = new ArrayList<>();
    }

    public Sample() {

    }

    public void setSeedling(Seedling seedling) {
        this.seedlings.add(seedling);
    }

    public Seedling getSeedling(int i) {
        return seedlings.get(i);
    }


    public List<Seedling> getSeedlings() {
        return seedlings;
    }

    public int[][][] getImage() {
        return image;
    }

    public void setImage(int[][][] imagem) {
        this.image = imagem;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String caminho) {
        this.path = caminho;
    }

    public double getMetric() {
        return metric;
    }

    public void setMetric(double metric) {
        this.metric = metric / 10;
    }

    public String getFilename() {
        String[] split = this.path.split("\\.");
        return split[0];
    }

    public double tallestSeedling() {
        double maior = 0;
        for (Seedling p : this.seedlings) {
            if (p.getTotalLength(this.metric) > maior)
                maior = p.getTotalLength(this.metric);
        }
        return maior;
    }
}
