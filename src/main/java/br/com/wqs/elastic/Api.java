package br.com.wqs.elastic;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/requisicao")
@RequiredArgsConstructor
@Slf4j
public class Api {

    private final RequestRepository repository;
    private final ElasticsearchOperations elasticsearchOperations;

    @GetMapping("/{id}")
    public ResponseEntity<List<RequisicaoDto>> getById(@PathVariable("id") String requestId) {
        log.info("consulta [{}]", requestId);
        var r = repository.findByRequestIdOrRequestHistory(requestId);
        var ret = r.stream().map(RequisicaoDto::create).toList();
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/p/{id}")
    public ResponseEntity<List<RequisicaoDto>> getByIdProjection(@PathVariable("id") String requestId) {
        log.info("projection [{}]", requestId);
        var r = repository.findByRequestId(requestId);
        var ret = r.stream().map(p -> new RequisicaoDto(p.getId(), p.getRequestId(), p.getRequests().stream().toList())
                ).toList();
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/q/{id}")
    public ResponseEntity<List<RequisicaoDto>> queryById(@PathVariable("id") String requestId) {
        log.info("query [{}]", requestId);
        BoolQuery.Builder queryBuilder = new BoolQuery.Builder();

        // Adiciona a condição de OU (should) para requestId
        queryBuilder.should(
            new TermQuery.Builder()
                    .field("requestId.keyword")
                    .value(requestId)
                    .build()
                    ._toQuery()
        ).should(
            new TermQuery.Builder()
            .field("requestHistory.keyword")
            .value(requestId)
            .build()
            ._toQuery()
        );

        // Constrói a query final
        NativeQuery query = NativeQuery.builder()
                .withQuery(queryBuilder.build()._toQuery())
                .build();

        // Executa a busca
        List<SearchHit<Request>> content = elasticsearchOperations
                .search(query, Request.class, IndexCoordinates.of("requisicoes"))
                .getSearchHits();

        var ret = content.stream().map(row -> RequisicaoDto.create(row.getContent())).toList();

       return ResponseEntity.ok(ret);

    }
}
