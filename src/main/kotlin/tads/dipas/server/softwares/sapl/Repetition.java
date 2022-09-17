package tads.dipas.server.softwares.sapl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Repetition {
    private ArrayList<Sample> samples;
    private float normalAérea;
    private float normalRadicular;
    private int IndiceEspécie;
    private int IndiceGerminação;
    private double wc;
    private double wu;

    //Resultados
    private double mediaR;
    private double mediaA;
    private double mediaT;

    private double desvioR;
    private double desvioA;
    private double desvioT;
    private double desvioRA;

    private double indiceV;
    private double indiceC;
    private double indiceU;
    private double indiceVC;



    public Repetition(){
        this.samples = new ArrayList<>();
        this.normalAérea = 11;
        this.normalRadicular = 11;
        IndiceEspécie = 1;
        IndiceGerminação = 97;
        this.wc = 0.7;
        this.wu = 1 - this.wc;
    }

    public Repetition(float normalAérea, float normalRadicular, int indiceEspécie, int indiceGerminação, double wc) {
        this.normalAérea = normalAérea;
        this.normalRadicular = normalRadicular;
        IndiceEspécie = indiceEspécie;
        IndiceGerminação = indiceGerminação;
        this.wc = wc;
        this.wu = 1 - this.wc;
        this.samples = new ArrayList<>();
    }

    public void setAmostra(Sample a){
        for(Seedling p : a.getSeedlings())
            p.measureRegular(this.normalAérea, this.normalRadicular, 55555);
        samples.add(a);
    }

    public double getIndiceV() {
        return indiceV;
    }

    public void setIndiceV(double indiceV) {
        this.indiceV = indiceV;
    }

    public double getIndiceC() {
        return indiceC;
    }

    public void setIndiceC(double indiceC) {
        this.indiceC = indiceC;
    }

    public double getIndiceU() {
        return indiceU;
    }

    public void setIndiceU(double indiceU) {
        this.indiceU = indiceU;
    }

    public double getIndiceVC() {
        return indiceVC;
    }

    public void setIndiceVC(double indiceVC) {
        this.indiceVC = indiceVC;
    }

    public ArrayList<Sample> getAmostras() {
        return samples;
    }

    public void setAmostras(ArrayList<Sample> samples) {
        this.samples = samples;
    }

    public float getNormalAérea() {
        return normalAérea;
    }

    public void setNormalAérea(float normalAérea) {
        this.normalAérea = normalAérea;
    }

    public float getNormalRadicular() {
        return normalRadicular;
    }

    public void setNormalRadicular(float normalRadicular) {
        this.normalRadicular = normalRadicular;
    }

    public int getIndiceEspécie() {
        return IndiceEspécie;
    }

    public void setIndiceEspécie(int indiceEspécie) {
        IndiceEspécie = indiceEspécie;
    }

    public int getIndiceGerminação() {
        return IndiceGerminação;
    }

    public void setIndiceGerminação(int indiceGerminação) {
        IndiceGerminação = indiceGerminação;
    }

    public double getWc() {
        return wc;
    }

    public void setWc(double wc) {
        this.wc = wc;
    }

    public double getWu() {
        return wu;
    }

    public void setWu(double wu) {
        this.wu = wu;
    }

    public double getMediaR() {
        return mediaR;
    }

    public void setMediaR(double mediaR) {
        this.mediaR = mediaR;
    }

    public double getMediaA() {
        return mediaA;
    }

    public void setMediaA(double mediaA) {
        this.mediaA = mediaA;
    }

    public double getMediaT() {
        return mediaT;
    }

    public void setMediaT(double mediaT) {
        this.mediaT = mediaT;
    }

    public double getDesvioR() {
        return desvioR;
    }

    public void setDesvioR(double desvioR) {
        this.desvioR = desvioR;
    }

    public double getDesvioA() {
        return desvioA;
    }

    public void setDesvioA(double desvioA) {
        this.desvioA = desvioA;
    }

    public double getDesvioT() {
        return desvioT;
    }

    public void setDesvioT(double desvioT) {
        this.desvioT = desvioT;
    }

    public double getDesvioRA() {
        return desvioRA;
    }

    public void setDesvioRA(double desvioRA) {
        this.desvioRA = desvioRA;
    }

    public void processamentoFinal() {
        ArrayList<Double> radicular = new ArrayList<>();
        ArrayList<Double> aérea = new ArrayList<>();
        ArrayList<Double> total = new ArrayList<>();
        ArrayList<Double> radiculaAérea = new ArrayList<>();

        for(Sample a : this.samples){
            for(Seedling p : a.getSeedlings()){
                radicular.add(p.getRootLength(55555));
                aérea.add(p.getAerialLength(55555));
                total.add(p.getTotal(55555));
                radiculaAérea.add(p.getAerialRoot(55555));
            }
        }

        this.mediaR = getMedia(radicular);
        this.mediaA = getMedia(aérea);
        this.mediaT = getMedia(total);

        this.desvioR = desvioPadrão(radicular);
        this.desvioA = desvioPadrão(aérea);
        this.desvioT = desvioPadrão(total);
        this.desvioRA = desvioPadrão(radiculaAérea);

        //Calculando índices
        double wh = 5;
        double wr = 2.5;
        double wrh = 50;

        double whU = 0.75;
        double wrU = 0.5;
        double wtotalU = 2.5;

        this.indiceU = (1000 - ( (whU*(desvioA * 10)) + (wrU*(desvioR * 10)) + (wtotalU*(desvioT * 10)) + (wrh*desvioRA)));
        this.indiceC = (10*((wr*mediaR)+(wh*mediaA)));
        this.indiceV = (indiceU*wu) + (indiceC*wc);
        this.indiceVC = indiceV*(IndiceGerminação/100.0);
    }

    private double getMedia(ArrayList<Double> lista){
        double soma =  lista.stream().mapToDouble(f -> f.doubleValue()).sum();
        return soma/lista.size();
    }

    private double desvioPadrão(ArrayList<Double> lista){
        double media = getMedia(lista);
        double var = 0;
        for(Double d : lista){
            var += Math.pow(d - media, 2);
        }
        var = var / lista.size();
        return Math.sqrt(var);
    }

    public void gerarCSV() {
        try (PrintWriter writer = new PrintWriter(new File("Resultados-java.csv"))) {
            StringBuilder sb = new StringBuilder();
            String[] texto  = {"Imagem", "Índice", "Parte Aérea (mm)", "Parte Radicular (mm)", "Total (mm)", "Nível de Uniformidade (Perc.)", "Tempo Gasto (Sec.)", "Germinação"};

            for(String título : texto){
                sb.append(título);
                sb.append(";");
            }
            sb.append('\n');

            Locale l = new Locale("pt","BR");
            NumberFormat nf = NumberFormat.getInstance(l);
            for(Sample a : samples){
                double maior = a.tallestSeedling();
                for(Seedling p : a.getSeedlings()){
                    sb.append(a.getFilename() + ";");
                    sb.append(p.getIndex() + ";");
                    sb.append(nf.format(p.getAerialLength(55555)) + ";");
                    sb.append(nf.format(p.getRootLength(55555)) + ";");
                    sb.append(nf.format(p.getTotalLength(55555)) + ";");
                    sb.append(nf.format(((p.getTotalLength(55555)*100) / maior)) + ";");
                    sb.append(nf.format(p.getProcessTime()) + ";");
                    if(p.isIsRegular())
                        sb.append("Normal");
                    else
                        sb.append("Anormal");
                    sb.append('\n');
                }
            }
            writer.write(sb.toString());
            System.out.println("Pronto!");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
}
