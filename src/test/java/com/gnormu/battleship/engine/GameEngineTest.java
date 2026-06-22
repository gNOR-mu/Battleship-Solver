package com.gnormu.battleship.engine;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.gnormu.battleship.domain.Board;
import com.gnormu.battleship.domain.Board2D;
import com.gnormu.battleship.domain.CellContent;
import com.gnormu.battleship.domain.FleetPlacer;
import com.gnormu.battleship.domain.RandomFleetPlacer;
import com.gnormu.battleship.strategy.BruteForceStrategy;

public class GameEngineTest {

    private static int perfectGame;

    @BeforeAll
    static void init() {
        perfectGame = CellContent.TOTAL_LIFES;
    }

    private List<Supplier<Board>> boardFactories;

    private FleetPlacer placer;

    @BeforeEach
    void setup() {
        placer = new RandomFleetPlacer();
        boardFactories = List.of(Board2D::new);
    }

    @TestFactory
    @DisplayName("Juegos con Bruteforce solucionan correctamente")
    Collection<DynamicTest> gameWithBruteforceSolvesCorrectly() {
        return boardFactories.stream()
                .flatMap(factory -> {
                    String boardName = factory.get().getClass().getSimpleName();
                    return IntStream.rangeClosed(1, 1000)
                            .mapToObj(i -> DynamicTest.dynamicTest("Tablero: " + boardName + " - Corrida " + i, () -> {
                                Board board = factory.get();
                                placer.placeShips(board);

                                GameEngine engine = new GameEngine(board);
                                BruteForceStrategy strategy = new BruteForceStrategy();
                                int turns = engine.resolve(strategy);

                                // 6. Validaciones
                                assertTrue(board.isGameOver(), "El juego debió terminar");
                                assertTrue(turns <= 100, "BruteForce no puede tomar más de 100 turnos");
                                assertTrue(turns >= perfectGame,
                                        "El juego debe tomar por lo menos " + perfectGame + " turnos");
                            }));
                })
                .toList();
    }
}
