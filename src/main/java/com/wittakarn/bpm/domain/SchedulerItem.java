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
@ToString
public class SchedulerItem extends WorkItem{
    private static final long serialVersionUID = 1L;
    
    private String testTotDay;
    private String testStartDate;
    
    public SchedulerItem(Item item){
        super(item);
    }
    
    /**
     * @return the testTotDay
     */
    public String getTestTotDay() {
        return testTotDay;
    }

    /**
     * @param testTotDay the testTotDay to set
     */
    public void setTestTotDay(String testTotDay) {
        this.testTotDay = testTotDay;
    }

    /**
     * @return the testStartDate
     */
    public String getTestStartDate() {
        return testStartDate;
    }

    /**
     * @param testStartDate the testStartDate to set
     */
    public void setTestStartDate(String testStartDate) {
        this.testStartDate = testStartDate;
    }

    public void putContentToWorkItem(HashMap<String, Object> content){
        if(content.get("testTotDay") != null){
            this.setTestTotDay(content.get("testTotDay").toString());
        }
        
        if(content.get("testStartDate") != null){
            this.setTestStartDate(content.get("testStartDate").toString());
        }
        
        super.putHashMapToWorkItem(content);
    }
    
}
