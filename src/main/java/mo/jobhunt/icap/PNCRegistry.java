package mo.jobhunt.icap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Prime number calculators registry.
 */
public class PNCRegistry {
    private final static Map<String, PrimeNumberCalculator> calculators = new ConcurrentHashMap<String, PrimeNumberCalculator>();

    private PNCRegistry() {
    }

    /**
     * @param name the name of requested calculator
     * @return returns a prime number calculator named {@code name}
     */
    public static PrimeNumberCalculator get(final String name) {
        return calculators.get(name);
    }

    /**
     * Registers a prime number calculator produced by {@code pncSupplier} under {@code name}.  Once a supplier
     * is registered, subsequent attempts to register one with the same name are ignored.  The calculator
     * is created only if it is required.
     *
     * @param name        the name of calculator
     * @param pncSupplier function creating the calculator
     */
    public static void register(final String name, final Supplier<PrimeNumberCalculator> pncSupplier) {
        calculators.computeIfAbsent(name, ignore -> pncSupplier.get());
    }
}
