package com.gnormu.battleship.engine;

import java.util.function.Supplier;

import com.gnormu.battleship.domain.Board;
import com.gnormu.battleship.domain.FleetPlacer;
import com.gnormu.battleship.strategy.BattleshipStrategy;

/**
 * Suministrador de suppliers para el motor de juego
 * 
 * @param strategyFactory Factory de estrategia a utilizar (ver
 *                        {@link BattleshipStrategy})
 * @param boardFactory    Factory de board a utilizar (ver {@link Board})
 * @param placerFactory   Factory de posicionador a utilizar
 *                        (ver {@link FleetPlacer})
 */
public record SimulationConfig(
        Supplier<BattleshipStrategy> strategyFactory,
        Supplier<Board> boardFactory,
        Supplier<FleetPlacer> placerFactory

) {
}
