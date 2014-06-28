/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.client;

import com.wittakarn.bpm.BPM;
import com.wittakarn.bpm.bonita.BonitaItem;
import com.wittakarn.bpm.context.BPMContext;
import com.wittakarn.bpm.domain.LeaveItem;
import com.wittakarn.bpm.exception.WorkflowException;

/**
 *
 * @author Wittakarn
 */
public class BonitaClient {

    public static void main(String[] args) {
        searchTaskAfterApplySingleton();
    }
    
    /*
    user:john,password:john,role:employee
    user:admin,password:bpm,role:leader
    */
    private static void searchTaskBeforeApplySingleton(){
        LeaveItem item;
        BPMContext bpm;
        try {

            item = new LeaveItem(new BonitaItem());
            item.setUserId("admin");
            item.setPassword("bpm");
            
            bpm = new BPMContext(item);
            
            bpm.getBpm().searchTask(item);
            
//            item.setTaskId("60003");
//            bpm.getBpm().completeTask(item);

        } catch (WorkflowException we) {
            we.printStackTrace();
        } finally {
            item = null;
            bpm = null;
        }
    }
    
    /*
    user:john,password:john,role:employee
    user:admin,password:bpm,role:leader
    */
    private static void searchTaskAfterApplySingleton(){
        LeaveItem item;
        BPM bpm;
        try {

            item = new LeaveItem(new BonitaItem());
            item.setUserId("admin");
            item.setPassword("bpm");
            
            bpm = BPMContext.getInstance(item);
            
            bpm.searchTask(item);
            
//            item.setTaskId("60003");
//            bpm.getBpm().completeTask(item);

        } catch (WorkflowException we) {
            we.printStackTrace();
        } finally {
            item = null;
            bpm = null;
        }
    }
}
