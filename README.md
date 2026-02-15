# Algebraic Equation Evaluator

A Spring Boot REST API that stores, manages, and evaluates algebraic equations. The application parses mathematical expressions, builds expression trees, and evaluates them with variable substitution.

## What does it do?

This service lets you:
- Store algebraic equations like `3x + 2y - z` or `(x + y) * z`
- Retrieve all stored equations
- Evaluate equations by providing variable values

The cool part? It handles implicit multiplication automatically. So you can write `2x + 3y` instead of `2*x + 3*y` — just like you'd write it on paper.

## Tech Stack

- Java 21
- Spring Boot 3.x
- Gradle
- JUnit 5

## Getting Started

### Prerequisites

Make sure you have these installed:
- JDK 21 or higher
- Gradle 8.x (or use the included Gradle wrapper)

### Clone and Run

```bash
# Clone the repo
git clone <repository-url>
cd algebraic.equation

# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

The API will start on `http://localhost:8080`

## API Endpoints

### Store an Equation

```http
POST /equations/store
Content-Type: application/json

{
    "equation": "3x + 2y - z"
}
```

Response:
```json
{
    "equationId": 1,
    "equation": "3x+2y-z"
}
```

### Get All Equations

```http
GET /equations
```

Response:
```json
[
    {
        "equationId": 1,
        "equation": "3x+2y-z"
    },
    {
        "equationId": 2,
        "equation": "(x+y)z"
    }
]
```

### Evaluate an Equation

```http
POST /equations/1/evaluate
Content-Type: application/json

{
    "variables": {
        "x": 2,
        "y": 3,
        "z": 1
    }
}
```

Response:
```json
{
    "equationId": 1,
    "equation": "3x+2y-z",
    "variable": {
        "x": 2.0,
        "y": 3.0,
        "z": 1.0
    },
    "result": 11.0
}
```

## Supported Operations

| Operator | Description | Example |
|----------|-------------|---------|
| `+` | Addition | `x + y` |
| `-` | Subtraction | `x - y` |
| `*` | Multiplication | `x * y` or `xy` |
| `/` | Division | `x / y` |
| `^` | Power | `x^2` |
| `()` | Parentheses | `(x + y) * z` |

## Implicit Multiplication

The parser is smart enough to understand implicit multiplication:

| You write | It understands |
|-----------|----------------|
| `2x` | `2 * x` |
| `xy` | `x * y` |
| `3(x+y)` | `3 * (x + y)` |
| `(x+y)(a+b)` | `(x + y) * (a + b)` |

## Error Handling

The API returns meaningful error messages:

| Error | HTTP Status | When |
|-------|-------------|------|
| `EQUATION_NOT_FOUND` | 404 | Equation ID doesn't exist |
| `INVALID_EQUATION` | 400 | Equation is null, empty, or malformed |
| `VARIABLE_NOT_FOUND` | 400 | Missing variable in evaluation request |
| `DIVISION_BY_ZERO` | 400 | Attempted division by zero |

Example error response:
```json
{
    "error": "VARIABLE_NOT_FOUND",
    "message": "Variable not found: z",
    "timestamp": 1739577600000
}
```

## Running Tests

```bash
# Run all tests
./gradlew test

# Run tests with detailed output
./gradlew test --info

# Run a specific test class
./gradlew test --tests EquationResponseServiceImplTest

# Run a specific test method
./gradlew test --tests "EquationResponseServiceImplTest.testStoreEquation_Success"
```

Test reports are generated at `build/reports/tests/test/index.html`

## Project Structure

```
src/
├── main/java/com/sreeram/algebraic/equation/
│   ├── AlgebraicEquationApplication.java    # Main entry point
│   ├── controller/
│   │   └── EquationsController.java         # REST endpoints
│   ├── exception/
│   │   ├── DivisionByZeroException.java
│   │   ├── EquationNotFoundException.java
│   │   ├── GlobalExceptionHandler.java      # Central error handling
│   │   ├── InvalidEquationException.java
│   │   └── VariableNotFoundException.java
│   ├── model/
│   │   ├── EquationRequest.java
│   │   ├── EquationResponse.java
│   │   ├── ErrorResponse.java
│   │   ├── EvaluationRequest.java
│   │   ├── EvaluationResponse.java
│   │   ├── ExpressionTreeNode.java          # Tree node for expressions
│   │   └── Stack.java
│   └── service/
│       ├── EquationService.java             # Service interface
│       └── impl/
│           └── EquationServiceImpl.java     # Business logic
└── test/java/com/sreeram/algebraic/equation/
    ├── controller/
    │   └── EquationsControllerTest.java
    ├── model/
    │   ├── EquationResponseTest.java
    │   └── EvaluationResponseTest.java
    └── service/impl/
        └── EquationResponseServiceImplTest.java
```

## How It Works (Under the Hood)

1. **Parsing**: The equation string is preprocessed to handle implicit multiplication
2. **Conversion**: Infix notation is converted to postfix using the Shunting Yard algorithm
3. **Tree Building**: A binary expression tree is built from the postfix notation
4. **Storage**: The tree is stored in memory (HashMap) with a unique ID
5. **Evaluation**: Variables are substituted and the tree is evaluated recursively
6. **Output**: Results are converted back to human-readable infix notation

## Current Limitations

- **In-memory storage**: Equations are lost when the application restarts
- **Single variables only**: Multi-character variable names aren't supported yet
- **No persistence**: No database integration (yet)

## Future Improvements

- [ ] Add database persistence (PostgreSQL/H2)
- [ ] Support multi-character variable names
- [ ] Add equation history and versioning
- [ ] Implement equation simplification
- [ ] Add support for functions (sin, cos, log, etc.)

## Contributing

Feel free to open issues or submit PRs. Just make sure to:
1. Write tests for new features
2. Follow the existing code style
3. Update documentation as needed

## License

This project is for educational purposes.

---

Built with perseverance and a lot of recursion.

