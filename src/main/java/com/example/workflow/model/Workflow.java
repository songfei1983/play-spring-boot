package com.example.workflow.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Workflow {
    private String id;
    private String name;
    private String description;
    private List<Step> steps;

    public void setId(String id) {
        this.id = id;
    }
}