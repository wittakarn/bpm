/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.context;

import com.wittakarn.bpm.BPM;
import com.wittakarn.bpm.domain.WorkItem;
import com.wittakarn.bpm.exception.WorkflowException;
import com.wittakarn.bpm.oracle.OracleBPMImpl;
import java.io.Serializable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wittakarn
 */
public class BPMContext implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private BPM bpm;
    //private static BPM instance;
    private final static int limit = 5;
    private static Vector<BPM> instance = new Vector<BPM>(limit);
    
    static {
        instance.setSize(limit);
    }

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
    
    public static BPM getInstance(WorkItem workItem) {
        boolean found  = false;
        int index = -1;
        BPM result = null;
        try{
            for (int i = 0; i < limit; i++) {
                if((instance.elementAt(i) == null) && !found){
                    instance.remove(i);
                    instance.add(i, (BPM) Class.forName(workItem.getItem().getItemType()).newInstance());
                    index = i;
                    found = true;
                    result = (BPM) instance.elementAt(index);
                }
            }
            if(result == null){
                System.out.println("No avaliable BPM instance");
            }
            
            return result;          
        } catch (InstantiationException e) {
            throw new WorkflowException(e);
        } catch (IllegalAccessException e) {
            throw new WorkflowException(e);
        } catch (ClassNotFoundException e) {
            throw new WorkflowException(e);
        }
    }
    
    public static void returnInstance(BPM instance){
        BPM clear;
        for (int i = 0; i < limit; i++) {
            clear = BPMContext.instance.elementAt(i);
            System.out.println("clear/instance :" + clear + "/" + instance);
            if((clear != null && clear.equals(instance))){
                BPMContext.instance.remove(i);
                BPMContext.instance.add(i, null);
            }
        }
    }
    
    public static void showAllInstance(){
        String displayLine = "Element :";
        for (int i = 0; i < limit; i++) {
            if((instance.elementAt(i) != null)){
                displayLine = displayLine + " [" + i + "] ";
            }else
                displayLine = displayLine + " [Empty] ";
        }
        System.out.println(displayLine);
        System.out.println("-------------------");
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
