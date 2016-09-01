package com.lachesis.mnisqm.vo.user;

import java.util.List;

import com.lachesis.mnisqm.module.user.domain.ComUser;
import com.lachesis.mnisqm.module.user.domain.ComUserEducation;
import com.lachesis.mnisqm.module.user.domain.ComUserFamily;
import com.lachesis.mnisqm.module.user.domain.ComUserLearning;
import com.lachesis.mnisqm.module.user.domain.ComUserNursing;
import com.lachesis.mnisqm.module.user.domain.ComUserPosition;
import com.lachesis.mnisqm.module.user.domain.ComUserTraining;

/**
 * 员工模块获取前端数据的vo
 * @author Administrator
 *
 */
public class ComUserInfoInVo {
	private String dataType;//数据类型
	
	private Long delSeqId;//删除数据的ID
	
	private ComUser comUser;
	
	private List<ComUserEducation> comUserEducation;//员工学历信息
	
	private List<ComUserFamily> comUserFamily; //员工家庭信息
	
	private List<ComUserLearning> comUserLearning; //员工教育信息
	
	private List<ComUserTraining> comUserTraining; //员工培训信息

	private ComUserPosition comUserPosition;//员工职称信息
	
	private ComUserNursing comUserNursing;//员工职务信息

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public ComUser getComUser() {
		return comUser;
	}

	public void setComUser(ComUser comUser) {
		this.comUser = comUser;
	}

	public List<ComUserEducation> getComUserEducation() {
		return comUserEducation;
	}

	public void setComUserEducation(List<ComUserEducation> comUserEducation) {
		this.comUserEducation = comUserEducation;
	}

	public List<ComUserFamily> getComUserFamily() {
		return comUserFamily;
	}

	public void setComUserFamily(List<ComUserFamily> comUserFamily) {
		this.comUserFamily = comUserFamily;
	}

	public List<ComUserLearning> getComUserLearning() {
		return comUserLearning;
	}

	public void setComUserLearning(List<ComUserLearning> comUserLearning) {
		this.comUserLearning = comUserLearning;
	}

	public List<ComUserTraining> getComUserTraining() {
		return comUserTraining;
	}

	public void setComUserTraining(List<ComUserTraining> comUserTraining) {
		this.comUserTraining = comUserTraining;
	}

	public ComUserPosition getComUserPosition() {
		return comUserPosition;
	}

	public void setComUserPosition(ComUserPosition comUserPosition) {
		this.comUserPosition = comUserPosition;
	}

	public ComUserNursing getComUserNursing() {
		return comUserNursing;
	}

	public void setComUserNursing(ComUserNursing comUserNursing) {
		this.comUserNursing = comUserNursing;
	}

	public Long getDelSeqId() {
		return delSeqId;
	}

	public void setDelSeqId(Long delSeqId) {
		this.delSeqId = delSeqId;
	}
}