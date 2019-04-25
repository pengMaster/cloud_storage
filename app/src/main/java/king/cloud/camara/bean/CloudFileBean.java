package king.cloud.camara.bean;

public class CloudFileBean {
	
	private String id;
	private String name;
	private String userId;
	private String isCanEdit;//0 否 1 能
	private String createTime;
	private String updateTime;
	private String type;
	private String isDelete; // 0删除 1 未删除

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsCanEdit() {
		return isCanEdit;
	}
	public void setIsCanEdit(String isCanEdit) {
		this.isCanEdit = isCanEdit;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	
	
	

}
