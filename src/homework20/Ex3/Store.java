package homework20.Ex3;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Elena Chinarina
 *
 **/

public class Store {
    private final Queue<String> products = new LinkedList<>();
    private final int MAX_CAPACITY = 3;
    private int totalProduced = 0;
    private final int TARGET_PRODUCED = 5;
    private int totalConsumed = 0;

    public synchronized void produce(String productName) throws InterruptedException {
        while (products.size() == MAX_CAPACITY || totalProduced >= TARGET_PRODUCED) {
            if (totalProduced >= TARGET_PRODUCED)
                return;

            wait();
        }

        products.add(productName);
        totalProduced++;
        System.out.printf("Производитель произвел: %s (в магазине: %d, всего произведено: %d)\n",
                productName, products.size(), totalProduced);

        notifyAll();
    }

    public synchronized void consume() throws InterruptedException {
        while (products.isEmpty() && totalProduced < TARGET_PRODUCED) {
            wait();
        }

        if (totalConsumed >= TARGET_PRODUCED)
            return;

        String product = products.poll();
        totalConsumed++;
        System.out.printf("Покупатель купил: %s (в магазине: %d, всего куплено: %d)\n",
                product, products.size(), totalConsumed);

        notifyAll();
    }
}
