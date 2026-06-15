package com.gnormu.battleship.benchmarks;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.openjdk.jmh.annotations.*;

import com.gnormu.battleship.domain.Board;
import com.gnormu.battleship.domain.Board1d;
import com.gnormu.battleship.domain.Board2d;
import com.gnormu.battleship.domain.RandomFleetPlacer;
import com.gnormu.battleship.engine.MetricAnalyzer;
import com.gnormu.battleship.engine.SimulationConfig;
import com.gnormu.battleship.strategy.BattleshipStrategy;
import com.gnormu.battleship.strategy.BruteForceStrategy;
import com.gnormu.battleship.strategy.HuntTargetStrategy;
import com.gnormu.battleship.strategy.TrueRandomMemoryStrategy;
import com.gnormu.battleship.strategy.TrueRandomStrategy;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SolverBenchmark {

    @Param({ "TrueRandom", "TrueRandomMemory", "BruteForce", "HuntTarget" })
    private String strategyType;

    @Param({ "Board1D", "Board2D" })
    private String boardType;

    private MetricAnalyzer analyzer;
    private SimulationConfig config;
    private final int totalGames = 500_000;

    @Setup(Level.Trial)
    public void setup() {
        analyzer = new MetricAnalyzer();

        // Usamos un Switch moderno (Java 14+) para convertir el String en un Supplier
        Supplier<Board> boardFactory = switch (boardType) {
            case "Board2D" -> Board2d::new;
            case "Board1D" -> Board1d::new;
            default -> throw new IllegalArgumentException("Tablero no soportado: " + boardType);
        };

        Supplier<BattleshipStrategy> strategyFactory = switch (strategyType) {
            case "TrueRandom" -> TrueRandomStrategy::new;
            case "TrueRandomMemory" -> TrueRandomMemoryStrategy::new;
            case "BruteForce" -> BruteForceStrategy::new;
            case "HuntTarget" -> HuntTargetStrategy::new;
            default -> throw new IllegalArgumentException("Estrategia no soportada: " + strategyType);
        };

        config = new SimulationConfig(strategyFactory, boardFactory, RandomFleetPlacer::new);
    }

    @Benchmark
    public void runSimulation() {
        analyzer.runSimulations(config, totalGames);
    }
}
