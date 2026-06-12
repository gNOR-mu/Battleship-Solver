package com.gnormu.battleship.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
/**
 * Clase que representa un barco, sus atributos, métodos y comportamiento
 */
public class Ship {
    /** Identificador del barco */
    private final char identifier;

    /** Largo del barco */
    private final int length;

    /** Vida del barco */
    private int life;

    /**
     * Constructor de la clase Ship
     * <p>
     * De forma predeterminada la vida de un barco recién creado corresponde a su
     * largo.
     *
     * @param identifier Identificador del barco
     * @param length     Largo del barco
     */
    public Ship(char identifier, int length) {
        this.identifier = identifier;
        this.length = length;
        this.life = length;
    }

    /**
     * Realiza un disparo al barco reduciendo su vida
     * 
     * <p>
     * La vida de un barco no puede ser menor a 0.
     * 
     * @return Vida actualizada del barco, luego de ser impactada
     */
    public int hit() {
        life = Math.max(0, life - 1);
        return life;
    }

    /**
     * Indica si un barco ha sido hundido
     * 
     * @return <code>true</code> si el barco ha sido hundido, <code>false</code> en
     *         caso contrario
     */
    public boolean isSunk() {
        return life == 0;
    }
}
