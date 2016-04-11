package mo.jobhunt.icap;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LowMemPNCTest {
    // On my PC it takes 14s to verify that 2^31-1 is prime.  I think it's reasonable to assume that
    // no one would want to use this naive implementation for anything bigger than that.
    public static final int MAX_VALUE = Integer.MAX_VALUE;

    final LowMemPNC service = new LowMemPNC();

    @DataProvider
    public Object[][] isPrimeDP() {
        return new Object[][]{
                {1, false},
                {2, true},
                {3, true},
                {4, false},
                {5, true},
                {6, false},
                {7, true},
                {8, false},
                {9, false},
                {52691, true}, // http://www.primos.mat.br/primeiros_10000_primos.txt
                {31477 * 31477, false}, // http://www.primos.mat.br/primeiros_10000_primos.txt
        };
    }

    @Test(dataProvider = "isPrimeDP")
    public void testIsPrime(final int n, final boolean expected) throws Exception {
        assertThat(service.isPrime(BigInteger.valueOf((long) n), 0), is(expected));
    }

    @DataProvider
    public Object[][] rangeDP() {
        return new Object[][]{
                {2, 5, asList(2, 3, 5)},
                {14, 16, emptyList()},
                {17, 17, singletonList(17)},
        };
    }

    @Test(dataProvider = "rangeDP")
    public void testRange(final int a, final int b, final List<Integer> expectedIn) throws Exception {
        final BigInteger biA = BigInteger.valueOf((long) a);
        final BigInteger biB = BigInteger.valueOf((long) b);
        final List<BigInteger> expected = expectedIn.stream().map(n -> BigInteger.valueOf((long) n)).collect(toList());
        assertThat(service.range(biA, biB, 0), equalTo(expected));
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
                {BigInteger.TEN, BigInteger.valueOf(1L + MAX_VALUE)},
        };
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "invalidRangeDP")
    public void testInvalidRange(final BigInteger a, final BigInteger b) throws Exception {
        service.range(a, b, 0);
    }

    @DataProvider
    public Object[][] invalidIsPrimeDP() {
        return new Object[][]{
                {null},
                {BigInteger.TEN.negate()},
                {BigInteger.ZERO},
                {BigInteger.valueOf(1L + MAX_VALUE)},
        };
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "invalidIsPrimeDP")
    public void testInvalidIsPrime(final BigInteger n) throws Exception {
        service.isPrime(n, 0);
    }
}