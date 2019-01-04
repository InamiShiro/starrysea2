package top.starrysea.object.view.in;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class WorkTypesForRemoveCar {

	@Valid
	@NotEmpty(message = "删除作品名单为空")
	private List<WorkTypeForRemoveCar> workTypes;
	@NotBlank(message="不要重复提交")
	private String token;

	public List<WorkTypeForRemoveCar> getWorkTypes() {
		return workTypes;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setWorkTypes(List<WorkTypeForRemoveCar> workTypes) {
		this.workTypes = workTypes;
	}

}
