package com.wittakarn.bpm;

import java.io.Serializable;
import java.util.List;

import com.wittakarn.bpm.domain.WorkItem;
import com.wittakarn.bpm.exception.CancelClaimTaskException;
import com.wittakarn.bpm.exception.ClaimTaskException;
import com.wittakarn.bpm.exception.CompleteTaskException;
import com.wittakarn.bpm.exception.CountTaskException;
import com.wittakarn.bpm.exception.InitialTaskException;
import com.wittakarn.bpm.exception.SearchTaskException;
import com.wittakarn.bpm.exception.UpdateTaskException;

public interface BPM extends Serializable {

    public Object initialTask(WorkItem workItem) throws InitialTaskException;

    public Object countTask(WorkItem workItem) throws CountTaskException;

    public Object searchTask(WorkItem workItem) throws SearchTaskException;

    public Object updateTask(WorkItem workItem) throws UpdateTaskException;

    public Object completeTask(WorkItem workItem) throws CompleteTaskException;

    public Object claimTask(WorkItem workItem) throws ClaimTaskException;

    public Object cancelClaimTask(WorkItem workItem) throws CancelClaimTaskException;

    public Object searchTaskByTaskId(WorkItem workItem) throws SearchTaskException;

    public Object updateTask(List<WorkItem> workItem) throws UpdateTaskException;

    public Object completeTask(List<WorkItem> workItem) throws CompleteTaskException;
}
