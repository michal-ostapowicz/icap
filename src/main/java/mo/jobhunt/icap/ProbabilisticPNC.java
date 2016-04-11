package mo.jobhunt.icap;

import java.math.BigInteger;
import java.util.List;

/**
 * Implementation giving approximate results (useful for very big numbers)
 */
public class ProbabilisticPNC implements PrimeNumberCalculator {
    @Override
    public boolean isPrime(final BigInteger n, final int certainty) {
        return false;
    }

    @Override
    public List<BigInteger> range(final BigInteger a, final BigInteger b, final int certainty) {
        return null;
    }
}
