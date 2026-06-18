package com.gnormu.battleship.strategy;

/**
 * Clase abstracta que define el comportamiento base de una estrategia de
 * resolución
 */
public abstract class AbstractBattleshipStrategy implements BattleshipStrategy {

    /**
     * {@inheritDoc}
     * 
     * @implNote Utiliza el nombre de la clase como nombre de la estrategia
     *           eliminando la palabra "Strategy" del nombre
     */
    @Override
    public final String getStrategyName() {
        return this.getClass().getSimpleName()
                .replace("Strategy", "");
    }
}
