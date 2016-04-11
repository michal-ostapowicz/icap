package mo.jobhunt.icap;

import spark.Request;
import spark.Response;

import java.math.BigInteger;

import static spark.Spark.*;

/**
 * Main class (entry point) --- runs the REST service for prime number generation.
 */
public class RESTService {
    private static int certainty(final Request req) {
        final String certainty = req.queryParams("certainty");
        if (certainty == null || certainty.trim().isEmpty()) {
            return CommonConstants.DEFAULT_CERTAINTY;
        }
        return Integer.valueOf(certainty);
    }

    private static Object isPrime(final Request req, final Response res) {
        return PNCRegistry.get(req.params(":algorithm"))
                .isPrime(new BigInteger(req.params(":number")), certainty(req));
    }

    private static Object range(final Request req, final Response res) {
        return PNCRegistry.get(req.params(":algorithm"))
                .range(new BigInteger(req.params(":lower")), new BigInteger(req.params(":upper")), certainty(req));
    }

    public static void main(String[] argv) {
        configureSpark(argv);
        PNCRegistry.register("simple", LowMemPNC::new);
        PNCRegistry.register("probabilistic", ProbabilisticPNC::new);

        exception(NumberFormatException.class, (e, req, res) -> sendError(res, 400, "Couldn't parse number. " + e.getMessage()));
        exception(IllegalArgumentException.class, (e, req, res) -> sendError(res, 400, e.getMessage()));
        get("/:algorithm/is-prime/:number", RESTService::isPrime);
        get("/:algorithm/range/:lower/:upper", RESTService::range);
        get("/*", (q, res) -> sendError(res, 404, "Not Found"));
    }

    private static void configureSpark(final String[] argv) {
        threadPool(8);
        if (argv.length == 0) {
            return;
        }
        try {
            port(Integer.parseInt(argv[0]));
        } catch (NumberFormatException e) {
            // ignore
        }
    }

    private static String sendError(final Response res, final int status, final String body) {
        res.status(status);
        res.body(body);
        return null;
    }
}
