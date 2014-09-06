/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.oracle;

import com.wittakarn.bpm.BPM;
import com.wittakarn.bpm.bonita.BonitaWrapper;
import com.wittakarn.bpm.domain.WorkItem;
import com.wittakarn.bpm.exception.CancelClaimTaskException;
import com.wittakarn.bpm.exception.ClaimTaskException;
import com.wittakarn.bpm.exception.CompleteTaskException;
import com.wittakarn.bpm.exception.CountTaskException;
import com.wittakarn.bpm.exception.InitialTaskException;
import com.wittakarn.bpm.exception.SearchTaskException;
import com.wittakarn.bpm.exception.UpdateTaskException;
import com.wittakarn.bpm.exception.BPMException;
import java.util.List;

/**
 *
 * @author wittakarn
 */
public class OracleBPMImpl implements BPM {

    private static final long serialVersionUID = 1L;

    @Override
    public Object initialTask(WorkItem workItem) throws InitialTaskException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object countTask(WorkItem workItem) throws CountTaskException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object searchTask(WorkItem workItem) throws SearchTaskException {
        try {
            return OracleWrapper.searchTask(workItem.getUserId(), workItem.getPassword());
        } catch (Exception e) {
            throw new BPMException(e);
        }
    }

    @Override
    public Object updateTask(WorkItem workItem) throws UpdateTaskException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object completeTask(WorkItem workItem) throws CompleteTaskException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object claimTask(WorkItem workItem) throws ClaimTaskException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object cancelClaimTask(WorkItem workItem) throws CancelClaimTaskException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object searchTaskByTaskId(WorkItem workItem) throws SearchTaskException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object updateTask(List<WorkItem> workItem) throws UpdateTaskException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object completeTask(List<WorkItem> workItem) throws CompleteTaskException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
