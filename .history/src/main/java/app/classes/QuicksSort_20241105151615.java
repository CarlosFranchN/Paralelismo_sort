package app.classes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class QuicksSort implements SortingAlgorithm {
    public int[] array;
    public int[] array_ord_paralelo;
    public int[] array_ord_serial;
    public static  int high;
    public static int low;
    public long time_paralelo;
    public long time_serial;
    private final int nThreads;
    private static HashMap<Integer, Long> mapTimes2 = new HashMap<>();
    private static TreeMap<Integer, Long> finalMap = new TreeMap<>();

    
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
            throw new UnsupportedOperationException("Not supported yet.");
        }
    
        @Override
        public int[] getArray() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    
        public QuicksSort(int[] array, int high, int low, int nThreads) {
            this.array = array;
            this.array_ord_paralelo = Arrays.copyOf(array, array.length);
            this.array_ord_serial = Arrays.copyOf(array, array.length);
            this.high = high;
            this.low = low;
            this.nThreads = nThreads;
            this.time_paralelo = 0;
            this.time_serial = 0;
        }
    
        public static int partition(int[] arr, int low, int high) {
            int pivot = arr[high];
            int i = (low - 1);
            for (int j = low; j < high; j++) {
                if (arr[j] < pivot) {
                    i++;
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            int temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;
            return i + 1;
        }
    
        static void quickSortSerial(int[] arr, int low, int high) {
            if (low < high) {
                int pi = partition(arr, low, high);
                quickSortSerial(arr, low, pi - 1);
                quickSortSerial(arr, pi + 1, high);
            }
        }
    
        public static void quickSortParalelo(int[] arr, int numeroThreads, int par, int par1) {
            if (low < high) {
                // Particiona o array e obtém o índice do pivô
                int pi = partition(arr, low, high);
                
                // Se o número de threads permitidas for maior que 1, divide o trabalho
                if (numeroThreads > 1) {
                    Thread leftThread = new Thread(() -> quickSortParalelo(arr, low, pi - 1, numeroThreads / 2));
                Thread rightThread = new Thread(() -> quickSortParalelo(arr, pi + 1, high, numeroThreads / 2));
                leftThread.start();
                rightThread.start();
                try {
                    leftThread.join();
                    rightThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // Caso contrário, chama a versão serial
                quickSortSerial(arr, low, pi - 1);
                quickSortSerial(arr, pi + 1, high);
            }
        }
    }

    public void gerarTesteSerial() {
        long startTime = System.nanoTime(); // Início do tempo
        quickSortSerial(array_ord_serial, 0, array_ord_serial.length - 1);
        long endTime = System.nanoTime(); // Fim do tempo
        time_serial = endTime - startTime; // Tempo total
        System.out.println("Tempo de execução do QuickSort serial: " + time_serial + " nanosegundos");
    }

    public void gerarTesteParalelo() {
        long startTime = System.nanoTime(); // Início do tempo
        quickSortParalelo(array_ord_paralelo, 0, array_ord_paralelo.length - 1, nThreads);
        long endTime = System.nanoTime(); // Fim do tempo
        time_paralelo = endTime - startTime; // Tempo total
        System.out.println("Tempo de execução do QuickSort paralelo: " + time_paralelo + " nanosegundos");
    }

    public void printArray(int[] arrayToPrint) {
        System.out.println(Arrays.toString(arrayToPrint));
    }

    public static void main(String[] args) {
        int[] array = {5, 3, 8, 1, 2, 7, 4, 6};
        int nThreads = 2; // Número de threads para o paralelo

        QuicksSort quickSort = new QuicksSort(array, array.length - 1, 0, nThreads);
        System.out.print("Array original : ");
        quickSort.printArray(quickSort.array);
        // Teste de ordenação serial
        quickSort.gerarTestesSerial();
        System.out.print("Array ordenado (serial): ");
        quickSort.printArray(quickSort.array_ord_serial);

        // Teste de ordenação paralelo
        quickSort.gerarTestesParalelo();
        System.out.print("Array ordenado (paralelo 2): ");
        quickSort.printArray(quickSort.array_ord_paralelo);
    }
}
