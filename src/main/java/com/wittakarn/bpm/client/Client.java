/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.client;

import com.wittakarn.bpm.bonita.BonitaBPMImpl;
import com.wittakarn.bpm.bonita.BonitaItem;
import com.wittakarn.bpm.context.BPMContext;
import com.wittakarn.bpm.domain.LeaveItem;
import com.wittakarn.bpm.oracle.OracleItem;
import java.util.HashMap;

/**
 *
 * @author wittakarn
 */
public class Client {

    public static void main(String[] args) {

        HashMap<String, Object> hash;

        try {

            LeaveItem item = new LeaveItem(new OracleItem());
            BPMContext bpm = new BPMContext(item);
            hash = (HashMap<String, Object>) bpm.getBpm().completeTask(item);
            item.putContentToWorkItem(hash);
            item.getItem(); // get OracleItem
            //bpm.completeTaskList(null);

            item = new LeaveItem(new BonitaItem());
            bpm = new BPMContext(new BonitaBPMImpl());
            hash = (HashMap<String, Object>) bpm.getBpm().completeTask(item);
            item.putContentToWorkItem(hash);
            item.getItem(); // get BonitaItem
            //bpm.completeTaskList(null);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            hash = null;
        }
    }
}
