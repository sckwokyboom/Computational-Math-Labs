import sys
from copy import copy

import matplotlib.pyplot as plt


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


def draw_godunov_scheme(initial_conditions, time_limit, spacial_step, start_point, end_point, r, time_step, frame_time):
    cur_tau_index = 0
    while cur_tau_index * time_step <= time_limit:
        x_values = []
        u_values = []
        cur_spacial_index = 0
        while cur_spacial_index * spacial_step < abs(end_point - start_point):
            if cur_spacial_index != 0:
                u_j = (initial_conditions[(cur_spacial_index, cur_tau_index)]
                       - r * (initial_conditions[(cur_spacial_index, cur_tau_index)]
                              - initial_conditions[(cur_spacial_index - 1, cur_tau_index)]))
                x = start_point + cur_spacial_index * spacial_step
                x_values.append(x)
                u_values.append(u_j)
                initial_conditions[(cur_spacial_index, cur_tau_index + 1)] = u_j
            else:
                x_values.append(start_point + cur_spacial_index * spacial_step)
                u_values.append(initial_conditions[(0, cur_tau_index)])
            cur_spacial_index += 1
        if round(cur_tau_index * time_step, 3) == round(frame_time, 3):
            plt.scatter(x_values, u_values, s=[4 for _ in range(len(x_values))],
                        label=f"u(x, t) по схеме Годунова при t = {"{:.3f}".format(cur_tau_index * time_step)}")
            plt.legend()
            break
        cur_tau_index += 1


def create_initial_conditions(start_point, end_point, spacial_step, time_limit, time_step):
    initial_conditions = {}
    cur_spatial_index = 0
    while cur_spatial_index * spacial_step < abs(end_point - start_point):
        x = start_point + cur_spatial_index * spacial_step
        initial_conditions[(cur_spatial_index, 0)] = 1.0 if x < 0.0 else 0.0
        cur_spatial_index += 1

    cur_time_index = 0
    count_of_spatial_steps = round(abs(end_point - start_point) / spacial_step)
    while cur_time_index * time_step <= time_limit:
        initial_conditions[(0, cur_time_index)] = 1.0
        initial_conditions[(count_of_spatial_steps, cur_time_index)] = 0.0
        cur_time_index += 1
    return initial_conditions


def draw_exact_solution(start_point, end_point, spatial_step, frame_time):
    cur_spatial_index = 0
    x_values = []
    u_values = []
    spatial_step = 0.01
    while cur_spatial_index * spatial_step <= abs(end_point - start_point):
        x = start_point + (cur_spatial_index * spatial_step)
        u = 1.0 if (x - frame_time) < 0 else 0.0
        x_values.append(x)
        u_values.append(u)
        cur_spatial_index += 1
    plt.plot(x_values, u_values, color="gray", label=f"Точное значение u(x, t) при t={"{:.3f}".format(frame_time)}")


def draw_implicit_scheme(start_point, end_point, spatial_step, r, time_limit, time_step, frame_time):
    alpha = r / 2
    cur_tau = 0
    N = round(abs(start_point - end_point) / spatial_step)
    initial_conditions = []
    cur_spatial = start_point + spatial_step

    x_values = []
    cur_index = 1
    x_values.append(start_point)
    while cur_index <= N - 1:
        x_values.append(cur_spatial)
        if cur_spatial == start_point + spatial_step:
            initial_conditions.append((1.0 if cur_spatial < 0 else 0.0) + alpha)
            cur_spatial += spatial_step
            cur_index += 1
            continue
        initial_conditions.append(1.0 if round(cur_spatial, 3) < 0 else 0.0)
        cur_spatial += spatial_step
        cur_index += 1
    x_values.append(end_point)

    solutions = []
    while cur_tau <= time_limit:
        main_diagonal = [1 for _ in range(1, N)]
        down_diagonal = [-alpha for _ in range(1, N - 1)]
        upper_diagonal = [alpha for _ in range(1, N - 1)]

        if cur_tau == 0:
            solutions = initial_conditions
        else:
            solutions[0] += alpha

        if round(cur_tau, 3) == round(frame_time, 3):
            tmp_solutions = copy(solutions)
            tmp_solutions.insert(0, 1)
            tmp_solutions.append(0)
            plt.scatter(x_values, tmp_solutions, label=f"u(x, t) по неявной схеме при t = {"{:.3f}".format(cur_tau)}",
                        color="red", s=[7 for _ in range(len(x_values))])
            plt.legend()
        solutions = tridiagonal_solve(main_diagonal, down_diagonal, upper_diagonal, solutions)
        cur_tau += time_step


def main():
    a = -1
    b = 10

    input_h = input("Enter spatial step (h): ")
    try:
        h = float(input_h)
        if h >= 0:
            print(f"Your spatial step (h): {h}")
        else:
            print("Spatial step (h) must be a positive float.")
            sys.exit(1)
    except ValueError:
        print("Spatial step (h) must be a positive float.")
        sys.exit(1)

    input_r = input("Enter r = τ / h (τ is the time step): ")
    try:
        r = float(input_r)
        if r >= 0:
            print(f"Your r: {r}")
        else:
            print("r must be a positive float.")
            sys.exit(1)
        if r > 1:
            print("Be careful! Godunov’s scheme loses stability (does not converge to an exact solution) for r > 1.")
    except ValueError:
        print("r must be a positive float.")
        sys.exit(1)
    tau = r * h

    input_T = input("Enter time limit (T): ")
    try:
        T = float(input_T)
        if T < 0:
            print("Time limit (T) must be a non-negative float.")
            sys.exit(1)
        if tau > T:
            print("Time limit (T) must be greater than or equal to the time step (τ).")
            sys.exit(1)
        print(f"Your time limit (T): {T}")
    except ValueError:
        print("Time limit (T) must be a non-negative float.")
        sys.exit(1)

    input_num_of_frames = input("Enter the number of frames with function graphs you want to get: ")
    try:
        num_of_frames = int(input_num_of_frames)
        if num_of_frames >= 0:
            print(f"Your number of frames with function graphs: {num_of_frames}")
        else:
            print("The number of frames must be a non-negative integer.")
            sys.exit(1)
    except ValueError:
        print("The number of frames must be a non-negative integer.")
        sys.exit(1)

    cur_frame_time = 0
    frame_times = []
    while cur_frame_time < T and len(frame_times) < num_of_frames:
        frame_times.append(cur_frame_time)
        cur_frame_time += tau

    frame_index = 0
    initial_conditions = create_initial_conditions(a, b, h, T, tau)
    for frame_time in frame_times:
        plt.xlabel("x")
        plt.ylabel("u(x, t)")
        plt.ylim(-1.5, 1.5)
        plt.xlim(-1.5, 10.5)
        draw_exact_solution(a, b, h, frame_time)
        draw_implicit_scheme(a, b, h, r, T, tau, frame_time)
        draw_godunov_scheme(initial_conditions, T, h, a, b, r, tau, frame_time)
        plt.savefig(f"fig{frame_index}.png")
        plt.clf()
        frame_index += 1


if __name__ == "__main__":
    main()
