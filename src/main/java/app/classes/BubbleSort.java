package app.classes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BubbleSort implements SortingAlgorithm{


    public int[] array;
    public int[] array_ord_paralelo;
    public int[] array_ord_serial;
    public long time_paralelo;
    public long time_serial;
    // public int numeroThreads;
    private static HashMap<String, Long> mapTimes = new HashMap<>();
    private static HashMap<Integer, Long> mapTimes2 = new HashMap<>();
    private static TreeMap<Integer, Long> finalMap = new TreeMap<>();

    public static HashMap<Integer, Long> getMapTimes2() {
        return mapTimes2;
    }
    @Override
    public void gerandoTeste() {
        try {
            gerarTesteSerial();
            gerarTesteParalelo();
            System.out.println("Testes gerados!");
            
        } catch (Exception e) {
            System.out.println("Erro");
        }
    }

    @Override
    public void printarFinalMap() {
        TreeMap<Integer, Long> Map = new TreeMap<>(mapTimes2);
        for (Map.Entry<Integer, Long> entry : Map.entrySet()) {
            finalMap.put(entry.getKey(), entry.getValue());
            System.out.println("NThreads: " + entry.getKey() + ", Tempo: " + entry.getValue());
        }
    }

    @Override
    public TreeMap<Integer, Long> getFinalMap() {
        return finalMap;
    }

    @Override
    public int[] getArray() {
        return  array;
    }

    
    public BubbleSort(int[] array){
        this.array = array;
        this.array_ord_paralelo = Arrays.copyOf(array, array.length);
        this.array_ord_serial = Arrays.copyOf(array, array.length);
        // this.numeroThreads = numeroThreads;
        this.time_paralelo = 0;
        this.time_serial = 0;

    }

    public void bubbleSorting() {
        int n = array_ord_serial.length;
        // array_ord_serial = array;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (array_ord_serial[j] > array_ord_serial[j + 1]) {
                    int temp = array_ord_serial[j];
                    array_ord_serial[j] = array_ord_serial[j + 1];
                    array_ord_serial[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }
    public int[][] dividirArray(int numeroThreads) {
        int tamanhoSubArray = array_ord_paralelo.length / numeroThreads;
        int[][] subArrays = new int[numeroThreads][];
        for (int i = 0; i < numeroThreads; i++) {
            int inicio = i * tamanhoSubArray;
            int fim = (i == numeroThreads - 1) ? array_ord_paralelo.length : (i + 1) * tamanhoSubArray;
            subArrays[i] = Arrays.copyOfRange(array_ord_paralelo, inicio, fim);
        }
        return subArrays;
    }

    public static int[] fundirArrays(int[][] subArrays) {
        int tamanhoTotal = 0;
        for (int[] subArray : subArrays) tamanhoTotal += subArray.length;
        int[] resultado = new int[tamanhoTotal];
        int[] indices = new int[subArrays.length];
        for (int i = 0; i < tamanhoTotal; i++) {
            int menorIndice = -1;
            int menorValor = Integer.MAX_VALUE;
            for (int j = 0; j < subArrays.length; j++) {
                if (indices[j] < subArrays[j].length && subArrays[j][indices[j]] < menorValor) {
                    menorIndice = j;
                    menorValor = subArrays[j][indices[j]];
                }
            }
            resultado[i] = menorValor;
            indices[menorIndice]++;
        }
        return resultado;
    }

    public void  serial(){    
        long inicio = System.nanoTime();    
        
        bubbleSorting();
        long fim  = System.nanoTime();
        this.time_serial = fim - inicio;
    }
    public static void sort(int[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    public void paralelo(int numeroThreads) {
        long inicio = System.nanoTime();    

        // 1. Dividir o array original em subarrays
        int[][] subArrays = dividirArray(numeroThreads);
        
        // 2. Criar e iniciar as threads
        Thread[] threads = new Thread[numeroThreads];
        for (int i = 0; i < numeroThreads; i++) {
            final int[] subArray = subArrays[i];  // Subarray correspondente a essa thread
            threads[i] = new Thread(() -> sort(subArray));  // Ordenar o subarray
            threads[i].start();  // Iniciar a thread
        }
    
        // 3. Aguardar todas as threads terminarem
        for (int i = 0; i < numeroThreads; i++) {
            try {
                threads[i].join();  // Espera pela finalização da thread
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    
        // 4. Fundir os subarrays ordenados de volta em um único array
        array_ord_paralelo = fundirArrays(subArrays);
        long fim  = System.nanoTime();
        this.time_paralelo = fim - inicio;
    }

    public void gerarTesteSerial(){

        BubbleSort task = new BubbleSort(this.array);
        task.serial();

        // mapTimes.put("BubbleSortSerial", task.time_serial);
        mapTimes2.put(0, task.time_serial);

    }

    public void gerarTesteParalelo(){

        int[] numThreads = {2,4,5,10,100,1000,10000};

        for (int t : numThreads) {
            BubbleSort task = new BubbleSort(this.array);
            task.paralelo(t);
            String key = String.format("BublueSortParalelo%d", t);
            // mapTimes.put(key, task.time_paralelo);
            mapTimes2.put(t,task.time_paralelo);
        }

    }
    public static void main(String[] args) {
               // Exemplo de array a ser ordenado
               int[] array = {64, 34, 25, 12, 22, 11, 90};
        
               // Definindo o número de threads para o exemplo
               int numeroThreads = 2; // Ou o valor que você desejar
               
               // Criando uma instância de BubbleSort
               BubbleSort bubbleSort = new BubbleSort(array);
               // Chamando o método de bubble sort serial (implementação normal)
            //    bubbleSort.bubbleSort();
            bubbleSort.serial();
               System.out.println("Array original (serial): " + Arrays.toString(bubbleSort.array));
               System.out.println("Array ordenado (serial): " + Arrays.toString(bubbleSort.array_ord_serial));
               System.err.println("Tempo do serial: " + bubbleSort.time_serial);
               // Dividindo o array em subarrays para ordenação paralela
            //    int[][] subArrays = bubbleSort.dividirArray(numeroThreads);
            //    System.out.println("Subarrays divididos: " + Arrays.deepToString(subArrays));
               
            //    // Chamando o método para fundir os arrays (simulando a ordenação paralela)
            //    int[] arrayOrdenado = BubbleSort.fundirArrays(subArrays);
            //    System.out.println("Array ordenado (fusão): " + Arrays.toString(arrayOrdenado));  
            bubbleSort.paralelo(2);
            System.out.println("Array original (serial): " + Arrays.toString(bubbleSort.array));
            System.out.println("Array ordenado (paralelo): " + Arrays.toString(bubbleSort.array_ord_paralelo));
            System.err.println("Tempo do paralelo: " + bubbleSort.time_paralelo);

            bubbleSort.gerarTesteSerial();
            bubbleSort.gerarTesteParalelo();
            // System.out.println("Map: " + Arrays.toString(bubbleSort.getMapTimes2()));
            bubbleSort.getMapTimes2().forEach((key, value) -> System.out.println("Chave: " + key + ", Valor: " + value));
            bubbleSort.printarFinalMap();


        }

        
    }


