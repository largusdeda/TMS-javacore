package homework20.Ex3;

/**
 * @author Elena Chinarina
 *
 **/

public class Producer implements Runnable {
    private final Store store;
    private int produceCount = 0;

    public Producer(Store store) {
        this.store = store;
    }

    @Override
    public void run() {
        while (produceCount < 5) {
            try {
                String product = "Продукт";
                store.produce(product);
                produceCount++;
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.printf("Производитель завершил работу (произведено: %d)\n", produceCount);
    }
}
