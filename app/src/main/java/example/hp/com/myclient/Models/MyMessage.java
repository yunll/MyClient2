package example.hp.com.myclient.Models;

/**
 * Created by hp on 2015/10/30.
 * 用于Server和Client之间传输信息
 * type表示消息类型，如入库、出库、查询详情等
 * message为json格式，根据type，解析成不同的内容
 */
public class MyMessage {
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MyMessage(int type, String message){
        this.type=type;
        this.message=message;
    }
    private int type;
    private String message;
}
