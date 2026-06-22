package com.gnormu.battleship.engine;

import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.domain.Board;
import com.gnormu.battleship.domain.BoardView;
import com.gnormu.battleship.strategy.BattleshipStrategy;

/**
 * Motor de juego encargado de resolver los tableros
 */
public class GameEngine {

    /** Tablero de juego */
    private final Board board;

    private final BoardView boardView;

    /**
     * Constructor del motor de juego
     * 
     * @param board Tablero a resolver
     */
    public GameEngine(Board board) {
        this.board = board;
        this.boardView = new BoardView(board);
    }

    /**
     * Resuelve un tablero a partir de la estrategia definida
     */
    public int resolve(BattleshipStrategy strategy) {
        int totalTurns = 0;
        int attempts = 0;
        while (!board.isGameOver()) {
            if (attempts > GameConfig.MAX_ATTEMPTS) {
                throw new RuntimeException("Demasiados intentos: Se excedió el numero de intentos máximo permitido "
                        + GameConfig.MAX_ATTEMPTS);
            }
            byte nextMove = strategy.calculateNextShot(boardView);
            board.shoot(nextMove);
            totalTurns++;
            attempts++;
        }
        return totalTurns;
    }
}
