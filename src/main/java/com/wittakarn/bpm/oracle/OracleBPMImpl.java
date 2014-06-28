/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wittakarn.bpm.oracle;

import com.wittakarn.bpm.BPM;
import com.wittakarn.bpm.constant.WorkflowConstant;
import com.wittakarn.bpm.domain.WorkItem;
import com.wittakarn.bpm.exception.WorkflowException;
import com.wittakarn.bpm.utils.DateUtils;
import com.wittakarn.bpm.utils.StringUtils;
import com.wittakarn.bpm.utils.WorkflowUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import oracle.bpel.services.workflow.IWorkflowConstants;
import oracle.bpel.services.workflow.client.IWorkflowServiceClient;
import oracle.bpel.services.workflow.query.ITaskQueryService;
import oracle.bpel.services.workflow.repos.Column;
import oracle.bpel.services.workflow.repos.Ordering;
import oracle.bpel.services.workflow.repos.Predicate;
import oracle.bpel.services.workflow.repos.TableConstants;
import oracle.bpel.services.workflow.task.ITaskService;
import oracle.bpel.services.workflow.task.model.IdentityTypeImpl;
import oracle.bpel.services.workflow.task.model.Task;
import oracle.bpel.services.workflow.verification.IWorkflowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Element;

/**
 *
 * @author wittakarn
 */
public class OracleBPMImpl implements BPM {

    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(OracleBPMImpl.class);

    private String keyword;
    private List<ITaskQueryService.OptionalInfo> optionalInfo;
    private ITaskQueryService.AssignmentFilter assignmentFilter;
    private Predicate predicate;
    private Ordering ordering;
    private HashMap<String, Column> hColumn;
    private List<String> displayColumns;
    private int totalRow;

    public OracleBPMImpl() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param processId (mandatory) is process id
     * @param taskDefinitionNames (mandatory) is a list of taskDefinitonName
     * @param userId (mandatory) is user id
     *
     * @return HashMap of taskName, value
     */
    public Object countTask(WorkItem workItem) throws WorkflowException {
        int countValue = 0;
        String taskName = null;
        String processId = null;
        List<String> taskNameList = null;
        OracleWrapper wfWrapper = null;
        HashMap<Object, Object> hashMap = null;
        IWorkflowServiceClient wfsClient = null;
        IWorkflowContext wfCtx = null;
        OracleItem oracleItem = null;
        try {
            logger.debug("countTask Begin");
            processId = workItem.getProcessId();
            
            oracleItem = (OracleItem) workItem.getItem();
            taskNameList = oracleItem.getTaskDefinitionNames();
            
            wfWrapper = new OracleWrapper();
            wfsClient = wfWrapper.getWorkflowServiceClient();
            wfCtx = wfWrapper.authenticate(wfsClient, workItem.getUserId(), workItem.getPassword());

            keyword = null;

            optionalInfo = new ArrayList<ITaskQueryService.OptionalInfo>();
            optionalInfo.add(ITaskQueryService.OptionalInfo.ALL_ACTIONS);

            hashMap = new HashMap<Object, Object>();

            for (int index = 0; index < taskNameList.size(); index++) {
                taskName = taskNameList.get(index);

                assignmentFilter = ITaskQueryService.AssignmentFilter.MY_AND_GROUP;

                logger.debug("processId:taskName is : " + processId + ":" + taskName);

                this.setPredicate(processId, taskName, null);

                countValue = wfWrapper.countTasks(wfCtx, wfsClient,
                        optionalInfo, assignmentFilter, keyword, predicate);

                hashMap.put(taskName, countValue);
            }

            logger.debug("countTask End");

            return hashMap;
        } catch (Exception e) {
            throw new WorkflowException(e);
        } finally {
            taskName = null;
            wfWrapper = null;
        }
    }

