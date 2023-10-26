import sys


def forward_pass(main_diagonal, down_diagonal, upper_diagonal, last_column):
    alphas, betas = [], []
    count_of_equations = len(last_column)

    assert count_of_equations == len(main_diagonal), "Length of main_diagonal must be equal to count_of_equations)"
    assert count_of_equations - 1 == len(down_diagonal), \
        ("Length of down_diagonal must be equal to count_of_equations - 1."
         f"Expected: {count_of_equations - 1}"
         f"Actual: {len(down_diagonal)}")
    assert count_of_equations - 1 == len(
        upper_diagonal), \
        ("Length of upper_diagonal must be equal to count_of_equations - 1"
         f"Expected: {count_of_equations - 1}"
         f"Actual: {len(upper_diagonal)}")

    for i in range(count_of_equations):
        if i == 0:
            alphas.append(-upper_diagonal[i] / main_diagonal[i])
            betas.append(last_column[i] / main_diagonal[i])
        elif i == count_of_equations - 1:
            alphas.append(0)
            betas.append((last_column[i] - down_diagonal[i - 1] * betas[i - 1]) / (
                    main_diagonal[i] + down_diagonal[i - 1] * alphas[i - 1]))
        else:
            alphas.append(-upper_diagonal[i] / (main_diagonal[i] + down_diagonal[i - 1] * alphas[i - 1]))
            betas.append((last_column[i] - down_diagonal[i - 1] * betas[i - 1]) / (
                    main_diagonal[i] + down_diagonal[i - 1] * alphas[i - 1]))

    assert len(alphas) == len(betas) and len(
        alphas) == count_of_equations, "Lengths of alphas and betas must be equal to the count_of_equations."

    return alphas, betas


def backward_pass(alphas, betas, count_of_equations):
    assert (len(alphas) == len(betas)
            and len(
                alphas) == count_of_equations), "Lengths of alphas and betas must be equal to the count_of_equations."

    solutions = [betas[count_of_equations - 1]]
    for i in range(count_of_equations - 2, -1, -1):
        solutions.insert(0, alphas[i] * solutions[0] + betas[i])
    return solutions


def tridiagonal_solve(main_diagonal, down_diagonal, upper_diagonal, last_column):
    count_of_equations = len(last_column)
    (alphas, betas) = forward_pass(main_diagonal, down_diagonal, upper_diagonal, last_column)
    return backward_pass(alphas, betas, count_of_equations)


def first_type_matrix_solve(matrix_size):
    main_diagonal = [2] * matrix_size
    down_diagonal = [-1] * (matrix_size - 1)
    upper_diagonal = [-1] * (matrix_size - 1)
    last_column = [2] * matrix_size
    return tridiagonal_solve(main_diagonal, down_diagonal, upper_diagonal, last_column)


def second_type_matrix_solve(matrix_size, epsilon):
    main_diagonal = [2] * matrix_size
    down_diagonal = [-1] * (matrix_size - 1)
    upper_diagonal = [-1] * (matrix_size - 1)
    last_column = [2 + epsilon] * matrix_size
    return tridiagonal_solve(main_diagonal, down_diagonal, upper_diagonal, last_column)


def third_type_matrix_solve(matrix_size, gamma):
    main_diagonal = []
    down_diagonal = [-1] * (matrix_size - 1)
    upper_diagonal = [-1] * (matrix_size - 1)
    last_column = []
    for i in range(1, matrix_size + 1):
        main_diagonal.append(2 * i + gamma)
    for i in range(1, matrix_size + 1):
        last_column.append(2 * (i + 1) + gamma)
    return tridiagonal_solve(main_diagonal, down_diagonal, upper_diagonal, last_column)


def main():
    input_precision = input("Enter precision (number of decimal places in the answer): ")
    try:
        precision = int(input_precision)
        if precision >= 0:
            print(f"Your precision: {precision}")
        else:
            print("Precision must be a non-negative integer.")
            sys.exit(1)
    except ValueError:
        print("Precision must be a non-negative integer.")
        sys.exit(1)

    input_matrix_size = input("Enter matrix size (number of rows and columns of a square matrix): ")
    try:
        matrix_size = int(input_matrix_size)
        if matrix_size > 0:
            print(f"Your matrix size: {matrix_size}")
        else:
            print("Matrix size must be a positive integer.")
            sys.exit(1)
    except ValueError:
        print("Matrix size must be a positive integer.")
        sys.exit(1)

    input_epsilon_for_tests = input("Enter epsilon (ε) value for formula f_i = 2 + ε: ")
    try:
        epsilon_for_tests = float(input_epsilon_for_tests)
        print(f"Your ε value: {epsilon_for_tests}")
    except ValueError:
        print("ε value must be a float.")
        sys.exit(1)

    input_gamma_for_tests = input("Enter gamma (γ) value for formulas "
                                  "(main_diagonal_i = 2i + γ) and (f_i = 2(i+1) + γ): ")
    try:
        gamma_for_tests = float(input_gamma_for_tests)
        print(f"Your γ value: {gamma_for_tests}")
    except ValueError:
        print("γ value must be a float.")
        sys.exit(1)

    solutions_first_matrix = first_type_matrix_solve(matrix_size)
    solutions_second_matrix = second_type_matrix_solve(matrix_size, epsilon_for_tests)
    solutions_third_matrix = third_type_matrix_solve(matrix_size, gamma_for_tests)

    print("\nFirst matrix solution:")
    for i, solutions_first_matrix in enumerate(solutions_first_matrix):
        print(f"\tX_{i} = {solutions_first_matrix:.{precision}f}")
    print("\nSecond (using ε) matrix solution:")
    for i, solutions_second_matrix in enumerate(solutions_second_matrix):
        print(f"\tX_{i} = {solutions_second_matrix:.{precision}f}")
    print("\nThird (using γ) matrix solution:")
    for i, solutions_third_matrix in enumerate(solutions_third_matrix):
        print(f"\tX_{i} = {solutions_third_matrix:.{precision}f}")


if __name__ == "__main__":
    main()
