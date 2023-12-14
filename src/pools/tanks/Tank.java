package pools.tanks;

import commons.Coins;
import commons.Simul;
import pools.Pool;
import pools.tanks.fish.Fish;

import java.util.ArrayList;
import java.util.Iterator;

public class Tank<T extends Fish> {
    ArrayList<Fish> fishArrayList;
    int tankNumber = 0;
    int maxcapacity;

    public Tank(int maxcapacity) {
        this.fishArrayList = new ArrayList<>();
        this.tankNumber++;
        this.maxcapacity = maxcapacity;
    }

    public boolean isFishTypeCompatible(Fish newFish) {
        for (Fish existingFish : fishArrayList) {
            if (!existingFish.getClass().equals(newFish.getClass())) {
                return false; // No es compatible con al menos uno de los peces en el tanque
            }
        }
        return true; // Es compatible con todos los peces en el tanque
    }

    public boolean addFish(Fish newFish) {
        if (isFishTypeCompatible(newFish) && fishArrayList.size() < maxcapacity) {
            fishArrayList.add(newFish);
            return true; // Pez agregado con éxito
        } else {
            return false; // El tanque no está disponible para el nuevo pez
        }
    }

    public ArrayList<Fish> getFishArrayList() {
        return fishArrayList;
    }

    public void setFishArrayList(ArrayList<Fish> fishArrayList) {
        this.fishArrayList = fishArrayList;
    }

    public int getTankNumber() {
        return tankNumber;
    }

    public void setTankNumber(int tankNumber) {
        this.tankNumber = tankNumber;
    }

    public int getMaxCapacity() {
        return maxcapacity;
    }

    public int selectFishWithLowestSex() { // SE ESTA CONTROLANDO MAL ESTO, CUANDO SE BORREN LOS PECES SE VA A LIAR
        int maleCount = 0;
        int femaleCount = 0;

        for (Fish fish : fishArrayList) {
            if (fish.isFemaleYN()) {
                femaleCount++;
            } else {
                maleCount++;
            }
        }

        if (femaleCount <= maleCount) {
            return 1; // MENOR SEXO FEMENINO
        } else {
            return 0; // MENOR SEXO MASCULINO
        }
    }

    // --------------------------- NEXTDAY ------------------

    public void nextDay(Pool pool) {
        Iterator<Fish> iteratorGrow = fishArrayList.iterator();

        while (iteratorGrow.hasNext()) {
            Fish fish = iteratorGrow.next();
            if (fish.isAliveYN()) {
                fish.grow(pool, this);
            }
        }

        Iterator<Fish> iteratorRemove = fishArrayList.iterator();
        while (iteratorRemove.hasNext()) {
            Fish fishRemoved = iteratorRemove.next();
            if (fishRemoved.isConsumedYN()) {
                iteratorRemove.remove();
                System.out.println("El pez " + fishRemoved + " ha sido comido");
            }
        }

        if (isThereAMale()) {
            for (int i = 0; i < fishArrayList.size(); i++) {
                fishArrayList.get(i).reproduce(this);
            }

        }

        Iterator<Fish> iteratorSell = fishArrayList.iterator();

        while (iteratorSell.hasNext()) {
            Fish fishToSell = iteratorSell.next();
            if (fishToSell.isOptimalYN()) {
                autoSellElderFish(fishToSell, iteratorSell);
            }
        }

    }

    public boolean isThereAMale() {
        for (Fish fishMacho : fishArrayList) {
            if (fishMacho.isAliveYN() && !fishMacho.isFemaleYN() && fishMacho.isFertileYN()) {
                return true;
            }
        }
        return false;
    }

    public boolean isThereADeadFish() {
        for (Fish fish : fishArrayList) {
            if (!fish.isAliveYN()) {
                return true;
            }
        }
        return false;
    }

    public void consumeDeadFish() {
        Iterator<Fish> iterator = fishArrayList.iterator();
        while (iterator.hasNext()) {
            Fish fish = iterator.next();
            if (!fish.isConsumedYN()) {
                fish.setConsumedYN(true);
                break;
            }
        }
    }

    public void autoSellElderFish(Fish fish, Iterator<Fish> it) {
        int monedasPagadas = fish.getFishData().getMonedas();
        System.out.println("Tu pez: " + fish + ", se ha vendido por " + monedasPagadas
                + " monedas.");
        it.remove();
        Coins.getInstance().add(monedasPagadas);
        Simul.fishStats.registrarVenta(fish.getFishName(), monedasPagadas);
    }

    public void addChildrenToTank(Fish newFish) {
        this.fishArrayList.add(newFish);
    }

    public int getAliveFish() {
        int totalAlive = 0;
        for (Fish fish : fishArrayList) {
            if (fish.isAliveYN()) {
                totalAlive++;
            }
        }
        return totalAlive;
    }

    public void sellFish() {

        Iterator<Fish> iteratorRemove = fishArrayList.iterator();
        boolean somethingSoldYN = false;
        while (iteratorRemove.hasNext()) {
            Fish fish = iteratorRemove.next();
            int monedasPagadas = fish.getFishData().getMonedas();
            if (fish.isAliveYN() && fish.isElderYN()) {
                iteratorRemove.remove();
                System.out.println("Se ha vendido el pez " + fish);
                Simul.fishStats.registrarVenta(fish.getFishName(), monedasPagadas);
                Coins.getInstance().add(monedasPagadas);
                somethingSoldYN = true;
            }
        }
        if (!somethingSoldYN) {
            System.out.println("\nNo hay peces que cumplan con las condiciones de venta");
        }
    }

    public void cleanTank() {
        Iterator<Fish> iteratorClean = fishArrayList.iterator();
        boolean somethingCleanedYN = false;
        while (iteratorClean.hasNext()) {
            Fish fish = iteratorClean.next();
            if (!fish.isAliveYN()) {
                iteratorClean.remove();
                System.out.println("Se ha eliminado el pez muerto " + fish);
                somethingCleanedYN = true;
            }
        }
        if (!somethingCleanedYN) {
            System.out.println("\nNo hay peces que cumplan con las condiciones de limpiado");
        }
    }

    public void emptyTank() {
        Iterator<Fish> iteratorEmpty = fishArrayList.iterator();
        boolean somethingEmptiedYN = false;
        while (iteratorEmpty.hasNext()) {
            Fish fish = iteratorEmpty.next();
            iteratorEmpty.remove();
            somethingEmptiedYN = true;
        }
        if (somethingEmptiedYN) {
            System.out.println("\nSe han eliminado todos los peces del tanque "
                    + this.getTankNumber());
        } else {
            System.out.println("\nNo hay peces que cumplan con las condiciones de limpiado");
        }

    }
}