    /**
     * @param admin (mandatory) is type of admin if true is admin else is not
     * admin
     * @param processId (mandatory) is process id
     * @param userId (mandatory) is user id
     * @param taskDefinitionName (mandatory) is taskId
     * @param filterField (optional) is a list of criteria
     * @param orderClause (optional) is a list of order by
     * @param returnField (optional) is a list of column data follow require
     * screen
     *
     * @return List of HashMap of individual column data
     */
    public Object searchTask(WorkItem workItem) throws WorkflowException {
        logger.info("searchTask Start.");
        boolean isAdmin;
        String processId;
        String taskName;
        List<String> filterField;
        List<String> returnField;
        List<String> orderClause;
        List<Task> taskList;
        OracleWrapper wfWrapper = null;
        IWorkflowServiceClient wfsClient = null;
        IWorkflowContext wfCtx = null;
        OracleItem oracleItem;
        try {
            oracleItem = (OracleItem) workItem.getItem();
            isAdmin = oracleItem.isAdmin();
            processId = workItem.getProcessId();
            taskName = oracleItem.getTaskDefinitionName();
            filterField = workItem.getFilterField();
            returnField = workItem.getReturnField();
            orderClause = workItem.getOrderClause();

            wfWrapper = new OracleWrapper();
            wfsClient = wfWrapper.getWorkflowServiceClient();
            wfCtx = wfWrapper.authenticate(wfsClient, workItem.getUserId(), workItem.getPassword());
//             Waiting for resolving.
//            initColumn((HashMap<String, Column>) workItem.getItemType().getSpecialType1());

            keyword = null;

            optionalInfo = new ArrayList<ITaskQueryService.OptionalInfo>();
            optionalInfo.add(ITaskQueryService.OptionalInfo.ALL_ACTIONS);

            if (isAdmin) {
                assignmentFilter = ITaskQueryService.AssignmentFilter.OWNER;
            } else {
                assignmentFilter = ITaskQueryService.AssignmentFilter.MY_AND_GROUP;
            }

            this.setPredicate(processId, taskName, filterField);
            logger.debug("predicate.getString() = " + predicate.getString());

            this.setOrdering(orderClause);
            totalRow = wfsClient.getTaskQueryService().countTasks(wfCtx, assignmentFilter, keyword, predicate);
            System.out.println("totalRow = " + totalRow);
            taskList = wfsClient.getTaskQueryService().queryTasks(wfCtx, displayColumns, optionalInfo, assignmentFilter, keyword, predicate, ordering, 0, totalRow);

            return prepareReturnField(wfsClient, wfCtx, taskList, returnField);
        } catch (Exception e) {
            throw new WorkflowException(e);
        } finally {
            taskList = null;
            logger.info("searchTask End.");
        }
    }

    /**
     * @param taskId (mandatory) is task id
     * @param userId (mandatory) is user id
     * @param returnField (optional) is a list of column data follow require
     * screen
     *
     * @return HashMap<String, Object> of data
     */
    public Object searchTaskByTaskId(WorkItem item) throws WorkflowException {
        String userId = "";
        String taskId = "";
        List<String> returnField = null;
        IWorkflowServiceClient wfsClient = null;
        IWorkflowContext wfCtx = null;
        OracleWrapper wfWrapper = null;
        try {

            returnField = item.getReturnField();
            taskId = item.getTaskId();
            userId = item.getUserId();

            wfWrapper = new OracleWrapper();
            wfsClient = wfWrapper.getWorkflowServiceClient();
            wfCtx = wfWrapper.authenticate(wfsClient, userId, item.getPassword());

            return prepareReturnField(wfsClient, wfCtx, taskId, returnField);
        } catch (Exception e) {
            throw new WorkflowException(e);
        } finally {
            wfsClient = null;
            wfCtx = null;
            wfWrapper = null;
        }
    }

    /**
     * @param userId (mandatory) is user id
     * @param taskId (mandatory) is task id
     * @param updateField (optional) is hashMap<String, Object> of data for
     * update payload in workitem
     * @param returnField (optional) is a list of column data follow require
     * screen
     *
     * @return HashMap<String, Object> of data
     */
    public Object claimTask(WorkItem workItem) throws WorkflowException {
        HashMap<String, Object> hashMap = null;
        OracleItem oracleItem;
        try {
            oracleItem = (OracleItem) workItem.getItem();
            oracleItem.setStepClmDtm(DateUtils.getCurrentDateTimeString());
            if (workItem.getUpdateField() == null) {
                workItem.setUpdateField(new HashMap<String, Object>());
            }

            workItem.getUpdateField().put("stepClmDtm", DateUtils.getCurrentDateTimeString());

            updateTask(workItem);

            hashMap = (HashMap<String, Object>) searchTaskByTaskId(workItem);
            return hashMap;
        } catch (Exception e) {
            logger.error("claimTask WorkflowException.", e);
            throw new WorkflowException(e);
        } finally {
            hashMap = null;
        }
    }

    /**
     * @param userId (mandatory) is user id
     * @param taskId (mandatory) is task id
     * @param updateField (optional) is hashMap of data for update payload in
     * workitem
     * @param returnField (optional) is a list of column data follow require
     * screen
     *
     * @return List of HashMap of individual column data
     */
    public Object cancelClaimTask(WorkItem workItem) throws WorkflowException {
        HashMap<String, Object> hashMap = null;
        String taskId = "";
        OracleWrapper wfWrapper = null;
        IWorkflowServiceClient wfsClient = null;
        IWorkflowContext wfCtx = null;
        OracleItem oracleItem = null;
        try {
            oracleItem = (OracleItem) workItem.getItem();
            taskId = workItem.getTaskId();
            oracleItem.setStepClmDtm("");
            if (workItem.getUpdateField() == null) {
                workItem.setUpdateField(new HashMap<String, Object>());
            }
            workItem.getUpdateField().put("stepClmDtm", "");

            wfWrapper = new OracleWrapper();
            wfsClient = wfWrapper.getWorkflowServiceClient();
            wfCtx = wfWrapper.authenticate(wfsClient, workItem.getUserId(), workItem.getPassword());

            doUpdateTask(workItem, wfWrapper, wfsClient, wfCtx);
            wfWrapper.cancelClaimTask(wfsClient, wfCtx, taskId);

            hashMap = (HashMap<String, Object>) searchTaskByTaskId(workItem);

            return hashMap;
        } catch (Exception e) {
            logger.error("cancelClaimTask WorkflowException.", e);
            throw new WorkflowException(e);
        } finally {
            wfWrapper = null;
            wfsClient = null;
            wfCtx = null;
        }
    }

