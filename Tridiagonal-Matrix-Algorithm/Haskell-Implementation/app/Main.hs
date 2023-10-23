module Main (main) where

import TridiagonalSolver
import Text.Printf

main :: IO ()
main = do
    let mainDiagonal = [2.0, 2.0, 2.0]
    let secondaryDownDiagonal = [1.0, 1.0]
    let secondaryUpperDiagonal = [1.0, 1.0]
    let lastColumn = [1.0, 2.0, 3.0]

    let solution = tridiagonalSolve mainDiagonal secondaryDownDiagonal secondaryUpperDiagonal lastColumn
    putStrLn "Solution:"
    print solution
    let indexedList = zip [1..] solution
    mapM_ (\(i, x) -> printf "X_%d = %.2f\n" (i :: Int) x) indexedList