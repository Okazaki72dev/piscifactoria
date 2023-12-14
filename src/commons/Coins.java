package commons;

public class Coins {

    private static Coins instance = null;
    private int coinAmmount = 0;

    private Coins() {

    }

    public static Coins getInstance() {
        if (Coins.instance == null) {
            Coins.instance = new Coins();
        }
        return Coins.instance;
    }

    public boolean buy(int prize) {
        if (coinAmmount >= prize) {
            coinAmmount -= prize;
            return true;
        } else {
            System.out.println("No tienes suficientes monedas");
            return false;
        }
    }

    public void add(int sum) {
        this.coinAmmount += sum;
    }

    public int getCoinAmmount() {
        return coinAmmount;
    }

}
