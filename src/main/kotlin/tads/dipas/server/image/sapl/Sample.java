package tads.dipas.server.image.sapl;

import tads.dipas.server.image.img.Image;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sample")
public class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Transient
    private int[][][] image;
    @OneToMany(mappedBy = "sample")
    private List<Seedling> seedlings;
    private String path;
    private String file;
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
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
        String[] split = this.file.split("\\.");
        return split[0];
    }

    public double tallestSeedling() {
        double maior = 0;
        for (Seedling p : this.seedlings) {
            if (p.getTotalLength() > maior)
                maior = p.getTotalLength();
        }
        return maior;
    }
}
