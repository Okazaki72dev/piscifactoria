package pools.tanks.fish.riverfish;

import pools.tanks.fish.feedtype.OmnivoroComedido;
import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;

public class CarpinTresEspinas extends OmnivoroComedido implements IRiverFish {
    static PecesDatos pd = AlmacenPropiedades.CARPIN_TRES_ESPINAS;

    public CarpinTresEspinas() {
        super(pd);
    }

}
