package com.wittakarn.bpm;

import java.io.Serializable;
import java.util.List;

import com.wittakarn.bpm.domain.WorkItem;
import com.wittakarn.bpm.exception.WorkflowException;

public interface BPM extends Serializable {

    public Object initialTask(WorkItem workItem) throws WorkflowException;

    public Object countTask(WorkItem workItem) throws WorkflowException;

    public Object searchTask(WorkItem workItem) throws WorkflowException;

    public Object updateTask(WorkItem workItem) throws WorkflowException;

    public Object completeTask(WorkItem workItem) throws WorkflowException;

    public Object claimTask(WorkItem workItem) throws WorkflowException;

    public Object cancelClaimTask(WorkItem workItem) throws WorkflowException;

    public Object searchTaskByTaskId(WorkItem workItem) throws WorkflowException;

    public Object updateTask(List<WorkItem> workItem) throws WorkflowException;

    public Object completeTask(List<WorkItem> workItem) throws WorkflowException;
}