    /**
     * @param userId (mandatory) is user id
     * @param taskId (mandatory) is task id
     * @param updateField (optional) is hashMap of data for update payload in
     * workitem
     *
     * @return workItem
     */
    public Object updateTask(WorkItem workItem) throws WorkflowException {
        String userId = "";
        OracleWrapper wfWrapper = null;
        IWorkflowServiceClient wfsClient = null;
        IWorkflowContext wfCtx = null;
        try {
            userId = workItem.getUserId();

            wfWrapper = new OracleWrapper();
            wfsClient = wfWrapper.getWorkflowServiceClient();
            wfCtx = wfWrapper.authenticate(wfsClient, userId, workItem.getPassword());

            doUpdateTask(workItem, wfWrapper, wfsClient, wfCtx);

            return workItem;
        } catch (Exception e) {
            logger.error("claimTask WorkflowException.", e);
            throw new WorkflowException(e);
        } finally {
            wfWrapper = null;
            wfsClient = null;
            wfCtx = null;
        }
    }

    public Object updateTask(List<WorkItem> workItem) throws WorkflowException {
        String userId = "";
        String password = "";
        OracleWrapper wfWrapper = null;
        IWorkflowServiceClient wfsClient = null;
        IWorkflowContext wfCtx = null;
        try {

            if (workItem == null || workItem.size() == 0) {
                return null;
            }
            userId = workItem.get(0).getUserId();
            password = workItem.get(0).getPassword();

            wfWrapper = new OracleWrapper();
            wfsClient = wfWrapper.getWorkflowServiceClient();
            wfCtx = wfWrapper.authenticate(wfsClient, userId, password);

            doUpdateTask(workItem, wfWrapper, wfsClient, wfCtx);

            return workItem;
        } catch (Exception e) {
            logger.error("claimTask WorkflowException.", e);
            throw new WorkflowException(e);
        } finally {
            wfWrapper = null;
            wfsClient = null;
            wfCtx = null;
        }
    }

    /**
     * @param processId (mandatory) is process id
     * @param userId (mandatory) is user id
     * @param taskId (mandatory) is task id
     * @param updateField (optional) is hashMap of data for update payload in
     * workitem
     * @param outcome is outcome of workitem such as APPROVE, REJECT etc
     *
     * @return workItem
     */
    public Object completeTask(WorkItem workItem) throws WorkflowException {
        String userId = "";
        String password = "";
        OracleWrapper wfWrapper = null;
        IWorkflowServiceClient wfsClient = null;
        IWorkflowContext wfCtx = null;
        HashMap taskHashMap;
        try {

            logger.info("begin completeTask...");
            userId = workItem.getUserId();
            password = workItem.getPassword();

            wfWrapper = new OracleWrapper();
            wfsClient = wfWrapper.getWorkflowServiceClient();
            wfCtx = wfWrapper.authenticate(wfsClient, userId, password);

            taskHashMap = (HashMap) doCompleteTask(wfWrapper, wfsClient, wfCtx, workItem);

            logger.info("completeTask end...");
            return workItem;
        } catch (Exception e) {
            logger.error("completeTask WorkflowException.", e);
            throw new WorkflowException(e);
        } finally {
            wfWrapper = null;
            wfsClient = null;
        }
    }

    public Object completeTask(List<WorkItem> workItem) throws WorkflowException {
        OracleWrapper wfWrapper = null;
        IWorkflowServiceClient wfsClient = null;
        try {

            if (workItem == null || workItem.size() == 0) {
                return null;
            }

            logger.info("begin completeTask...");

            wfWrapper = new OracleWrapper();
            wfsClient = wfWrapper.getWorkflowServiceClient();

            doCompleteTask(wfWrapper, wfsClient, workItem);

            logger.info("completeTask end...");
            return workItem;
        } catch (Exception e) {
            logger.error("completeTask WorkflowException.", e);
            throw new WorkflowException(e);
        } finally {
            wfWrapper = null;
            wfsClient = null;
        }
    }

