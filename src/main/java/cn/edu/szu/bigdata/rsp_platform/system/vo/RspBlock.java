package cn.edu.szu.bigdata.rsp_platform.system.vo;

import java.util.Date;

public class RspBlock {
    private String name;
    private String capacity;
    private long createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "RspBlock{" +
                "name='" + name + '\'' +
                ", capacity='" + capacity + '\'' +
                ", createTime=" + date.toString() +
                '}';
    }
}
