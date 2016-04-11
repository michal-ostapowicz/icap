package mo.jobhunt.icap;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class CommonDataProviders {
    /**
     * @return vectors with: [number-to-check, expected-result]
     */
    static Object[][] isPrimeDP() {
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
                {(int) (Math.pow(2, 31) - 1), true},  // with sqrt it doesn't take so long anymore
                {52691, true}, // http://www.primos.mat.br/primeiros_10000_primos.txt
                {31477 * 31477, false}, // http://www.primos.mat.br/primeiros_10000_primos.txt
        };
    }

    static Object[][] rangeDP() {
        return new Object[][]{
                {2, 5, asList(2, 3, 5)},
                {1, 5, asList(2, 3, 5)},
                {14, 16, emptyList()},
                {17, 17, singletonList(17)},
                {2, 2, singletonList(2)},
                {3, 3, singletonList(3)},
                {4, 6, singletonList(5)},
                {1, 2, singletonList(2)},
        };
    }
}
