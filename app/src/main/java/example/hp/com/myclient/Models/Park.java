package example.hp.com.myclient.Models;

/**
 * Created by hp on 2015/10/30.
 */
public class Park {

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public int getAvailableSpace() {
        return availableSpace;
    }

    public void setAvailableSpace(int availableSpace) {
        this.availableSpace = availableSpace;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Park(){}

    // 构造函数
    public Park(String parkId, String parkName, int availableSpace, int unitPrice, String introduction) {
        this.parkId = parkId;
        this.parkName = parkName;
        this.availableSpace = availableSpace;
        this.unitPrice = unitPrice;
        this.introduction = introduction;
    }

    private String parkId;
    private String parkName;
    private int availableSpace;
    /**
     * 每小时收费
     */
    private int unitPrice;
    /**
     * 简介，可以包括停车场基本信息、收费情况比如30分钟内不收费等规则
     */
    private String introduction;
}
