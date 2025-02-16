package com.zblouse.fantasyfitness.core;

import java.util.Map;

public class RepositoryEvent<T> extends Event {

    private final T repositoryResponseObject;

    public RepositoryEvent(EventType eventType, T repositoryResponseObject, Map<String, Object> metadata) {
        super(eventType, metadata);
        this.repositoryResponseObject = repositoryResponseObject;
    }

    public T getRepositoryResponseObject(){
        return this.repositoryResponseObject;
    }
}
