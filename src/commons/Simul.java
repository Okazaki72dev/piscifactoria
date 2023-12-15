package commons;

import pools.Pool;
import pools.RiverPool;
import pools.SeaPool;
import pools.tanks.Tank;
import pools.tanks.fish.Fish;
import pools.tanks.fish.riverfish.CarpinTresEspinas;
import propiedades.AlmacenPropiedades;
import registro.ErrorLogger;
import registro.Transcripcion;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class Simul {
    static Scanner scan = new Scanner(System.in);
    static int currentDay = 0;
    static int poolCount = 1;
    static String gameName;
    static ArrayList<RiverPool> riverPoolArray;
    static ArrayList<SeaPool> seaPoolArray;
    static ArrayList<Pool> everyPoolArray;
    static String[] peces = {
            AlmacenPropiedades.CARPIN_TRES_ESPINAS.getNombre(),
    };
    public static estadisticas.Estadisticas fishStats = new estadisticas.Estadisticas(peces);

    static Coins purse = Coins.getInstance(); // Coins por SINGLETON
    static AlmacenCentral almacenCentral = null;
    static Transcripcion trans = null;

    static void init(String gameName) {
        Simul.gameName = gameName;

        // DESPUES PASARA A REGISTRO
        trans = Transcripcion.getInstance();

        createArrays();
        System.out.println("Dame el nombre de tu primera piscifactoria (de río)");
        String poolName = scan.nextLine();
        RiverPool p1 = new RiverPool(poolName);
        p1.setCurrentFood(25);
        riverPoolArray.add(p1);
        everyPoolArray.add(p1);
        purse.add(10000);
        Simul.initMenu();
    }

    static void createArrays() {
        if (riverPoolArray == null && seaPoolArray == null && everyPoolArray == null) {
            riverPoolArray = new ArrayList<>();
            seaPoolArray = new ArrayList<>();
            everyPoolArray = new ArrayList<>();
        }
    }

    private static void menu() {

        System.out.println("\n\n=====Menú=====\n");
        System.out.println("Dia: " + Simul.currentDay);
        System.out.println("\n1. Estado general");
        System.out.println("\n2. Estado piscifactoría");
        System.out.println("\n3. Estado tanques");
        System.out.println("\n4. Informes");
        System.out.println("\n5. Ictiopedia");
        System.out.println("\n6. Pasar día");
        System.out.println("\n7. Comprar comida");
        System.out.println("\n8. Comprar peces");
        System.out.println("\n9. Vender peces");
        System.out.println("\n10. Limpiar tanques");
        System.out.println("\n11. Vaciar tanque");
        System.out.println("\n12. Mejorar");
        System.out.println("\n13. Pasar varios días");
        System.out.println("\n14. Salir");
        System.out.println("\n15. Mostrar info peces");
        System.out.println("\nMonedas disponibles: " + purse.getCoinAmmount() + "\n\n");
        System.out.print("=====Elige una opción===== \n\n");
    }


    /// 1MENU
    private static void initMenu() {
        boolean salir = false;

        while (!salir) {
            menu();
            try {
                int opcion = scan.nextInt(); // Intenta leer la elección del usuario

                switch (opcion) {

                    case 1:
                        showGeneralStatus();
                        break;

                    case 2:
                        showSpecificStatus();
                        break;

                    case 3:
                        showTankStatus();
                        break;

                    case 4:
                        showStats();
                        break;

                    case 5:
                        showIctio();
                        break;

                    case 6:
                        nextDay();
                        break;

                    case 7:
                        addFood();
                        break;

                    case 8:
                        addFish();
                        break;

                    case 9:
                        sell();
                        break;

                    case 10:
                        cleanTank();
                        break;

                    case 11:
                        emptyTank();
                        break;

                    case 12:
                        showUpgradeMenu();
                        int opcionMejora = scan.nextInt();
                        processUpgradeOption(opcionMejora);
                        break;

                    case 13:
                        System.out.println("\nCuantos dias quieres avanzar?\n");
                        skipDays(scan.nextInt());
                        break;

                    case 14:
                        salir = true;
                        break;

                    case 15:
                        mostrarInfoPeces();
                        break;

                    default:
                        System.out.println("Opción no válida. Por favor, elige una opción del menú.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingresa un número entero.");
                scan.nextLine(); // Limpia la entrada no válida
            }

        }
    }

    private static void nextDay() {
        currentDay++;

        // Itera sobre las piscifactorías
        for (Pool everyPool : everyPoolArray) {
            everyPool.nextDay();
        }
    }

    private static void skipDays(int daysToSkip) {
        for (int i = 0; i < daysToSkip; i++) {
            nextDay();
        }
    }

    // SHOW STATUS METHODS ************-------------------

    private static void showGeneralStatus() {
        System.out.println("\n=====Estado General=====\n");
        System.out.println("Día actual: " + currentDay);
        System.out.println("Monedas disponibles: " + purse.getCoinAmmount());

        // Mostrar estado de todas las piscifactorias
        for (Pool everyPool : everyPoolArray) {
            everyPool.showStatus();
        }

        if (almacenCentral != null) {
            System.out.println("\n--- Almacén Central ---");
            System.out.println("Comida almacenada: " + almacenCentral.getComidaActual());
            System.out.println("Capacidad máxima: " + almacenCentral.getCapacidadComidaMaxima());
            double percentage = (almacenCentral.getComidaActual() * 100.0) /
                    almacenCentral.getCapacidadComidaMaxima();
            System.out.println("Porcentaje de capacidad utilizada: " +
                    String.format("%.1f", percentage) + "%");
        } else {
            System.out.println("\nAun no has añadido almacencentral");
        }

    }

    private static void showSpecificStatus() {

        if (Simul.everyPoolArray.isEmpty()) {
            System.out.println("\nNo hay piscifactorías disponibles para mostrar estado específico.");
            return;
        }

        Pool selectedPool = selectPisc(Simul.everyPoolArray);
        if (selectedPool != null) {
            selectedPool.showStatus();
        }

    }

    private static void showTankStatus() {
        System.out.println("\n=====Estado de los tanques=====\n");
        for (Pool everyPool : everyPoolArray) {
            System.out.println("Piscifactoría: " + everyPool.getPoolName());
            int tankNumber = 1;
            for (Tank<? extends Fish> tank : everyPool.getTankArrayList()) {
                System.out.println("\n=====Informacion de los tanques de Piscifactoría: " + everyPool.getPoolName() + "=====\n\n" + "Tanque #" + tankNumber + ":");
                int fishCount = tank.getFishArrayList().size();
                int tankCapacity = tank.getMaxCapacity();
                double capacityPercentage = (fishCount * 100.0) / tankCapacity;
                System.out.println("\nOcupación: " + fishCount + " peces / " + tankCapacity + " espacios (" + String.format("%.1f", capacityPercentage) + "%)");
                System.out.println("Peces vivos: " + fishCount + " / " + tankCapacity + " (" + String.format("%.1f", capacityPercentage) + "%)");

                if (!tank.getFishArrayList().isEmpty()) {
                    System.out.println("Tipo de pez en el tanque: " + tank.getFishArrayList().get(0).getClass().getSimpleName());

                }

                tankNumber++;
            }
        }
    }

    private static void showStats() {
        Simul.fishStats.mostrar();
    }

    private static void showIctio() {
        printIctio(addFishHelper());
    }

    private static void printIctio(Fish fishToShowIctio) {
        if (fishToShowIctio != null) {
            System.out.println("\n--" + fishToShowIctio.getFishData().getNombre() + "--\n" + "\nNombre científico: " + fishToShowIctio.getFishData().getCientifico() + "\nTipo: " + fishToShowIctio.getFishData().getTipo() + "\nCoste: " + fishToShowIctio.getFishData().getCoste() + "\nMonedas: " + fishToShowIctio.getFishData().getMonedas() + "\nHuevos: " + fishToShowIctio.getFishData().getHuevos() + "\nCiclo: " + fishToShowIctio.getFishData().getCiclo() + "\nMadurez: " + fishToShowIctio.getFishData().getMadurez() + "\nÓptimo: " + fishToShowIctio.getFishData().getOptimo());

        }
    }

    //

    // --------------END OF STATS ***************---------------

    // --------------ADD FISH ***************---------------

    //

    private static void addFish() {
        Fish newFish = addFishHelper();

        if (newFish != null) {

            for (Pool everyPool : everyPoolArray) {
                ArrayList<Tank<? extends Fish>> availableTanks = new ArrayList<>();
                for (Tank<? extends Fish> tank : everyPool.getTankArrayList()) {
                    if (isTankAvailableForFish(tank, newFish)) {
                        availableTanks.add(tank);

                    }
                }

                if (!availableTanks.isEmpty()) {
                    System.out.println("\nTanques disponibles en la Piscifactoría " + (everyPoolArray.indexOf(everyPool) + 1) + ":\n");
                    for (int i = 0; i < availableTanks.size(); i++) {
                        System.out.println((i + 1) + ". Piscifactoría " + (everyPoolArray.indexOf(everyPool) + 1) + " Tanque " + (i + 1) + "\n");
                    }

                    System.out.print("Elige el número del tanque donde deseas agregar el pez: ");
                    int selectedTankIndex = scan.nextInt();
                    if (selectedTankIndex > 0 && selectedTankIndex <= availableTanks.size()) {
                        Tank<? extends Fish> selectedTank = availableTanks.get(selectedTankIndex - 1);
                        int selectedSex = selectedTank.selectFishWithLowestSex();
                        newFish.setFemaleYN(selectedSex == 1);
                        boolean added = selectedTank.addFish(newFish);
                        Simul.fishStats.registrarNacimiento(newFish.getFishName());
                        if (added && purse.buy(newFish.getFishData().getCoste())) {
                            System.out.println("\nSe ha agregado un pez al tanque " + selectedTankIndex);
                            int fishCount = selectedTank.getFishArrayList().size();
                            int tankCapacity = selectedTank.getMaxCapacity();
                            double capacityPercentage = (fishCount * 100.0) / tankCapacity;
                            System.out.println("Tanque #" + selectedTank.getTankNumber() + " de la Piscifactoría " + (everyPoolArray.indexOf(everyPool) + 1) + " al " + capacityPercentage + "% de capacidad. [" + fishCount + " peces / " + tankCapacity + " espacios]");

                        } else {
                            System.out.println("No se pudo agregar el pez al tanque. Verifica las condiciones del tanque.");
                        }
                    } else {
                        System.out.println("Número de tanque inválido. Volviendo al menú principal.");
                    }
                } else {
                    System.out.println("No hay ningun tanque disponible");
                }
            }

        }
    }

    private static Fish addFishHelper() {
        System.out.println("\n=====Selección de peces=====\n\nMonedas disponibles: " + purse.getCoinAmmount());
        System.out.println("Elige el tipo de pez que quieres escoger:\n");
        System.out.println("1. Carpin Tres Espinas - Costo: " + AlmacenPropiedades.CARPIN_TRES_ESPINAS.getCoste() + " monedas");
        int fishType = scan.nextInt();

        Fish newFish = createFish(fishType);

        if (newFish != null) {
            return newFish;
        } else {
            System.out.println("Tipo de pez no válido.");
            return null;
        }
    }

    private static Fish createFish(int fishType) {
        switch (fishType) {
            case 1:
                return new CarpinTresEspinas();

            default:
                return null;
        }
    }

    private static boolean isTankAvailableForFish(Tank<? extends Fish> tank, Fish newFish) {
        if (!isFishTypeCompatible(tank, newFish)) {
            return false;
        }

        if (tank.getFishArrayList().size() >= tank.getMaxCapacity()) {
            System.out.println("No hay tanques disponibles, volviendo al menu principal \n" + "Recuerda que puedes añadir tanques nuevos o ampliar el tamaño de estos en el menu de \"Mejora\" ");
            return false;
        }

        return true;
    }

    private static boolean isFishTypeCompatible(Tank<? extends Fish> tank, Fish newFish) {
        for (Fish fish : tank.getFishArrayList()) {
            if (fish.getClass() != newFish.getClass()) {
                return false;
            }
        }
        return true;
    }

    // -------------- END OF ADD FISH ***************---------------

    //

    private static void mostrarInfoPeces() {
        System.out.println("\n=====Información de los peces en tus tanques=====\n");

        for (Pool everyPool : everyPoolArray) {
            for (Tank<? extends Fish> tank : everyPool.getTankArrayList()) {
                ArrayList<Fish> fishArrayList = tank.getFishArrayList();
                if (!fishArrayList.isEmpty()) {
                    System.out.println("Piscifactoría: " + everyPool.getPoolName() + " - Tanque " + tank.getTankNumber() + "\n");
                    for (Fish fish : fishArrayList) {
                        String sexo = fish.isFemaleYN() ? "Hembra" : "Macho";
                        System.out.println("Tipo de pez: " + fish.getClass().getSimpleName() + " - Sexo: " + sexo);
                        System.out.println("Nombre científico: " + fish.getScientificFishName());
                        System.out.println("Edad: " + fish.getAge() + " días");
                        System.out.println("¿Está vivo? " + (fish.isAliveYN() ? "Sí" : "No"));
                        System.out.println("¿Ha sido alimentado? " + (fish.isFedYN() ? "Sí" : "No"));
                        System.out.println("¿Es fértil? " + (fish.isFertileYN() ? "Sí" : "No"));
                        System.out.println("¿Es adulto? " + (fish.isElderYN() ? "Sí" : "No"));
                        System.out.println("¿Está en su estado óptimo? " + (fish.isOptimalYN() ? "Sí" : "No"));
                        System.out.println("\n\n");
                    }
                }
            }
        }
    }

    // ADD FOOOD ------------------ // ******************

    //

    private static void addFood() {

        if (almacenCentral != null) {
            if (almacenCentral.getComidaActual() < almacenCentral.getCapacidadComidaMaxima()) {
                System.out.println("Cuanta comida quieres añadir?");
                int foodAmount = askForFoodAmountAlmacen(almacenCentral);
                almacenCentral.addFoodAlmacen(foodAmount);
            } else {
                System.out.println("El almacen esta lleno, no se puede añadir comida");
            }
        } else {
            ArrayList<Pool> availablePools = getAvailablePools();

            if (availablePools.isEmpty()) {
                System.out.println("\nNo hay piscifactorías disponibles para añadir comida.");
                return;
            }

            Pool selectedPool = selectPisc(availablePools);
            if (selectedPool != null) {
                int foodAmount = askForFoodAmount(selectedPool);
                selectedPool.addFoodToPool(foodAmount);
            }
        }

    }

    private static ArrayList<Pool> getAvailablePools() {
        ArrayList<Pool> availablePools = new ArrayList<>();

        for (Pool pool : Simul.everyPoolArray) {
            if (pool.getCurrentFood() < pool.getMaxFood()) {
                availablePools.add(pool);
            }
        }

        return availablePools;
    }

    private static int askForFoodAmount(Pool selectedPool) {
        System.out.println("\n---------------------- Elegir cantidad ----------------------");
        System.out.println("\nLa piscifactoría seleccionada (" + selectedPool.getPoolName() + ") contiene:\n" + "Comida actual: " + selectedPool.getCurrentFood() + "   Comida máxima: " + selectedPool.getMaxFood());
        System.out.println("\n===== Opciones de Cantidad de Comida =====\n" + "Cada unidad de comida vale: " + Pool.getCostPerUnit() + " moneda" + "\n(Cuando se compren 25 unidades de comida, se rebajarán 5 monedas)\n");
        System.out.println("1. 5 unidades");
        System.out.println("2. 10 unidades");
        System.out.println("3. 25 unidades");
        System.out.println("4. Llenar (máximo)");

        int choice = askForInt("\nElige una opción (1-4): ", 1, 4);

        return switch (choice) {
            case 1 -> 5;
            case 2 -> 10;
            case 3 -> 25;
            case 4 -> selectedPool.getMaxFood() - selectedPool.getCurrentFood();
            default -> 0;
        };
    }

    private static int askForFoodAmountAlmacen(AlmacenCentral almacenCentral) {
        System.out.println("\n---------------------- Elegir cantidad ----------------------");
        System.out.println("\nEl almacen central contiene:\n" + "Comida actual: " + almacenCentral.getComidaActual() + "   Comida máxima: " + almacenCentral.getCapacidadComidaMaxima());
        System.out.println("\n===== Opciones de Cantidad de Comida =====\n" + "Cada unidad de comida vale: " + Pool.getCostPerUnit() + " moneda" + "\n(Cuando se compren 25 unidades de comida, se rebajarán 5 monedas)\n");
        System.out.println("1. 5 unidades");
        System.out.println("2. 10 unidades");
        System.out.println("3. 25 unidades");
        System.out.println("4. Llenar (máximo)");

        int choice = askForInt("\nElige una opción (1-4): ", 1, 4);

        return switch (choice) {
            case 1 -> 5;
            case 2 -> 10;
            case 3 -> 25;
            case 4 -> almacenCentral.getCapacidadComidaMaxima() - almacenCentral.getComidaActual();
            default -> 0;
        };
    }

    // Por parametro le pasamos las pisc que nos interesa enseñar en menu

    private static Pool selectPisc(ArrayList<? extends Pool> availablePools) {
        while (true) {
            menuPisc(availablePools);

            System.out.print("\nElige una piscifactoría (1-" + availablePools.size() + ", 0 para ir atrás): ");
            int choice = askForInt("\nOpción: ", 0, availablePools.size());

            if (choice == 0) {
                return null; // El usuario eligió "ir atrás"
            } else if (choice >= 1 && choice <= availablePools.size()) {
                return availablePools.get(choice - 1);
            } else {
                System.out.println("\nOpción no válida. Inténtalo de nuevo.");
            }
        }
    }

    // Clase ayudante
    private static void menuPisc(ArrayList<? extends Pool> availablePools) {
        System.out.println("\nSeleccione una opción:");
        System.out.println("\n--------------------------- Piscifactorías ---------------------------");

        int totalFishGlobal = 0;
        int livingFishGlobal = 0;
        int totalSpaceGlobal = 0;

        for (Pool pool : availablePools) {
            for (Tank<? extends Fish> tank : pool.getTankArrayList()) {
                totalSpaceGlobal += tank.getMaxCapacity();
                totalFishGlobal += tank.getFishArrayList().size();
                for (Fish fish : tank.getFishArrayList()) {
                    if (fish.isAliveYN()) {
                        livingFishGlobal++;
                    }
                }
            }
        }

        System.out.println("\nPeces vivos totales: " + livingFishGlobal + " Peces totales: " + totalFishGlobal + " Espacio total: " + totalSpaceGlobal + "\n");

        for (Pool pool : availablePools) {
            int totalFish = 0;
            int livingFish = 0;
            int totalSpace = 0;
            int i = 0;
            for (Tank<? extends Fish> tank : pool.getTankArrayList()) {
                totalFish += tank.getFishArrayList().size(); // Suma todos los peces (vivos y muertos)
                livingFish += tank.getAliveFish(); // Suma solo los peces vivos
                totalSpace += tank.getMaxCapacity(); // Suma la capacidad del tanque
            }
            System.out.println("[Peces vivos / Peces totales / Espacio total]");
            System.out.println("\n" + (i + 1) + ". " + pool.getPoolName() + " [" + livingFish + "/" + totalFish + "/" + totalSpace + "]" + "\nComida actual: " + pool.getCurrentFood() + "  Comida máxima: " + pool.getMaxFood());
            System.out.println("\nTienes " + purse.getCoinAmmount() + " monedas");
        }
    }

    private static int askForInt(String message, int min, int max) {
        int choice;

        while (true) {
            System.out.print(message);

            try {
                choice = scan.nextInt();
                if (choice >= min && choice <= max) {
                    break;
                } else {
                    System.out.println("\nOpción no válida. Inténtalo de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nIngresa un número válido.");
                scan.next(); // Limpiar el búfer del escáner
            }
        }

        return choice;
    }

    // ---------------------- END ADDFOOD *************


    //


    // MENUUUUUUUUUUUUUUUUUUUUUUUU UPGRAAAAAAAADE


    private static void showUpgradeMenu() {
        System.out.println("\n===== Menú de Mejoras =====\n");
        System.out.println("1. Comprar Edificios");
        System.out.println("2. Mejorar Edificios");
        System.out.println("3. Cancelar");
        System.out.print("\nElige una opción (1-3): ");
    }

    private static void processUpgradeOption(int option) {
        switch (option) {
            case 1:
                showBuildingOptions();
                int buildingOption = scan.nextInt();
                processBuildingOption(buildingOption);
                break;
            case 2:
                showUpgradeOptions();
                int upgradeOption = scan.nextInt();
                processUpgradeBuildingOption(upgradeOption);
                break;
            case 3:
                System.out.println("Cancelando...");
                break;
            default:
                System.out.println("Opción no válida. Inténtalo de nuevo.");
        }
    }

    private static void showBuildingOptions() {
        System.out.println("\n----- Comprar Edificios -----");
        System.out.println("1. Piscifactoría");
        System.out.println("2. Almacén central");
        System.out.print("\nElige una opción (1, 2): ");
    }

    private static void processBuildingOption(int option) {
        switch (option) {
            case 1:
                System.out.print("Elige el tipo de la Piscifactoría: \n1.Rio\n2.Mar");
                int opcionTipo = scan.nextInt();
                System.out.print("Dame el nombre de la Piscifactoría: ");
                String piscifactoriaName = scan.next();
                switch (opcionTipo) {
                    case 1:
                        comprarRiverPoolHelper(piscifactoriaName);
                        break;
                    case 2:
                        comprarSeaPoolHelper(piscifactoriaName);
                        break;

                    default:
                        System.out.println("Por favor, escribe (1) o (2)");
                }
                break;

            // ALMACEEEEEN COMPRA
            case 2:
                if (almacenCentral == null) {
                    if (AlmacenCentral.COSTE_ALMACEN <= purse.getCoinAmmount()) {
                        System.out.println("Tienes " + purse.getCoinAmmount() + " monedas, " +
                                "quieres comprar un almacen central por " + AlmacenCentral.COSTE_ALMACEN +
                                " monedas?");
                        System.out.println("1. Si");
                        System.out.println("2. No");

                        switch (scan.nextInt()) {
                            case 1:
                                almacenCentral = AlmacenCentral.getInstance();
                                purse.buy(AlmacenCentral.COSTE_ALMACEN);
                                System.out.println("\nHas comprado " + almacenCentral.toString());
                                break;

                            case 2:
                                System.out.println("No se ha comprado el almacen");
                                break;

                            default:
                                System.out.println("Por favor, introduce un numero valido");
                        }

                    } else {
                        System.out.println("No tienes dinero sufieciente para comprar un almacen central");
                    }
                } else {
                    System.out.println("Ya dispones de un almacen central");
                }

                break;
            default:
                System.out.println("Opción no válida. Inténtalo de nuevo.");
        }
    }


    private static void comprarRiverPoolHelper(String piscifactoriaName) {
        int costeRiverPool = RiverPool.getBASE_COST_POOL() * (riverPoolArray.size() + 1);

        if (purse.getCoinAmmount() >= costeRiverPool) {
            System.out.println("Tienes " + purse.getCoinAmmount() + " monedas, quieres " +
                    "comprar una piscifactoria de rio por " + costeRiverPool + "?" + "\n1.Si\n2.No");
            int opcionSiNo = scan.nextInt();
            switch (opcionSiNo) {
                case 1:
                    RiverPool riverPoolToAdd = new RiverPool(piscifactoriaName);
                    riverPoolArray.add(riverPoolToAdd);
                    everyPoolArray.add(riverPoolToAdd);
                    purse.buy(costeRiverPool);
                    System.out.println("\nHas añadido una piscifactoria de rio");
                    break;
                case 2:
                    System.out.println("Pos nada");
                    break;
                default:
                    System.out.println("Por favor, introduce el numero (1) o el (2)");
                    break;

            }
        } else {
            System.out.println("No tienes suficientes monedas para comprar una piscifactoria " +
                    "de río, tienes " + purse.getCoinAmmount() + " monedas y necesitas " +
                    costeRiverPool + " monedas");
        }
    }

    private static void comprarSeaPoolHelper(String piscifactoriaName) {

        int costeSeaPool = SeaPool.getBASE_COST_POOL() * (seaPoolArray.size() + 1);

        if (purse.getCoinAmmount() >= costeSeaPool) {
            System.out.println("Tienes " + purse.getCoinAmmount() + " monedas, quieres " +
                    "comprar una piscifactoria de mar por " + costeSeaPool + "?" + "\n1.Si\n2.No");
            int opcionSiNo = scan.nextInt();
            switch (opcionSiNo) {
                case 1:
                    SeaPool seaPoolToAdd = new SeaPool(piscifactoriaName);
                    seaPoolArray.add(seaPoolToAdd);
                    everyPoolArray.add(seaPoolToAdd);
                    purse.buy(costeSeaPool);
                    System.out.println("\nHas añadido una piscifactoria de mar");
                    break;
                case 2:
                    System.out.println("Pos nada");
                    break;
                default:
                    System.out.println("Por favor, introduce el numero (1) o el (2)");
                    break;

            }
        } else {
            System.out.println("No tienes suficientes monedas para comprar una piscifactoria " +
                    "de mar, tienes " + purse.getCoinAmmount() + " monedas y necesitas " +
                    costeSeaPool + " monedas");
        }
    }

    private static void showUpgradeOptions() {
        System.out.println("\n----- Mejorar Edificios -----");
        System.out.println("1. Piscifactoría");
        System.out.println("2. Almacén central");
        System.out.print("\nElige una opción (1, 2): ");
    }

    private static void processUpgradeBuildingOption(int option) {
        switch (option) {
            case 1:
                showPiscifactoriaOptions();
                int piscifactoriaOption = scan.nextInt();
                processPiscifactoriaOption(piscifactoriaOption);
                break;
            case 2:
                showAlmacenCentralOptions();
                int almacenCentralOption = scan.nextInt();
                processAlmacenCentralOption(almacenCentralOption);
                break;
            default:
                System.out.println("Opción no válida. Inténtalo de nuevo.");
        }
    }

    private static void showPiscifactoriaOptions() {
        System.out.println("\n--- Opciones de Piscifactoría ---");
        System.out.println("1. Comprar tanque");
        System.out.println("2. Aumentar almacén de comida");
        System.out.print("\nElige una opción (1, 2): ");
    }

    private static void showAlmacenCentralOptions() {
        System.out.println("\n--- Opciones de Almacén Central ---");
        System.out.println("1. Aumentar capacidad");
        System.out.print("\nElige una opción (1): ");
    }


    private static void processPiscifactoriaOption(int option) {
        switch (option) {
            case 1:
                // Si
                break;
            case 2:
                // Lógica para aumentar almacén de comida en Piscifactoría
                break;
            default:
                System.out.println("Opción no válida. Inténtalo de nuevo.");
        }
    }

    private static void processAlmacenCentralOption(int option) {
        switch (option) {
            case 1:
                // Lógica para aumentar capacidad en Almacén Central
                break;
            default:
                System.out.println("Opción no válida. Inténtalo de nuevo.");
        }
    }

    // ------------ TANK MANAGEMENT ***********************

    public static void sell() {
        selectPisc(everyPoolArray).sellFish();
    }

    private static void cleanTank() {
        selectPisc(everyPoolArray).cleanTank();
    }

    private static void emptyTank() {
        selectPisc(everyPoolArray).emptyTank();
    }


    // GETTERS

    public static String getGameName() {
        return gameName;
    }

    public static String[] getPeces() {
        return peces;
    }

    public static void main(String[] args) throws Exception {
        try {
            System.out.println("Nombre de tu partida: ");
            init(scan.nextLine());
            System.out.println(1/0);
        }
        catch (Exception e){
            trans.finalizarTranscripcion();
            ErrorLogger.getInstance().registrarError("Cerrando streams debido a error.\nFin del programa");
        }

    }
}
