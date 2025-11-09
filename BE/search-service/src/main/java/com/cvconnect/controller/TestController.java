package com.cvconnect.controller;

import com.cvconnect.document.JobAdDocument;
import com.cvconnect.repository.JobAdRepository;
import nmquan.commonlib.dto.response.Response;
import nmquan.commonlib.utils.ResponseUtils;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private EmbeddingModel embeddingModel;
    @Autowired
    private JobAdRepository jobRepository;
    @PostMapping("/insert")
    public ResponseEntity<Response<Void>> insert(@RequestBody Map<String, Object> map) {
        String title = String.valueOf(map.get("title"));
        List<String> careers = (List<String>) map.get("careers");
        List<String> level = (List<String>) map.get("level");
        List<String> location = (List<String>) map.get("location");
        Double salaryFrom = Double.valueOf(String.valueOf(map.get("salaryFrom")));
        Double salaryTo = Double.valueOf(String.valueOf(map.get("salaryTo")));
        String jobType = String.valueOf(map.get("jobType"));
        String description = String.valueOf(map.get("description"));
        String benefit = String.valueOf(map.get("benefit"));

        String input = "Title: " + title + "\n" +
                "Careers: " + String.join(", ", careers) + "\n" +
                "Level: " + String.join(", ", level) + "\n" +
                "Location: " + String.join(", ", location) + "\n" +
                "Salary From: " + salaryFrom + "\n" +
                "Salary To: " + salaryTo + "\n" +
                "Job Type: " + jobType + "\n" +
                "Description: " + description + "\n" +
                "Benefit: " + benefit;

        EmbeddingResponse response = embeddingModel.embedForResponse(List.of(input));
        float[] vector = response.getResult().getOutput();

        JobAdDocument doc = new JobAdDocument();
        doc.setTitle(title);
        doc.setCareers(careers);
        doc.setLevel(level);
        doc.setLocation(location);
        doc.setSalaryFrom(salaryFrom);
        doc.setSalaryTo(salaryTo);
        doc.setJobType(jobType);
        doc.setDescription(description);
        doc.setBenefit(benefit);
        doc.setEmbedding(vector);

        jobRepository.save(doc);

        return ResponseUtils.success(null);
    }
}
