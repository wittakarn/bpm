/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.bonita;

import com.wittakarn.bpm.BPM;
import com.wittakarn.bpm.domain.WorkItem;
import com.wittakarn.bpm.exception.CancelClaimTaskException;
import com.wittakarn.bpm.exception.ClaimTaskException;
import com.wittakarn.bpm.exception.CompleteTaskException;
import com.wittakarn.bpm.exception.CountTaskException;
import com.wittakarn.bpm.exception.InitialTaskException;
import com.wittakarn.bpm.exception.SearchTaskException;
import com.wittakarn.bpm.exception.UpdateTaskException;
import java.util.List;

/**
 *
 * @author wittakarn
 */
public class BonitaBPMImpl implements BPM {

    private static final long serialVersionUID = 1L;

    public Object initialTask(WorkItem workItem) throws InitialTaskException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object countTask(WorkItem workItem) throws CountTaskException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object searchTask(WorkItem workItem) throws SearchTaskException {
        try {
            return BonitaWrapper.listPendingTasks(workItem.getUserId(), workItem.getPassword());
        } catch (Exception e) {
            throw new SearchTaskException(e);
        }
    }

    public Object updateTask(WorkItem workItem) throws UpdateTaskException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object completeTask(WorkItem workItem) throws CompleteTaskException {
        try {
            Long taskId = Long.valueOf(workItem.getTaskId());
            BonitaWrapper.executeATask(workItem.getUserId(), workItem.getPassword(), taskId);
            return null;
        } catch (Exception e) {
            throw new CompleteTaskException(e);
        }
    }

    public Object claimTask(WorkItem workItem) throws ClaimTaskException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object cancelClaimTask(WorkItem workItem) throws CancelClaimTaskException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object searchTaskByTaskId(WorkItem workItem) throws SearchTaskException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object updateTask(List<WorkItem> workItem) throws UpdateTaskException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object completeTask(List<WorkItem> workItem) throws CompleteTaskException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
