package com.harmazing.openbridge.sys.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.sys.user.model.SysDepart;

/**
 * 
 * <pre>
 * 组织机构数据库访问层实现
 * </pre>
 * 
 * @author taoshuangxi
 *
 */
public interface SysDepartMapper extends IBaseMapper {

	@Select("select dept.* from sys_department dept ORDER BY d_order ASc")
	List<SysDepart> getAllDeparts();
	
	@Select("select dept.* from sys_department dept where dept.parent_id=#{parentId} order by dept.d_order asc")
	List<SysDepart> getDepartByParentId(@Param("parentId")String parentId);
	
	@Select("select * from sys_department where sys_department.dept_id=#{deptId}")
	SysDepart getDepartById(@Param("deptId")String deptId);
	
	@Delete("delete from sys_department where sys_department.dept_id=#{deptId}")
	void deleteDepartById(@Param("deptId")String deptId);
	
	@Select("select dept.* from sys_department dept where dept.dept_name=#{deptName} and parent_id=#{parentId} limit 0,1")
	SysDepart getDepartByNameAndParentId(@Param("deptName")String deptName,@Param("parentId")String parentId);
	
	@Insert("insert into sys_department values(#{deptId},#{deptName},#{parentId},#{hierarchyId},#{createTime},#{createUser},#{dOrder})")
	void addDepart(SysDepart depart);
	
	@Update("update sys_department set dept_name=#{deptName},parent_id=#{parentId},d_order=#{dOrder} where dept_id=#{deptId}")
	void updateDepart(SysDepart depart);
	
	@Update("update sys_department set hierarchy_id= CONCAT(#{newHID}, substring(hierarchy_id, length(#{oldHID})+1, length(hierarchy_id))) where hierarchy_id like CONCAT(#{oldHID}, '%')")
	void updateDepartHierarchyId(@Param("newHID")String newHID, @Param("oldHID")String oldHID);
	
}
