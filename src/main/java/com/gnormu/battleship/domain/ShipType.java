package com.gnormu.battleship.domain;

/**
 * Representa los tipos de barcos inmutables y sus longitudes.
 * 
 * @implNote Se utilizan constantes primitivas de tipo byte para reducir a cero
 *           el uso de memoria en heap de arreglos de objetos y maximizar
 *           rendimiento.
 */
public final class ShipType {
    // identificación
    public static final byte NONE = -1;
    public static final byte CARRIER = 0;
    public static final byte BATTLESHIP = 1;
    public static final byte CRUISER = 2;
    public static final byte SUBMARINE = 3;
    public static final byte DESTROYER = 4;

    // largo de los barcos
    public static final byte CARRIER_LENGTH = 5;
    public static final byte BATTLESHIP_LENGTH = 4;
    public static final byte CRUISER_LENGTH = 3;
    public static final byte SUBMARINE_LENGTH = 3;
    public static final byte DESTROYER_LENGTH = 2;

    public static final byte[] LENGTHS = {
            CARRIER_LENGTH,
            BATTLESHIP_LENGTH,
            CRUISER_LENGTH,
            SUBMARINE_LENGTH,
            DESTROYER_LENGTH
    };

    public static final int COUNT = LENGTHS.length;
    public static final int TOTAL_HEALTHS;

    static {
        int sum = 0;
        for (byte len : LENGTHS) {
            sum += len;
        }
        TOTAL_HEALTHS = sum;
    }

    private ShipType() {
    }
}
