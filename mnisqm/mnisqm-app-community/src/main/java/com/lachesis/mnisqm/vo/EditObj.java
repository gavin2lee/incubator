package com.lachesis.mnisqm.vo;

import com.lachesis.mnisqm.module.user.domain.ComUser;
import com.lachesis.mnisqm.module.user.domain.ComUserEducation;
import com.lachesis.mnisqm.module.user.domain.ComUserFamily;
import com.lachesis.mnisqm.module.user.domain.ComUserLearning;
import com.lachesis.mnisqm.module.user.domain.ComUserNursing;
import com.lachesis.mnisqm.module.user.domain.ComUserPosition;
import com.lachesis.mnisqm.module.user.domain.ComUserTraining;

public class EditObj {
	private String dataType;
	
	private ComUser comUser;
	
	private ComUserEducation comUserEducation;//员工学历信息
	
	private ComUserFamily comUserFamily; //员工家庭信息
	
	private ComUserLearning comUserLearning; //员工教育信息
	
	private ComUserTraining comUserTraining; //员工培训信息

	private ComUserPosition comUserPosition;//员工职位信息
	
	private ComUserNursing comUserNursing;//员工护理信息

	public EditObj() {
		super();
	}

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

	public ComUserEducation getComUserEducation() {
		return comUserEducation;
	}

	public void setComUserEducation(ComUserEducation comUserEducation) {
		this.comUserEducation = comUserEducation;
	}

	public ComUserFamily getComUserFamily() {
		return comUserFamily;
	}

	public void setComUserFamily(ComUserFamily comUserFamily) {
		this.comUserFamily = comUserFamily;
	}

	public ComUserLearning getComUserLearning() {
		return comUserLearning;
	}

	public void setComUserLearning(ComUserLearning comUserLearning) {
		this.comUserLearning = comUserLearning;
	}

	public ComUserTraining getComUserTraining() {
		return comUserTraining;
	}

	public void setComUserTraining(ComUserTraining comUserTraining) {
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
	
	
	

}
