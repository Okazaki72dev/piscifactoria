package pools;

import commons.Coins;
import pools.tanks.Tank;
import pools.tanks.fish.Fish;

import java.util.ArrayList;

public abstract class Pool {

    static final int COST_PER_UNIT = 1;
    private static final int DISCOUNT_PER_QUANTITY = 5;
    private static final int UNITS_PER_DISCOUNT = 25;

    String poolName;
    int currentFood = 0;
    int maxFood;
    ArrayList<Tank<? extends Fish>> tankArrayList = new ArrayList<>();
    Coins purse = Coins.getInstance();

    public Pool(String poolName, int maxFood) {
        this.poolName = poolName;
        this.maxFood = maxFood;
    }

    public static int getCostPerUnit() {
        return COST_PER_UNIT;
    }

    public void nextDay() {
        for (Tank<? extends Fish> tank : tankArrayList) {
            tank.nextDay(this);
        }
    }

    public String getPoolName() {
        return poolName;
    }

    public int getCurrentFood() {
        return currentFood;
    }

    public void setCurrentFood(int currentFood) {
        this.currentFood = currentFood;
    }

    public int getMaxFood() {
        return maxFood;
    }

    public ArrayList<Tank<? extends Fish>> getTankArrayList() {
        return tankArrayList;
    }

    public void setTankArrayList(ArrayList<Tank<? extends Fish>> tankArrayList) {
        this.tankArrayList = tankArrayList;
    }

    // STATUS RELATED ************-------------------------

    public void showStatus() {
        int totalFish = 0;
        int totalCapacity = 0;
        int totalAliveFish = 0;
        int totalFedFish = 0;
        int totalElderFish = 0;
        int totalFemaleFish = 0;
        int totalMaleFish = 0;
        int totalFertileFish = 0;


        System.out.println("\n==== " + poolName + " ====");

        if (this instanceof SeaPool) {
            System.out.println("Tipo: Mar");
        } else {
            System.out.println("Tipo: Rio");
        }

        System.out.println("Tanques: " + getTankArrayList().size());

        for (Tank<? extends Fish> tank : getTankArrayList()) {
            totalCapacity += tank.getMaxCapacity();
            for (Fish fishy : tank.getFishArrayList()) {
                totalFish++;
                if (fishy.isAliveYN()) {
                    totalAliveFish++;
                }

                if (fishy.isFedYN()) {
                    totalFedFish++;
                }
                if (fishy.isElderYN()) {
                    totalElderFish++;
                }
                if (fishy.isFemaleYN()) {
                    totalFemaleFish++;
                } else {
                    totalMaleFish++;
                }

            }
        }

        int fishPercentage = (totalFish == 0) ? 0 : (totalFish * 100 / totalCapacity);
        int aliveFishPercentage = (totalFish == 0) ? 0 : (totalAliveFish * 100 / totalFish);
        int fedFishPercentage = (totalAliveFish == 0) ? 0 : (totalFedFish * 100 / totalAliveFish);
        int elderFishPercentage = (totalAliveFish == 0) ? 0 : (totalElderFish * 100 / totalAliveFish);
        int fertileFishPercentage = (totalAliveFish == 0) ? 0 : (totalFertileFish * 100 / totalAliveFish);

        System.out.println("Ocupacion: " + totalFish + "/" + totalCapacity + "(" + fishPercentage + "%)");
        System.out.println("Peces vivos: " + totalAliveFish + "/" + totalFish + " (" + aliveFishPercentage + "%)");
        System.out
                .println("Peces alimentados: " + totalFedFish + "/" + totalAliveFish + " (" + fedFishPercentage + "%)");
        System.out
                .println("Peces adultos: " + totalElderFish + "/" + totalAliveFish + " (" + elderFishPercentage + "%)");
        System.out.println("Hembras / Machos: " + totalFemaleFish + "/" + totalMaleFish);
        System.out
                .println("Fertiles: " + totalFertileFish + "/" + totalAliveFish + " (" + fertileFishPercentage + "%)");

        int foodPercentage = (getMaxFood() == 0) ? 0 : (getCurrentFood() * 100 / getMaxFood());
        System.out.println(
                "Almacén de comida: " + getCurrentFood() + " / " + getMaxFood() + " (" + foodPercentage + "%)");

    }

    // ADDFOOD RELATED ************------------------------

    //

    public void addFoodToPool(int foodAmount) {
        int availableSpace = getMaxFood() - getCurrentFood();

        if (foodAmount > availableSpace) {
            System.out.println("\n¡Atención! La piscifactoría no puede almacenar toda la comida que estás comprando.");
            System.out.println("Solo se te cobrará la cantidad que la piscifactoría puede almacenar.");
            foodAmount = availableSpace;
        }

        int cost = calculateCost(foodAmount);
        int discountedCost = applyDiscount(foodAmount, cost);

        System.out.println("\nHas pagado " + discountedCost + " monedas por " + foodAmount + " unidades de comida.");

        setCurrentFood(getCurrentFood() + foodAmount);
        purse.buy(discountedCost);
    }

    private int calculateCost(int foodAmount) {
        return foodAmount * COST_PER_UNIT;
    }

    private int applyDiscount(int foodAmount, int cost) {
        int discount = (foodAmount / UNITS_PER_DISCOUNT) * DISCOUNT_PER_QUANTITY;
        return Math.max(0, cost - discount);
    }

    // ---------- END OF ADDFOOD -------------

    // ----------- TANK MANAGEMENT --------

    public void sellFish() {
        for (Tank<? extends Fish> tank : tankArrayList) {
            tank.sellFish();
        }
    }

    public void cleanTank() {
        for (Tank<? extends Fish> tank : tankArrayList) {
            tank.cleanTank();
        }
    }

    public void emptyTank() {
        for (Tank<? extends Fish> tank : tankArrayList) {
            tank.emptyTank();
        }
    }


}
