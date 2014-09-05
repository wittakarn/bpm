/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.domain;

import com.wittakarn.bpm.exception.WorkflowException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import lombok.ToString;

/**
 *
 * @author Wittakarn
 */
@ToString
public abstract class WorkItem implements Item, Serializable {

    private static final long serialVersionUID = 1L;
    private String userId;
    private String password;
    private String taskId;
    private String processId;
    private String processName;
    private HashMap<String, Object> updateField;
    private List<String> filterField;
    private List<String> returnField;
    private List<String> orderClause;
    private Item item;

    public String getItemType() {
        return item.getItemType();
    }

    public void putHashMapToWorkItem(HashMap<String, Object> content) throws WorkflowException {
        String methodName;
        String parameterTypeName;
        String key;
        Object hashValue;
        Method[] methodList;

        try {
            methodList = this.getClass().getMethods();

            for (Method method : methodList) {

                methodName = method.getName();
                if (methodName.indexOf("set") == 0) {

                    if (!methodName.equals("setItem")) {

                        parameterTypeName = method.getParameterTypes()[0].getName();
                        key = methodName.substring(3, 4).toString().toLowerCase() + methodName.substring(4);

                        hashValue = content.get(key);
                        
                        if(hashValue == null) continue;
                        
                        if (parameterTypeName.equals("boolean")) {
                            if (content.get(key) != null) {
                                method.invoke(this, Boolean.valueOf(hashValue.toString()));
                            }
                        } else if (parameterTypeName.equals("java.lang.String")) {
                            method.invoke(this, hashValue.toString());
                        }

                    }

                }
            }

            item.putContentToWorkItem(content);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WorkflowException(ex);
        } finally {
            methodName = null;
            methodList = null;
        }
    }

    public WorkItem(Item item) {
        this.item = item;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the taskId
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * @return the processName
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * @param processName the processName to set
     */
    public void setProcessName(String processName) {
        this.processName = processName;
    }

    /**
     * @return the updateField
     */
    public HashMap<String, Object> getUpdateField() {
        return updateField;
    }

    /**
     * @param updateField the updateField to set
     */
    public void setUpdateField(HashMap<String, Object> updateField) {
        this.updateField = updateField;
    }

    /**
     * @return the filterField
     */
    public List<String> getFilterField() {
        return filterField;
    }

    /**
     * @param filterField the filterField to set
     */
    public void setFilterField(List<String> filterField) {
        this.filterField = filterField;
    }

    /**
     * @return the returnField
     */
    public List<String> getReturnField() {
        return returnField;
    }

    /**
     * @param returnField the returnField to set
     */
    public void setReturnField(List<String> returnField) {
        this.returnField = returnField;
    }

    /**
     * @return the orderClause
     */
    public List<String> getOrderClause() {
        return orderClause;
    }

    /**
     * @param orderClause the orderClause to set
     */
    public void setOrderClause(List<String> orderClause) {
        this.orderClause = orderClause;
    }

    /**
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * @return the processId
     */
    public String getProcessId() {
        return processId;
    }

    /**
     * @param processId the processId to set
     */
    public void setProcessId(String processId) {
        this.processId = processId;
    }

}
