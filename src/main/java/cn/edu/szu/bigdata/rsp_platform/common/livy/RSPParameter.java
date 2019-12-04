package cn.edu.szu.bigdata.rsp_platform.common.livy;

/**
 * Created by longhao on 2019/9/20
 */
public class RSPParameter {
    private String JobName;
    private String rspJarPath;
    private String proxyUser;
    private String queue;
    private String inputPath;
    private String outputPath;
    private String blockNumber;
    private String executorCores;
    private String executorMemory;
    private String driverCores;
    private String driverMemory;
    private String numExecutors;



    public String getJobName() {
        return JobName;
    }

    public void setJobName(String jobName) {
        JobName = jobName;
    }

    public String getRspJarPath() {
        return rspJarPath;
    }

    public void setRspJarPath(String rspJarPath) {
        this.rspJarPath = rspJarPath;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getExecutorCores() {
        return executorCores;
    }

    public void setExecutorCores(String executorCores) {
        this.executorCores = executorCores;
    }

    public String getExecutorMemory() {
        return executorMemory;
    }

    public void setExecutorMemory(String executorMemory) {
        this.executorMemory = executorMemory;
    }

    public String getDriverCores() {
        return driverCores;
    }

    public void setDriverCores(String driverCores) {
        this.driverCores = driverCores;
    }

    public String getDriverMemory() {
        return driverMemory;
    }

    public void setDriverMemory(String driverMemory) {
        this.driverMemory = driverMemory;
    }

    public String getNumExecutors() {
        return numExecutors;
    }

    public void setNumExecutors(String numExecutors) {
        this.numExecutors = numExecutors;
    }
}
