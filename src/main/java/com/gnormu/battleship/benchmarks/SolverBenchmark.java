package com.gnormu.battleship.benchmarks;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;

import com.gnormu.battleship.domain.Board2d;
import com.gnormu.battleship.domain.RandomFleetPlacer;
import com.gnormu.battleship.engine.MetricAnalyzer;
import com.gnormu.battleship.engine.SimulationConfig;
import com.gnormu.battleship.strategy.BruteForceStrategy;
import com.gnormu.battleship.strategy.TrueRandomStrategy;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SolverBenchmark {

    private MetricAnalyzer metricAnalyzer;

    @Param({ "500000" })
    public int totalGames;

    @Setup(Level.Trial)
    public void setup() {
        metricAnalyzer = new MetricAnalyzer();
    }

    @Benchmark
    public double trueRandomStrategy2D() {
        SimulationConfig config = new SimulationConfig(
                TrueRandomStrategy::new,
                Board2d::new,
                RandomFleetPlacer::new);
        metricAnalyzer.runSimulations(config, totalGames);
        return metricAnalyzer.getAverageTurns();
    }

    @Benchmark
    public double bruteForceStrategy2D() {
        SimulationConfig config = new SimulationConfig(
                BruteForceStrategy::new,
                Board2d::new,
                RandomFleetPlacer::new);
        metricAnalyzer.runSimulations(config, totalGames);
        return metricAnalyzer.getAverageTurns();
    }
}
