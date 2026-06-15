package com.gnormu.battleship.benchmarks;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.CommandLineOptions;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        CommandLineOptions cmdOptions = new CommandLineOptions(args);

        ChainedOptionsBuilder builder = new OptionsBuilder().parent(cmdOptions);

        // Si no se pasaron filtros específicos de benchmark por argumento,
        // ejecutamos SolverBenchmark por defecto
        if (cmdOptions.getIncludes().isEmpty()) {
            builder.include(SolverBenchmark.class.getSimpleName());
        }

        Options opt = builder
                .forks(1)
                .warmupIterations(3)
                .warmupTime(TimeValue.seconds(1))
                .measurementIterations(5)
                .measurementTime(TimeValue.seconds(1))
                .build();

        new Runner(opt).run();
    }
}
