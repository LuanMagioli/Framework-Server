package tads.dipas.server.image.sapl;

import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import tads.dipas.server.image.img.Image;
import tads.dipas.server.image.img.Properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SAPL {
    public static Sample processarImagem(Sample sample) {
        sample.setImage(resize(sample.getImage()));

        boolean[][] imBackground = background(sample.getImage());
        Verificacao pontosCorte = verificaImagem(imBackground);

        //Image.imWrite(imBackground, "im/Moringa/results/background.png");

        removeMarcacao(sample.getImage(), imBackground, pontosCorte.caminhos, pontosCorte.tamanho);
        //Image.imWrite(imBackground, "im/Moringa/results/background_semMarcacao.png");

        boolean[][] imMetrica = Image.imCrop(imBackground, 0, pontosCorte.posicoes.get(pontosCorte.tamanho - 1),
                imBackground.length - 1, imBackground[0].length - 1);
        Image.imWrite(imMetrica, "im/Moringa/Metricas/" + sample.getFilename() + ".png");

        double dist = 0;
        try {
            dist = metrica(imMetrica);
        } catch (Exception e) {
            throw new Error("Não foi possível encontrar a métrica");
        }

        sample.setMetric(dist);

        for (int i = 0; i < pontosCorte.tamanho - 1; i++) {
            Seedling p = new Seedling(
                    i + 1, Image.imCrop(sample.getImage(), 0, pontosCorte.posicoes.get(i),
                    imBackground.length, pontosCorte.posicoes.get(i + 1))
            );
            boolean[][] bin = Image.imCrop(imBackground, 0, pontosCorte.posicoes.get(i),
                    imBackground.length, pontosCorte.posicoes.get(i + 1));

            try {
                p = processarPlantula(p, bin, sample.getFilename());
                if (p != null) {
                    sample.setSeedling(p);
                }
            } catch (Error e) {
                System.out.println("------------------------------------");
                System.out.println("Erro na plântula " + i + 1 + ": " + e.getMessage());
                System.out.println("Por favor verificar se a imagem encontra-se nos padrões ou solicitar ajuda ao suporte.");
                System.out.println("Caminho da imagem: " + sample.getPath());
                System.out.println("------------------------------------");
            }
        }

        return sample;
    }

    private static int[][][] resize(int[][][] im) {
        while ((im.length * im[0].length) > 800000) {
            im = Image._imResize(im);
        }
        return im;
    }

    private static boolean[][] background(int[][][] im) {
        int[][][] imYCBCR = Image.rgb2ycbcr(im);

        int[][] Y = Image.splitChannel(imYCBCR, 0);
        int[][] Cb = Image.splitChannel(imYCBCR, 1);
        int[][] Cr = Image.splitChannel(imYCBCR, 2);

        int[][] a = new int[im.length][im[0].length];
        int[][] b = new int[im.length][im[0].length];
        int[][] c = new int[im.length][im[0].length];

        for (int i = 0; i < im.length; i++) {
            for (int j = 0; j < im[0].length; j++) {
                a[i][j] = Cb[i][j] - Cr[i][j];
                if (a[i][j] > 255) a[i][j] = 255;
                if (a[i][j] < 0) a[i][j] = 0;

                b[i][j] = Cr[i][j] + Y[i][j];
                if (b[i][j] > 255) b[i][j] = 255;
                if (b[i][j] < 0) b[i][j] = 0;

                b[i][j] = b[i][j] - Cb[i][j];
                if (b[i][j] > 255) b[i][j] = 255;
                if (b[i][j] < 0) b[i][j] = 0;

                c[i][j] = b[i][j] - a[i][j];
                if (c[i][j] > 255) c[i][j] = 255;
                if (c[i][j] < 0) c[i][j] = 0;
            }
        }

        int[] hist = Image.imHist(c);

        int lim = 0;

        for (int i = 0; i < hist.length - 2; i++) {
            if ((hist[i] < hist[i + 1]) && (hist[i + 1] < hist[i + 2])) {
                lim = i;
                break;
            }
        }

        boolean[][] imBin = new boolean[im.length][im[0].length];

        for (int i = 0; i < im.length; i++) {
            for (int j = 0; j < im[0].length; j++) {
                imBin[i][j] = (c[i][j] > lim);
            }
        }

        return imBin;
    }

    private static Verificacao verificaImagem(boolean[][] im) {
        Verificacao v = new Verificacao();
        int pos = 0;

        for (int i = 1; i < im[0].length; i++) {
            if (im[0][i]) {
                if (!im[0][i - 1]) {
                    v.caminhos.add(i);
                    v.posicoes.add(pos);
                    v.tamanho++;
                } else {
                    pos = i + 1;
                }
            }
            if (i + 1 == im[0].length) {
                v.caminhos.add(i);
                v.posicoes.add(pos);
                v.tamanho++;
            }
        }

        return v;
    }

    private static void removeMarcacao(int[][][] im, boolean[][] bw, List<Integer> cam, int tam) {
        int[][] bwLabel = Image.bwLabel(bw);

        for (int l = 0; l < tam - 1; l++) {
            for (int i = 0; i < bwLabel.length; i++) {
                for (int j = 0; j < bwLabel[0].length; j++) {
                    if (bwLabel[i][j] == bwLabel[0][cam.get(l)]) {
                        bw[i][j] = false;
                        im[i][j][0] = 0;
                        im[i][j][1] = 0;
                        im[i][j][2] = 255;
                    }
                }
            }
        }
    }

    private static double metrica(boolean[][] met) {
        //met = Image.bwOpen(met, 1);
        ArrayList<Properties> prop = Image.regionProps(met);

        int[] Areas = new int[prop.size()];

        for (int i = 0; i < prop.size(); i++) {
            Areas[i] = prop.get(i).area;
        }

        Arrays.sort(Areas);

        int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
        boolean flag = true;

        for (int i = 0; i < prop.size(); i++) {
            if (prop.get(i).area == Areas[0] && flag) {
                x1 = prop.get(i).boundingBox[0] + prop.get(i).image.length / 2;
                y1 = prop.get(i).boundingBox[1] + prop.get(i).image[0].length / 2;
                flag = false;
            } else {
                if (prop.get(i).area == Areas[1]) {
                    x2 = prop.get(i).boundingBox[0] + prop.get(i).image.length / 2;
                    y2 = prop.get(i).boundingBox[1] + prop.get(i).image[0].length / 2;
                }
            }
        }
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private static Seedling processarPlantula(Seedling seedling, boolean[][] bw, String filename) {
        bw = Image.bwClose(bw, 5);

        //Image.imWrite(bw, "im/Moringa/results/plantula_" + plântula + ".png");
        //Image.imWrite(plântula.getImagem(), "im/Moringa/results/binario_" + plântula + ".png");
        try {
            bw = maiorObjeto(bw);
            Image.imWrite(seedling.getImage(), "im/Moringa/results/" + filename + "_" + seedling + ".png");
            Image.imWrite(bw, "im/Moringa/results/" + filename + "_" + seedling + "_bin" + ".png");
            if (Image.max(Image.bwLabel(bw)) > 1) {
                bw = juntarPontos(bw);
            }
            int[] p = pontodeCorte(seedling.getImage(), filename + "_" + seedling);
            seedling = medirDistancias(seedling, bw, p, filename);
            seedling.updateTime();
            return seedling;
        } catch (Error e) {
            return null;
        }
    }

    private static boolean[][] maiorObjeto(boolean[][] im) {
        boolean[][] resultado = new boolean[im.length][im[0].length];
        int[][] lb = Image.bwLabel(im);
        int[] areas = new int[Image.max(lb)];

        if (Image.max(lb) == 0) {
            throw new Error("Não foi possível encontrar o maior objeto");
        }

        for (int l = 0; l < Image.max(lb); l++) {
            for (int i = 0; i < lb.length; i++) {
                for (int j = 0; j < lb[0].length; j++) {
                    if (lb[i][j] == l + 1 && lb[i][j] != 0)
                        areas[l]++;
                }
            }
        }

        int[] areasAux = areas.clone();
        Arrays.sort(areasAux);

        double limiar = areasAux[areasAux.length - 1] * 0.02;

        for (int i = 0; i < lb.length; i++) {
            for (int j = 0; j < lb[0].length; j++) {
                if (lb[i][j] != 0)
                    resultado[i][j] = (areas[lb[i][j] - 1] > limiar);
            }
        }

        return resultado;
    }

    private static boolean[][] juntarPontos(boolean[][] im) {
        boolean[][] original = Image.copy(im);
        int[][] lb = Image.bwLabel(im);
        ArrayList<int[]> apagar = new ArrayList<>();
        int[][] p = new int[Image.max(lb)][4];
        boolean[] flag = new boolean[Image.max(lb)];

        if (Image.max(lb) > 1) {
            for (int i = 0; i < lb.length; i++) {
                for (int j = 0; j < lb[0].length; j++) {
                    if (lb[i][j] != 0) {
                        if (!flag[lb[i][j] - 1]) {
                            p[lb[i][j] - 1][0] = i;
                            p[lb[i][j] - 1][1] = j;
                            flag[lb[i][j] - 1] = true;
                        } else {
                            p[lb[i][j] - 1][2] = i;
                            p[lb[i][j] - 1][3] = j;
                        }
                    }
                }
            }


            ArrayList<int[]> regioes = new ArrayList<>();
            ArrayList<Double> distancias = new ArrayList<>();
            ArrayList<int[]> pontos = new ArrayList<>();
            int[] pos;

            int[] pMenor = new int[4];
            double menor = 0;

            //Preenchendo a arraylist
            for (int r = 0; r < Image.max(lb); r++) {
                regioes.add(p[r]);
            }


            while (regioes.size() != 1) {
                distancias = new ArrayList<>();
                pontos = new ArrayList<>();
                pMenor = new int[4];
                for (int i = 0; i < regioes.size(); i++) {
                    for (int j = i + 1; j < regioes.size(); j++) {
                        pos = new int[4];
                        pos[0] = regioes.get(i)[0]; //x1 da primeira regiao
                        pos[1] = regioes.get(i)[1]; //y1 da primeira regiao
                        pos[2] = regioes.get(j)[0]; //x1 da segunda regiao
                        pos[3] = regioes.get(j)[1]; //y1 da segunda regiao
                        distancias.add(Math.sqrt(Math.pow(pos[0] - pos[2], 2) - Math.pow(pos[1] - pos[3], 2)));
                        pontos.add(pos);

                        pos = new int[4];
                        pos[0] = regioes.get(i)[0]; //x1 da primeira regiao
                        pos[1] = regioes.get(i)[1]; //y1 da primeira regiao
                        pos[2] = regioes.get(j)[2]; //x2 da segunda regiao
                        pos[3] = regioes.get(j)[3]; //y2 da segunda regiao
                        distancias.add(Math.sqrt(Math.pow(pos[0] - pos[2], 2) - Math.pow(pos[1] - pos[3], 2)));
                        pontos.add(pos);

                        pos = new int[4];
                        pos[0] = regioes.get(i)[2]; //x2 da primeira regiao
                        pos[1] = regioes.get(i)[3]; //y2 da primeira regiao
                        pos[2] = regioes.get(j)[0]; //x1 da segunda regiao
                        pos[3] = regioes.get(j)[1]; //y1 da segunda regiao
                        distancias.add(Math.sqrt(Math.pow(pos[0] - pos[2], 2) - Math.pow(pos[1] - pos[3], 2)));
                        pontos.add(pos);
                        pos = new int[4];
                        pos[0] = regioes.get(i)[2]; //x2 da primeira regiao
                        pos[1] = regioes.get(i)[3]; //y2 da primeira regiao
                        pos[2] = regioes.get(j)[2]; //x2 da segunda regiao
                        pos[3] = regioes.get(j)[3]; //y2 da segunda regiao
                        distancias.add(Math.sqrt(Math.pow(pos[0] - pos[2], 2) - Math.pow(pos[1] - pos[3], 2)));
                        pontos.add(pos);
                    }
                }

                for (int i = 0; i < distancias.size() - 1; i++) {
                    if (distancias.get(i) < menor || i == 0) {
                        menor = distancias.get(i);
                        pMenor[0] = pontos.get(i)[0];
                        pMenor[1] = pontos.get(i)[1];
                        pMenor[2] = pontos.get(i)[2];
                        pMenor[3] = pontos.get(i)[3];
                    }
                }

                apagar.add(pMenor);

                int lb1 = 0, lb2 = 0;
                boolean f = true, f2 = true;
                for (int i = 0; i < regioes.size(); i++) {
                    if (regioes.get(i)[0] == pMenor[0] && regioes.get(i)[1] == pMenor[1]) {
                        lb1 = i;
                        f = true;
                    }
                    if (regioes.get(i)[2] == pMenor[0] && regioes.get(i)[3] == pMenor[1]) {
                        lb1 = i;
                        f = false;
                    }
                    if (regioes.get(i)[0] == pMenor[2] && regioes.get(i)[1] == pMenor[3]) {
                        lb2 = i;
                        f2 = true;
                    }
                    if (regioes.get(i)[2] == pMenor[2] && regioes.get(i)[3] == pMenor[3]) {
                        lb2 = i;
                        f2 = false;
                    }
                }

                if (f) {
                    if (f2) {
                        regioes.get(lb1)[2] = regioes.get(lb2)[2];
                        regioes.get(lb1)[3] = regioes.get(lb2)[3];
                    } else {
                        regioes.get(lb1)[2] = regioes.get(lb2)[0];
                        regioes.get(lb1)[3] = regioes.get(lb2)[1];
                    }
                } else {
                    if (f2) {
                        regioes.get(lb1)[0] = regioes.get(lb2)[2];
                        regioes.get(lb1)[1] = regioes.get(lb2)[3];
                    } else {
                        regioes.get(lb1)[0] = regioes.get(lb2)[0];
                        regioes.get(lb1)[1] = regioes.get(lb2)[1];
                    }
                }

                regioes.remove(lb2);
            }
        }

        for (int[] a : apagar) {
            original = Image.bwLine(original, a);
        }

        return original;
    }

    private static int[] pontodeCorte(int[][][] im, String s) {
        im = Image.imGaussian(im, 3);
        double[][] hue = Image.splitChannel(Image.rgb2hsv(im), 0);
        boolean[][] bin = new boolean[im.length][im[0].length];
        int[][] huegray = new int[im.length][im[0].length];
        for (int i = 0; i < im.length; i++) {
            for (int j = 0; j < im[0].length; j++) {
                bin[i][j] = (hue[i][j] * 255) < 50;
                huegray[i][j] = (int) (hue[i][j] * 255);
            }
        }

        bin = Image.bwErode(bin, 3);


        ArrayList<Properties> props = Image.regionProps(bin);

        if (props.size() == 0)
            throw new Error("Não foi possível encontrar o ponto de corte");

        Image.imWrite(bin, "im/Moringa/results/" + s + "_pcorteBin" + ".png");

        Properties p = null;

        int maior = 0;
        int index = 0;
        for (int l = 0; l < props.size(); l++) {
            if (props.get(l).area > maior) {
                maior = props.get(l).area;
                index = l;
            }
        }
        p = props.get(index);

        int[] pt = new int[2];
        pt[0] = p.boundingBox[0] + ((p.boundingBox[2] - p.boundingBox[0]) / 2);
        pt[1] = p.boundingBox[1] + ((p.boundingBox[3] - p.boundingBox[1]) / 2);

        return pt;
    }

    private static Seedling medirDistancias(Seedling seedling, boolean[][] bw, int[] p, String filename) {
        boolean[][] esqueleto = Image.skeletonize(bw);
        boolean[][] bwendpoints = Image.bwEndPoints(esqueleto);

        //Image.imWrite(esqueleto, "im/Moringa/results/esqueleto_" + plântula + ".png");
        Image.imWrite(esqueleto, "im/Moringa/results/" + filename + "_" + seedling + "_esqueleto.png");

        if (!esqueleto[p[0]][p[1]])
            p = pontodeCorteEsqueleto(esqueleto, p);

        Graph<int[], DefaultEdge> grafo = new DefaultDirectedGraph<>(DefaultEdge.class);
        ArrayList<int[]> endpoints = new ArrayList<>();

        //Cria uma matriz de nós com as posições
        int[][][] nodes = new int[bw.length][bw[0].length][2];
        for (int i = 0; i < esqueleto.length; i++) {
            for (int j = 0; j < esqueleto[0].length; j++) {
                if (esqueleto[i][j]) {
                    nodes[i][j] = new int[]{i, j};
                    if (bwendpoints[i][j])
                        endpoints.add(nodes[i][j]);
                } else {
                    nodes[i][j] = null;
                }
            }
        }

        //Armazena os nós no grafo
        for (int i = 1; i < nodes.length - 1; i++)
            for (int j = 1; j < nodes[0].length - 1; j++) {
                if (nodes[i][j] != null) {
                    int[] now = nodes[i][j];
                    if (!grafo.containsVertex(now))
                        grafo.addVertex(now);
                    int[] neighbor;
                    for (int x = -1; x <= 1; x++)
                        for (int y = -1; y <= 1; y++) {
                            if (nodes[i + x][j + y] != null && !(x == 0 && y == 0)) {
                                neighbor = nodes[i + x][j + y];
                                if (!grafo.containsVertex(neighbor))
                                    grafo.addVertex(neighbor);
                                if (!grafo.containsEdge(now, neighbor))
                                    grafo.addEdge(now, neighbor);
                            }
                        }
                }
            }

        //Atualiza P
        p = nodes[p[0]][p[1]];
        seedling.setMiddlePoint(p);

        //PROCURANDO OS MAIORES CAMINHOS QUE NÃO SE CRUZAM

        int maiorIndex = 0;
        int maiorIndex2 = 0;
        List<int[]> maiorCaminho = new ArrayList<>();
        List<int[]> maiorCaminho2 = new ArrayList<>();

        //Cria um vetor de caminhos e seus comprimentos
        DijkstraShortestPath<int[], DefaultEdge> dijkstra = new DijkstraShortestPath<>(grafo);
        ArrayList<GraphPath<int[], DefaultEdge>> caminhos = new ArrayList<>();
        Integer[] comprimentos = new Integer[endpoints.size()];
        for (int i = 0; i < endpoints.size(); i++) {
            GraphPath path = dijkstra.findPathBetween(grafo, p, endpoints.get(i));
            caminhos.add(i, path);
            if (caminhos.get(i) != null)
                comprimentos[i] = caminhos.get(i).getLength();
            else
                comprimentos[i] = 0;
        }

        //Ordena o vetor de comprimentos
        Arrays.sort(comprimentos, Collections.reverseOrder());

        //Procura o caminho mais distante
        for (int i = 0; i < endpoints.size(); i++) {
            if (caminhos.get(i) != null) {
                if (caminhos.get(i).getLength() == comprimentos[0]) {
                    maiorIndex = i;
                    maiorCaminho = caminhos.get(i).getVertexList();
                }
            }
        }

        //Procura o segundo maior caminho que não sobrepõe o maior
        for (int i = 0; i < endpoints.size(); i++) {
            if (i != maiorIndex && caminhos.get(i) != null) {
                List<int[]> caminho = caminhos.get(i).getVertexList();
                int count = 0;
                for (int[] t : caminho) {
                    if (maiorCaminho.contains(t)) {
                        count++;
                    }
                }
                if (count <= 20 && caminhos.get(i).getLength() > maiorCaminho2.size()) {
                    maiorIndex2 = i;
                    maiorCaminho2 = caminhos.get(i).getVertexList();
                }
            }
        }


        bwendpoints = Image.bwEndPoints(esqueleto);
        //Image.imWrite(esqueleto, "im/Moringa/results/esqueleto_" + plântula + ".png");

        if (maiorIndex < maiorIndex2) {
            seedling.setAerial(maiorCaminho);
            seedling.setRoot(maiorCaminho2);
        } else {
            seedling.setAerial(maiorCaminho2);
            seedling.setRoot(maiorCaminho);
        }

        return seedling;
    }

    static private int[] pontodeCorteEsqueleto(boolean[][] esqueleto, int[] p) {
        int[] N = {p[0] - 1, p[1]};
        int[] S = {p[0] + 1, p[1]};
        int[] W = {p[0], p[1] - 1};
        int[] E = {p[0], p[1] + 1};
        int[] NW = {p[0] - 1, p[1] - 1};
        int[] NE = {p[0] - 1, p[1] + 1};
        int[] SW = {p[0] + 1, p[1] - 1};
        int[] SE = {p[0] + 1, p[1] + 1};

        for (int i = 0; i < 50; i++) {
            if (esqueleto[N[0]][N[1]]) {
                p = N;
                break;
            } else if (esqueleto[S[0]][S[1]]) {
                p = S;
                break;
            } else if (esqueleto[W[0]][W[1]]) {
                p = W;
                break;
            } else if (esqueleto[E[0]][E[1]]) {
                p = E;
                break;
            } else if (esqueleto[NW[0]][NW[1]]) {
                p = NW;
                break;
            } else if (esqueleto[NE[0]][NE[1]]) {
                p = NE;
                break;
            } else if (esqueleto[SW[0]][SW[1]]) {
                p = SW;
                break;
            } else if (esqueleto[SE[0]][SE[1]]) {
                p = SE;
                break;
            }

            N[0]--;
            S[0]++;
            W[1]--;
            E[0]++;
            NW[0]--;
            NW[1]--;
            NE[0]--;
            NE[1]++;
            SW[0]++;
            SW[1]--;
            SE[0]++;
            SE[1]++;
        }

        return p;
    }
}

class Verificacao {
    List<Integer> caminhos, posicoes;
    int tamanho = 0;

    public Verificacao() {
        caminhos = new ArrayList<>();
        posicoes = new ArrayList<>();
        tamanho = 0;
    }
}

class Par<tipo1, tipo2> {
    tipo1 v1;
    tipo2 v2;

    public Par(tipo1 v1, tipo2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }
}