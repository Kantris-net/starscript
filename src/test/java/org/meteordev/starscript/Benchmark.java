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

/**
 Benchmark Comparison Analysis (Run 1 is Current with Cnt=10, Run 2 is Earlier with Cnt=3)

 Run 1 (Current, Cnt = 10):
 Benchmark              Mode  Cnt   Score   Error   Units
 Benchmark.format      thrpt   10   7.092 ± 1.102  ops/us
 Benchmark.formatter   thrpt   10   7.598 ± 0.237  ops/us
 Benchmark.starscript  thrpt   10  11.587 ± 0.470  ops/us
 Benchmark.format       avgt   10   0.137 ± 0.006   us/op
 Benchmark.formatter    avgt   10   0.134 ± 0.008   us/op
 Benchmark.starscript   avgt   10   0.088 ± 0.001   us/op

 Run 2 (Earlier/Original starscript, Cnt = 3):
 Benchmark              Mode  Cnt   Score    Error   Units
 Benchmark.format      thrpt    3   1.417 ±  0.766  ops/us
 Benchmark.formatter   thrpt    3   2.161 ±  0.448  ops/us
 Benchmark.starscript  thrpt    3   7.423 ±  1.771  ops/us
 Benchmark.format       avgt    3   0.707 ±  0.262  us/op
 Benchmark.formatter    avgt    3   0.469 ±  0.273  us/op
 Benchmark.starscript   avgt    3   0.141 ±  0.051  us/op
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

        // Standard Formatter Setup
        formatter = new Formatter(sb);

        // Starscript Setup
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
        sb.setLength(0);
        bh.consume(ss.run(script, sb).toString());
    }
}
