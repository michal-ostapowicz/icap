package mo.jobhunt.icap;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Implementation giving approximate results (useful for very big numbers)
 */
public class ProbabilisticPNC implements PrimeNumberCalculator {
    @Override
    public boolean isPrime(final BigInteger n, final int certainty) {
        if (certainty < 1) {
            throw new IllegalArgumentException("Cannot guarantee results for probabilistic prime number calculator");
        }
        return validatePrimeCandidate(n).isProbablePrime(certainty);
    }

    private BigInteger validatePrimeCandidate(final BigInteger n) {
        if (n == null) {
            throw new IllegalArgumentException("The number to verify cannot be null");
        }

        if (n.longValue() < 1) {
            throw new IllegalArgumentException("Numbers to check cannot be below 1");
        }
        return n;
    }

    @Override
    public List<BigInteger> range(final BigInteger lower, final BigInteger upper, final int certainty) {
        if (certainty < 1) {
            throw new IllegalArgumentException("Cannot guarantee results for probabilistic prime number calculator");
        }
        validatePrimeCandidate(lower);
        validatePrimeCandidate(upper);

        final BigInteger range = upper.subtract(lower).add(BigInteger.ONE);
        if (lower.compareTo(upper) > 0) {
            throw new IllegalArgumentException("Upper range limit has to be greater or equal to the lower");
        }

        if (CommonConstants.MAX_RANGE.compareTo(range) < 0) {
            throw new IllegalArgumentException("The range is limited to the size of: " + CommonConstants.MAX_RANGE);
        }

        final BigInteger two = BigInteger.valueOf(2);
        final Stream<BigInteger> toCheck;
        final Stream<BigInteger> odds = Stream
                .iterate(lower.divide(two).multiply(two).add(BigInteger.ONE), v -> v.add(two))
                .limit(range.longValue());

        if (lower.compareTo(two) <= 0) {
            toCheck = Stream.concat(Stream.of(two), odds);
        } else {
            toCheck = odds;
        }
        return toCheck
                .parallel()
                .filter(v -> v.compareTo(upper) <= 0 && v.isProbablePrime(certainty))
                .collect(toList());
    }
}
