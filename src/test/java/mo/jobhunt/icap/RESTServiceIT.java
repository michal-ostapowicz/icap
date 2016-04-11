package mo.jobhunt.icap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RESTServiceIT {
    private static final String NOT_FOUND = "<html><body><h2>404 Not found</h2></body></html>";
    private String port;

    @BeforeClass
    public void setUp() throws Exception {
        final Properties props = new Properties();
        props.load(getClass().getResourceAsStream("/config.properties"));
        port = props.getProperty("sparkPort", "4567").trim();
        if (port.isEmpty()) {
            port = "4567";
        }
        RESTService.main(new String[]{port});
    }

    @DataProvider
    public Object[][] httpDP() {
        return new Object[][]{
                {"/foo", new TestResponse(404, NOT_FOUND)},
                {"/simple/is-prime/0", new TestResponse(400, "Numbers to check cannot be below 1")},
                {"/simple/is-prime/4", new TestResponse(200, "false")},
                {"/simple/is-prime/5", new TestResponse(200, "true")},
                {"/simple/range/5/20", new TestResponse(200, "[5, 7, 11, 13, 17, 19]")},
                {"/simple/is-prime/354298532473298472387", new TestResponse(400, "Numbers to check cannot be above: " + Long.MAX_VALUE)},
                {"/simple/range/2/3?certainty=xxx", new TestResponse(400, "Couldn't parse number. For input string: \"xxx\"")},
                {"/probabilistic/is-prime/0", new TestResponse(400, "Numbers to check cannot be below 1")},
                {"/probabilistic/is-prime/4", new TestResponse(200, "false")},
                {"/probabilistic/is-prime/5", new TestResponse(200, "true")},
                {"/probabilistic/is-prime/354298532473298472387", new TestResponse(200, "false")},
                {"/probabilistic/range/354298532473298472387/354298532473298472487", new TestResponse(200, "[354298532473298472403, 354298532473298472439]")},
                {"/probabilistic/range/a/b", new TestResponse(400, "Couldn't parse number. For input string: \"a\"")},
                {"/probabilistic/range/2/3?certainty=0", new TestResponse(400, "Cannot guarantee results for probabilistic prime number calculator")},
        };
    }

    @Test(dataProvider = "httpDP")
    public void testHttp(final String url, final TestResponse expected) throws Exception {
        assertThat(request(url), equalTo(expected));
    }

    @AfterClass
    public void tearDown() throws Exception {
        Spark.stop();
    }

    private TestResponse request(final String url) throws IOException {
        final HttpClient a = HttpClientBuilder.create().build();
        final HttpGet get = new HttpGet("http://localhost:" + port + url);
        get.addHeader("accept", "text/plain");
        final HttpResponse response = a.execute(get);
        final int status = response.getStatusLine().getStatusCode();
        final String body = IOUtils.toString(response.getEntity().getContent());
        return new TestResponse(status, body);
    }


    private class TestResponse {
        private final int status;
        private final String body;

        public TestResponse(final int status, final String body) {
            this.status = status;
            this.body = body;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final TestResponse that = (TestResponse) o;

            if (status != that.status) return false;
            return body.equals(that.body);

        }

        @Override
        public int hashCode() {
            int result = status;
            result = 31 * result + body.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return status + ": " + body;
        }
    }
}