package cn.edu.szu.bigdata.rsp_platform.system.vo;

import java.util.Date;

public class RspDir {
    private String name;
    private String capacity;
    private int blockNumber;
    private long createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        Date date=new Date(createTime);
        return "RspDir{" +
                "name='" + name + '\'' +
                ", capacity='" + capacity + '\'' +
                ", blockNumber=" + blockNumber +
                ", createTime=" + date.toString() +
                '}';
    }
}
