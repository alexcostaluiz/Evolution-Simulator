package com.costa.alex.evolutionsimulator;

/**
 * Created by Alex on 10/17/2017.
 * Useful formulas
 */
public class UsefulMaths {

    public static int factorial(int n) {
        if(n == 0) {
            return 1;
        }

        int result = 1;
        for(int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
