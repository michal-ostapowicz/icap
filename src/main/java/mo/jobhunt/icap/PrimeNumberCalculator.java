package mo.jobhunt.icap;

import java.math.BigInteger;
import java.util.List;

public interface PrimeNumberCalculator {
    /**
     * Checks if the number {@code n} is a prime.
     *
     * @param n         the number to be checked
     * @param certainty a measure of the uncertainty that the caller is
     *                  willing to tolerate: if the call returns {@code true}
     *                  the probability that this BigInteger is prime exceeds
     *                  (1 - 1/2<sup>{@code certainty}</sup>).
     *                  If {@code certainty} is 0, the result is guaranteed.
     * @return {@code false} if {@code n} is a composite, {@code true} if {@code n} is a prime with the given certainty
     */
    boolean isPrime(final BigInteger n, final int certainty);

    /**
     * Generates a list of prime numbers between {@code a} and {@code b}
     *
     * @param a         lower bound (inclusive)
     * @param b         upper bound (inclusive)
     * @param certainty see {@link PrimeNumberCalculator#isPrime(BigInteger, int)}
     * @return the list of all prime numbers in the specified range
     */
    List<BigInteger> range(final BigInteger a, final BigInteger b, final int certainty);
}
