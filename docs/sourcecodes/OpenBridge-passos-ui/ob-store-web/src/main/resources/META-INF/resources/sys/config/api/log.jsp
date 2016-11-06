<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<table class="table table-bordered">
	<tr>
		<td colspan="2" style="background:#dddddd;">日志配置</td> 
	</tr> 
	<tr>
		<td  style="width: 200px;background: #efefef;">Elasticsearch的访问地址</td>
		<td>
			<input type="text" class="form-control "  value="${ config['elasticsearch.url'] }" name="config['elasticsearch.url']"  />
			<p class="help_text" >
			<span>Elasticsearch的地址,配置如<em>127.0.0.1:88;127.0.0.1:89</em>
			 其中IP及端口地址为实际Elasticsearch部署的服务器IP及端口,支持集群部署,其中地址用<em>;</em>隔开</span></p>
		</td>
	</tr>
	<tr>
		<td  style="width: 200px;background: #efefef;">Elasticsearch集群名称</td>
		<td>
			<input type="text" class="form-control " value="${ config['elasticsearch.cluster'] }" name="config['elasticsearch.cluster']" />
		<p class="help_text" >
			<span>Elasticsearch 集群名称</span></p>
		</td>
	</tr>
		<tr>
		<td  style="width: 200px;background: #efefef;">Logstash的访问地址</td>
		<td>
			<input type="text" class="form-control " value="${ config['logstash.url'] }" name="config['logstash.url']" />
			<p class="help_text" >
			<span>Logstash的地址,配置如<em>127.0.0.1:88;127.0.0.1:89</em>
			 其中IP及端口地址为实际Logstash部署的服务器IP及端口,支持集群部署,其中地址用<em>;</em>隔开</span></p>
		</td>
	</tr>
</table>