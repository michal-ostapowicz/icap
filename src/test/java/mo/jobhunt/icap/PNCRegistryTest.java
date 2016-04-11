package mo.jobhunt.icap;

import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class PNCRegistryTest {
    @Test
    public void testGetRegistered() throws Exception {
        PNCRegistry.register("pnc1", () -> mock(PrimeNumberCalculator.class));
        PNCRegistry.register("pnc2", () -> mock(PrimeNumberCalculator.class));

        PNCRegistry.get("pnc1").isPrime(BigInteger.ONE, 0);
        PNCRegistry.get("pnc2").isPrime(BigInteger.ONE, 0);
        PNCRegistry.get("pnc2").isPrime(BigInteger.ONE, 0);

        verify(PNCRegistry.get("pnc1"), times(1)).isPrime(BigInteger.ONE, 0);
        verify(PNCRegistry.get("pnc2"), times(2)).isPrime(BigInteger.ONE, 0);
    }

    @Test
    public void testRegisterOnlyOnce() throws Exception {
        final AtomicInteger counter = new AtomicInteger();
        PNCRegistry.register("register-only-once", () -> {
            counter.incrementAndGet();
            return mock(PrimeNumberCalculator.class);
        });
        assertThat("First instance created", counter.get(), is(1));

        PNCRegistry.register("register-only-once", () -> {
            counter.incrementAndGet();
            return mock(PrimeNumberCalculator.class);
        });

        assertThat("Subsequent call ignored", counter.get(), is(1));
    }
}