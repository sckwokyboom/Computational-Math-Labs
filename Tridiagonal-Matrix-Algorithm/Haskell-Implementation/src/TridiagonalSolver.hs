module TridiagonalSolver
    ( tridiagonalSolve,
    forwardPass,
    backwardPass
    ) where

tridiagonalSolve :: [Double] -> [Double] -> [Double] -> [Double] -> [Double]
tridiagonalSolve mainDiagonal downDiagonal upperDiagonal lastColumn =
    let countOfEquations = length lastColumn
        (alphas, betas) = forwardPass mainDiagonal downDiagonal upperDiagonal lastColumn countOfEquations [] []
    in backwardPass alphas betas (countOfEquations - 1) countOfEquations []

forwardPass :: [Double] -> [Double] -> [Double] -> [Double] -> Int -> [Double] -> [Double] -> ([Double], [Double])
forwardPass _ _ _ _ 0 alphas betas = (alphas, betas)
forwardPass mainDiagonal downDiagonal upperDiagonal lastColumn remainingElements alphas betas =
    let countOfEquations = length lastColumn
        curIndex = countOfEquations - remainingElements
        alpha
          | curIndex == 0
              = -(head upperDiagonal / head mainDiagonal)
          | curIndex == (countOfEquations - 1)
              = 0
          | otherwise
              = -(upperDiagonal !! curIndex) / ((mainDiagonal !! curIndex) + (downDiagonal !! (curIndex - 1)) * (alphas !! (curIndex - 1)))

        beta
          | curIndex == 0
              = head lastColumn / head mainDiagonal
          | otherwise
              = ((lastColumn !! curIndex) - (downDiagonal !! (curIndex - 1)) * (betas !! (curIndex - 1))) / ((mainDiagonal !! curIndex) + (downDiagonal !! (curIndex - 1)) * (alphas !! (curIndex - 1)))

    in forwardPass mainDiagonal downDiagonal upperDiagonal lastColumn (remainingElements - 1) (alphas ++ [alpha]) (betas ++ [beta])

backwardPass :: [Double] -> [Double] -> Int -> Int -> [Double] -> [Double]
backwardPass _ _ (-1) _ solutions = solutions
backwardPass alphas betas curIndex countOfEquations solutions =
    let solution
          | curIndex == (countOfEquations - 1)
              = betas !! curIndex
          | otherwise
              = (alphas !! curIndex) * head solutions + (betas !! curIndex)
    in backwardPass alphas betas (curIndex-1) countOfEquations (solution : solutions)