    private void setPredicate(Column column, List<String> dataList) throws WorkflowException {
        String value = "";
        Predicate subPredicate = null;
        try {
            if (column == null) {
                return;
            }

            logger.debug("dataList = " + dataList);

            if (dataList.size() == 1) {
                value = dataList.get(0);
                predicate.addClause(Predicate.AND, column, Predicate.OP_EQ, value);
            } else if (dataList.size() > 1) {
                if (dataList.get(1).equals(WorkflowConstant.BETWEEN)) {
                    predicate.addClause(Predicate.AND, column, Predicate.OP_GTE, dataList.get(0));
                    predicate.addClause(Predicate.AND, column, Predicate.OP_LTE, dataList.get(2));
                } else if (dataList.get(0).equals(WorkflowConstant.LIKE)) {
                    predicate.addClause(Predicate.AND, column, Predicate.OP_LIKE, "%".concat(dataList.get(1)).concat("%"));
                } else if (dataList.get(0).equals(WorkflowConstant.OR)) {
                    value = dataList.get(1);

                    subPredicate = new Predicate(column, Predicate.OP_EQ, value);

                    for (int index = 2; index < dataList.size(); index++) {
                        value = dataList.get(index);

                        subPredicate.addClause(Predicate.OR, column, Predicate.OP_EQ, value);
                    }

                    predicate = new Predicate(predicate, Predicate.AND, subPredicate);
                } else if (dataList.get(0).equals(WorkflowConstant.LE)) {
                    predicate.addClause(Predicate.AND, column, Predicate.OP_LTE, dataList.get(1));
                } else if (dataList.get(0).equals(WorkflowConstant.GE)) {
                    predicate.addClause(Predicate.AND, column, Predicate.OP_GTE, dataList.get(1));
                } else {
                    predicate.addClause(Predicate.AND, column, Predicate.OP_IN, dataList);
                }
            }

        } catch (Exception e) {
            throw new WorkflowException(e);
        } finally {
        }
    }

    private void setPredicate(String columnName, List<String> dataList)
            throws WorkflowException {
        Column column = null;
        try {
            column = hColumn.get(columnName);

            setPredicate(column, dataList);
        } finally {
            column = null;
        }
    }

    private void setPredicate(String processId, String taskName, List<String> filterField) throws WorkflowException {
        try {
            predicate = new Predicate(TableConstants.WFTASK_STATE_COLUMN, Predicate.OP_EQ, IWorkflowConstants.TASK_STATE_ASSIGNED);
            predicate.addClause(Predicate.AND, TableConstants.WFTASK_PROCESSID_COLUMN, Predicate.OP_EQ, processId);
            predicate.addClause(Predicate.AND, TableConstants.WFTASK_TASKDEFINITIONNAME_COLUMN, Predicate.OP_EQ, taskName);

            // this.setPredicate(TableConstants.WFTASK_TASKDEFINITIONNAME_COLUMN,
            // split(taskName, ";"));
            if (filterField != null) {
                this.setPredicateFilterField(filterField);
            }
        } catch (Exception ex) {
            throw new WorkflowException(ex);
        }
    }

    private void setPredicateFilterField(List<String> filterField) throws WorkflowException {
        String columnName = "";
        List<String> dataList = null;
        try {

            for (int index = 0; index < filterField.size(); index++) {
                dataList = split(filterField.get(index), ":");
                columnName = dataList.get(0);
                dataList.remove(0);

                this.setPredicate(columnName, dataList);
            }
        } catch (Exception ex) {
            throw new WorkflowException(ex);
        } finally {
            dataList = null;
        }
    }

    private void setOrdering(List<String> orders) throws WorkflowException {
        boolean isAscending = false;
        String columnName = "";
        Column column = null;
        List<String> orderClauseList = null;
        List<String> orderList = null;
        try {

            if (orders == null) {
                isAscending = true;

                System.out.println("Set default order.");

                ordering = new Ordering(
                        TableConstants.WFTASK_CREATEDDATE_COLUMN, isAscending,
                        true);
                ordering.addClause(TableConstants.WFTASK_ASSIGNEDDATE_COLUMN,
                        isAscending, true);
            } else {
                orderList = orders;
                for (int index = 0; index < orderList.size(); index++) {
                    String orderClause = orderList.get(index);

                    orderClauseList = this.split(orderClause, " ");

                    columnName = orderClauseList.get(0);

                    isAscending = true;
                    if (orderClauseList.size() > 1) {
                        if (orderClauseList.get(1).equals("DESC")) {
                            isAscending = false;
                        }
                    }

                    column = hColumn.get(columnName);
                    if (column == null) {
                        continue;
                    }

                    if (ordering == null) {
                        ordering = new Ordering(column, isAscending, true);
                    } else {
                        ordering.addClause(column, isAscending, true);
                    }
                }
            }
        } catch (Exception e) {
            throw new WorkflowException(e);
        } finally {
            column = null;
            orderClauseList = null;
            orderList = null;
        }
    }

    private void initColumn(HashMap<String, Column> columns) {
        try {
            hColumn = new HashMap<String, Column>();
            hColumn.put(TableConstants.WFTASK_ISGROUP_COLUMN.getName(), TableConstants.WFTASK_ISGROUP_COLUMN);
            hColumn.put(TableConstants.WFTASK_ASSIGNEES_COLUMN.getName(), TableConstants.WFTASK_ASSIGNEES_COLUMN);
            hColumn.putAll(columns);

            displayColumns = new ArrayList<String>();
            displayColumns.add(TableConstants.WFTASK_TASKID_COLUMN.getName());
            displayColumns.add(TableConstants.WFTASK_TASKNUMBER_COLUMN.getName());
            displayColumns.add(TableConstants.WFTASK_ACTIVITYID_COLUMN.getName());
            displayColumns.add(TableConstants.WFTASK_ACTIVITYNAME_COLUMN.getName());
            displayColumns.add(TableConstants.WFTASK_TITLE_COLUMN.getName());
        } finally {
        }
    }

