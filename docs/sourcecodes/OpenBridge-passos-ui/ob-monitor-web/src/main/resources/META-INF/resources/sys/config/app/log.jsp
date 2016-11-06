<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="form_control form_control_setting p20">
	<div class="form_block">
		<label>Elasticsearch的访问地址</label> 
		<div class="form-right"><input type="text" style="width: 400px;"
			value="${ config['elasticsearch.url'] }" name="config['elasticsearch.url']"  />
		<p class="help_text" >
			<span>Elasticsearch的地址,配置如<em>127.0.0.1:88;127.0.0.1:89</em>
			 其中IP及端口地址为实际Elasticsearch部署的服务器IP及端口,支持集群部署,其中地址用<em>;</em>隔开</span></p>
	</div>
	</div>
	
	<div class="form_block">
		<label>Elasticsearch集群名称</label> 
		<div class="form-right"><input type="text" style="width: 400px;"
			value="${ config['elasticsearch.cluster'] }" name="config['elasticsearch.cluster']"  />
		<p class="help_text" >
			<span>Elasticsearch 集群名称</span></p>
	</div>
	</div>
<div class="form_block">
		<label>Logstash的访问地址</label> 
		<div class="form-right"><input type="text" style="width: 400px;"
			value="${ config['logstash.url'] }" name="config['logstash.url']"  />
		<p class="help_text" >
			<span>Logstash的地址,配置如<em>127.0.0.1:88;127.0.0.1:89</em>
			 其中IP及端口地址为实际Logstash部署的服务器IP及端口,支持集群部署,其中地址用<em>;</em>隔开</span></p>
	</div>
	</div>
</div>
 <div class="line3"></div>