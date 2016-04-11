package mo.jobhunt.icap;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Naive implementation of random number generator with low memory footprint, giving guaranteed results.
 */
public class LowMemPNC implements PrimeNumberCalculator {
    private static final BigInteger MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE);

    @Override
    public boolean isPrime(final BigInteger n, final int ignored) {
        final long toVerify = validatePrimeCandidate(n).longValue();

        return isPrime(toVerify);
    }

    private boolean isPrime(final long toVerify) {
        if (toVerify == 2 || toVerify == 3) {
            return true;
        }
        if (toVerify == 1) {
            return false;
        }
        if (toVerify % 2 == 0) {
            return false;
        }

        final int max = ((int) Math.sqrt(toVerify) + 1) / 2;
        return IntStream.rangeClosed(1, max)
                .parallel()  // Use threads
                .map(v -> v * 2 + 1)
                .noneMatch(v -> toVerify % v == 0);
    }

    private BigInteger validatePrimeCandidate(final BigInteger n) {
        if (n == null) {
            throw new IllegalArgumentException("The number to verify cannot be null");
        }
        if (n.compareTo(MAX_VALUE) > 0) {
            throw new IllegalArgumentException("Numbers to check cannot be above: " + MAX_VALUE);
        }
        if (n.longValue() < 1) {
            throw new IllegalArgumentException("Numbers to check cannot be below 1");
        }
        return n;
    }

    @Override
    public List<BigInteger> range(final BigInteger a, final BigInteger b, final int certainty) {
        final long lower = validatePrimeCandidate(a).intValue();
        final long upper = validatePrimeCandidate(b).intValue();
        if (lower > upper) {
            throw new IllegalArgumentException("Upper range limit has to be greater or equal to the lower");
        }
        if (CommonConstants.MAX_RANGE.compareTo(BigInteger.valueOf(upper - lower + 1)) < 0) {
            throw new IllegalArgumentException("The range is limited to the size of: " + CommonConstants.MAX_RANGE);
        }

        final Stream<Long> odds = Stream.iterate((lower / 2) * 2 + 1, v -> v + 2)
                .limit((upper - lower) / 2 + 1);
        final Stream<Long> toCheck;
        if (lower < 3) {
            toCheck = Stream.concat(Stream.of(2L), odds);
        } else {
            toCheck = odds;
        }
        return toCheck
                .parallel()  // threads less likely to be effective here, but why not?
                .filter((v) -> isPrime(v) && v <= upper)
                .map(BigInteger::valueOf)
                .collect(toList());
    }
}
