/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.oracle;

import com.wittakarn.bpm.BPM;
import com.wittakarn.bpm.bonita.BonitaWrapper;
import com.wittakarn.bpm.domain.WorkItem;
import com.wittakarn.bpm.exception.WorkflowException;
import java.util.List;

/**
 *
 * @author wittakarn
 */
public class OracleBPMImpl implements BPM {

    private static final long serialVersionUID = 1L;

    @Override
    public Object initialTask(WorkItem workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object countTask(WorkItem workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object searchTask(WorkItem workItem) throws WorkflowException {
        try {
            return OracleWrapper.searchTask(workItem.getUserId(), workItem.getPassword());
        } catch (Exception e) {
            throw new WorkflowException(e);
        }
    }

    @Override
    public Object updateTask(WorkItem workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object completeTask(WorkItem workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object claimTask(WorkItem workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object cancelClaimTask(WorkItem workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object searchTaskByTaskId(WorkItem workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object updateTask(List<WorkItem> workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object completeTask(List<WorkItem> workItem) throws WorkflowException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
