# Bisection Method

This program finds the roots and their multiplicities of a cubic equation of the form:
`x³ + bx² + cx + d = 0` using the bisection method (sequential division of segments in half).

## How to use?

To start searching for roots, you can run the `.jar` and follow the instructions on the screen, sequentially entering double coefficients:

- `b` — the coefficient of the quadratic term of the equation.
- `c` — the coefficient of the linear term.
- `d` — the free coefficient of the equation.
- `epsilon` — the precision with which you want to specify the roots.

You can also enter these arguments directly on the command line, in the same order, separated by a space.

## Result

The result of the program is the output of roots sorted by value, indicating their multiplicity in square brackets.

## Example

**Input:** `-5.0 8.0 -4.0 0.001`

**Output:**

```
0.999 [1]
2.000 [2]
```
