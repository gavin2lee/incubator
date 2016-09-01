package com.anyi.report.service;

import java.util.List;

import com.anyi.report.entity.Metadata;
import com.anyi.report.entity.MetadataOption;

public interface DocServiceForMetadata {
	public String isExitSameCode(String metadata_code);
	public String addMetadada(Metadata metadata);
	public String deletaMetadata(Metadata metadata);
	public String updateMetadata(Metadata metadata);
	public List<Metadata> queryMetadata(Metadata metadata);
	
	public String addMetadataOption(MetadataOption option);
	public String deleteMetadataOption(MetadataOption option);
	public String updateMetadataOption(MetadataOption option);
	public List<MetadataOption> queryMetadataOption(MetadataOption option);
}
