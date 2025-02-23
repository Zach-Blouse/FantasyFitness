package com.zblouse.fantasyfitness.core;

import java.util.Map;

public interface DomainService<T> {

    public static final String INTER_DOMAIN_SERVICE_ORIGIN_KEY = "domainServiceRequestOrigin";
    public static final String INTER_DOMAIN_SERVICE_RESPONSE_CLASS_KEY = "domainServiceResponseClass";
    public static final String ORIGIN_FUNCTION = "originFunction";

    public void repositoryResponse(T responseBody, Map<String, Object> metadata);

    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata);
}
