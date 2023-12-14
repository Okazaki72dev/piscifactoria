package commons;

public class AlmacenCentral {

    static final int COSTE_POR_50 = 100;
    static final int CAPACIDAD_CADA_100 = 50;
    static final int COSTE_ALMACEN = 2000;
    static final int COST_PER_UNIT = 1;
    private static final int DISCOUNT_PER_QUANTITY = 5;
    private static final int UNITS_PER_DISCOUNT = 25;
    private static AlmacenCentral instance = null;
    Coins purse = Coins.getInstance();
    private int capacidadComidaMaxima = 200;
    private int comidaActual = 0;

    private AlmacenCentral() {

    }

    public static AlmacenCentral getInstance() {
        if (AlmacenCentral.instance == null) {
            AlmacenCentral.instance = new AlmacenCentral();
        }
        return AlmacenCentral.instance;
    }


    public int getCapacidadComidaMaxima() {
        return capacidadComidaMaxima;
    }

    public void setCapacidadComidaMaxima(int capacidadComidaMaxima) {
        this.capacidadComidaMaxima = capacidadComidaMaxima;
    }

    public int getComidaActual() {
        return comidaActual;
    }

    public void setComidaActual(int comidaActual) {
        this.comidaActual = comidaActual;
    }

    public void addFoodAlmacen(int foodToAdd) {
        int availableSpace = this.capacidadComidaMaxima - this.comidaActual;

        if (foodToAdd > availableSpace) {
            System.out.println("\n¡Atención! El almacen no puede almacenar toda la comida que estás comprando.");
            System.out.println("Solo se te cobrará la cantidad que el almacen puede almacenar.");
            foodToAdd = availableSpace;
        }
        int cost = calculateCost(foodToAdd);
        int discountedCost = applyDiscount(foodToAdd, cost);

        System.out.println("\nHas pagado " + discountedCost + " monedas por " + foodToAdd + " unidades de comida.");

        this.comidaActual = this.comidaActual + foodToAdd;
        purse.buy(discountedCost);

    }

    private int calculateCost(int foodAmount) {
        return foodAmount * COST_PER_UNIT;
    }

    private int applyDiscount(int foodToAdd, int cost) {
        int discount = (foodToAdd / UNITS_PER_DISCOUNT) * DISCOUNT_PER_QUANTITY;
        return Math.max(0, cost - discount);
    }

    @Override
    public String toString() {
        return "AlmacenCentral{" +
                "capacidadComidaMaxima=" + capacidadComidaMaxima +
                '}';
    }
}