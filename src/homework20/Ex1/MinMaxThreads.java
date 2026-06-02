package homework20.Ex1;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * @author Elena Chinarina
 *
 **/

public class MinMaxThreads {
    static class MaxFinder implements Callable<Integer> {
        private final int[] array;


        public MaxFinder(int[] array) {
            this.array = array;
        }

        @Override
        public Integer call() throws Exception {
            int max = array[0];
            for (int i = 1; i < array.length; i++) {
                if (array[i] > max)
                    max = array[i];
            }

            return max;
        }
    }

    static class MinFinder implements Callable<Integer> {
        private final int[] array;


        public MinFinder(int[] array) {
            this.array = array;
        }

        @Override
        public Integer call() throws Exception {
            int min = array[0];
            for (int i = 1; i < array.length; i++) {
                if (array[i] < min)
                    min = array[i];
            }

            return min;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите размер массива: ");
        int size = scanner.nextInt();

        int[] array = new int[size];

        System.out.println("Введите элементы:");
        for (int i = 0; i < size; i++) {
            System.out.print("Элемент " + i + ": ");
            array[i] = scanner.nextInt();
        }

        System.out.println("Массив: " + Arrays.toString(array));

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<Integer> maxFuture = executor.submit(new MaxFinder(array));
        Future<Integer> minFuture = executor.submit(new MinFinder(array));


        try {
            int max = maxFuture.get();
            int min = minFuture.get();

            executor.shutdown();

            System.out.println("Минимальное значение: " + min);
            System.out.println("Максимальное значение: " + max);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        scanner.close();
    }
}
