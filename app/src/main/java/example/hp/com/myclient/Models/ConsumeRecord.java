package example.hp.com.myclient.Models;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by hp on 2015/10/30.
 */
public class ConsumeRecord {
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public double getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(double costMoney) {
        this.costMoney = costMoney;
    }
    public Time getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(Time timeBegin) {
        this.timeBegin = timeBegin;
    }

    public Time getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Time timeEnd) {
        this.timeEnd = timeEnd;
    }

    private int recordId;
    private String username;
    private String parkId;
    private Date dateBegin;
    private Time timeBegin;

    private Date dateEnd;
    private Time timeEnd;
    private double costMoney;




    public String toString(){
        StringBuilder res=new StringBuilder();
        res.append('{').append("\"recordId\":").append("\"" + this.recordId + "\"").append(",");
        res.append("\"username\":").append("\"" + this.username + "\"").append(",");
        res.append("\"parkId\":").append("\"" + this.parkId + "\"").append(",");
//        res.append("\"dateBegin\":").append("\"" + this.dateBegin + "\"").append(",");
//        res.append("\"timeBegin\":").append("\"" + this.timeBegin + "\"").append(",");
//        res.append("\"dateEnd\":").append("\"" + this.dateEnd + "\"").append(",");
//        res.append("\"timeEnd\":").append("\"" + this.timeEnd + "\"").append(",");
        res.append("\"costMoney\":").append(Double.toString(this.costMoney));
        res.append('}');
        return res.toString();
    }
}
