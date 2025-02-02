package com.zblouse.fantasyfitness.core;

public class RepositoryEvent<T> extends Event {

    private final T repositoryResponseObject;

    public RepositoryEvent(EventType eventType, T repositoryResponseObject) {
        super(eventType);
        this.repositoryResponseObject = repositoryResponseObject;
    }

    public T getRepositoryResponseObject(){
        return this.repositoryResponseObject;
    }
}
