package com.zblouse.fantasyfitness.core;

import java.util.Map;

public interface Repository<T> {

    public static final String REPOSITORY_INTERACTION = "repositoryInteraction";

    public void readCallback(T object, Map<String, Object> metadata);
    public void writeCallback(T object, Map<String, Object> metadata);
    public void updateCallback(boolean success, Map<String, Object> metadata);
}
