/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.context;

import com.wittakarn.bpm.BPM;
import com.wittakarn.bpm.domain.User;
import com.wittakarn.bpm.domain.WorkItem;
import com.wittakarn.bpm.exception.WorkflowException;
import com.wittakarn.bpm.oracle.OracleBPMImpl;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wittakarn
 */
public class BPMContext implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private BPM bpm;
    private static BPM instance;

    public BPMContext() throws WorkflowException{
        try {
            this.bpm = OracleBPMImpl.class.newInstance();
        } catch (InstantiationException e) {
            throw new WorkflowException(e);
        } catch (IllegalAccessException e) {
            throw new WorkflowException(e);
        }
    }
    
    public BPMContext(BPM bpm) {
        this.bpm = bpm;
    }
    
    public BPMContext(WorkItem workItem) {
        try{
            bpm = (BPM) Class.forName(workItem.getItem().getItemType()).newInstance();
        } catch (InstantiationException e) {
            throw new WorkflowException(e);
        } catch (IllegalAccessException e) {
            throw new WorkflowException(e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BPMContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static synchronized BPM getInstance(WorkItem workItem) {

        try{
            if(instance == null || !instance.getClass().getName().equals(workItem.getItem().getItemType()))
                instance = (BPM) Class.forName(workItem.getItem().getItemType()).newInstance();
            return instance;
        } catch (InstantiationException e) {
            throw new WorkflowException(e);
        } catch (IllegalAccessException e) {
            throw new WorkflowException(e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BPMContext.class.getName()).log(Level.SEVERE, null, ex);
            return instance;
        }
    }

    /**
     * @return the bpm
     */
    public BPM getBpm() {
        return bpm;
    }

    /**
     * @param bpm the bpm to set
     */
    public void setBpm(BPM bpm) {
        this.bpm = bpm;
    }
    
}
