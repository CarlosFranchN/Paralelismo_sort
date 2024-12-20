package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JFrame;

import app.classes.Graph2;
import app.classes.GraphCreator;
import app.classes.MergeSort;
import app.classes.SimpleCSVReader;
import app.classes.SortingAlgorithm;


public class App 
{
    
    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {
        // HashMap<String, long[]> times = new HashMap<String, long[]>();
        // Graph2.testGraph2();
        
        // int[] arr = {38, 27, 43, 3, 9, 82, 10}; // Array de exemplo
        int[] arr = gerarArrayAleatorio(1000);
        int[] arr2 = gerarArrayAleatorio(100);
        int[] arr3 = gerarArrayAleatorio(10);


        MergeSort array = new MergeSort(arr, 0, arr.length-1, 2);
        MergeSort array2 = new MergeSort(arr2, 0, arr2.length-1, 2);
        MergeSort array3 = new MergeSort(arr3, 0, arr3.length-1, 2);

        MergeSort[] conjunto1= {array,array2,array3};



        List<double[]> data = readCSV(gerarCsv(conjunto1));
        GraphCreator.createGraph(data);
        InsertionSort arrayI1 = new InsertionSort(arr);
        InsertionSort arrayI2 = new InsertionSort(arr2);
        InsertionSort arrayI3 = new InsertionSort(arr3);

        array.gerandoTeste();
        array.printMapTimes();
        InsertionSort[] conjunto2 = {arrayI1,arrayI2,arrayI3};



        List<double[]> data2 = readCSV(gerarCsv(conjunto2));
        GraphCreator.createGraph(data2);


        


  
    }
        public static int[] gerarArrayAleatorio(int N) {
        Random random = new Random();
        int[] array = new int[N];

        for (int i = 0; i < N; i++) {
            array[i] = random.nextInt(100);  // Gera n?meros aleat?rios de 0 a 99
        }

        return array;
        }

        static void printarArray(int[] array){
            for (int valor: array){
                System.out.print(valor + " ");
            }
            System.out.println();
        }

        static String gerarCsv(SortingAlgorithm[] arrays) throws IOException {
            
        String path = "src/main/java/app/csv/file.csv";
        
        FileWriter writer = null;
        // BufferedWriter writer = new BufferedWriter(new FileWriter("path"));
        try {
            // Verifica se o arquivo j? existe e modifica o nome para evitar sobrescrita
            File file = new File(path);
            String originalPath = path;
            int counter = 1;
    
            // Se o arquivo existir, adiciona um sufixo para criar um novo arquivo
            while (file.exists()) {
                String newPath = originalPath.replace(".csv", "_" + counter + ".csv");
                file = new File(newPath);
                path = newPath;  // Atualiza o path para o novo nome
                counter++;
            }
    
            writer = new FileWriter(path);  // Cria o arquivo novo
           
            writer.append("Amostra,0,2,4,5,10,100,1000,\n");

            for (SortingAlgorithm array : arrays) {
                array.gerandoTeste();
                array.printarFinalMap();
                TreeMap<Integer, Long> map = array.getFinalMap();
                
                writer.append(String.valueOf(array.getArray().length) + ",");
                for (Entry<Integer, Long> entrada : map.entrySet()) {
                    writer.append(String.valueOf(entrada.getValue())).append(",");
                }
                writer.append("\n");
        
            }
            System.out.println("Arquivo CSV gerado com sucesso! Caminho: " + path);
    
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
        }

    public static List<double[]> readCSV(String filePath) {
        List<double[]> data = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Ler cabe?alho
            String header = br.readLine(); // Ignora o cabe?alho
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                double[] row = new double[values.length];
                row[0] = Double.parseDouble(values[0]);
                for (int i = 1; i < values.length; i++) {
                    row[i] = Double.parseDouble(values[i])/1000;
                }
                data.add(row);
            }
        } catch (IOException e) {
        }
        // System.out.println(data.get);
        return data;
        }

        
        static void gerarGrafico(MergeSort[] conjunto) throws IOException {
            String path = gerarCsv(conjunto);
            System.out.println(path);
        
            SimpleCSVReader file = new SimpleCSVReader(path);
            String[][] dados = file.getColumns();
            System.out.println(dados[1][0]);
            List<Double> times = new ArrayList<>();
            List<Double> keys = new ArrayList<>();
            
            try {
                // Inicializando mergeSort com dados do CSV
                for(int j = 1;j <=3;j++){

                    for (int i = 1; i <= 7; i++) {  // Ajustar o range conforme o CSV
                        times.add(Double.parseDouble(dados[1][i]) / 1000); // Nanossegundo para microssegundo
                        // keys.add(Double.parseDouble(dados[0][i]));
                        keys.add((double) i);
                    }
            
                }
                // keys.add(map.keySet());
        
                // Convertendo para arrays
                double[] chaves = keys.stream().mapToDouble(Double::doubleValue).toArray();
                double[] tempos = times.stream().mapToDouble(Double::doubleValue).toArray();
                String[] StringChaves = keys.stream().map(String::valueOf).toArray(String[]::new);
                String[] StringTempos = times.stream().map(String::valueOf).toArray(String[]::new);
            
                // Preparando os valores para o gr?fico
                // int[][] xyValues = { tempos,chaves };
                String[] xlabels = StringTempos;
                String[] ylabels = StringChaves;
                // Preparando os valores para o gr?fico
                // int[][] xyValues = {tempos, chaves};
        
                // Criando o gr?fico
                // Graph2 graph = new Graph2(dimensions, adj, xlabels, ylabels, xyValues);
                Graph2 graph = new Graph2(1200,tempos, chaves , xlabels, ylabels, (String)dados[1][0]);
                
                JFrame frame = new JFrame("Merge Sort Tempos");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                int[] dimensions = {1200,600};
                frame.setSize(dimensions[0], dimensions[1]);
                frame.add(graph);
                frame.setVisible(true);
            } catch (NumberFormatException e) {
                System.err.println("Erro ao converter os dados do CSV: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
