package com.zblouse.fantasyfitness.core;

public interface Repository<T> {

    public void readCallback(T object);
    public void writeCallback(T object);
}
