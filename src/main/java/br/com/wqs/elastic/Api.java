package br.com.wqs.elastic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/{id}")
    public ResponseEntity<List<RequisicaoDto>> getById(@PathVariable("id") String requestId) {
        log.info("consulta [{}]", requestId);
        var r = repository.findByRequestIdOrRequestHistory(requestId);
        var ret = r.stream().map(RequisicaoDto::create).toList();
        return ResponseEntity.ok(ret);
    }
}
