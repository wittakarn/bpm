/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.domain;

import com.wittakarn.bpm.exception.WorkflowException;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author wittakarn
 */
public interface Item extends Serializable{
    public String getItemType();
    public void putContentToWorkItem(HashMap<String, Object> content) throws WorkflowException;
}
