package pools;

import pools.tanks.Tank;
import pools.tanks.fish.Fish;

import java.util.ArrayList;

public class SeaPool extends Pool implements ISeaPool{

    private static final int BASE_COST_POOL = 2000;

    public SeaPool(String poolName) {
        super(poolName, 25);
        tankArrayList.add(new Tank<Fish>(100));
    }

    public static int getBASE_COST_POOL() {
        return BASE_COST_POOL;
    }

    public ArrayList<Tank<? extends Fish>> getRiverPoolTanks() {
        return tankArrayList;
    }

    public void setRiverPoolTanks(ArrayList<Tank<? extends Fish>> tanks) {
        this.tankArrayList = tanks;
    }

}
