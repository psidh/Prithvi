package src.metrics;


import java.util.concurrent.atomic.AtomicLong;

public class MetricsCollector {
    private static final AtomicLong totalRequests = new AtomicLong();
    private static final AtomicLong totalReads = new AtomicLong();
    private static final AtomicLong totalWrites = new AtomicLong();
    private static final AtomicLong totalErrors = new AtomicLong();
    private static final AtomicLong totalLatencyMicros = new AtomicLong();

    public static void recordRequest() { totalRequests.incrementAndGet(); }
    public static void recordRead() { totalReads.incrementAndGet(); }
    public static void recordWrite() { totalWrites.incrementAndGet(); }
    public static void recordError() { totalErrors.incrementAndGet(); }

    public static void recordLatency(long micros) {
        totalLatencyMicros.addAndGet(micros);
    }

    public static String getMetrics() {
        long avgLatency = totalRequests.get() == 0 ? 0 : totalLatencyMicros.get() / totalRequests.get();

        return new StringBuilder()
            .append("# HELP prithvi_total_requests Total number of requests\n")
            .append("# TYPE prithvi_total_requests counter\n")
            .append("prithvi_total_requests ").append(totalRequests.get()).append("\n")

            .append("# HELP prithvi_total_reads Total read commands\n")
            .append("# TYPE prithvi_total_reads counter\n")
            .append("prithvi_total_reads ").append(totalReads.get()).append("\n")

            .append("# HELP prithvi_total_writes Total write commands\n")
            .append("# TYPE prithvi_total_writes counter\n")
            .append("prithvi_total_writes ").append(totalWrites.get()).append("\n")

            .append("# HELP prithvi_total_errors Total failed commands\n")
            .append("# TYPE prithvi_total_errors counter\n")
            .append("prithvi_total_errors ").append(totalErrors.get()).append("\n")

            .append("# HELP prithvi_avg_latency Average latency in microseconds\n")
            .append("# TYPE prithvi_avg_latency gauge\n")
            .append("prithvi_avg_latency ").append(avgLatency).append("\n")
            .toString();
    }
}