    @Override
    public Object initialTask(WorkItem workItem) throws WorkflowException {
        try {
//            WorkflowUtils.chooseProcessId(workItem);

            return "Initial task success.";
        } finally {
        }
    }

    private void doUpdateTask(WorkItem workItem,
            OracleWrapper wfWrapper, IWorkflowServiceClient wfsClient,
            IWorkflowContext wfCtx) throws Exception {
        String taskId = "";
        HashMap<String, Object> updateHashMapField = null;
        Task task = null;
        try {
            updateHashMapField = workItem.getUpdateField();
            taskId = workItem.getTaskId();

            task = wfWrapper.getTaskDetailsById(wfsClient, wfCtx, taskId);
            task = updateTaskPayload(task, updateHashMapField);

            wfWrapper.updateTask(wfsClient, wfCtx, task);
        } finally {
            task = null;
        }
    }

    private void doUpdateTask(List<WorkItem> workList,
            OracleWrapper wfWrapper, IWorkflowServiceClient wfsClient,
            IWorkflowContext wfCtx) throws Exception {
        String taskId = "";
        HashMap<String, Object> updateHashMapField = null;
        Task task = null;
        ITaskService taskSrv = null;

        try {

            taskSrv = wfsClient.getTaskService();
            for (Iterator<?> iterator = workList.iterator(); iterator.hasNext();) {

                WorkItem workItem = (WorkItem) iterator.next();

                updateHashMapField = workItem.getUpdateField();
                taskId = workItem.getTaskId();

                task = wfWrapper.getTaskDetailsById(wfsClient, wfCtx, taskId);
                task = updateTaskPayload(task, updateHashMapField);

                taskSrv.updateTask(wfCtx, task);
                logger.debug("update taskId id " + taskId + " complete");
                // throw new Exception("throw threw thrown");

            }
        } finally {
            task = null;
        }
    }

    private Task updateTaskPayload(Task task,
            HashMap<String, Object> updateHashMapField) throws Exception {
        String field = "";
        String fieldValue = "";
        Element dataElement = null;
        try {
            if (task == null) {
                throw new Exception("Task not found.");
            }

            logger.info("Task id = " + task.getSystemAttributes().getTaskId());
            logger.info("Task Number = "
                    + task.getSystemAttributes().getTaskNumber());

            dataElement = task.getPayloadAsElement();
            for (Object key : updateHashMapField.keySet()) {
                Object value = updateHashMapField.get(key);

                field = key.toString();
                if (value != null) {
                    fieldValue = value.toString();
                }

                if (field.equals("outcome")) {
                    continue;
                }
                if (dataElement.getElementsByTagName(field) == null) {
                    logger.error("Field not found in Payload. [" + field + "]");
                    continue;
                }
                if (dataElement.getElementsByTagName(field).item(0) == null) {
                    logger.error("Field not found in Payload item(0). ["
                            + field + "]");
                    continue;
                }
                logger.info("Update task payload "
                        + field
                        + " from "
                        + dataElement.getElementsByTagName(field).item(0)
                        .getTextContent() + " to " + fieldValue);
                dataElement.getElementsByTagName(field).item(0)
                        .setTextContent(fieldValue);
            }

            task.setPayloadAsElement(dataElement);
            return task;
        } finally {

        }
    }

