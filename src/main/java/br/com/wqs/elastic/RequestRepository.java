package br.com.wqs.elastic;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RequestRepository extends ElasticsearchRepository<Request, String> {

    @Query("{\"bool\": { \"should\": [" +
            "{\"term\": { \"requestId.keyword\": \"?0\" }}," +
            "{\"term\": { \"requestHistory.keyword\": \"?0\" }}" +
            "] }}")
    List<Request> findByRequestIdOrRequestHistory(String requestId);

    @Query("{\"bool\": { \"should\": [" +
            "{\"term\": { \"requestId.keyword\": \"?0\" }}," +
            "{\"term\": { \"requestHistory.keyword\": \"?0\" }}" +
            "] }}")
    Collection<RequisicaoProjection> findByRequestId(String requestId);
}

