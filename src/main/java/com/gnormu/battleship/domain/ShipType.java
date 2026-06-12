package com.gnormu.battleship.domain;

/**
 * Representa los tipos de barcos inmutables (Flyweight) y su longitud.
 * 
 * @implNote Utilizar el patrón Flyweight aporta ventajas respecto a utilizar
 *           una clase Ship tradicional para la instanciación de los barcos,
 *           reduciendo el uso de memoria y tiempo de clonado al momento de
 *           utilizarlos en un tablero, ya que se crean múltiples instancias de
 *           barcos en un mismo tablero de {@link Board}.
 * 
 * @implNote Se ha delegado la lógica de manejo de vida del barco a la clase
 *           {@link Board}
 * 
 */
public enum ShipType {
    CARRIER(5),
    BATTLESHIP(4),
    CRUISER(3),
    SUBMARINE(3),
    DESTROYER(2);

    private final int length;

    ShipType(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
