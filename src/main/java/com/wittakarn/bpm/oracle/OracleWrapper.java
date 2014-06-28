/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wittakarn.bpm.oracle;

import com.wittakarn.bpm.config.WorkflowConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.bpel.services.workflow.IWorkflowConstants;
import oracle.bpel.services.workflow.StaleObjectException;
import oracle.bpel.services.workflow.WorkflowException;
import oracle.bpel.services.workflow.client.IWorkflowServiceClient;
import oracle.bpel.services.workflow.client.IWorkflowServiceClientConstants;
import oracle.bpel.services.workflow.client.WorkflowServiceClientFactory;
import oracle.bpel.services.workflow.query.ITaskQueryService;
import oracle.bpel.services.workflow.query.ITaskQueryService.AssignmentFilter;
import oracle.bpel.services.workflow.repos.Ordering;
import oracle.bpel.services.workflow.repos.Predicate;
import oracle.bpel.services.workflow.task.ITaskService;
import oracle.bpel.services.workflow.task.impl.TaskAssignee;
import oracle.bpel.services.workflow.task.model.Task;
import oracle.bpel.services.workflow.verification.IWorkflowContext;
import oracle.bpm.client.BPMServiceClientFactory;

/**
 *
 * @author Wittakarn
 */
public class OracleWrapper {
    private static String ejbProviderUrl = WorkflowConfig.getString(WorkflowConfig.EJB_URL);
    private static String password = WorkflowConfig.getString(WorkflowConfig.WLS_PASSWORD);
    private static String protocol = WorkflowServiceClientFactory.REMOTE_CLIENT;

    public OracleWrapper() {}
    
    public OracleWrapper(String ejbProviderUrl, String password, String protocol) {
    	OracleWrapper.ejbProviderUrl = ejbProviderUrl;
    	OracleWrapper.password = password;
    	OracleWrapper.protocol = protocol;
    }

