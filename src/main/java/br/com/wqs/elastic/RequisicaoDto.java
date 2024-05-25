package br.com.wqs.elastic;

import java.util.List;

public record RequisicaoDto(
        String id,
        String requestId,
        List<String>requests
) {

    public static RequisicaoDto create(Request reque) {
        return new RequisicaoDto(reque.getId(), reque.getRequestId(), reque.getRequests());
    }
}
