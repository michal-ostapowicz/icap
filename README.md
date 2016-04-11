# Prime Number Calculator

This project is an implementation of the Prime Number Calculator as REST service.

## Build and deploy instructions

In order to build a stand-alone application, use:
```
mvn package
```

In order to run integration tests use `verify` target (server will start on random port).

Server can be started from the main project directory by running:
```
java -jar target/primes-1.0-SNAPSHOT-jar-with-dependencies.jar [port]
```
(press ctrl+c to stop).  By default HTTP server listens on port 4567, which can be changed by specifying
argument `port`.


## Description of interface

Only `GET` method is implemented.  The following URL patterns are registered:

  * `/:algorithm/is-prime/:number` checks if the given `number` is a prime number.  Returns `true` or `false`

  * `/:algorithm/range/:lower/:upper` generates a list of prime numbers between `lower` and `upper` bounds
    (both inclusive).  The maximum range size is 1000000 (specified in CommonConstants). The array is returned
    in the following form: `[2, 3, 5, 7]`

Algorithm can be one of the following:

  * `simple` a simple implementation, which checks the numbers by attempting to divide by numbers 2, 3, 5, 7, 9....
    This algorithm gives definitive answers, but can be slow for big ranges of large numbers.

  * `probabilistic`: uses probabilistic method of verifying if a number is prime (`BigInteger::isProbablePrime`).
    The certainty can be specified by adding query parameter `certainty`: integer number > 0.

### Error handling

  * in case of requesting unmapped URL, `404 Not Found` response is generated
  * in case of invalid numbers, `400` status is returned with description of error in body.  The possible
    problems are as follow:
      - range length exceeds 1000000
      - lower and upper range bounds are reversed
      - numbers are invalid or are below 1
      - requested certainty for probabilistic calculator is 0