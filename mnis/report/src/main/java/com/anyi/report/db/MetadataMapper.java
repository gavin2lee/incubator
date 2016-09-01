package com.anyi.report.db;

import java.util.List;

import com.anyi.report.entity.Metadata;
import com.anyi.report.entity.MetadataOption;

public interface MetadataMapper {

	public String isExitSameCode(String metadata_code);
	public void addMetadada(Metadata metadata);
	public void deletaMetadata(Metadata metadata);
	public void updateMetadata(Metadata metadata);
	public List<Metadata> queryMetadata(Metadata metadata);
	
	public void addMetadataOption(MetadataOption option);
	public void deleteMetadataOption(MetadataOption option);
	public void updateMetadataOption(MetadataOption option);
	public List<MetadataOption> queryMetadataOption(MetadataOption option);
}
