 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.domain;

import java.util.HashMap;
import lombok.ToString;

/**
 *
 * @author wittakarn
 */
@ToString(callSuper = true)
public class LeaveItem extends WorkItem{
    private static final long serialVersionUID = 1L;
    
    private String leaveTotDay;
    private String leaveIssue;
    
    public LeaveItem(Item item){
        super(item);
    }

    /**
     * @return the leaveTotDay
     */
    public String getLeaveTotDay() {
        return leaveTotDay;
    }

    /**
     * @param leaveTotDay the leaveTotDay to set
     */
    public void setLeaveTotDay(String leaveTotDay) {
        this.leaveTotDay = leaveTotDay;
    }

    /**
     * @return the leaveIssue
     */
    public String getLeaveIssue() {
        return leaveIssue;
    }

    /**
     * @param leaveIssue the leaveIssue to set
     */
    public void setLeaveIssue(String leaveIssue) {
        this.leaveIssue = leaveIssue;
    }
    
    public void putContentToWorkItem(HashMap<String, Object> content){
        if(content.get("leaveTotDay") != null){
            this.leaveTotDay = content.get("leaveTotDay").toString();
        }
        
        if(content.get("leaveIssue") != null){
            this.leaveIssue = content.get("leaveIssue").toString();
        }
        super.putHashMapToWorkItem(content);
    }
    
}
