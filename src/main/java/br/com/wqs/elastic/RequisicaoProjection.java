package br.com.wqs.elastic;

import java.util.Collection;
import java.util.List;

public interface RequisicaoProjection {
    String getId();

    String getRequestId();

    Collection<String> getRequests();
}
