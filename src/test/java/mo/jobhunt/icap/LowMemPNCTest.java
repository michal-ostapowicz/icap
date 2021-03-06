package mo.jobhunt.icap;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LowMemPNCTest {
    public static final BigInteger MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE);

    final LowMemPNC service = new LowMemPNC();

    @DataProvider
    public Object[][] isPrimeDP() {
        return CommonDataProviders.isPrimeDP();
    }

    @Test
    public void testValidLargeNumber() throws Exception {
        final BigInteger two = BigInteger.valueOf(2);
        final BigInteger largestAllowedNumber = MAX_VALUE.subtract(BigInteger.ONE).divide(two).multiply(two);
        assertThat(service.isPrime(largestAllowedNumber, 0), is(false)); // divisible by 2
    }

    @Test(dataProvider = "isPrimeDP")
    public void testIsPrime(final int n, final boolean expected) throws Exception {
        assertThat(service.isPrime(BigInteger.valueOf((long) n), 0), is(expected));
    }

    @DataProvider
    public Object[][] rangeDP() {
        return CommonDataProviders.rangeDP();
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
                {BigInteger.TEN, BigInteger.TEN.add(CommonConstants.MAX_RANGE)},
                {BigInteger.TEN, MAX_VALUE.add(BigInteger.ONE)},
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
                {MAX_VALUE.add(BigInteger.ONE)},
        };
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "invalidIsPrimeDP")
    public void testInvalidIsPrime(final BigInteger n) throws Exception {
        service.isPrime(n, 0);
    }
}