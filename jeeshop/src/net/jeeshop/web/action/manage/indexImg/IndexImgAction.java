/**
 * 2012-7-8
 * jqsl2012@163.com
 */
package net.jeeshop.web.action.manage.indexImg;

import java.io.File;
import java.io.IOException;

import net.jeeshop.core.BaseAction;
import net.jeeshop.services.manage.indexImg.bean.IndexImg;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

/**
 * 滚动图片
 * 
 * @author huangf
 * 
 */
public class IndexImgAction extends BaseAction<IndexImg> {
	private static final long serialVersionUID = 1L;
	private File image;

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	@Override
	public IndexImg getE() {
		return this.e;
	}

	@Override
	public void prepare() throws Exception {
		if (this.e == null) {
			this.e = new IndexImg();
		}
	}

	@Override
	public void insertAfter(IndexImg e) {
		e.clear();
	}

	@Override
	public String selectList() throws Exception {
		super.selectList();
		return toList;
	}
	@Override
	protected void selectListAfter() {
		pager.setPagerUrl("indexImg!selectList.action");
	}
	@Override
	public String insert() throws Exception {
//		uploadImage();
		return super.insert();
	}
	
	@Override
	public String update() throws Exception {
//		uploadImage();
		return super.update();
	}
	
	//上传文件
	@Deprecated
	private void uploadImage() throws IOException{
		if(image==null){
			return;
		}
		String imageName = String.valueOf(System.currentTimeMillis()) + ".jpg";
		String realpath = ServletActionContext.getServletContext().getRealPath("/indexImg/");
		// D:\apache-tomcat-6.0.18\webapps\struts2_upload\images
		System.out.println("realpath: " + realpath);
		if (image != null) {
			File savefile = new File(new File(realpath), imageName);
			if (!savefile.getParentFile().exists())
				savefile.getParentFile().mkdirs();
			FileUtils.copyFile(image, savefile);
			ActionContext.getContext().put("message", "文件上传成功");
		}
//		SystemInfo sInfo = SystemSingle.getInstance().getSystemInfo();
//		String url = sInfo.getWww_ip() + "/file/img/" + imageName;
		String url = "/indexImg/" + imageName;
		e.setPicture(url);
		image = null;
	}
	
	/**
	 * 同步缓存
	 * @return
	 * @throws Exception 
	 */
	public String syncCache() throws Exception{
//		SystemSingle.getInstance().sync(Container.imgList);
		return super.selectList();
	}
}
