package main.java.server;

import interfaces.Result;

import java.io.Serializable;

public class ResultImpl implements Result, Serializable {
    Object output;
    double scoreTime;
    public ResultImpl(Object o, double c) {
        output = o;
        scoreTime = c;
    }
    public Object output() {
        return output;
    }
    public double scoreTime() {
        return scoreTime;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "ResultImpl{" +
                "output=" + output +
                ", scoreTime=" + scoreTime +
                '}';
    }
}
