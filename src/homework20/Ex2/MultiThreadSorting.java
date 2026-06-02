package homework20.Ex2;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author Elena Chinarina
 *
 **/

public class MultiThreadSorting {
    private static final int[] originalArray = generateArray(10);

    public static void main(String[] args) {
        System.out.println("Массив: " + Arrays.toString(originalArray) + "\n");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Callable<SortResult> insertionTask = () -> sortWithAlgorithm(originalArray.clone(), "Insertion sort",
                MultiThreadSorting::insertionSort);
        Callable<SortResult> bubbleTask = () -> sortWithAlgorithm(originalArray.clone(), "Bubble sort",
                MultiThreadSorting::bubbleSort);
        Callable<SortResult> selectionTask = () -> sortWithAlgorithm(originalArray.clone(), "Selection sort",
                MultiThreadSorting::selectionSort);

        Future<SortResult> insertionFuture = executor.submit(insertionTask);
        Future<SortResult> bubbleFuture = executor.submit(bubbleTask);
        Future<SortResult> selectionFuture = executor.submit(selectionTask);

        try {
            System.out.println(insertionFuture.get());
            System.out.println(bubbleFuture.get());
            System.out.println(selectionFuture.get());

            executor.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static SortResult sortWithAlgorithm(int[] array, String algorithmName, SortAlgorithm algorithm) {
        long startTime = System.nanoTime();
        algorithm.sort(array);
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;

        return new SortResult(algorithmName, array, duration);
    }

    private static void insertionSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    private static void selectionSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[minIndex])
                    minIndex = j;
            }
            if (minIndex != i) {
                int tmp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = tmp;
            }
        }
    }

    private static void bubbleSort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int tmp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = tmp;
                }
            }
        }
    }

    private static int[] generateArray(int size) {
        int[] array = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100);
        }

        return array;
    }

    @FunctionalInterface
    interface SortAlgorithm {
        void sort(int[] array);
    }

    static class SortResult {
        private final String algorithm;
        private final int[] sortedArray;
        private final double durationMs;

        public SortResult(String algorithm, int[] sortedArray, double durationMs) {
            this.algorithm = algorithm;
            this.sortedArray = sortedArray;
            this.durationMs = durationMs;
        }

        @Override
        public String toString() {
            return String.format("%s: завершено за %.3f мс\nРезультат: %s",
                    algorithm, durationMs, Arrays.toString(sortedArray));
        }
    }
}
