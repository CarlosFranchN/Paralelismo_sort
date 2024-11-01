package app.classes;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class InsertionSort {
    public int[] array;
    public int[] array_ord_serial;
    public int[] array_ord_paralelo;
    public int[] OpThreads =  {0,2,4,5,10,100,1000,10000};
    public long time_serial;
    public long time_paralelo;
    public int nThreads;
    private static TreeMap<Integer, Long> mapTimes = new TreeMap<>();
    private static TreeMap<Integer, Long> finalMap = new TreeMap<>();
    

    public static TreeMap<Integer, Long> getFinalMap() {
        return finalMap;
    }
    public void printarFinalMap(){
        TreeMap<Integer, Long> Map = new TreeMap<>(finalMap);
        for (Map.Entry<Integer, Long> entry : Map.entrySet()) {
            finalMap.put(entry.getKey(), entry.getValue());
            System.out.println("NThreads: " + entry.getKey() + ", Tempo: " + entry.getValue());
        }
    }
    public void printMapTimes() {
        System.out.println("Tempos de execução armazenados no mapTimes:");
        for (Entry<Integer, Long> entry : mapTimes.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue() + " ns");
        }
    }

    public InsertionSort(int[] array){
        this.array = array;
        this.array_ord_serial = new int[this.array.length];
        this.array_ord_paralelo = new int[this.array.length];
        this.time_serial = 0;
        this.time_paralelo = 0;
        this.nThreads = 0;
    }

    public void setnThreads(int nThreads) {
        this.nThreads = nThreads;
    }
    public void sort(){
        long inicio = System.nanoTime();
        int n = array.length;
        this.array_ord_serial = this.array.clone();
        for (int i = 1; i < n; i++) {
            int key = this.array_ord_serial[i];
            int j = i - 1;

            while (j>=0 && this.array_ord_serial[j] > key) {
                this.array_ord_serial[j+1] =  this.array_ord_serial[j];
                j = j - 1;
            }
            this.array_ord_serial[j+1] = key;
        }
        long fim = System.nanoTime();
        this.time_serial = fim - inicio;

    }
    public void parallelSort() throws InterruptedException{
        long inicio = System.nanoTime();
        int n = this.array.length;
        Thread[] threads = new Thread[nThreads];
        int partSize = n / nThreads;
        this.array_ord_paralelo = this.array.clone();

        for(int t = 0; t <nThreads; t++){
            int start = t*partSize;
            int end = (t == nThreads - 1) ? n : (t + 1) * partSize;
        
            threads[t] = new Thread(()->{
                for(int i = start+1; i< end; i++){
                    int key = this.array_ord_paralelo[i];
                    int j = i-1;

                    while (j >= start && this.array_ord_paralelo[j] > key) {
                        this.array_ord_paralelo[j+1] = this.array_ord_paralelo[j];
                        j--;
                    }
                    this.array_ord_paralelo[j+1] = key;
                }

            });
            threads[t].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        insertionParts(partSize);
        long fim = System.nanoTime();
        this.time_paralelo = fim - inicio;
    }
    private void insertionParts(int partSize){
        for(int t = 0; t <nThreads; t++){
            // System.out.println(this.array_ord_paralelo);
            int start = t*partSize;
            int end = (t == nThreads - 1) ? this.array_ord_paralelo.length : (t + 1) * partSize;
        
            for (int i = start; i < end ; i++){
                int key = this.array_ord_paralelo[i];
                int j = i -1;
                while (j >= 0 && this.array_ord_paralelo[j] > key) {
                    this.array_ord_paralelo[j + 1] = this.array_ord_paralelo[j];
                    j--;
                }
                this.array_ord_paralelo[j + 1] = key;

            }
        
        }

    }

    public void gerarTesteSerial(){

        InsertionSort task = new InsertionSort(this.array);
        task.sort();

        // mapTimes.put("MergeSortSerial", task.time_serial);
        mapTimes.put(0, task.time_serial);

    }

    public void gerarTesteParalelo(){

        // int[] numThreads = {2,4,5,10,100,1000,10000};

        for (int t : OpThreads) {
            InsertionSort task = new InsertionSort(this.array);
            task.setnThreads(t);
            try {
                task.parallelSort();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mapTimes.put(t,task.time_paralelo);
        }

    }

    
}