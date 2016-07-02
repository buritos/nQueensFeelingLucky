#nQueensFeelingLucky

A lucky program that finds a solution for a large n queens problem.

This program uses a minimum conflicts approach to find a solution to the n queens problem for large n values. 

![min-conflicts solution to n queens](https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/8queensminconflict.gif/440px-8queensminconflict.gif)

A brute force solution to the n queens problem would be intractable for problems of n > 20, as 20! = 2433*10^18. If the goal is to find a single solution the min-conflicts algorithm will usually find one in less than 50 steps on average, even for very large n values.

The program was developed for the purpose of having fun while practicing algorithms.