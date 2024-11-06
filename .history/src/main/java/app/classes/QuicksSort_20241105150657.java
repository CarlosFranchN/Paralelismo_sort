package app.classes;

import java.util.Arrays;
import java.util.TreeMap;

public class QuicksSort implements SortingAlgorithm {
    public int[] array;
    public int[] array_ord_paralelo;
    public int[] array_ord_serial;
    public int high;
    public int low;
    public long time_paralelo;
    public long time_serial;
    private final int nThreads;

    @Override
    public void gerandoTeste() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void printarFinalMap() {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public static void quickSortParalelo(int[] arr, int numeroThreads) {
        if (numeroThreads <= 1) {
            quickSortSerial(arr, 0, arr.length - 1);
        } else {
            int pi = partition(arr, 0, arr.length - 1);
            Thread leftThread = new Thread(() -> quickSortParalelo(Arrays.copyOfRange(arr, 0, pi), numeroThreads / 2));
            Thread rightThread = new Thread(() -> quickSortParalelo(Arrays.copyOfRange(arr, pi + 1, arr.length), numeroThreads / 2));
            leftThread.start();
            rightThread.start();
            try {
                leftThread.join();
                rightThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void gerarTestesSerial() {
        long startTime = System.nanoTime(); // Início do tempo
        quickSortSerial(array_ord_serial, 0, array_ord_serial.length - 1);
        long endTime = System.nanoTime(); // Fim do tempo
        time_serial = endTime - startTime; // Tempo total
        System.out.println("Tempo de execução do QuickSort serial: " + time_serial + " nanosegundos");
    }

    public void gerarTestesParalelo() {
        long startTime = System.nanoTime(); // Início do tempo
        quickSortParalelo(array_ord_paralelo, nThreads);
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
        System.out.print("Array ordenado (paralelo): ");
        quickSort.printArray(quickSort.array_ord_paralelo);
    }
}