    private HashMap<String, Object> prepareReturnField(
            IWorkflowServiceClient wfSvcClient, IWorkflowContext wfCtx,
            String taskId, List<String> returnField) throws Exception {
        HashMap<String, Object> hashDataReturn;
        Element dataElement = null;
        IdentityTypeImpl identityType = null;
        String acquiredBy = "";
        String state = "";
        String substate = "";
        boolean isGroup = false;

        try {

            Task task = wfSvcClient.getTaskQueryService().getTaskDetailsById(
                    wfCtx, taskId);
            dataElement = task.getPayloadAsElement();
            System.out.println("dataElement= " + dataElement);
            hashDataReturn = new HashMap<String, Object>();

            if (returnField != null) {

                System.out.println("returnField.size()= " + returnField.size());

                for (Iterator<String> iterator2 = returnField.iterator(); iterator2
                        .hasNext();) {
                    String field = (String) iterator2.next();
                    if ("taskId,taskNumber,taskName,state,owner,stepClmDtm,escalated,stepArrDtm"
                            .indexOf(field) >= 0) {
                        continue;
                    }

                    // System.out.println("payload field= " + field);
                    hashDataReturn.put(field, getData(dataElement, field));
                }
            }

            hashDataReturn
                    .put("taskId", task.getSystemAttributes().getTaskId());
            hashDataReturn.put("taskNumber", task.getSystemAttributes()
                    .getTaskNumber());
            hashDataReturn.put("taskDefinitionName", task.getSystemAttributes()
                    .getTaskDefinitionName());
            hashDataReturn.put("compositeInstanceId", task.getSca()
                    .getCompositeInstanceId());
            hashDataReturn.put("compositeName", task.getSca()
                    .getCompositeName());
            hashDataReturn.put("compositeVersion", task.getSca()
                    .getCompositeVersion());

            // logger.info("taskName => " +
            // task.getSystemAttributes().getTaskDefinitionName());
            hashDataReturn.put("taskName", task.getSystemAttributes()
                    .getTaskDefinitionName());

            // substatePredicate = new
            // Predicate(TableConstants.WFTASK_SUBSTATE_COLUMN,
            // Predicate.OP_IS_NULL, "");
            // substatePredicate.addClause(Predicate.OR,
            // TableConstants.WFTASK_SUBSTATE_COLUMN, Predicate.OP_EQ,
            // IWorkflowConstants.TASK_SUBSTATE_ACQUIRED);
            acquiredBy = StringUtils.trim(task.getSystemAttributes()
                    .getAcquiredBy());
            state = StringUtils.trim(task.getSystemAttributes().getState());
            substate = StringUtils.trim(task.getSystemAttributes()
                    .getSubstate());

            isGroup = task.getSystemAttributes().isIsGroup();

            System.out.println("[TaskId="
                    + task.getSystemAttributes().getTaskId() + "TaskNumber => "
                    + task.getSystemAttributes().getTaskNumber() + ","
                    + "TaskDefinitionName => "
                    + task.getSystemAttributes().getTaskDefinitionName() + ","
                    + "CompositeInstanceId => "
                    + task.getSca().getCompositeInstanceId() + ","
                    + "CompositeName => " + task.getSca().getCompositeName()
                    + "," + "CompositeVersion => "
                    + task.getSca().getCompositeVersion() + "," + "IsGroup: "
                    + isGroup + "," + "AcquiredBy: " + acquiredBy + ","
                    + "State: " + state + "," + "Substate: " + substate);

            if (!task.getSystemAttributes().getAssignees().isEmpty()) {
                identityType = (IdentityTypeImpl) task.getSystemAttributes()
                        .getAssignees().get(0);

                hashDataReturn.put("assigneesId", identityType.getId());
                hashDataReturn.put("assigneesType", identityType.getType());
            }

            if (!isGroup) {
                System.out.println("state => STATE_CLAIMED isGroup = "
                        + isGroup);

                identityType = (IdentityTypeImpl) task.getSystemAttributes()
                        .getAssignees().get(0);

                hashDataReturn.put("state", "STATE_CLAIMED");
                hashDataReturn.put("owner", identityType.getId().toUpperCase());
                hashDataReturn.put("stepClmDtm", DateUtils
                        .toStringDateTime(task.getSystemAttributes()
                                .getAssignedDate()));
            } else if (substate
                    .equals(IWorkflowConstants.TASK_SUBSTATE_ACQUIRED)) {
                System.out
                        .println("state => STATE_CLAIMED Substate = ACQUIRED");
                hashDataReturn.put("state", "STATE_CLAIMED");
                hashDataReturn.put("owner", acquiredBy);
                if (dataElement.getElementsByTagName("stepClmDtm").item(0) != null) {
                    hashDataReturn.put("stepClmDtm", dataElement
                            .getElementsByTagName("stepClmDtm").item(0)
                            .getTextContent());
                }
            } else {
                System.out.println("state => STATE_READY");
                hashDataReturn.put("state", "STATE_READY");
                hashDataReturn.put("owner", "");
                hashDataReturn.put("stepClmDtm", "");
            }

            // task.getSystemAttributes().getExpirationDate()
            hashDataReturn.put("escalated", "0"); // ๏ฟฝาน๏ฟฝ๏ฟฝาช๏ฟฝ๏ฟฝ๏ฟฝ๏ฟฝ๏ฟฝ๏ฟฝ๏ฟฝัง
            // 1=๏ฟฝ๏ฟฝาช๏ฟฝ๏ฟฝ/0=๏ฟฝัง๏ฟฝ๏ฟฝ๏ฟฝ๏ฟฝ๏ฟฝาช๏ฟฝ๏ฟฝ

            // stepArrDtm
            hashDataReturn.put("stepArrDtm", WorkflowUtils
                    .convertCalendarToStringDateTime(task.getSystemAttributes()
                            .getAssignedDate()));

            return hashDataReturn;
        } catch (Exception ex) {
            throw ex;
        } finally {
            hashDataReturn = null;
            dataElement = null;
            identityType = null;
            acquiredBy = null;
            state = null;
            substate = null;
        }
    }

