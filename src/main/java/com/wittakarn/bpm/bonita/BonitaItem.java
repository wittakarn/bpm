/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.bonita;

import com.wittakarn.bpm.domain.Item;
import com.wittakarn.bpm.exception.BPMException;
import java.lang.reflect.Method;
import java.util.HashMap;
import lombok.ToString;

/**
 *
 * @author wittakarn
 */
@ToString
public class BonitaItem implements Item{
    
    private static final long serialVersionUID = 1L;
    private static final String itemType = "com.wittakarn.bpm.bonita.BonitaBPMImpl";
    
    protected String activityInstance;
    
    public String getItemType() {
        return itemType;
    }

    public void putContentToWorkItem(HashMap<String, Object> content) throws BPMException {
        String methodName;
        String parameterTypeName;
        String key;
        Method[] methodList;

        try {


        } catch (Exception ex) {
            throw new BPMException(ex);
        } finally {
            methodName = null;
            methodList = null;
        }
    }
}
