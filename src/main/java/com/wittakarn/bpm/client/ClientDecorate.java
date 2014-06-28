/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.client;

import com.wittakarn.bpm.domain.LeaveItem;
import com.wittakarn.bpm.oracle.OracleItem;
import java.util.HashMap;

/**
 *
 * @author Wittakarn
 */
public class ClientDecorate {

    public static void main(String[] args) {
        HashMap<String, Object> hash;
        try {
            hash = new HashMap<String, Object>();
            hash.put("userId", "1");
            hash.put("password", "2");
            hash.put("outcome", "3");
            hash.put("state", "4");
            hash.put("leaveIssue", "5");
            hash.put("leaveTotDay", "6");

            LeaveItem item = new LeaveItem(new OracleItem());
            item.putContentToWorkItem(hash);

            System.out.println(item.getItem().getItemType());
            System.out.println(item.toString());
            System.out.println(item.getItem().toString());
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            hash = null;
        }
    }
}
