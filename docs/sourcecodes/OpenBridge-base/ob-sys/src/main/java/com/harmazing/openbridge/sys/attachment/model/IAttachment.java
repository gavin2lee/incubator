package com.harmazing.openbridge.sys.attachment.model;


import java.util.Date;

import com.harmazing.framework.common.model.IBaseModel;

public interface IAttachment extends IBaseModel {

	public String getAttId();

	public String getAttName();

	public String getFilePath();

	public String getBusinessId();

	public String getBusinessKey();

	public String getBusinessType();

	public int getAttSize();

	public Date getCreateTime();

}
