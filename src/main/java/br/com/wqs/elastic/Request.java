package br.com.wqs.elastic;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "requisicoes", createIndex = true)
@Data
public class Request {

    @Id
    @Field(name = "_id", type = FieldType.Keyword)
    private String id;

    @Field(name = "requestId", type = FieldType.Keyword)
    private String requestId;

    @Field(name = "requestHistory", type = FieldType.Keyword)
    private List<String> requests;
}
