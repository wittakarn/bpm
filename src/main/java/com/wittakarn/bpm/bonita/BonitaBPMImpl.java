/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.bonita;

import com.wittakarn.bpm.BPM;
import com.wittakarn.bpm.domain.WorkItem;
import com.wittakarn.bpm.exception.WorkflowException;
import java.util.List;

/**
 *
 * @author wittakarn
 */
public class BonitaBPMImpl implements BPM {

    private static final long serialVersionUID = 1L;

    public Object initialTask(WorkItem workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object countTask(WorkItem workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object searchTask(WorkItem workItem) throws WorkflowException {
        try {
            return BonitaWrapper.listPendingTasks(workItem.getUserId(), workItem.getPassword());
        } catch (Exception e) {
            throw new WorkflowException(e);
        }
    }

    public Object updateTask(WorkItem workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object completeTask(WorkItem workItem) throws WorkflowException {
        try {
            Long taskId = Long.valueOf(workItem.getTaskId());
            BonitaWrapper.executeATask(workItem.getUserId(), workItem.getPassword(), taskId);
            return null;
        } catch (Exception e) {
            throw new WorkflowException(e);
        }
    }

    public Object claimTask(WorkItem workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object cancelClaimTask(WorkItem workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object searchTaskByTaskId(WorkItem workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object updateTask(List<WorkItem> workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object completeTask(List<WorkItem> workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
