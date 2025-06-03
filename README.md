# Calculator

Other than what was written in the assignment description,
- the project uses Maven to build, and `Google Java Format` to format the Java code
- the project does not use any application framework (Spring Boot, Quarkus, Dropwizard, etc.)
- I only used some testing libraries (JUnit, Mockito, AssertJ) to implement the assignment
- the project's entry point is the `CalculationController.calculate()` method
- the project uses a functional test (`com.tacton.candidate.functional.CalculatorFunctionalTest`)
  in which anyone can test the functionality by overriding the CsvSource of the first test case.
