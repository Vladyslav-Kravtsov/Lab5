package client;

import interfaces.Executable;
import java.math.BigInteger;
import java.io.Serializable;

public class Factorial implements Executable, Serializable {
    private final static long serialVersionUID = -1L;
    private int n;

    public Factorial(int n) {
        this.n = n;
    }
    public Object execute() {
        BigInteger result = BigInteger.valueOf(1);
        for (int i = 1; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
}
