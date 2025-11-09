package com.cvconnect.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "job-ad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobAdDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Keyword)
    private List<String> careers;

    @Field(type = FieldType.Keyword)
    private List<String> level;

    @Field(type = FieldType.Keyword)
    private List<String> location;

    @Field(type = FieldType.Double)
    private Double salaryFrom;

    @Field(type = FieldType.Double)
    private Double salaryTo;

    @Field(type = FieldType.Keyword)
    private String jobType;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String benefit;

    @Field(type = FieldType.Dense_Vector, dims = 1536)
    private float[] embedding;
}
