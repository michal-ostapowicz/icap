package mo.jobhunt.icap;

import java.math.BigInteger;
import java.util.List;

/**
 * Naive implementation of random number generator with low memory footprint, giving guaranteed results.
 */
public class LowMemPNC implements PrimeNumberCalculator {
    @Override
    public boolean isPrime(final BigInteger n, final int ignored) {
        return false;
    }

    @Override
    public List<BigInteger> range(final BigInteger a, final BigInteger b, final int ignored) {
        return null;
    }
}
