package king.cloud.camara.bean;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/10/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class User {

    private String id;
    private String createTime;
    private String intoCount;
    private String deviceId;
    private String imageSize;


    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIntoCount() {
        return intoCount;
    }

    public void setIntoCount(String intoCount) {
        this.intoCount = intoCount;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