    /**
     * Get a workflow service client to which the connection properties are
     * passed dynamically
     *
     * Examples of the parameters are: String clientType =
     * WorkflowServiceClientFactory.REMOTE_CLIENT or
     * WorkflowServiceClientFactory.SOAP_CLIENT; Logger logger => logger object.
     */
    public IWorkflowServiceClient getWorkflowServiceClient() {
        Map<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String> properties = null;
        try {
            // create a map of properties
            properties = new HashMap<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String>();

            // populate the properties
            properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_PROVIDER_URL, ejbProviderUrl);
//          properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
//          properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_JNDI_SUFFIX, "");

//	        properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_SECURITY_PRINCIPAL, "cross-domain");
//	        properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_SECURITY_CREDENTIALS, "oracle123");
//	        properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.CLIENT_TYPE, "REMOTE");
            properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.TASK_SERVICE_PARTICIPATE_IN_CLIENT_TRANSACTION, "true");
            // get the client
            return WorkflowServiceClientFactory.getWorkflowServiceClient(protocol, properties, null);
        } finally {
            properties = null;
        }
    }

    public IWorkflowServiceClient getWorkflowServiceClientForTest() {
        Map<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String> properties = null;
        try {
            // create a map of properties
            properties = new HashMap<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String>();

            // populate the properties
            properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_PROVIDER_URL, "t3://naccbpm:7003");
            properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.TASK_SERVICE_PARTICIPATE_IN_CLIENT_TRANSACTION, "true");
            // get the client
            return WorkflowServiceClientFactory.getWorkflowServiceClient(protocol, properties, null);
        } finally {
            properties = null;
        }
    }

    public BPMServiceClientFactory getBPMServiceClientFactoryForTest() {
        Map<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String> properties = null;
        try {
            // create a map of properties
            properties = new HashMap<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String>();

            // populate the properties
            properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_PROVIDER_URL, "t3://172.17.2.178:7003");
            properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.TASK_SERVICE_PARTICIPATE_IN_CLIENT_TRANSACTION, "true");
            // get the client
            return BPMServiceClientFactory.getInstance(properties, null, null);
        } finally {
            properties = null;
        }
    }
    
    public IWorkflowContext authenticate(IWorkflowServiceClient wfsClient, String userId, String password) throws WorkflowException {
        ITaskQueryService querySvc = null;
        IWorkflowContext wfCtx = null;
        try {
        	
        	if(password == null || password.equals("")){
        		return authenticate(wfsClient, userId);
        	}
        	
            querySvc = wfsClient.getTaskQueryService();
//            OracleWrapper.log.debug("real password : " + password);
            wfCtx = querySvc.authenticate(userId, password.toCharArray(), "jazn.com");
            return wfCtx;
        } finally {
            wfsClient = null;
            querySvc = null;
            wfCtx = null;
        }
    }

    private IWorkflowContext authenticate(IWorkflowServiceClient wfsClient, String userId) throws WorkflowException {
        ITaskQueryService querySvc = null;
        IWorkflowContext wfCtx = null;
        try {
            querySvc = wfsClient.getTaskQueryService();
//            OracleWrapper.log.debug("fix password : " + password);
            wfCtx = querySvc.authenticate(userId, OracleWrapper.password.toCharArray(), "jazn.com");
            return wfCtx;
        } finally {
            wfsClient = null;
            querySvc = null;
            wfCtx = null;
        }
    }

    public List<Task> queryTasks(IWorkflowContext wfCtx, IWorkflowServiceClient wfsClient, List<String> displayColumns,
            List<ITaskQueryService.OptionalInfo> optionalInfo, AssignmentFilter assignmentFilter,
            String keyword, Predicate predicate, Ordering ordering, int startRow, int endRow) throws WorkflowException {
        ITaskQueryService querySvc = null;
        try {
            querySvc = wfsClient.getTaskQueryService();
            return querySvc.queryTasks(wfCtx, displayColumns, optionalInfo, assignmentFilter,
                    keyword, predicate, ordering, startRow, endRow);
        } finally {
            querySvc = null;
        }
    }

    public int countTasks(IWorkflowContext wfCtx, IWorkflowServiceClient wfsClient,
            List<ITaskQueryService.OptionalInfo> optionalInfo, AssignmentFilter assignmentFilter,
            String keyword, Predicate predicate) throws WorkflowException {
        ITaskQueryService querySvc = null;
        try {
            querySvc = wfsClient.getTaskQueryService();
            return querySvc.countTasks(wfCtx, assignmentFilter, keyword, predicate);
        } finally {
            querySvc = null;
        }
    }

    public Task getTaskDetailsById(IWorkflowServiceClient wfsClient, IWorkflowContext wfCtx, String taskId) throws WorkflowException {
        ITaskQueryService querySvc = null;
        try {
            querySvc = wfsClient.getTaskQueryService();
            return querySvc.getTaskDetailsById(wfCtx, taskId);
        } finally {
            querySvc = null;
        }
    }

    public void initialTask(IWorkflowServiceClient wfsClient, Task task) throws StaleObjectException, WorkflowException {
        ITaskService taskSrv = null;
        try {
            taskSrv = wfsClient.getTaskService();
            taskSrv.initiateTask(task);
        } finally {
            taskSrv = null;
        }
    }

    public void completeTask(IWorkflowServiceClient wfsClient, IWorkflowContext wfCtx, Task task, String outcome) throws StaleObjectException, WorkflowException {
        ITaskService taskSrv = null;
        try {
            taskSrv = wfsClient.getTaskService();
            taskSrv.updateTaskOutcome(wfCtx, task, outcome);
        } finally {
            taskSrv = null;
        }
    }

    public void claimTask(IWorkflowServiceClient wfsClient, IWorkflowContext wfCtx, String taskId) throws StaleObjectException, WorkflowException {
        ITaskService taskSrv = null;
        try {
            taskSrv = wfsClient.getTaskService();
            taskSrv.acquireTask(wfCtx, taskId);
        } finally {
            taskSrv = null;
        }
    }

    public void cancelClaimTask(IWorkflowServiceClient wfsClient, IWorkflowContext wfCtx, String taskId) throws StaleObjectException, WorkflowException {
        ITaskService taskSrv = null;
        try {
            taskSrv = wfsClient.getTaskService();
            taskSrv.releaseTask(wfCtx, taskId);
        } finally {
            taskSrv = null;
        }
    }

    public void updateTask(IWorkflowServiceClient wfsClient, IWorkflowContext wfCtx, Task task) throws StaleObjectException, WorkflowException {
        ITaskService taskSrv = null;
        try {
            taskSrv = wfsClient.getTaskService();
            taskSrv.updateTask(wfCtx, task);
        } finally {
            taskSrv = null;
        }
    }

    public Task reassignTaskToUser(IWorkflowServiceClient wfsClient, IWorkflowContext wfCtx, String taskId, String userId) throws StaleObjectException, WorkflowException {
        TaskAssignee taskAssignee = null;
        List<TaskAssignee> taskAssignees = null;
        ITaskService taskSrv = null;
        try {
            taskAssignee = new TaskAssignee(userId, IWorkflowConstants.IDENTITY_TYPE_USER);
            taskAssignees = new ArrayList<TaskAssignee>();
            taskAssignees.add(taskAssignee);

            taskSrv = wfsClient.getTaskService();
            return taskSrv.reassignTask(wfCtx, taskId, taskAssignees);
        } finally {
            taskAssignee = null;
            taskAssignees = null;
            taskSrv = null;
        }
    }

    public Task reassignTaskToRole(IWorkflowServiceClient wfsClient, IWorkflowContext wfCtx, String taskId, String roleName) throws StaleObjectException, WorkflowException {
        TaskAssignee taskAssignee = null;
        List<TaskAssignee> taskAssignees = null;
        ITaskService taskSrv = null;
        try {
            taskAssignee = new TaskAssignee(roleName, IWorkflowConstants.IDENTITY_TYPE_APPLICATION_ROLE);
            taskAssignees = new ArrayList<TaskAssignee>();
            taskAssignees.add(taskAssignee);

            taskSrv = wfsClient.getTaskService();
            return taskSrv.reassignTask(wfCtx, taskId, taskAssignees);
        } finally {
            taskAssignee = null;
            taskAssignees = null;
            taskSrv = null;
        }
    }

    public Task reassignTaskToGroup(IWorkflowServiceClient wfsClient, IWorkflowContext wfCtx, String taskId, String groupName) throws StaleObjectException, WorkflowException {
        TaskAssignee taskAssignee = null;
        List<TaskAssignee> taskAssignees = null;
        ITaskService taskSrv = null;
        try {
            taskAssignee = new TaskAssignee(groupName, IWorkflowConstants.IDENTITY_TYPE_GROUP);
            taskAssignees = new ArrayList<TaskAssignee>();
            taskAssignees.add(taskAssignee);

            taskSrv = wfsClient.getTaskService();
            return taskSrv.reassignTask(wfCtx, taskId, taskAssignees);
        } finally {
            taskAssignee = null;
            taskAssignees = null;
            taskSrv = null;
        }
    }
}