    private List<HashMap<String, Object>> prepareReturnField(
            IWorkflowServiceClient wfSvcClient, IWorkflowContext wfCtx,
            List<Task> taskList, List<String> returnField) throws Exception {
        List<HashMap<String, Object>> hashList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> hashDataReturn;
        Element dataElement = null;
        IdentityTypeImpl identityType = null;
        String acquiredBy = "";
        String state = "";
        String substate = "";
        boolean isGroup = false;

        try {
            for (Iterator<Task> iterator = taskList.iterator(); iterator
                    .hasNext();) {
                Task task = (Task) iterator.next();

                task = wfSvcClient.getTaskQueryService().getTaskDetailsById(
                        wfCtx, task.getSystemAttributes().getTaskId());
                dataElement = task.getPayloadAsElement();
                hashDataReturn = new HashMap<String, Object>();

                if (returnField != null) {
                    for (Iterator<String> iterator2 = returnField.iterator(); iterator2
                            .hasNext();) {
                        String field = (String) iterator2.next();
                        if ("taskId,taskNumber,taskName,state,owner,stepClmDtm,escalated,stepArrDtm"
                                .indexOf(field) >= 0) {
                            continue;
                        }

                        // System.out.println("payload field= " + field);
                        hashDataReturn.put(field, getData(dataElement, field));
                    }
                }

                hashDataReturn.put("taskId", task.getSystemAttributes()
                        .getTaskId());
                hashDataReturn.put("taskNumber", task.getSystemAttributes()
                        .getTaskNumber());
                hashDataReturn.put("taskDefinitionName", task
                        .getSystemAttributes().getTaskDefinitionName());
                hashDataReturn.put("compositeInstanceId", task.getSca()
                        .getCompositeInstanceId());
                hashDataReturn.put("compositeName", task.getSca()
                        .getCompositeName());
                hashDataReturn.put("compositeVersion", task.getSca()
                        .getCompositeVersion());

                // logger.info("taskName => " +
                // task.getSystemAttributes().getTaskDefinitionName());
                hashDataReturn.put("taskName", task.getSystemAttributes()
                        .getTaskDefinitionName());

                // substatePredicate = new
                // Predicate(TableConstants.WFTASK_SUBSTATE_COLUMN,
                // Predicate.OP_IS_NULL, "");
                // substatePredicate.addClause(Predicate.OR,
                // TableConstants.WFTASK_SUBSTATE_COLUMN, Predicate.OP_EQ,
                // IWorkflowConstants.TASK_SUBSTATE_ACQUIRED);
                acquiredBy = StringUtils.trim(task.getSystemAttributes()
                        .getAcquiredBy());
                state = StringUtils.trim(task.getSystemAttributes().getState());
                substate = StringUtils.trim(task.getSystemAttributes()
                        .getSubstate());

                isGroup = task.getSystemAttributes().isIsGroup();

                System.out.println("[TaskId="
                        + task.getSystemAttributes().getTaskId()
                        + "TaskNumber => "
                        + task.getSystemAttributes().getTaskNumber() + ","
                        + "TaskDefinitionName => "
                        + task.getSystemAttributes().getTaskDefinitionName()
                        + "," + "CompositeInstanceId => "
                        + task.getSca().getCompositeInstanceId() + ","
                        + "CompositeName => "
                        + task.getSca().getCompositeName() + ","
                        + "CompositeVersion => "
                        + task.getSca().getCompositeVersion() + ","
                        + "IsGroup: " + isGroup + "," + "AcquiredBy: "
                        + acquiredBy + "," + "State: " + state + ","
                        + "Substate: " + substate);

                if (!task.getSystemAttributes().getAssignees().isEmpty()) {
                    identityType = (IdentityTypeImpl) task
                            .getSystemAttributes().getAssignees().get(0);

                    hashDataReturn.put("assigneesId", identityType.getId());
                    hashDataReturn.put("assigneesType", identityType.getType());
                }

                if (!isGroup) {
                    System.out.println("state => STATE_CLAIMED isGroup = "
                            + isGroup);

                    identityType = (IdentityTypeImpl) task
                            .getSystemAttributes().getAssignees().get(0);

                    hashDataReturn.put("state", "STATE_CLAIMED");
                    hashDataReturn.put("owner", identityType.getId()
                            .toUpperCase());
                    hashDataReturn.put("stepClmDtm", DateUtils
                            .toStringDateTime(task.getSystemAttributes()
                                    .getAssignedDate()));
                } else if (substate
                        .equals(IWorkflowConstants.TASK_SUBSTATE_ACQUIRED)) {
                    System.out
                            .println("state => STATE_CLAIMED Substate = ACQUIRED");
                    hashDataReturn.put("state", "STATE_CLAIMED");
                    hashDataReturn.put("owner", acquiredBy);
                    if (dataElement.getElementsByTagName("stepClmDtm").item(0) != null) {
                        hashDataReturn.put("stepClmDtm", dataElement
                                .getElementsByTagName("stepClmDtm").item(0)
                                .getTextContent());
                    }
                } else {
                    System.out.println("state => STATE_READY");
                    hashDataReturn.put("state", "STATE_READY");
                    hashDataReturn.put("owner", "");
                    hashDataReturn.put("stepClmDtm", "");
                }

                // task.getSystemAttributes().getExpirationDate()
                hashDataReturn.put("escalated", "0"); // ๏ฟฝาน๏ฟฝ๏ฟฝาช๏ฟฝ๏ฟฝ๏ฟฝ๏ฟฝ๏ฟฝ๏ฟฝ๏ฟฝัง
                // 1=๏ฟฝ๏ฟฝาช๏ฟฝ๏ฟฝ/0=๏ฟฝัง๏ฟฝ๏ฟฝ๏ฟฝ๏ฟฝ๏ฟฝาช๏ฟฝ๏ฟฝ

                // stepArrDtm
                hashDataReturn.put("stepArrDtm", WorkflowUtils
                        .convertCalendarToStringDateTime(task
                                .getSystemAttributes().getAssignedDate()));

                // logger.info("getXmlObject end.");
                hashList.add(hashDataReturn);

            }

            return hashList;
        } catch (Exception ex) {
            throw ex;
        } finally {
            hashDataReturn = null;
            dataElement = null;
            identityType = null;
            acquiredBy = null;
            state = null;
            substate = null;
        }
    }

