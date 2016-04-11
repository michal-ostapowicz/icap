package mo.jobhunt.icap;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.function.BooleanSupplier;

import static java.util.stream.Collectors.toList;
import static mo.jobhunt.icap.CommonConstants.MAX_RANGE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

public class ProbabilisticPNCTest {
    public static final int CERTAINTY = 6;
    public static final double ACCEPTED_MISS_PROBABILITY = 1.0 / Math.pow(2.0, CERTAINTY);
    public static final int N_TIRES = 1000;

    private ProbabilisticPNC service = new ProbabilisticPNC();

    @DataProvider
    public Object[][] isPrimeDP() {
        return CommonDataProviders.isPrimeDP();
    }

    @Test(dataProvider = "isPrimeDP")
    public void testIsPrime(final int n, final boolean expected) throws Exception {
        final BigInteger toCheck = BigInteger.valueOf((long) n);
        checkWithAcceptedProbability(() -> service.isPrime(toCheck, CERTAINTY) != expected);
    }

    private void checkWithAcceptedProbability(final BooleanSupplier fail) {
        int misses = 0;
        for (int i = 0; i < N_TIRES; i++) {
            if (fail.getAsBoolean()) {
                misses++;
            }
        }

        assertThat(1.0 * misses / N_TIRES, is(lessThan(ACCEPTED_MISS_PROBABILITY)));
    }

    @DataProvider
    public Object[][] rangeDP() {
        return CommonDataProviders.rangeDP();
    }

    @Test(dataProvider = "rangeDP")
    public void testRange(final int a, final int b, final List<Integer> expectedIn) throws Exception {
        final List<BigInteger> expected = expectedIn.stream().map(n -> BigInteger.valueOf((long) n)).collect(toList());

        final BigInteger biA = BigInteger.valueOf((long) a);
        final BigInteger biB = BigInteger.valueOf((long) b);

        checkWithAcceptedProbability(() -> !service.range(biA, biB, 0).equals(expected));
    }

    @DataProvider
    public Object[][] invalidRangeDP() {
        return new Object[][]{
                {null, null},
                {null, BigInteger.TEN},
                {BigInteger.TEN, null},
                {BigInteger.TEN.negate(), BigInteger.TEN},
                {BigInteger.ZERO, BigInteger.TEN},
                {BigInteger.TEN, BigInteger.ONE},
                {BigInteger.TEN, BigInteger.TEN.add(MAX_RANGE)},
        };
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "invalidRangeDP")
    public void testInvalidRange(final BigInteger a, final BigInteger b) throws Exception {
        service.range(a, b, 0);
    }
}