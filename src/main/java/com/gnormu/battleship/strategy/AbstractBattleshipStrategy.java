package com.gnormu.battleship.strategy;

import com.gnormu.battleship.domain.CellContent;

/**
 * Clase abstracta que define el comportamiento base de una estrategia de
 * resolución
 */
public abstract class AbstractBattleshipStrategy implements BattleshipStrategy {
    /**
     * último disparo realizado, inicialmente siempre se considera que el último
     * disparo es agua por conveniencia
     */
    protected byte lastShoot = CellContent.WATER;

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

    @Override
    public void setLastShoot(byte lastShoot) {
        this.lastShoot = lastShoot;
    }

    @Override
    public final void reset() {
        lastShoot = CellContent.WATER;
        resetStrategy();
    }

    /** Reinicia una estrategia a su estado inicial */
    protected abstract void resetStrategy();

}