    private Object doCompleteTask(OracleWrapper wfWrapper,
            IWorkflowServiceClient wfsClient, IWorkflowContext wfCtx,
            WorkItem workItem) throws Exception {
        String taskId = "";
        String outcome = "";
        Task task = null;
        HashMap<String, Object> hashMapTaskDetail = null;
        OracleItem oracleItem = null;
        try {
            oracleItem = (OracleItem) workItem.getItem();
            oracleItem.setStepClmDtm("");
            if (workItem.getUpdateField() == null) {
                workItem.setUpdateField(new HashMap<String, Object>());
            }
            workItem.getUpdateField().put("stepClmDtm", "");
            taskId = workItem.getTaskId();
            outcome = oracleItem.getOutcome();

            System.out.println("outcome= " + outcome);

            task = wfWrapper.getTaskDetailsById(wfsClient, wfCtx, taskId);
            task = updateTaskPayload(task, workItem.getUpdateField());

            hashMapTaskDetail = new HashMap<String, Object>();
            hashMapTaskDetail.put("taskId", taskId);
            hashMapTaskDetail.put("taskNumber", task.getSystemAttributes()
                    .getTaskNumber());
            hashMapTaskDetail.put("taskDefinitionName", task
                    .getSystemAttributes().getTaskDefinitionName());
            hashMapTaskDetail.put("compositeInstanceId", task.getSca()
                    .getCompositeInstanceId());
            hashMapTaskDetail.put("compositeName", task.getSca()
                    .getCompositeName());
            hashMapTaskDetail.put("compositeVersion", task.getSca()
                    .getCompositeVersion());

            wfWrapper.completeTask(wfsClient, wfCtx, task, outcome);

            return hashMapTaskDetail;
        } finally {
            task = null;
        }
    }

    private Object doCompleteTask(OracleWrapper wfWrapper,
            IWorkflowServiceClient wfsClient, List<WorkItem> item)
            throws Exception {
        String taskId = "";
        String outcome = "";
        Task task = null;
        HashMap<String, Object> hashMapTaskDetail = null;
        ITaskService taskService = null;
        IWorkflowContext wfCtx = null;
        
        try {
            
            for (Iterator<?> iterator = item.iterator(); iterator.hasNext();) {
                WorkItem workItem = (WorkItem) iterator.next();
                OracleItem oracleItem = (OracleItem) workItem.getItem();

                System.out.println("workItem.getUserId()= "
                        + workItem.getUserId());
                wfCtx = wfWrapper.authenticate(wfsClient, workItem.getUserId(), workItem.getPassword());
                taskService = wfsClient.getTaskService();
                oracleItem.setStepClmDtm("");
                if (workItem.getUpdateField() == null) {
                    workItem.setUpdateField(new HashMap<String, Object>());
                }
                workItem.getUpdateField().put("stepClmDtm", "");
                taskId = workItem.getTaskId();
                outcome = oracleItem.getOutcome();

                System.out.println("outcome= " + outcome);

                task = wfWrapper.getTaskDetailsById(wfsClient, wfCtx, taskId);
                task = updateTaskPayload(task, workItem.getUpdateField());

                hashMapTaskDetail = new HashMap<String, Object>();
                hashMapTaskDetail.put("taskId", taskId);
                hashMapTaskDetail.put("taskNumber", task.getSystemAttributes()
                        .getTaskNumber());
                hashMapTaskDetail.put("taskDefinitionName", task
                        .getSystemAttributes().getTaskDefinitionName());
                hashMapTaskDetail.put("compositeInstanceId", task.getSca()
                        .getCompositeInstanceId());
                hashMapTaskDetail.put("compositeName", task.getSca()
                        .getCompositeName());
                hashMapTaskDetail.put("compositeVersion", task.getSca()
                        .getCompositeVersion());

                taskService.updateTaskOutcome(wfCtx, task, outcome);
                System.out.println("update item 1 complete");
            }

            return hashMapTaskDetail;
        } finally {
            task = null;
        }
    }

    private static String getData(Element dataElement, String tagName) {
        try {
            return dataElement.getElementsByTagName(tagName).item(0)
                    .getTextContent();
        } catch (Exception e) {
            return "";
        } finally {
        }
    }

    private List<String> split(String strValue, String regex) {
        List<String> dataList = null;
        String[] dataSpit = null;
        try {

            dataList = new Vector<String>();
            dataSpit = strValue.split(regex);
            for (int index = 0; index < dataSpit.length; index++) {
                if (!dataSpit[index].equals("")) {
                    dataList.add(StringUtils.trim(dataSpit[index]));
                }
            }

            return dataList;
        } finally {
            dataList = null;
            dataSpit = null;
        }
    }

}
