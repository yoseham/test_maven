package cn.edu.szu.bigdata.rsp_platform.common.eum;

/**
 * Created by longhao on 2019/9/24
 */
public enum SparkJobState {



    /**
     * Session is waiting to submit,自定义的
     */
    WAITING_SUBMIT("waiting_submit"),

    /**
     * job 已经提交
     */
    SUBMITED("submited"),

    /**
     * Session has not been started
     */
    NOT_STARTED("not_started"),
    /**
     * Session is starting
     */
    STARTING("starting"),
    /**
     * Session is waiting for input
     */
    IDLE("idle"),
    /**
     * Session is executing a statement
     */
    BUSY("busy"),
    /**
     * Session is shutting down
     */
    SHUTTING_DOWN("shutting_down"),
    /**
     * Session errored out
     */
    ERROR("error"),
    /**
     * Session has exited
     */
    DEAD("dead"),
    /**
     * Session is successfully stopped
     */
    SUCCESS("success"),

    /**
     * Statement is enqueued but execution hasn't started
     */
    WAITING("waiting"),

    /**
     * Statement is currently running
     */
    RUNING("running"),

    /**
     * Statement has a response ready
     */
    AVAiLABEL("available"),

    /**
     * Statement is being cancelling
     */
    CANCELLING("canceling"),

    /**
     * Statement is cancelled
     */
    CANCELLED("cancelled");
    /**
     * 描述
     */
    private String description;

    /**
     * 私有构造,防止被外部调用
     *
     * @param description 描述
     */
    SparkJobState(String description) {
        this.description = description;
    }

    /**
     * 定义方法,返回描述,跟常规类的定义没区别
     *
     * @return 描述
     */
    public String getDescription() {
        return description;
    }

    public static SparkJobState fromDescription(String state) {
        for (SparkJobState jobStateEnum : SparkJobState.values()) {
            if (jobStateEnum.description.equalsIgnoreCase(state)) {
                return jobStateEnum;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
