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
import com.wittakarn.bpm.exception.BPMException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Wittakarn
 */
public class BonitaClient {

    public static void main(String[] args) {
        searchTask();
    }
    
    /*
    user:john,password:john,role:employee
    user:admin,password:bpm,role:leader
    */
    private static void searchTask(){
        LeaveItem item;
        List<LeaveItem> items = new ArrayList<LeaveItem>();
        BPM bpm;
        try {

            item = new LeaveItem(new BonitaItem());
            item.setUserId("admin");
            item.setPassword("bpm");
            
            bpm = BPMContext.getInstance(item);
            
            List<HashMap<String, Object>> hashs = (List<HashMap<String, Object>>) bpm.searchTask(item);
            System.out.println("hashs.size() = " + hashs.size());
            for (Iterator<HashMap<String, Object>> it = hashs.iterator(); it.hasNext();) {
                LeaveItem itemRes = new LeaveItem(new BonitaItem());
                HashMap<String, Object> hashMap = it.next();
                itemRes.putContentToWorkItem(hashMap);
                items.add(itemRes);
            }
            
            System.out.println("items = " + items);
            BPMContext.returnInstance(bpm);
//            item.setTaskId("60003");
//            bpm.getBpm().completeTask(item);

        } catch (BPMException we) {
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
    private static void executeTaskAfterApplySingleton(){
        HashMap<String, Object> hash;
        LeaveItem item;
        BPM bpm;
        try {

            item = new LeaveItem(new BonitaItem());
            item.setUserId("admin");
            item.setPassword("bpm");
            item.setTaskId("60003");
            
            bpm = BPMContext.getInstance(item);
            hash = (HashMap<String, Object>) bpm.completeTask(item);
            
            item.putContentToWorkItem(hash);
            BPMContext.returnInstance(bpm);
            System.out.println("item = " + item);

        } catch (BPMException we) {
            we.printStackTrace();
        } finally {
            hash = null;
            item = null;
            bpm = null;
        }
    }
    
    public static void testSingleton(){
        LeaveItem item;
        BPM bpm;
        item = new LeaveItem(new BonitaItem());
        bpm = BPMContext.getInstance(item);
        BPMContext.returnInstance(bpm);
        System.out.println(bpm);
        bpm = BPMContext.getInstance(item);
        BPMContext.returnInstance(bpm);
        System.out.println(bpm);
        bpm = BPMContext.getInstance(item);
        System.out.println(bpm);
        bpm = BPMContext.getInstance(item);
        System.out.println(bpm);
        bpm = BPMContext.getInstance(item);
        System.out.println(bpm);
        
        BPMContext.showAllInstance();
        
        bpm = BPMContext.getInstance(item);
        System.out.println(bpm);
        
        BPMContext.showAllInstance();
    }
}
