/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.oracle;

import com.wittakarn.bpm.domain.Item;
import com.wittakarn.bpm.exception.BPMException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import lombok.ToString;

/**
 *
 * @author wittakarn
 */
@ToString
public class OracleItem implements Item {

    private static final long serialVersionUID = 1L;
    private static final String itemType = "com.wittakarn.bpm.oracle.OracleBPMImpl";

    private String taskDefinitionName;
    private String compositeInstanceId;
    private String compositeName;
    private String compositeVersion;
    private String state;			//	ready : claim : lock 
    private String assigneesId;
    private String assigneesType;
    private String owner;
    private String outcome;
    private String stepArrDtm;		//	create date
    private String stepClmDtm;		//	claim date
    private boolean admin;
    
    private List<String> taskDefinitionNames;
    
    public String getItemType(){
        return itemType;
    }

    public void putContentToWorkItem(HashMap<String, Object> content) throws BPMException {
        String methodName;
        String parameterTypeName;
        String key;
        Method[] methodList;

        try {

            methodList = OracleItem.class.getMethods();

            for (Method method : methodList) {

                methodName = method.getName();
                if (methodName.indexOf("set") == 0) {

                    parameterTypeName = method.getParameterTypes()[0].getName();
                    key = methodName.substring(3, 4).toString().toLowerCase() + methodName.substring(4);

                    if (parameterTypeName.equals("boolean")) {
                        if (content.get(key) != null) {
                            method.invoke(this, Boolean.valueOf(content.get(key).toString()));
                        }
                    } else {
                        method.invoke(this, content.get(key));
                    }

                }
            }

        } catch (Exception ex) {
            throw new BPMException(ex);
        } finally {
            methodName = null;
            methodList = null;
        }
    }

    /**
     * @return the taskDefinitionName
     */
    public String getTaskDefinitionName() {
        return taskDefinitionName;
    }

    /**
     * @param taskDefinitionName the taskDefinitionName to set
     */
    public void setTaskDefinitionName(String taskDefinitionName) {
        this.taskDefinitionName = taskDefinitionName;
    }

    /**
     * @return the compositeInstanceId
     */
    public String getCompositeInstanceId() {
        return compositeInstanceId;
    }

    /**
     * @param compositeInstanceId the compositeInstanceId to set
     */
    public void setCompositeInstanceId(String compositeInstanceId) {
        this.compositeInstanceId = compositeInstanceId;
    }

    /**
     * @return the compositeName
     */
    public String getCompositeName() {
        return compositeName;
    }

    /**
     * @param compositeName the compositeName to set
     */
    public void setCompositeName(String compositeName) {
        this.compositeName = compositeName;
    }

    /**
     * @return the compositeVersion
     */
    public String getCompositeVersion() {
        return compositeVersion;
    }

    /**
     * @param compositeVersion the compositeVersion to set
     */
    public void setCompositeVersion(String compositeVersion) {
        this.compositeVersion = compositeVersion;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the assigneesId
     */
    public String getAssigneesId() {
        return assigneesId;
    }

    /**
     * @param assigneesId the assigneesId to set
     */
    public void setAssigneesId(String assigneesId) {
        this.assigneesId = assigneesId;
    }

    /**
     * @return the assigneesType
     */
    public String getAssigneesType() {
        return assigneesType;
    }

    /**
     * @param assigneesType the assigneesType to set
     */
    public void setAssigneesType(String assigneesType) {
        this.assigneesType = assigneesType;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the outcome
     */
    public String getOutcome() {
        return outcome;
    }

    /**
     * @param outcome the outcome to set
     */
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    /**
     * @return the stepArrDtm
     */
    public String getStepArrDtm() {
        return stepArrDtm;
    }

    /**
     * @param stepArrDtm the stepArrDtm to set
     */
    public void setStepArrDtm(String stepArrDtm) {
        this.stepArrDtm = stepArrDtm;
    }

    /**
     * @return the stepClmDtm
     */
    public String getStepClmDtm() {
        return stepClmDtm;
    }

    /**
     * @param stepClmDtm the stepClmDtm to set
     */
    public void setStepClmDtm(String stepClmDtm) {
        this.stepClmDtm = stepClmDtm;
    }

    /**
     * @return the admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * @param admin the admin to set
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * @return the taskDefinitionNames
     */
    public List<String> getTaskDefinitionNames() {
        return taskDefinitionNames;
    }

    /**
     * @param taskDefinitionNames the taskDefinitionNames to set
     */
    public void setTaskDefinitionNames(List<String> taskDefinitionNames) {
        this.taskDefinitionNames = taskDefinitionNames;
    }
}
