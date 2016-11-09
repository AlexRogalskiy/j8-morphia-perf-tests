package com.mechanitis;

import org.mongodb.morphia.converters.BooleanConverter;
import org.mongodb.morphia.converters.ConvertersUnderTest;
import org.mongodb.morphia.converters.IdentityConverter;
import org.mongodb.morphia.converters.IterableConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
public class ConvertersBenchmark {
    @Param({"1", "10", "100", "1000", "10000", "100000"})
    public int numberOfItems;

    private List<TypeConverter> source;
    private ConvertersUnderTest converters;

    @Setup()
    public void setup(BenchmarkParams params) {
        source = new LinkedList<>();

        for (int i = 0; i < numberOfItems - 1; i++) {
            source.add(new BooleanConverter());
        }
        source.add(new IterableConverter());
        converters = new ConvertersUnderTest(source);
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public TypeConverter original() {
        return converters.original(List.class);
        //126590
        //53732
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public TypeConverter refactored() {
        return converters.refactored(List.class);
        //12588
        //14823
    }

    @Benchmark
    @OutputTimeUnit(MILLISECONDS)
    public TypeConverter parallel() {
        return converters.parallel(List.class);
        //
        //35
    }
}
