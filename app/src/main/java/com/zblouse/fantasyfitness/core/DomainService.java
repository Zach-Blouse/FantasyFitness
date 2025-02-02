package com.zblouse.fantasyfitness.core;

import java.util.Map;

public interface DomainService<T> {

    public void repositoryResponse(T responseBody, Map<String, Object> metadata);
}
