package org.meteordev.starscript;

import org.meteordev.starscript.compiler.Compiler;
import org.meteordev.starscript.compiler.Parser;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.Formatter;
import java.util.concurrent.TimeUnit;

/*
Here are the results of the benchmark below ran on my machine with JDK 26.0.1

Benchmark              Mode  Cnt   Score    Error   Units
Benchmark.format      thrpt   10   7.290 ±  0.218  ops/us
Benchmark.formatter   thrpt   10   7.812 ±  0.257  ops/us
Benchmark.starscript  thrpt   10  11.939 ±  0.082  ops/us
Benchmark.format       avgt   10   0.137 ±  0.003   us/op
Benchmark.formatter    avgt   10   0.130 ±  0.007   us/op
Benchmark.starscript   avgt   10   0.084 ±  0.001   us/op
 */

@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class Benchmark {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmark.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .warmupTime(TimeValue.seconds(1))
                .measurementTime(TimeValue.seconds(1))
                .forks(2)
                .build();

        new Runner(opt).run();
    }

    public final String formatSource = "FPS: %.0f";
    public final String starscriptSource = "FPS: {round(fps)}";

    public StringBuilder sb;

    private Formatter formatter;

    public Script script;
    public Starscript ss;

    @Setup
    public void setup() {
        sb = new StringBuilder();

        // Format
        formatter = new Formatter(sb);

        // Starscript
        script = Compiler.compile(Parser.parse(starscriptSource));

        ss = new Starscript();
        StandardLib.init(ss);
        ss.set("name", "MineGame159");
        ss.set("fps", 59.68223);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void format(Blackhole bh) {
        bh.consume(String.format(formatSource, 59.68223));
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void formatter(Blackhole bh) {
        sb.setLength(0);
        bh.consume(formatter.format(formatSource, 59.68223).toString());
    }

    @org.openjdk.jmh.annotations.Benchmark
    public void starscript(Blackhole bh) {
        bh.consume(ss.run(script, sb).toString());
    }
}
