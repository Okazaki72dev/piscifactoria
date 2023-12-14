package pools.tanks.fish;

import pools.Pool;
import pools.tanks.Tank;
import propiedades.PecesDatos;

import java.util.Random;

public abstract class Fish implements Cloneable {
    protected PecesDatos fishData;

    protected String fishName;
    protected String scientificFishName;

    protected int age;
    protected boolean femaleYN;

    protected boolean aliveYN;
    protected boolean fedYN;
    protected boolean fertileYN;
    protected boolean elderYN;
    protected boolean optimalYN;
    protected boolean consumedYN;

    public Fish(PecesDatos pd) {
        this.fishData = pd;
        this.fishName = fishData.getNombre(); // Inicializa el nombre del pez
        this.scientificFishName = fishData.getCientifico(); // Inicializa el nombre científico del pez
        this.age = 0; // El pez comienza con 0 días de edad
        this.femaleYN = (new Random().nextBoolean()); // Pseudo-randomiza si es hembra o macho
        this.aliveYN = true; // El pez comienza vivo
        this.fedYN = false; // El pez comienza sin haber sido alimentado
        this.fertileYN = false; // El pez comienza no siendo fértil
        this.elderYN = false; // El pez comienza sin ser adulto
        this.optimalYN = false;
        this.consumedYN = false; // El pez comienza sin estar en su óptimo
    }

    public abstract void feed(Pool pool, Tank<? extends Fish> tank);

    public PecesDatos getFishData() {
        return fishData;
    }

    public String getFishName() {
        return fishName;
    }

    public String getScientificFishName() {
        return scientificFishName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isFemaleYN() {
        return femaleYN;
    }

    public void setFemaleYN(boolean femaleYN) {
        this.femaleYN = femaleYN;
    }

    public boolean isAliveYN() {
        return aliveYN;
    }

    public void setAliveYN(boolean aliveYN) {
        this.aliveYN = aliveYN;
    }

    public boolean isFedYN() {
        return fedYN;
    }

    public void setFedYN(boolean fedYN) {
        this.fedYN = fedYN;
    }

    public boolean isFertileYN() {
        return fertileYN;
    }

    public void setFertileYN(boolean fertileYN) {
        this.fertileYN = fertileYN;
    }

    public boolean isElderYN() {
        return elderYN;
    }

    public void setElderYN(boolean elderYN) {
        this.elderYN = elderYN;
    }

    public boolean isOptimalYN() {
        return optimalYN;
    }

    public void setOptimalYN(boolean optimalYN) {
        this.optimalYN = optimalYN;
    }

    public boolean isConsumedYN() {
        return consumedYN;
    }

    public void setConsumedYN(boolean consumedYN) {
        this.consumedYN = consumedYN;
    }

    public void grow(Pool pool, Tank<? extends Fish> tank) {

        // SE ALIMENTA
        this.feed(pool, tank);

        // SUMA 1 EDAD
        this.age++;

        if (aliveYN) {
            int madurez = this.fishData.getMadurez();
            int ciclo = this.fishData.getCiclo();

            if (this.age == madurez) {
                this.fertileYN = true; // Marcar como fértil cuando llega a la edad de madurez
                this.elderYN = true; // Marcar como maduro cuando llega a la edad de madurez
            }

            if (this.age > madurez) {
                int diasDesdeMadurez = this.age - madurez;
                // Dejar de ser fértil si no está en un día de ciclo
                this.fertileYN = diasDesdeMadurez % ciclo == 0; // El pez se vuelve fértil

                if (this.age >= this.fishData.getOptimo()) {
                    // Vender el pez y otorgar las monedas correspondientes
                    this.optimalYN = true;
                }
            }
        }

    }

    public void reproduce(Tank<? extends Fish> tank) {
        if (this.isFemaleYN() && this.isFertileYN() && this.isAliveYN()) {
            int huevosPorDefecto = this.fishData.getHuevos();
            int huevosAPoner = Math.min(huevosPorDefecto, (tank.getMaxCapacity() - tank.getFishArrayList().size()));

            for (int i = 0; i < huevosAPoner; i++) {
                tank.addChildrenToTank(this.createChild());

            }
            System.out.println("La hembra ha puesto " + huevosAPoner + " huevos en el tanque.");
            this.fertileYN = false;

        }
    }

    private Fish createChild() {
        try {
            Fish newFish = (Fish) this.clone();
            newFish.reset();
            return newFish;
        } catch (CloneNotSupportedException e) {
            System.out.println("Error al crear el hijo");
            e.printStackTrace();
        }
        return null;
    }

    private void reset() {
        this.age = 0;
        this.aliveYN = true;
        this.fedYN = false;
        this.fertileYN = false;
        this.elderYN = false;
        this.optimalYN = false;
    }


}
