package pools.tanks.fish.feedtype;

import pools.Pool;
import pools.tanks.Tank;
import pools.tanks.fish.Fish;
import propiedades.PecesDatos;

import java.util.Random;

public abstract class OmnivoroComedido extends Fish {

    public OmnivoroComedido(PecesDatos pd) {
        super(pd);
    }

    @Override
    public void feed(Pool pool, Tank<? extends Fish> tank) {
        Random rd = new Random();

        if (rd.nextInt(4) < 3 || rd.nextInt(4) < 1) { // No quiere comer nada
            this.fedYN = true;
        } else {
            if (tank.isThereADeadFish()) { // Hay peces muertos en el tanque
                if (rd.nextBoolean()) { // Consume un pez muerto y se alimenta
                    tank.consumeDeadFish();
                    this.fedYN = true;

                } else { // No consume pez muerto y se alimenta
                    this.fedYN = true;
                }
            } else { // No hay peces muertos, intenta comer comida
                int foodNeeded = 1;
                // ¿Hay comida?
                if (pool.getCurrentFood() >= foodNeeded) { // Hay comida, se alimenta
                    this.fedYN = true;
                    pool.setCurrentFood(pool.getCurrentFood() - foodNeeded); // Se reduce la comida en X
                } else { // No hay comida, 50-50 de morir
                    if (rd.nextBoolean()) { // 50% de probabilidad de morir
                        this.aliveYN = false;
                        this.fedYN = false;
                    }
                }
            }
        }

    }

}
