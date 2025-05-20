package com.example.workflow.service;

import com.example.workflow.model.Workflow;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WorkflowService {
    private final Map<String, Workflow> workflowStore = new ConcurrentHashMap<>();

    public List<Workflow> getAllWorkflows() {
        return new ArrayList<>(workflowStore.values());
    }

    public Workflow getWorkflow(String id) {
        return workflowStore.get(id);
    }

    public Workflow createWorkflow(Workflow workflow) {
        String id = UUID.randomUUID().toString();
        workflow.setId(id);
        workflowStore.put(id, workflow);
        return workflow;
    }

    public Workflow updateWorkflow(String id, Workflow workflow) {
        workflow.setId(id);
        workflowStore.put(id, workflow);
        return workflow;
    }

    public void deleteWorkflow(String id) {
        workflowStore.remove(id);
    }

    public String executeWorkflow(String id) {
        Workflow workflow = workflowStore.get(id);
        if (workflow == null) {
            throw new NoSuchElementException("Workflow not found");
        }
        // Just simulate execution for now
        return "Workflow " + id + " 执行完成";
    }
}