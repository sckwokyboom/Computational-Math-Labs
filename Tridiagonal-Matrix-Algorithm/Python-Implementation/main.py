import sys


def forward_pass(main_diagonal, down_diagonal, upper_diagonal, last_column):
    alphas, betas = [], []
    count_of_equations = len(last_column)
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
    return alphas, betas


def backward_pass(alphas, betas, count_of_equations):
    solutions = [betas[count_of_equations - 1]]
    for i in range(count_of_equations - 2, -1, -1):
        solutions.insert(0, alphas[i] * solutions[0] + betas[i])
        print(alphas[i] * solutions[0] + betas[i])
    return solutions


def tridiagonal_solve(main_diagonal, down_diagonal, upper_diagonal, last_column):
    count_of_equations = len(last_column)
    (alphas, betas) = forward_pass(main_diagonal, down_diagonal, upper_diagonal, last_column)
    return backward_pass(alphas, betas, count_of_equations)


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

    solutions = tridiagonal_solve([2, 2, 2], [-1, -1], [-1, -1], [2, 2, 2])
    result = {f'X_{i}': solutions[i] for i in range(len(solutions))}
    print(result)


if __name__ == "__main__":
    main()
