package com.harmazing.openbridge.monitor.dao;

import com.harmazing.framework.common.dao.IBaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by 李杨 [liyang@yihecloud.com] on 2016/8/15 11:57.
 */
public interface AppMonitorMapper extends IBaseMapper {
    @Select("SELECT project_code FROM os_project WHERE project_id = #{projectId}")
    String findProjectCodeByProjectId(String projectId);
    @Select("SELECT pd.`deploy_id` deployId, pd.`project_id` projectId, pd.`deploy_name` deployName, pd.`description` description, pd.`tenant_id` tenantId, pd.`env_type` envType, pd.`create_user` createUser, pd.`create_time` createTime, pd.`service_ip` serviceIp, pd.`public_ip` publicIp, pd.`replicas` replicas, pd.`restart_policy` restartPolicy, pd.`modify_user` modifyUser, pd.`modify_time` modifyTime, pd.`cpu_` cpu, pd.`memory_` memory, pd.`compute_config` computeConfig, pd.`build_id` buildId, pd.`status` status, pd.`delete_status` deleteStatus, pd.`deploy_code` deployCode, pd.`image_id` imageId, pd.`resource_config` resourceConfig, pd.`env_id` envId, pd.`extra_data` extraData, pd.`extra_key` extraKey, pd.`deploy_time` deployTime, pd.`health_content` healthContent , pe.env_name envName " +
            "FROM os_project_deploy pd LEFT OUTER JOIN os_project_env pe ON pd.env_id = pe.env_id " +
            "WHERE pd.status=#{0} AND pd.project_id=#{1} " +
            "ORDER BY create_time DESC ")
    List<Map<String, Object>> findDeployByProjectId(int status, String projectId);
}

