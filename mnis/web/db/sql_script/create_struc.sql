USE [MNIS_V2]
GO
/****** Object:  UserDefinedFunction [dbo].[count_unexec_orders]    Script Date: 09/22/2013 15:08:33 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[count_unexec_orders]') AND type in (N'FN', N'IF', N'TF', N'FS', N'FT'))
DROP FUNCTION [dbo].[count_unexec_orders]
GO
/****** Object:  StoredProcedure [dbo].[uih_sp_bodysign_speedy]    Script Date: 09/22/2013 15:08:32 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[uih_sp_bodysign_speedy]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[uih_sp_bodysign_speedy]
GO
/****** Object:  Table [dbo].[vital_sign_info]    Script Date: 09/22/2013 15:08:30 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_vital_sign_info_create_time]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[vital_sign_info] DROP CONSTRAINT [DF_vital_sign_info_create_time]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_vital_sign_info_temperature_unit]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[vital_sign_info] DROP CONSTRAINT [DF_vital_sign_info_temperature_unit]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_vital_sign_info_pluse_unit]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[vital_sign_info] DROP CONSTRAINT [DF_vital_sign_info_pluse_unit]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_vital_sign_info_hr_unit]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[vital_sign_info] DROP CONSTRAINT [DF_vital_sign_info_hr_unit]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_vital_sign_info_breath_unit]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[vital_sign_info] DROP CONSTRAINT [DF_vital_sign_info_breath_unit]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_vital_sign_info_bp_unit]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[vital_sign_info] DROP CONSTRAINT [DF_vital_sign_info_bp_unit]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_vital_sign_info_in_take_unit]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[vital_sign_info] DROP CONSTRAINT [DF_vital_sign_info_in_take_unit]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_vital_sign_info_urine_unit]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[vital_sign_info] DROP CONSTRAINT [DF_vital_sign_info_urine_unit]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_vital_sign_info_other_out_put_unit]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[vital_sign_info] DROP CONSTRAINT [DF_vital_sign_info_other_out_put_unit]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_vital_sign_info_total_out_put_unit]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[vital_sign_info] DROP CONSTRAINT [DF_vital_sign_info_total_out_put_unit]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_vital_sign_info_shit_unit]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[vital_sign_info] DROP CONSTRAINT [DF_vital_sign_info_shit_unit]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_vital_sign_info_height_unit]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[vital_sign_info] DROP CONSTRAINT [DF_vital_sign_info_height_unit]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_vital_sign_info_weight_unit]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[vital_sign_info] DROP CONSTRAINT [DF_vital_sign_info_weight_unit]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[vital_sign_info]') AND type in (N'U'))
DROP TABLE [dbo].[vital_sign_info]
GO
/****** Object:  Table [dbo].[ward_patrol]    Script Date: 09/22/2013 15:08:30 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_ward_patrol_tend_level]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[ward_patrol] DROP CONSTRAINT [DF_ward_patrol_tend_level]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ward_patrol]') AND type in (N'U'))
DROP TABLE [dbo].[ward_patrol]
GO
/****** Object:  Table [dbo].[ass_nurse_item_dept]    Script Date: 09/22/2013 15:08:30 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ass_nurse_item_dept]') AND type in (N'U'))
DROP TABLE [dbo].[ass_nurse_item_dept]
GO
/****** Object:  Table [dbo].[ass_nurse_record_dept]    Script Date: 09/22/2013 15:08:30 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ass_nurse_record_dept]') AND type in (N'U'))
DROP TABLE [dbo].[ass_nurse_record_dept]
GO
/****** Object:  Table [dbo].[bed]    Script Date: 09/22/2013 15:08:30 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_bed_bed_status]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[bed] DROP CONSTRAINT [DF_bed_bed_status]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[bed]') AND type in (N'U'))
DROP TABLE [dbo].[bed]
GO
/****** Object:  Table [dbo].[criticalvalue_info]    Script Date: 09/22/2013 15:08:30 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[criticalvalue_info]') AND type in (N'U'))
DROP TABLE [dbo].[criticalvalue_info]
GO
/****** Object:  Table [dbo].[dict_body_sign]    Script Date: 09/22/2013 15:08:30 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_body_sign]') AND type in (N'U'))
DROP TABLE [dbo].[dict_body_sign]
GO
/****** Object:  Table [dbo].[dict_diag]    Script Date: 09/22/2013 15:08:30 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_diag__infec__6E01572D]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_diag] DROP CONSTRAINT [DF__dict_diag__infec__6E01572D]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_diag__cance__6EF57B66]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_diag] DROP CONSTRAINT [DF__dict_diag__cance__6EF57B66]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_dict_diag_applied_gender]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_diag] DROP CONSTRAINT [DF_dict_diag_applied_gender]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_diag__is_va__6FE99F9F]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_diag] DROP CONSTRAINT [DF__dict_diag__is_va__6FE99F9F]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_diag]') AND type in (N'U'))
DROP TABLE [dbo].[dict_diag]
GO
/****** Object:  Table [dbo].[dict_drug]    Script Date: 09/22/2013 15:08:29 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_drug__drug___01142BA1]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_drug] DROP CONSTRAINT [DF__dict_drug__drug___01142BA1]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_drug__dose___02084FDA]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_drug] DROP CONSTRAINT [DF__dict_drug__dose___02084FDA]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_drug__dose___02FC7413]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_drug] DROP CONSTRAINT [DF__dict_drug__dose___02FC7413]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_drug__drug___03F0984C]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_drug] DROP CONSTRAINT [DF__dict_drug__drug___03F0984C]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_drug__pack___04E4BC85]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_drug] DROP CONSTRAINT [DF__dict_drug__pack___04E4BC85]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_drug__pack___05D8E0BE]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_drug] DROP CONSTRAINT [DF__dict_drug__pack___05D8E0BE]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_drug__min_u__06CD04F7]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_drug] DROP CONSTRAINT [DF__dict_drug__min_u__06CD04F7]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_drug__min_b__07C12930]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_drug] DROP CONSTRAINT [DF__dict_drug__min_b__07C12930]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_drug__order__08B54D69]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_drug] DROP CONSTRAINT [DF__dict_drug__order__08B54D69]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_drug__is_va__09A971A2]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_drug] DROP CONSTRAINT [DF__dict_drug__is_va__09A971A2]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_drug__creat__0A9D95DB]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_drug] DROP CONSTRAINT [DF__dict_drug__creat__0A9D95DB]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_drug]') AND type in (N'U'))
DROP TABLE [dbo].[dict_drug]
GO
/****** Object:  Table [dbo].[dict_freq]    Script Date: 09/22/2013 15:08:29 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_freq__is_va__20C1E124]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_freq] DROP CONSTRAINT [DF__dict_freq__is_va__20C1E124]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_dict_freq_create_date]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_freq] DROP CONSTRAINT [DF_dict_freq_create_date]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_freq]') AND type in (N'U'))
DROP TABLE [dbo].[dict_freq]
GO
/****** Object:  Table [dbo].[dict_lab_sample]    Script Date: 09/22/2013 15:08:29 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_dict_lab_sample_priority]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_lab_sample] DROP CONSTRAINT [DF_dict_lab_sample_priority]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_lab___is_va__787EE5A0]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_lab_sample] DROP CONSTRAINT [DF__dict_lab___is_va__787EE5A0]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_dict_lab_sample_create_date]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_lab_sample] DROP CONSTRAINT [DF_dict_lab_sample_create_date]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_lab_sample]') AND type in (N'U'))
DROP TABLE [dbo].[dict_lab_sample]
GO
/****** Object:  Table [dbo].[dict_measure_note]    Script Date: 09/22/2013 15:08:29 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_meas__in_nu__7E37BEF6]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_measure_note] DROP CONSTRAINT [DF__dict_meas__in_nu__7E37BEF6]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_measure_note]') AND type in (N'U'))
DROP TABLE [dbo].[dict_measure_note]
GO
/****** Object:  Table [dbo].[dict_measure_rule]    Script Date: 09/22/2013 15:08:29 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_measure_rule]') AND type in (N'U'))
DROP TABLE [dbo].[dict_measure_rule]
GO
/****** Object:  Table [dbo].[dict_nurse_item]    Script Date: 09/22/2013 15:08:29 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_dict_nurse_item_nurse_item_type_code]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_nurse_item] DROP CONSTRAINT [DF_dict_nurse_item_nurse_item_type_code]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_dict_nurse_item_is_valid]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_nurse_item] DROP CONSTRAINT [DF_dict_nurse_item_is_valid]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_nurse_item]') AND type in (N'U'))
DROP TABLE [dbo].[dict_nurse_item]
GO
/****** Object:  Table [dbo].[dict_nurse_record]    Script Date: 09/22/2013 15:08:29 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_dict_nurse_record_is_valid]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_nurse_record] DROP CONSTRAINT [DF_dict_nurse_record_is_valid]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_nurse_record]') AND type in (N'U'))
DROP TABLE [dbo].[dict_nurse_record]
GO
/****** Object:  Table [dbo].[dict_nurse_record_value]    Script Date: 09/22/2013 15:08:28 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_dict_nurse_record_value_is_valid]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_nurse_record_value] DROP CONSTRAINT [DF_dict_nurse_record_value_is_valid]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_dict_nurse_record_value_seq_no]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_nurse_record_value] DROP CONSTRAINT [DF_dict_nurse_record_value_seq_no]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_nurse_record_value]') AND type in (N'U'))
DROP TABLE [dbo].[dict_nurse_record_value]
GO
/****** Object:  Table [dbo].[dict_order_class]    Script Date: 09/22/2013 15:08:28 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_orde__is_va__1BFD2C07]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_order_class] DROP CONSTRAINT [DF__dict_orde__is_va__1BFD2C07]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_dict_order_class_create_date]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_order_class] DROP CONSTRAINT [DF_dict_order_class_create_date]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_order_class]') AND type in (N'U'))
DROP TABLE [dbo].[dict_order_class]
GO
/****** Object:  Table [dbo].[dict_undrug]    Script Date: 09/22/2013 15:08:28 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_undr__is_va__0E6E26BF]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_undrug] DROP CONSTRAINT [DF__dict_undr__is_va__0E6E26BF]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_undr__creat__0F624AF8]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_undrug] DROP CONSTRAINT [DF__dict_undr__creat__0F624AF8]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_undrug]') AND type in (N'U'))
DROP TABLE [dbo].[dict_undrug]
GO
/****** Object:  Table [dbo].[dict_usage]    Script Date: 09/22/2013 15:08:28 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__dict_usag__is_va__276EDEB3]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_usage] DROP CONSTRAINT [DF__dict_usag__is_va__276EDEB3]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_dict_usage_create_date]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_usage] DROP CONSTRAINT [DF_dict_usage_create_date]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_usage]') AND type in (N'U'))
DROP TABLE [dbo].[dict_usage]
GO
/****** Object:  Table [dbo].[dict_ward]    Script Date: 09/22/2013 15:08:28 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_dict_ward_is_in_use]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_ward] DROP CONSTRAINT [DF_dict_ward_is_in_use]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_dict_ward_create_date]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[dict_ward] DROP CONSTRAINT [DF_dict_ward_create_date]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_ward]') AND type in (N'U'))
DROP TABLE [dbo].[dict_ward]
GO
/****** Object:  Table [dbo].[item_price]    Script Date: 09/22/2013 15:08:28 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_item_price_item_type]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[item_price] DROP CONSTRAINT [DF_item_price_item_type]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[item_price]') AND type in (N'U'))
DROP TABLE [dbo].[item_price]
GO
/****** Object:  Table [dbo].[order_exec]    Script Date: 09/22/2013 15:08:28 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[order_exec]') AND type in (N'U'))
DROP TABLE [dbo].[order_exec]
GO
/****** Object:  Table [dbo].[order_group]    Script Date: 09/22/2013 15:08:28 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_order_group_order_status_code]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[order_group] DROP CONSTRAINT [DF_order_group_order_status_code]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_order_group_emergent]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[order_group] DROP CONSTRAINT [DF_order_group_emergent]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[order_group]') AND type in (N'U'))
DROP TABLE [dbo].[order_group]
GO
/****** Object:  Table [dbo].[order_item]    Script Date: 09/22/2013 15:08:27 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_order_item_skin_test_required]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[order_item] DROP CONSTRAINT [DF_order_item_skin_test_required]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[order_item]') AND type in (N'U'))
DROP TABLE [dbo].[order_item]
GO
/****** Object:  Table [dbo].[pat_base]    Script Date: 09/22/2013 15:08:27 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_pat_base_gender]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[pat_base] DROP CONSTRAINT [DF_pat_base_gender]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_pat_base_is_married]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[pat_base] DROP CONSTRAINT [DF_pat_base_is_married]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_pat_base_education_level]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[pat_base] DROP CONSTRAINT [DF_pat_base_education_level]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_pat_base_financial_level]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[pat_base] DROP CONSTRAINT [DF_pat_base_financial_level]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[pat_base]') AND type in (N'U'))
DROP TABLE [dbo].[pat_base]
GO
/****** Object:  Table [dbo].[pat_hosp]    Script Date: 09/22/2013 15:08:27 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_pat_hosp_in_state]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[pat_hosp] DROP CONSTRAINT [DF_pat_hosp_in_state]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_pat_hosp_tend_level]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[pat_hosp] DROP CONSTRAINT [DF_pat_hosp_tend_level]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_pat_hosp_illness_status]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[pat_hosp] DROP CONSTRAINT [DF_pat_hosp_illness_status]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_pat_hosp_charge_type]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[pat_hosp] DROP CONSTRAINT [DF_pat_hosp_charge_type]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[pat_hosp]') AND type in (N'U'))
DROP TABLE [dbo].[pat_hosp]
GO
/****** Object:  Table [dbo].[phone_bindings]    Script Date: 09/22/2013 15:08:27 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[phone_bindings]') AND type in (N'U'))
DROP TABLE [dbo].[phone_bindings]
GO
/****** Object:  Table [dbo].[poc_bindings]    Script Date: 09/22/2013 15:08:27 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[poc_bindings]') AND type in (N'U'))
DROP TABLE [dbo].[poc_bindings]
GO
/****** Object:  Table [dbo].[rec_allergy]    Script Date: 09/22/2013 15:08:27 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_allergy_allergy_degree]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_allergy] DROP CONSTRAINT [DF_rec_allergy_allergy_degree]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__rec_aller__is_va__72C60C4A]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_allergy] DROP CONSTRAINT [DF__rec_aller__is_va__72C60C4A]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_allergy_rec_date]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_allergy] DROP CONSTRAINT [DF_rec_allergy_rec_date]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_allergy]') AND type in (N'U'))
DROP TABLE [dbo].[rec_allergy]
GO
/****** Object:  Table [dbo].[rec_body_sign_detail]    Script Date: 09/22/2013 15:08:26 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_body_sign_detail_abnormal_flag]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_body_sign_detail] DROP CONSTRAINT [DF_rec_body_sign_detail_abnormal_flag]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_body_sign_detail]') AND type in (N'U'))
DROP TABLE [dbo].[rec_body_sign_detail]
GO
/****** Object:  Table [dbo].[rec_body_sign_mas]    Script Date: 09/22/2013 15:08:26 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_body_sign_mas_cooled]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_body_sign_mas] DROP CONSTRAINT [DF_rec_body_sign_mas_cooled]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_body_sign_mas_rec_datetime]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_body_sign_mas] DROP CONSTRAINT [DF_rec_body_sign_mas_rec_datetime]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_body_sign_mas]') AND type in (N'U'))
DROP TABLE [dbo].[rec_body_sign_mas]
GO
/****** Object:  Table [dbo].[rec_diag]    Script Date: 09/22/2013 15:08:26 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_diag_is_main]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_diag] DROP CONSTRAINT [DF_rec_diag_is_main]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_diag_rec_date]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_diag] DROP CONSTRAINT [DF_rec_diag_rec_date]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__rec_diag__is_val__75A278F5]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_diag] DROP CONSTRAINT [DF__rec_diag__is_val__75A278F5]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_diag]') AND type in (N'U'))
DROP TABLE [dbo].[rec_diag]
GO
/****** Object:  Table [dbo].[rec_infu_monitor]    Script Date: 09/22/2013 15:08:26 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__rec_infu___abnor__3D5E1FD2]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_infu_monitor] DROP CONSTRAINT [DF__rec_infu___abnor__3D5E1FD2]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_infu_monitor]') AND type in (N'U'))
DROP TABLE [dbo].[rec_infu_monitor]
GO
/****** Object:  Table [dbo].[rec_inspection_detail]    Script Date: 09/22/2013 15:08:26 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_inspection_detail]') AND type in (N'U'))
DROP TABLE [dbo].[rec_inspection_detail]
GO
/****** Object:  Table [dbo].[rec_inspection_mas]    Script Date: 09/22/2013 15:08:26 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_inspection_mas]') AND type in (N'U'))
DROP TABLE [dbo].[rec_inspection_mas]
GO
/****** Object:  Table [dbo].[rec_lab_test_detail]    Script Date: 09/22/2013 15:08:26 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_lab_test_detail_normal_flag]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_lab_test_detail] DROP CONSTRAINT [DF_rec_lab_test_detail_normal_flag]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_lab_test_detail]') AND type in (N'U'))
DROP TABLE [dbo].[rec_lab_test_detail]
GO
/****** Object:  Table [dbo].[rec_lab_test_mas]    Script Date: 09/22/2013 15:08:25 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_lab_test_mas_lab_test_status]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_lab_test_mas] DROP CONSTRAINT [DF_rec_lab_test_mas_lab_test_status]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_lab_test_mas_exec_datetime]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_lab_test_mas] DROP CONSTRAINT [DF_rec_lab_test_mas_exec_datetime]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_lab_test_mas]') AND type in (N'U'))
DROP TABLE [dbo].[rec_lab_test_mas]
GO
/****** Object:  Table [dbo].[rec_nurse_item_detail]    Script Date: 09/22/2013 15:08:25 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_item_detail]') AND type in (N'U'))
DROP TABLE [dbo].[rec_nurse_item_detail]
GO
/****** Object:  Table [dbo].[rec_nurse_item_mas]    Script Date: 09/22/2013 15:08:25 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_nurse_item_mas_rec_datetime]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_nurse_item_mas] DROP CONSTRAINT [DF_rec_nurse_item_mas_rec_datetime]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__rec_nurse__is_va__60A75C0F]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_nurse_item_mas] DROP CONSTRAINT [DF__rec_nurse__is_va__60A75C0F]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__rec_nurse__remar__5FB337D6]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_nurse_item_mas] DROP CONSTRAINT [DF__rec_nurse__remar__5FB337D6]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_item_mas]') AND type in (N'U'))
DROP TABLE [dbo].[rec_nurse_item_mas]
GO
/****** Object:  Table [dbo].[rec_nurse_record_detail]    Script Date: 09/22/2013 15:08:25 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_record_detail]') AND type in (N'U'))
DROP TABLE [dbo].[rec_nurse_record_detail]
GO
/****** Object:  Table [dbo].[rec_nurse_record_mas]    Script Date: 09/22/2013 15:08:25 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_nurse_record_mas_rec_datetime]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_nurse_record_mas] DROP CONSTRAINT [DF_rec_nurse_record_mas_rec_datetime]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__rec_nurse__is_va__6477ECF3]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_nurse_record_mas] DROP CONSTRAINT [DF__rec_nurse__is_va__6477ECF3]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__rec_nurse__coole__640DD89F]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_nurse_record_mas] DROP CONSTRAINT [DF__rec_nurse__coole__640DD89F]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__rec_nurse__remar__6383C8BA]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_nurse_record_mas] DROP CONSTRAINT [DF__rec_nurse__remar__6383C8BA]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_record_mas]') AND type in (N'U'))
DROP TABLE [dbo].[rec_nurse_record_mas]
GO
/****** Object:  Table [dbo].[rec_nurse_shift]    Script Date: 09/22/2013 15:08:25 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF__rec_nurse__is_va__07020F21]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_nurse_shift] DROP CONSTRAINT [DF__rec_nurse__is_va__07020F21]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_nurse_shift_rec_datetime]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_nurse_shift] DROP CONSTRAINT [DF_rec_nurse_shift_rec_datetime]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_shift]') AND type in (N'U'))
DROP TABLE [dbo].[rec_nurse_shift]
GO
/****** Object:  Table [dbo].[rec_pat_event]    Script Date: 09/22/2013 15:08:24 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_pat_event_event_datetime]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_pat_event] DROP CONSTRAINT [DF_rec_pat_event_event_datetime]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_pat_event]') AND type in (N'U'))
DROP TABLE [dbo].[rec_pat_event]
GO
/****** Object:  Table [dbo].[rec_skin_test]    Script Date: 09/22/2013 15:08:24 ******/
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_skin_test_test_result]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_skin_test] DROP CONSTRAINT [DF_rec_skin_test_test_result]
END
GO
IF  EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[DF_rec_skin_test_test_datetime]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[rec_skin_test] DROP CONSTRAINT [DF_rec_skin_test_test_datetime]
END
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_skin_test]') AND type in (N'U'))
DROP TABLE [dbo].[rec_skin_test]
GO
/****** Object:  Table [dbo].[sys_dept]    Script Date: 09/22/2013 15:08:24 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_dept]') AND type in (N'U'))
DROP TABLE [dbo].[sys_dept]
GO
/****** Object:  Table [dbo].[sys_employee]    Script Date: 09/22/2013 15:08:24 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_employee]') AND type in (N'U'))
DROP TABLE [dbo].[sys_employee]
GO
/****** Object:  Table [dbo].[sys_employee_serve]    Script Date: 09/22/2013 15:08:24 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_employee_serve]') AND type in (N'U'))
DROP TABLE [dbo].[sys_employee_serve]
GO
/****** Object:  Table [dbo].[sys_group]    Script Date: 09/22/2013 15:08:24 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_group]') AND type in (N'U'))
DROP TABLE [dbo].[sys_group]
GO
/****** Object:  Table [dbo].[sys_module]    Script Date: 09/22/2013 15:08:24 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_module]') AND type in (N'U'))
DROP TABLE [dbo].[sys_module]
GO
/****** Object:  Table [dbo].[sys_operate]    Script Date: 09/22/2013 15:08:24 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_operate]') AND type in (N'U'))
DROP TABLE [dbo].[sys_operate]
GO
/****** Object:  Table [dbo].[sys_permission]    Script Date: 09/22/2013 15:08:24 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_permission]') AND type in (N'U'))
DROP TABLE [dbo].[sys_permission]
GO
/****** Object:  Table [dbo].[sys_role]    Script Date: 09/22/2013 15:08:23 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_role]') AND type in (N'U'))
DROP TABLE [dbo].[sys_role]
GO
/****** Object:  Table [dbo].[sys_role_group]    Script Date: 09/22/2013 15:08:23 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_role_group]') AND type in (N'U'))
DROP TABLE [dbo].[sys_role_group]
GO
/****** Object:  Table [dbo].[sys_role_permission]    Script Date: 09/22/2013 15:08:23 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_role_permission]') AND type in (N'U'))
DROP TABLE [dbo].[sys_role_permission]
GO
/****** Object:  Table [dbo].[sys_user]    Script Date: 09/22/2013 15:08:23 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_user]') AND type in (N'U'))
DROP TABLE [dbo].[sys_user]
GO
/****** Object:  Table [dbo].[sys_user_finger]    Script Date: 09/22/2013 15:08:23 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_user_finger]') AND type in (N'U'))
DROP TABLE [dbo].[sys_user_finger]
GO
/****** Object:  Table [dbo].[sys_user_group]    Script Date: 09/22/2013 15:08:23 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_user_group]') AND type in (N'U'))
DROP TABLE [dbo].[sys_user_group]
GO
/****** Object:  Table [dbo].[sys_user_role]    Script Date: 09/22/2013 15:08:23 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_user_role]') AND type in (N'U'))
DROP TABLE [dbo].[sys_user_role]
GO
/****** Object:  Table [dbo].[sys_user_session]    Script Date: 09/22/2013 15:08:23 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_user_session]') AND type in (N'U'))
DROP TABLE [dbo].[sys_user_session]
GO
/****** Object:  Table [dbo].[sys_user_session]    Script Date: 09/22/2013 15:08:23 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_user_session]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_user_session](
	[session_id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[token] [varchar](64) NOT NULL,
	[logon_datetime] [datetime] NOT NULL,
	[last_op_datetime] [datetime] NOT NULL,
	[client_ip] [varchar](15) NOT NULL,
	[client_os] [varchar](30) NOT NULL,
	[alive] [int] NOT NULL,
 CONSTRAINT [PK_SYS_USER_SESSION] PRIMARY KEY CLUSTERED 
(
	[session_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[sys_user_role]    Script Date: 09/22/2013 15:08:23 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_user_role]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_user_role](
	[role_id] [int] NOT NULL,
	[user_id] [int] NOT NULL,
 CONSTRAINT [PK_SYS_USER_ROLE] PRIMARY KEY CLUSTERED 
(
	[role_id] ASC,
	[user_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[sys_user_group]    Script Date: 09/22/2013 15:08:23 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_user_group]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_user_group](
	[user_id] [int] NOT NULL,
	[group_id] [int] NOT NULL,
 CONSTRAINT [PK_SYS_USER_GROUP] PRIMARY KEY CLUSTERED 
(
	[user_id] ASC,
	[group_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[sys_user_finger]    Script Date: 09/22/2013 15:08:23 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_user_finger]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_user_finger](
	[fp_id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[fp_feature] [varchar](2048) NOT NULL,
	[sec_key] [varchar](50) NOT NULL,
	[create_user_id] [int] NOT NULL,
	[create_datetime] [datetime] NOT NULL,
	[modify_user_id] [int] NULL,
	[modify_datetime] [datetime] NULL,
	[deleted] [int] NOT NULL,
 CONSTRAINT [PK_SYS_USER_FINGER] PRIMARY KEY CLUSTERED 
(
	[fp_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[sys_user]    Script Date: 09/22/2013 15:08:23 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_user]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_user](
	[user_id] [int] IDENTITY(1,1) NOT NULL,
	[empl_code] [varchar](15) NOT NULL,
	[login_name] [varchar](10) NOT NULL,
	[password] [varchar](16) NOT NULL,
	[name] [varchar](20) NOT NULL,
	[gender] [varchar](1) NOT NULL,
	[birthday] [datetime] NULL,
	[phone] [varchar](20) NULL,
	[email] [varchar](30) NULL,
	[create_user_id] [int] NOT NULL,
	[create_datetime] [datetime] NOT NULL,
	[modify_user_id] [int] NULL,
	[modify_datetime] [datetime] NULL,
	[remark] [varchar](50) NULL,
	[valid] [int] NOT NULL,
 CONSTRAINT [PK_SYS_USER] PRIMARY KEY CLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [uniq_login_name] UNIQUE NONCLUSTERED 
(
	[login_name] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[sys_role_permission]    Script Date: 09/22/2013 15:08:23 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_role_permission]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_role_permission](
	[role_id] [int] NOT NULL,
	[module_id] [int] NOT NULL,
	[operate_code] [varchar](15) NOT NULL,
	[valid_time] [int] NOT NULL,
 CONSTRAINT [PK_SYS_ROLE_PERMISSION] PRIMARY KEY CLUSTERED 
(
	[role_id] ASC,
	[module_id] ASC,
	[operate_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[sys_role_group]    Script Date: 09/22/2013 15:08:23 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_role_group]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_role_group](
	[role_id] [int] NOT NULL,
	[group_id] [int] NOT NULL,
 CONSTRAINT [PK_SYS_ROLE_GROUP] PRIMARY KEY CLUSTERED 
(
	[role_id] ASC,
	[group_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[sys_role]    Script Date: 09/22/2013 15:08:23 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_role]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_role](
	[role_id] [int] IDENTITY(1,1) NOT NULL,
	[role_code] [varchar](20) NOT NULL,
	[role_name] [varchar](30) NOT NULL,
	[create_user_id] [int] NOT NULL,
	[create_datetime] [datetime] NOT NULL,
	[modify_user_id] [int] NULL,
	[modify_datetime] [datetime] NULL,
	[description] [varchar](50) NULL,
 CONSTRAINT [PK_SYS_ROLE] PRIMARY KEY CLUSTERED 
(
	[role_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[sys_permission]    Script Date: 09/22/2013 15:08:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_permission]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_permission](
	[permission_id] [int] IDENTITY(1,1) NOT NULL,
	[permission_name] [varchar](30) NOT NULL,
	[module_id] [int] NOT NULL,
	[operate_code] [varchar](15) NULL,
	[create_user_id] [int] NOT NULL,
	[create_datetime] [datetime] NOT NULL,
	[modify_user_id] [int] NULL,
	[modify_datetime] [datetime] NULL,
 CONSTRAINT [PK_PERMISSION] PRIMARY KEY CLUSTERED 
(
	[permission_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[sys_operate]    Script Date: 09/22/2013 15:08:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_operate]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_operate](
	[operate_code] [varchar](15) NOT NULL,
	[operate_name] [varchar](30) NULL,
	[description] [varchar](50) NULL,
 CONSTRAINT [PK_SYS_OPERATE] PRIMARY KEY CLUSTERED 
(
	[operate_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[sys_module]    Script Date: 09/22/2013 15:08:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_module]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_module](
	[module_id] [int] IDENTITY(1,1) NOT NULL,
	[module_code] [varchar](20) NOT NULL,
	[module_name] [varchar](30) NOT NULL,
	[module_parent_id] [int] NOT NULL,
	[create_user_id] [int] NOT NULL,
	[create_datetime] [datetime] NOT NULL,
	[modify_user_id] [int] NULL,
	[modify_datetime] [datetime] NULL,
	[description] [varchar](100) NULL,
 CONSTRAINT [PK_SYS_MODULE] PRIMARY KEY CLUSTERED 
(
	[module_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[sys_group]    Script Date: 09/22/2013 15:08:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_group]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_group](
	[group_id] [int] IDENTITY(1,1) NOT NULL,
	[group_name] [varchar](30) NOT NULL,
	[group_code] [varchar](15) NULL,
	[group_type] [int] NOT NULL,
	[create_user_id] [int] NOT NULL,
	[create_datetime] [datetime] NOT NULL,
	[modify_user_id] [int] NULL,
	[modify_datetime] [datetime] NULL,
	[description] [varchar](50) NULL,
 CONSTRAINT [PK_SYS_GROUP] PRIMARY KEY CLUSTERED 
(
	[group_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[sys_employee_serve]    Script Date: 09/22/2013 15:08:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_employee_serve]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_employee_serve](
	[empl_code] [varchar](6) NOT NULL,
	[dept_code] [varchar](4) NOT NULL,
	[ward_code] [varchar](4) NULL
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[sys_employee]    Script Date: 09/22/2013 15:08:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_employee]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_employee](
	[empl_code] [varchar](6) NOT NULL,
	[empl_name] [varchar](10) NOT NULL,
	[gender] [varchar](1) NULL,
	[birthday] [datetime] NULL,
	[post_code] [varchar](2) NULL,
	[rank_code] [varchar](2) NULL,
	[empl_type] [varchar](2) NULL,
	[empl_name_py] [varchar](8) NULL,
	[empl_name_wb] [varchar](8) NULL,
 CONSTRAINT [PK_SYS_EMPLOYEE] PRIMARY KEY CLUSTERED 
(
	[empl_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[sys_dept]    Script Date: 09/22/2013 15:08:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sys_dept]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[sys_dept](
	[dept_code] [varchar](4) NOT NULL,
	[dept_name] [varchar](16) NOT NULL,
	[dept_name_py] [varchar](8) NULL,
	[is_in_use] [int] NULL,
	[create_date] [datetime] NULL,
 CONSTRAINT [PK_SYS_DEPT] PRIMARY KEY CLUSTERED 
(
	[dept_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[rec_skin_test]    Script Date: 09/22/2013 15:08:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_skin_test]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_skin_test](
	[rec_skin_test_id] [int] IDENTITY(1,1) NOT NULL,
	[pat_id] [int] NOT NULL,
	[pat_name] [varchar](20) NOT NULL,
	[test_nurse_code] [varchar](6) NOT NULL,
	[test_result] [varchar](1) NOT NULL CONSTRAINT [DF_rec_skin_test_test_result]  DEFAULT ('N'),
	[test_datetime] [datetime] NOT NULL CONSTRAINT [DF_rec_skin_test_test_datetime]  DEFAULT (getdate()),
	[drug_code] [varchar](12) NOT NULL,
	[drug_name] [varchar](80) NOT NULL,
	[order_group_id] [int] NULL,
	[order_exec_id] [int] NULL,
	[body_sign_mas_id] [int] NULL,
	[drug_batch_no] [varchar](30) NULL,
	[test_nurse_name] [varchar](10) NULL,
	[approve_nurse_code] [varchar](6) NULL,
	[approve_nurse_name] [varchar](10) NULL,
 CONSTRAINT [PK__rec_skin__0F0746136E765879] PRIMARY KEY CLUSTERED 
(
	[rec_skin_test_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[rec_skin_test]') AND name = N'IX_rec_skin_test')
CREATE NONCLUSTERED INDEX [IX_rec_skin_test] ON [dbo].[rec_skin_test] 
(
	[pat_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[rec_pat_event]    Script Date: 09/22/2013 15:08:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_pat_event]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_pat_event](
	[rec_pat_event_id] [int] IDENTITY(1,1) NOT NULL,
	[pat_id] [int] NOT NULL,
	[pat_name] [varchar](10) NOT NULL,
	[body_sign_mas_id] [int] NOT NULL,
	[event_datetime] [datetime] NOT NULL CONSTRAINT [DF_rec_pat_event_event_datetime]  DEFAULT (getdate()),
	[rec_nurse_code] [varchar](6) NOT NULL,
	[rec_nurse_name] [varchar](10) NULL,
	[confirm_nurse_code] [varchar](6) NULL,
	[confirm_nurse_name] [varchar](10) NULL,
	[problem] [varchar](30) NULL,
	[interv] [varchar](30) NULL,
	[outcome] [varchar](30) NULL,
	[event_code] [varchar](10) NULL,
	[event_name] [varchar](20) NULL,
 CONSTRAINT [PK__rec_pat___2E9A67A8742F31CF] PRIMARY KEY CLUSTERED 
(
	[rec_pat_event_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[rec_pat_event]') AND name = N'IX_rec_pat_event')
CREATE NONCLUSTERED INDEX [IX_rec_pat_event] ON [dbo].[rec_pat_event] 
(
	[pat_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[rec_nurse_shift]    Script Date: 09/22/2013 15:08:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_shift]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_nurse_shift](
	[rec_nurse_shift_id] [int] IDENTITY(1,1) NOT NULL,
	[pat_id] [int] NOT NULL,
	[rec_dept_code] [varchar](4) NOT NULL,
	[rec_nurse_code] [varchar](6) NOT NULL,
	[is_valid] [tinyint] NOT NULL CONSTRAINT [DF__rec_nurse__is_va__07020F21]  DEFAULT ((1)),
	[rec_datetime] [datetime] NOT NULL CONSTRAINT [DF_rec_nurse_shift_rec_datetime]  DEFAULT (getdate()),
	[event_info] [varchar](500) NULL,
 CONSTRAINT [PK__rec_nurse_shift__060DEAE8] PRIMARY KEY CLUSTERED 
(
	[rec_nurse_shift_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_shift]') AND name = N'IX_rec_nurse_shift')
CREATE NONCLUSTERED INDEX [IX_rec_nurse_shift] ON [dbo].[rec_nurse_shift] 
(
	[pat_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[rec_nurse_record_mas]    Script Date: 09/22/2013 15:08:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_record_mas]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_nurse_record_mas](
	[nurse_record_mas_id] [int] IDENTITY(1,1) NOT NULL,
	[pat_id] [int] NOT NULL,
	[rec_datetime] [datetime] NOT NULL CONSTRAINT [DF_rec_nurse_record_mas_rec_datetime]  DEFAULT (getdate()),
	[rec_nurse_code] [varchar](6) NOT NULL,
	[is_valid] [tinyint] NOT NULL CONSTRAINT [DF__rec_nurse__is_va__6477ECF3]  DEFAULT ((1)),
	[cooled] [tinyint] NOT NULL CONSTRAINT [DF__rec_nurse__coole__640DD89F]  DEFAULT ((0)),
	[record_type_code] [varchar](2) NULL,
	[remark] [varchar](50) NULL CONSTRAINT [DF__rec_nurse__remar__6383C8BA]  DEFAULT (NULL),
 CONSTRAINT [PK__rec_nurse_record__628FA481] PRIMARY KEY CLUSTERED 
(
	[nurse_record_mas_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_record_mas]') AND name = N'IX_rec_nurse_record_mas')
CREATE NONCLUSTERED INDEX [IX_rec_nurse_record_mas] ON [dbo].[rec_nurse_record_mas] 
(
	[pat_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[rec_nurse_record_detail]    Script Date: 09/22/2013 15:08:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_record_detail]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_nurse_record_detail](
	[nurse_record_detail_id] [int] IDENTITY(1,1) NOT NULL,
	[nurse_record_mas_id] [int] NOT NULL,
	[item_code] [varchar](20) NULL,
	[item_value_code] [varchar](20) NULL,
	[item_value] [varchar](20) NULL,
	[measure_note_code] [varchar](10) NULL,
	[measure_note_name] [varchar](400) NULL,
 CONSTRAINT [PK__rec_nurs__6C3B816773852659] PRIMARY KEY CLUSTERED 
(
	[nurse_record_detail_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_record_detail]') AND name = N'IX_rec_nurse_record_detail')
CREATE NONCLUSTERED INDEX [IX_rec_nurse_record_detail] ON [dbo].[rec_nurse_record_detail] 
(
	[nurse_record_mas_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[rec_nurse_item_mas]    Script Date: 09/22/2013 15:08:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_item_mas]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_nurse_item_mas](
	[nurse_item_mas_id] [int] IDENTITY(1,1) NOT NULL,
	[pat_id] [int] NOT NULL,
	[rec_datetime] [datetime] NOT NULL CONSTRAINT [DF_rec_nurse_item_mas_rec_datetime]  DEFAULT (getdate()),
	[rec_nurse_code] [varchar](6) NOT NULL,
	[is_valid] [tinyint] NOT NULL CONSTRAINT [DF__rec_nurse__is_va__60A75C0F]  DEFAULT ((1)),
	[remark] [varchar](50) NULL CONSTRAINT [DF__rec_nurse__remar__5FB337D6]  DEFAULT (NULL),
 CONSTRAINT [PK__rec_nurse_item_m__5EBF139D] PRIMARY KEY CLUSTERED 
(
	[nurse_item_mas_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_item_mas]') AND name = N'IX_rec_nurse_item_mas')
CREATE NONCLUSTERED INDEX [IX_rec_nurse_item_mas] ON [dbo].[rec_nurse_item_mas] 
(
	[pat_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[rec_nurse_item_detail]    Script Date: 09/22/2013 15:08:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_item_detail]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_nurse_item_detail](
	[nurse_item_detail_id] [int] IDENTITY(1,1) NOT NULL,
	[nurse_item_mas_id] [int] NOT NULL,
	[nurse_item_code] [varchar](10) NULL,
PRIMARY KEY CLUSTERED 
(
	[nurse_item_detail_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[rec_nurse_item_detail]') AND name = N'IX_rec_nurse_item_detail')
CREATE NONCLUSTERED INDEX [IX_rec_nurse_item_detail] ON [dbo].[rec_nurse_item_detail] 
(
	[nurse_item_mas_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[rec_lab_test_mas]    Script Date: 09/22/2013 15:08:25 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_lab_test_mas]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_lab_test_mas](
	[lab_test_mas_id] [int] IDENTITY(1,1) NOT NULL,
	[pat_id] [int] NOT NULL,
	[pat_hosp_no] [varchar](10) NOT NULL,
	[order_group_id] [int] NOT NULL,
	[order_exec_id] [int] NULL,
	[lab_test_status] [varchar](1) NOT NULL CONSTRAINT [DF_rec_lab_test_mas_lab_test_status]  DEFAULT ('R'),
	[exec_datetime] [datetime] NOT NULL CONSTRAINT [DF_rec_lab_test_mas_exec_datetime]  DEFAULT (getdate()),
	[exec_dept_code] [varchar](4) NOT NULL,
	[test_subject] [varchar](20) NULL,
	[test_specimen] [varchar](20) NULL,
	[reporter_code] [varchar](10) NULL,
	[confirmer_code] [varchar](10) NULL,
	[record_flag] [int] NULL,
 CONSTRAINT [PK__rec_lab_test_mas__7E6CC920] PRIMARY KEY CLUSTERED 
(
	[lab_test_mas_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[rec_lab_test_mas]') AND name = N'IX_rec_lab_test_mas')
CREATE NONCLUSTERED INDEX [IX_rec_lab_test_mas] ON [dbo].[rec_lab_test_mas] 
(
	[pat_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[rec_lab_test_detail]    Script Date: 09/22/2013 15:08:26 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_lab_test_detail]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_lab_test_detail](
	[lab_test_detail_id] [int] IDENTITY(1,1) NOT NULL,
	[lab_test_mas_id] [int] NOT NULL,
	[item_code] [varchar](20) NULL,
	[item_name] [varchar](40) NULL,
	[result_value] [varchar](20) NULL,
	[result_unit] [varchar](20) NULL,
	[ref_ranges] [varchar](20) NULL,
	[normal_flag] [varchar](1) NOT NULL CONSTRAINT [DF_rec_lab_test_detail_normal_flag]  DEFAULT ('N'),
	[inst_code] [varchar](16) NULL,
	[inst_name] [varchar](16) NULL,
 CONSTRAINT [PK__rec_lab_test_det__5441852A] PRIMARY KEY CLUSTERED 
(
	[lab_test_detail_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[rec_inspection_mas]    Script Date: 09/22/2013 15:08:26 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_inspection_mas]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_inspection_mas](
	[insp_mas_id] [int] IDENTITY(1,1) NOT NULL,
	[insp_subject] [varchar](30) NULL,
	[pat_id] [int] NOT NULL,
	[applicant] [varchar](10) NULL,
	[reporter] [varchar](10) NULL,
	[auditor] [varchar](10) NULL,
	[insp_datetime] [datetime] NOT NULL,
	[report_datetime] [datetime] NOT NULL,
 CONSTRAINT [PK_rec_inspection_mas] PRIMARY KEY CLUSTERED 
(
	[insp_mas_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[rec_inspection_detail]    Script Date: 09/22/2013 15:08:26 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_inspection_detail]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_inspection_detail](
	[insp_detail_id] [int] IDENTITY(1,1) NOT NULL,
	[insp_mas_id] [int] NOT NULL,
	[body_parts] [varchar](20) NOT NULL,
	[insp_result] [varchar](100) NOT NULL,
	[insp_suggestion] [varchar](100) NULL,
 CONSTRAINT [PK_rec_inspection_detail] PRIMARY KEY CLUSTERED 
(
	[insp_detail_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[rec_infu_monitor]    Script Date: 09/22/2013 15:08:26 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_infu_monitor]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_infu_monitor](
	[infu_monitor_id] [int] IDENTITY(1,1) NOT NULL,
	[pat_id] [int] NOT NULL,
	[rec_datetime] [datetime] NOT NULL,
	[rec_nurse_code] [varchar](6) NOT NULL,
	[order_exec_id] [int] NOT NULL,
	[pat_name] [varchar](10) NULL,
	[rec_nurse_name] [varchar](10) NULL,
	[abnormal] [tinyint] NOT NULL CONSTRAINT [DF__rec_infu___abnor__3D5E1FD2]  DEFAULT ((0)),
	[deliver_speed] [int] NULL,
	[anomaly_msg] [varchar](20) NULL,
	[anomaly_disposal] [varchar](20) NULL,
	[deliver_speed_unit] [varchar](10) NULL,
 CONSTRAINT [PK__rec_infu_monitor__3C69FB99] PRIMARY KEY CLUSTERED 
(
	[infu_monitor_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[rec_infu_monitor]') AND name = N'IX_rec_infu_monitor')
CREATE NONCLUSTERED INDEX [IX_rec_infu_monitor] ON [dbo].[rec_infu_monitor] 
(
	[pat_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[rec_diag]    Script Date: 09/22/2013 15:08:26 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_diag]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_diag](
	[rec_diag_id] [int] IDENTITY(1,1) NOT NULL,
	[pat_id] [int] NOT NULL,
	[icd_code] [varchar](50) NOT NULL,
	[diag_name] [varchar](150) NOT NULL,
	[is_main] [tinyint] NOT NULL CONSTRAINT [DF_rec_diag_is_main]  DEFAULT ((1)),
	[rec_date] [datetime] NOT NULL CONSTRAINT [DF_rec_diag_rec_date]  DEFAULT (getdate()),
	[is_valid] [tinyint] NOT NULL CONSTRAINT [DF__rec_diag__is_val__75A278F5]  DEFAULT ((1)),
	[rec_empl_code] [varchar](6) NOT NULL,
	[diag_kind] [varchar](2) NULL,
	[priority] [int] NULL,
	[modify_date] [datetime] NULL,
	[modify_empl_code] [varchar](6) NULL,
 CONSTRAINT [PK__rec_diag__74AE54BC] PRIMARY KEY CLUSTERED 
(
	[rec_diag_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[rec_diag]') AND name = N'IX_rec_diag')
CREATE NONCLUSTERED INDEX [IX_rec_diag] ON [dbo].[rec_diag] 
(
	[pat_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'rec_diag', N'COLUMN',N'is_main'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'is it main diagnosis' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rec_diag', @level2type=N'COLUMN',@level2name=N'is_main'
GO

/****** Object:  Table [dbo].[rec_body_sign_mas]    Script Date: 09/22/2013 15:08:26 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_body_sign_mas]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_body_sign_mas](
	[body_sign_mas_id] [int] IDENTITY(1,1) NOT NULL,
	[pat_id] [int] NOT NULL,
	[cooled] [tinyint] NOT NULL CONSTRAINT [DF_rec_body_sign_mas_cooled]  DEFAULT ((0)),
	[rec_datetime] [datetime] NOT NULL CONSTRAINT [DF_rec_body_sign_mas_rec_datetime]  DEFAULT (getdate()),
	[rec_nurse_code] [varchar](6) NOT NULL,
	[remark] [varchar](50) NULL,
 CONSTRAINT [PK__rec_body__AFE269F277FFC2B3] PRIMARY KEY CLUSTERED 
(
	[body_sign_mas_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[rec_body_sign_mas]') AND name = N'IX_rec_body_sign_mas')
CREATE NONCLUSTERED INDEX [IX_rec_body_sign_mas] ON [dbo].[rec_body_sign_mas] 
(
	[pat_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[rec_body_sign_detail]    Script Date: 09/22/2013 15:08:26 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_body_sign_detail]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_body_sign_detail](
	[body_sign_detail_id] [int] IDENTITY(1,1) NOT NULL,
	[body_sign_mas_id] [int] NOT NULL,
	[item_code] [varchar](20) NULL,
	[item_value] [varchar](10) NULL,
	[measure_note_code] [varchar](10) NULL,
	[measure_note_name] [varchar](20) NULL,
	[abnormal_flag] [tinyint] NOT NULL CONSTRAINT [DF_rec_body_sign_detail_abnormal_flag]  DEFAULT ((0)),
 CONSTRAINT [PK_rec_body_sign_detail] PRIMARY KEY CLUSTERED 
(
	[body_sign_detail_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[rec_body_sign_detail]') AND name = N'IX_rec_body_sign_detail')
CREATE NONCLUSTERED INDEX [IX_rec_body_sign_detail] ON [dbo].[rec_body_sign_detail] 
(
	[body_sign_mas_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[rec_allergy]    Script Date: 09/22/2013 15:08:27 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[rec_allergy]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[rec_allergy](
	[rec_allergy_id] [int] IDENTITY(1,1) NOT NULL,
	[allergen_code] [varchar](12) NOT NULL,
	[pat_id] [int] NOT NULL,
	[allergy_degree] [varchar](1) NOT NULL CONSTRAINT [DF_rec_allergy_allergy_degree]  DEFAULT ((0)),
	[allergen_name] [varchar](80) NULL,
	[is_valid] [tinyint] NOT NULL CONSTRAINT [DF__rec_aller__is_va__72C60C4A]  DEFAULT ((1)),
	[rec_date] [datetime] NOT NULL CONSTRAINT [DF_rec_allergy_rec_date]  DEFAULT (getdate()),
	[rec_empl_code] [varchar](6) NOT NULL,
	[priority] [int] NULL,
	[modify_date] [datetime] NULL,
	[modify_empl_code] [varchar](6) NULL,
 CONSTRAINT [PK__rec_allergy__71D1E811] PRIMARY KEY CLUSTERED 
(
	[rec_allergy_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[poc_bindings]    Script Date: 09/22/2013 15:08:27 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[poc_bindings]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[poc_bindings](
	[bindind_id] [int] IDENTITY(1,1) NOT NULL,
	[aor] [varchar](50) NULL,
	[contact] [varchar](150) NULL,
	[callid] [varchar](80) NULL,
	[cseq] [int] NULL,
	[expirationtime] [bigint] NULL,
 CONSTRAINT [PK__poc_bind__4C2351367DB89C09] PRIMARY KEY CLUSTERED 
(
	[bindind_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[phone_bindings]    Script Date: 09/22/2013 15:08:27 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[phone_bindings]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[phone_bindings](
	[binding_id] [int] IDENTITY(1,1) NOT NULL,
	[aor] [varchar](50) NULL,
	[contact] [varchar](150) NULL,
	[callid] [varchar](80) NULL,
	[cseq] [int] NULL,
	[expiration_time] [bigint] NULL,
 CONSTRAINT [PK__PHONE_BI__2F15A557160F4887] PRIMARY KEY CLUSTERED 
(
	[binding_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[pat_hosp]    Script Date: 09/22/2013 15:08:27 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[pat_hosp]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[pat_hosp](
	[in_hosp_no] [int] IDENTITY(1,1) NOT NULL,
	[pat_id] [int] NOT NULL,
	[bed_code] [varchar](10),
	[in_state] [char](1) NOT NULL CONSTRAINT [DF_pat_hosp_in_state]  DEFAULT ('R'),
	[tend_level] [char](1) NOT NULL CONSTRAINT [DF_pat_hosp_tend_level]  DEFAULT ((3)),
	[illness_status] [char](1) NOT NULL CONSTRAINT [DF_pat_hosp_illness_status]  DEFAULT ((0)),
	[charge_type] [char](1) NOT NULL CONSTRAINT [DF_pat_hosp_charge_type]  DEFAULT ((0)),
	[admit_datetime] [datetime] NULL,
	[admit_diagnosis] [varchar](20) NULL,
	[surgery_datetime] [datetime] NULL,
	[pat_barcode] [varchar](20) NULL,
	[out_datetime] [datetime] NULL,
	[diet_code] [varchar](20) NULL,
	[duty_doc_code] [varchar](10) NULL,
	[duty_doc_name] [varchar](20) NULL,
	[duty_nurse_code] [varchar](10) NULL,
	[duty_nurse_name] [varchar](20) NULL,
	[fee_paid] [decimal](10, 0) NULL,
	[fee_used] [decimal](10, 0) NULL,
	[trans_in_datetime] [datetime] NULL,
	[trans_out_datetime] [datetime] NULL,
	[receive_datetime] [datetime] NULL,
	[receive_nurse_code] [varchar](10) NULL,
	[out_card_no] [varchar](16) NULL,
	[his_pat_no] [varchar](16) NULL,
	[ward_code] [varchar](10) NULL,
	[ward_name] [nvarchar](8) NULL,
	[dept_code] [varchar](10) NULL,
	[dept_name] [varchar](16) NULL,
	[diet_name] [varchar](80) NULL,
 CONSTRAINT [PK__pat_hosp__1273C1CD] PRIMARY KEY CLUSTERED 
(
	[in_hosp_no] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[pat_hosp]') AND name = N'IX_pat_hosp')
CREATE NONCLUSTERED INDEX [IX_pat_hosp] ON [dbo].[pat_hosp] 
(
	[pat_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'pat_hosp', N'COLUMN',N'in_state'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'R-Registered I-In B-x O-Out P-Preordered N-new' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'pat_hosp', @level2type=N'COLUMN',@level2name=N'in_state'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'pat_hosp', N'COLUMN',N'tend_level'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'0-super 1-levelOne 2-levelTwo 3-levelThree' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'pat_hosp', @level2type=N'COLUMN',@level2name=N'tend_level'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'pat_hosp', N'COLUMN',N'illness_status'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'0-normal 1- 2-' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'pat_hosp', @level2type=N'COLUMN',@level2name=N'illness_status'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'pat_hosp', N'COLUMN',N'charge_type'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'charge type: 0-, 1- , 2- ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'pat_hosp', @level2type=N'COLUMN',@level2name=N'charge_type'
GO
/****** Object:  Table [dbo].[pat_base]    Script Date: 09/22/2013 15:08:27 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[pat_base]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[pat_base](
	[pat_id] [int] IDENTITY(1,1) NOT NULL,
	[pat_name] [varchar](20) NOT NULL,
	[pat_name_py] [varchar](5) NULL,
	[gender] [char](1) NOT NULL CONSTRAINT [DF_pat_base_gender]  DEFAULT ('U'),
	[birthday] [datetime] NULL,
	[is_married] [tinyint] NOT NULL CONSTRAINT [DF_pat_base_is_married]  DEFAULT ((0)),
	[education_level] [char](1) NOT NULL CONSTRAINT [DF_pat_base_education_level]  DEFAULT ((0)),
	[financial_level] [char](1) NOT NULL CONSTRAINT [DF_pat_base_financial_level]  DEFAULT ((0)),
	[insurance_no] [varchar](15) NULL,
	[medical_record_no] [varchar](15) NULL,
	[height] [int] NULL,
	[weight] [int] NULL,
	[id_card_no] [varchar](20) NULL,
	[career_code] [varchar](4) NULL,
	[birth_place_code] [varchar](2) NULL,
	[phone_no] [varchar](16) NULL,
 CONSTRAINT [PK__pat_base__014935CB] PRIMARY KEY CLUSTERED 
(
	[pat_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'pat_base', N'COLUMN',N'gender'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'M-Male F-Female U-Unknown' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'pat_base', @level2type=N'COLUMN',@level2name=N'gender'
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'pat_base', N'COLUMN',N'is_married'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'1-married 0-not' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'pat_base', @level2type=N'COLUMN',@level2name=N'is_married'
GO

/****** Object:  Table [dbo].[order_item]    Script Date: 09/22/2013 15:08:27 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[order_item]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[order_item](
	[order_item_id] [int] IDENTITY(1,1) NOT NULL,
	[order_group_id] [int] NOT NULL,
	[skt_order_group_id] [int] NULL,
	[item_code] [varchar](12) NOT NULL,
	[item_name] [varchar](80) NOT NULL,
	[dosage] [decimal](10, 2) NULL,
	[dosage_unit] [varchar](16) NULL,
	[skin_test_required] [tinyint] NOT NULL CONSTRAINT [DF_order_item_skin_test_required]  DEFAULT ((0)),
	[drug_specs] [varchar](32) NULL,
	[specimen_code] [varchar](20) NULL,
	[specimen_name] [varchar](30) NULL,
	[exam_loc_code] [varchar](20) NULL,
	[exam_loc_name] [varchar](30) NULL,
	[remark] [varchar](30) NULL,
 CONSTRAINT [PK__order_item__5CD6CB2B] PRIMARY KEY CLUSTERED 
(
	[order_item_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[order_item]') AND name = N'IX_order_item')
CREATE NONCLUSTERED INDEX [IX_order_item] ON [dbo].[order_item] 
(
	[order_group_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[order_group]    Script Date: 09/22/2013 15:08:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[order_group]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[order_group](
	[order_group_id] [int] IDENTITY(1,1) NOT NULL,
	[pat_id] [int] NOT NULL,
	[pat_name] [varchar](10) NOT NULL,
	[pat_bed_code] [varchar](10) NOT NULL,
	[freq_code] [varchar](10) NULL,
	[freq_name] [varchar](30) NULL,
	[order_type_code] [varchar](10) NULL,
	[order_type_name] [varchar](16) NULL,
	[order_exec_type_code] [varchar](10) NULL,
	[order_exec_type_name] [varchar](16) NULL,
	[create_datetime] [datetime] NULL,
	[create_doc_id] [varchar](10) NULL,
	[create_doc_name] [varchar](10) NULL,
	[stop_datetime] [datetime] NULL,
	[stop_doc_id] [varchar](10) NULL,
	[stop_doc_name] [varchar](10) NULL,
	[order_status_code] [char](1) NOT NULL CONSTRAINT [DF_order_group_order_status_code]  DEFAULT ((0)),
	[order_status_name] [varchar](16) NULL,
	[usage_code] [varchar](10) NULL,
	[usage_name] [varchar](16) NULL,
	[emergent] [tinyint] NOT NULL CONSTRAINT [DF_order_group_emergent]  DEFAULT ((0)),
	[his_comb_no] [varchar](14) NULL,
	[confirm_datetime] [datetime] NULL,
	[confirm_nurse_code] [varchar](10) NULL,
	[confirm_nurse_name] [varchar](10) NULL,
	[is_skin_test] [tinyint] NULL,
	[plan_exec_time] [varchar](80) NULL,
	[plan_first_exec_time] [varchar](5) NULL,
 CONSTRAINT [PK__order_group__5629CD9C] PRIMARY KEY CLUSTERED 
(
	[order_group_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO

/****** Object:  Table [dbo].[order_exec]    Script Date: 09/22/2013 15:08:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[order_exec]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[order_exec](
	[order_exec_id] [int] IDENTITY(1,1) NOT NULL,
	[order_group_id] [int] NOT NULL,
	[plan_datetime] [datetime] NOT NULL,
	[exec_datetime] [datetime] NULL,
	[exec_nurse_code] [varchar](6) NULL,
	[exec_nurse_name] [varchar](10) NULL,
	[finish_datetime] [datetime] NULL,
	[finish_nurse_code] [varchar](6) NULL,
	[finish_nurse_name] [varchar](10) NULL,
	[order_exec_barcode] [varchar](20) NULL,
	[deliver_speed] [int] NULL,
	[pda_exec_datetime] [datetime] NULL,
	[pda_exec_nurse_code] [varchar](6) NULL,
	[pda_exec_nurse_name] [varchar](10) NULL,
	[deliver_speed_unit] [varchar](10) NULL,
	[approve_nurse_code] [varchar](6) NULL,
	[approve_nurse_name] [varchar](10) NULL,
 CONSTRAINT [PK__order_exec__66603565] PRIMARY KEY CLUSTERED 
(
	[order_exec_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[order_exec]') AND name = N'IX_order_exec')
CREATE NONCLUSTERED INDEX [IX_order_exec] ON [dbo].[order_exec] 
(
	[order_group_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[item_price]    Script Date: 09/22/2013 15:08:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[item_price]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[item_price](
	[item_price_id] [int] IDENTITY(1,1) NOT NULL,
	[item_type] [varchar](1) NOT NULL CONSTRAINT [DF_item_price_item_type]  DEFAULT ((1)),
	[item_code] [varchar](12) NOT NULL,
	[item_price] [decimal](12, 2) NOT NULL,
	[begin_date] [datetime] NULL,
	[end_date] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[item_price_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO

/****** Object:  Table [dbo].[dict_ward]    Script Date: 09/22/2013 15:08:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_ward]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[dict_ward](
	[ward_code] [varchar](4) NOT NULL,
	[ward_name] [varchar](16) NOT NULL,
	[ward_name_py] [varchar](8) NULL,
	[is_in_use] [tinyint] NOT NULL CONSTRAINT [DF_dict_ward_is_in_use]  DEFAULT ((1)),
	[create_date] [datetime] NOT NULL CONSTRAINT [DF_dict_ward_create_date]  DEFAULT (getdate())
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[dict_ward]') AND name = N'IX_dict_ward')
CREATE UNIQUE NONCLUSTERED INDEX [IX_dict_ward] ON [dbo].[dict_ward] 
(
	[ward_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[dict_usage]    Script Date: 09/22/2013 15:08:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_usage]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[dict_usage](
	[usage_code] [varchar](10) NOT NULL,
	[usage_name] [varchar](30) NOT NULL,
	[is_valid] [tinyint] NOT NULL CONSTRAINT [DF__dict_usag__is_va__276EDEB3]  DEFAULT ((1)),
	[create_date] [datetime] NOT NULL CONSTRAINT [DF_dict_usage_create_date]  DEFAULT (getdate())
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[dict_usage]') AND name = N'IX_dict_usage')
CREATE UNIQUE NONCLUSTERED INDEX [IX_dict_usage] ON [dbo].[dict_usage] 
(
	[usage_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[dict_undrug]    Script Date: 09/22/2013 15:08:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_undrug]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[dict_undrug](
	[undrug_id] [int] IDENTITY(1,1) NOT NULL,
	[undrug_code] [varchar](12) NOT NULL,
	[undrug_name] [varchar](80) NOT NULL,
	[py_code] [varchar](18) NULL,
	[wb_code] [varchar](18) NULL,
	[order_class_code] [varchar](3) NOT NULL,
	[min_bill_unit] [varchar](10) NULL,
	[is_valid] [tinyint] NOT NULL CONSTRAINT [DF__dict_undr__is_va__0E6E26BF]  DEFAULT ((1)),
	[create_date] [datetime] NOT NULL CONSTRAINT [DF__dict_undr__creat__0F624AF8]  DEFAULT (getdate()),
 CONSTRAINT [PK__dict_undrug__0C85DE4D] PRIMARY KEY CLUSTERED 
(
	[undrug_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO

/****** Object:  Table [dbo].[dict_order_class]    Script Date: 09/22/2013 15:08:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_order_class]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[dict_order_class](
	[order_class_code] [varchar](3) NOT NULL,
	[order_class_name] [varchar](10) NOT NULL,
	[is_valid] [tinyint] NOT NULL CONSTRAINT [DF__dict_orde__is_va__1BFD2C07]  DEFAULT ((1)),
	[create_date] [datetime] NOT NULL CONSTRAINT [DF_dict_order_class_create_date]  DEFAULT (getdate())
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[dict_order_class]') AND name = N'IX_dict_order_class')
CREATE UNIQUE NONCLUSTERED INDEX [IX_dict_order_class] ON [dbo].[dict_order_class] 
(
	[order_class_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[dict_nurse_record_value]    Script Date: 09/22/2013 15:08:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_nurse_record_value]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[dict_nurse_record_value](
	[value_code] [varchar](20) NOT NULL,
	[value_name] [varchar](30) NOT NULL,
	[item_code] [varchar](20) NOT NULL,
	[is_valid] [tinyint] NOT NULL CONSTRAINT [DF_dict_nurse_record_value_is_valid]  DEFAULT ((1)),
	[seq_no] [int] NOT NULL CONSTRAINT [DF_dict_nurse_record_value_seq_no]  DEFAULT ((1))
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO

/****** Object:  Table [dbo].[dict_nurse_record]    Script Date: 09/22/2013 15:08:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_nurse_record]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[dict_nurse_record](
	[item_code] [varchar](20) NOT NULL,
	[item_name] [varchar](30) NOT NULL,
	[item_unit] [varchar](10) NULL,
	[parent_code] [varchar](20) NULL,
	[is_valid] [tinyint] NOT NULL CONSTRAINT [DF_dict_nurse_record_is_valid]  DEFAULT ((1)),
	[priority] [int] NULL
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[dict_nurse_record]') AND name = N'IX_dict_nurse_record')
CREATE UNIQUE NONCLUSTERED INDEX [IX_dict_nurse_record] ON [dbo].[dict_nurse_record] 
(
	[item_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[dict_nurse_item]    Script Date: 09/22/2013 15:08:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_nurse_item]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[dict_nurse_item](
	[nurse_item_code] [varchar](10) NOT NULL,
	[nurse_item_name] [varchar](20) NOT NULL,
	[nurse_item_type_code] [varchar](1) NOT NULL CONSTRAINT [DF_dict_nurse_item_nurse_item_type_code]  DEFAULT ('B'),
	[nurse_item_type_name] [varchar](20) NULL,
	[is_valid] [tinyint] NOT NULL CONSTRAINT [DF_dict_nurse_item_is_valid]  DEFAULT ((1))
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[dict_nurse_item]') AND name = N'IX_dict_nurse_item')
CREATE UNIQUE NONCLUSTERED INDEX [IX_dict_nurse_item] ON [dbo].[dict_nurse_item] 
(
	[nurse_item_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[dict_measure_rule]    Script Date: 09/22/2013 15:08:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_measure_rule]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[dict_measure_rule](
	[rule_id] [int] IDENTITY(1,1) NOT NULL,
	[rule_code] [varchar](10) NOT NULL,
	[rule_name] [varchar](20) NOT NULL,
	[rule_type] [varchar](10) NOT NULL,
	[rule_value] [varchar](10) NOT NULL,
	[rule_time] [varchar](100) NOT NULL,
 CONSTRAINT [PK_dict_measure_rule] PRIMARY KEY CLUSTERED 
(
	[rule_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[dict_measure_note]    Script Date: 09/22/2013 15:08:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_measure_note]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[dict_measure_note](
	[measure_note_code] [varchar](10) NOT NULL,
	[measure_note_name] [varchar](20) NOT NULL,
	[in_nurse_shift] [tinyint] NOT NULL CONSTRAINT [DF__dict_meas__in_nu__7E37BEF6]  DEFAULT ((0))
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[dict_measure_note]') AND name = N'IX_dict_measure_note')
CREATE UNIQUE NONCLUSTERED INDEX [IX_dict_measure_note] ON [dbo].[dict_measure_note] 
(
	[measure_note_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'dict_measure_note', N'COLUMN',N'in_nurse_shift'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'1-in, 0-not in' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'dict_measure_note', @level2type=N'COLUMN',@level2name=N'in_nurse_shift'
GO
/****** Object:  Table [dbo].[dict_lab_sample]    Script Date: 09/22/2013 15:08:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_lab_sample]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[dict_lab_sample](
	[lab_sample_code] [varchar](20) NOT NULL,
	[lab_sample_name] [varchar](30) NOT NULL,
	[priority] [int] NOT NULL CONSTRAINT [DF_dict_lab_sample_priority]  DEFAULT ((1)),
	[is_valid] [tinyint] NOT NULL CONSTRAINT [DF__dict_lab___is_va__787EE5A0]  DEFAULT ((1)),
	[create_date] [datetime] NOT NULL CONSTRAINT [DF_dict_lab_sample_create_date]  DEFAULT (getdate())
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[dict_lab_sample]') AND name = N'IX_dict_lab_sample')
CREATE UNIQUE NONCLUSTERED INDEX [IX_dict_lab_sample] ON [dbo].[dict_lab_sample] 
(
	[lab_sample_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[dict_freq]    Script Date: 09/22/2013 15:08:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_freq]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[dict_freq](
	[freq_code] [varchar](10) NOT NULL,
	[freq_name] [varchar](30) NOT NULL,
	[is_valid] [tinyint] NOT NULL CONSTRAINT [DF__dict_freq__is_va__20C1E124]  DEFAULT ((1)),
	[create_date] [datetime] NOT NULL CONSTRAINT [DF_dict_freq_create_date]  DEFAULT (getdate())
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[dict_freq]') AND name = N'IX_dict_freq')
CREATE UNIQUE NONCLUSTERED INDEX [IX_dict_freq] ON [dbo].[dict_freq] 
(
	[freq_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[dict_drug]    Script Date: 09/22/2013 15:08:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_drug]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[dict_drug](
	[drug_id] [int] IDENTITY(1,1) NOT NULL,
	[drug_code] [varchar](12) NOT NULL,
	[drug_name] [varchar](80) NOT NULL,
	[py_code] [varchar](18) NULL,
	[wb_code] [varchar](18) NULL,
	[drug_type] [char](1) NOT NULL CONSTRAINT [DF__dict_drug__drug___01142BA1]  DEFAULT ('P'),
	[dose_base] [decimal](12, 2) NULL CONSTRAINT [DF__dict_drug__dose___02084FDA]  DEFAULT (NULL),
	[dose_unit] [varchar](4) NULL CONSTRAINT [DF__dict_drug__dose___02FC7413]  DEFAULT (NULL),
	[drug_spec] [varchar](32) NULL CONSTRAINT [DF__dict_drug__drug___03F0984C]  DEFAULT (NULL),
	[pack_unit] [varchar](4) NULL CONSTRAINT [DF__dict_drug__pack___04E4BC85]  DEFAULT (NULL),
	[pack_qty] [int] NULL CONSTRAINT [DF__dict_drug__pack___05D8E0BE]  DEFAULT (NULL),
	[min_use_unit] [varchar](10) NULL CONSTRAINT [DF__dict_drug__min_u__06CD04F7]  DEFAULT (NULL),
	[min_bill_unit] [varchar](16) NULL CONSTRAINT [DF__dict_drug__min_b__07C12930]  DEFAULT (NULL),
	[order_class_code] [varchar](3) NULL CONSTRAINT [DF__dict_drug__order__08B54D69]  DEFAULT (NULL),
	[is_valid] [tinyint] NOT NULL CONSTRAINT [DF__dict_drug__is_va__09A971A2]  DEFAULT ((1)),
	[create_date] [datetime] NULL CONSTRAINT [DF__dict_drug__creat__0A9D95DB]  DEFAULT (NULL),
 CONSTRAINT [PK__dict_drug__00200768] PRIMARY KEY CLUSTERED 
(
	[drug_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'dict_drug', N'COLUMN',N'drug_type'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'C-Herb,Z-Chinese Phamacy, P-Western Phamacy' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'dict_drug', @level2type=N'COLUMN',@level2name=N'drug_type'
GO

/****** Object:  Table [dbo].[dict_diag]    Script Date: 09/22/2013 15:08:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_diag]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[dict_diag](
	[seq_no] [varchar](10) NOT NULL,
	[icd_code] [varchar](50) ,
	[py_code] [varchar](8) NULL,
	[wb_code] [varchar](8) NULL,
	[icd_name] [varchar](150) ,
	[infectious] [tinyint] DEFAULT ((0)),
	[cancerous] [tinyint] DEFAULT ((0)),
	[applied_gender] [varchar](1)  DEFAULT ('A'),
	[is_valid] [tinyint] DEFAULT ((1)),
 CONSTRAINT [PK__dict_diag__6D0D32F4] PRIMARY KEY CLUSTERED 
(
	[seq_no] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO

/****** Object:  Table [dbo].[dict_body_sign]    Script Date: 09/22/2013 15:08:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dict_body_sign]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[dict_body_sign](
	[item_code] [varchar](20) NOT NULL,
	[item_name] [varchar](20) NOT NULL,
	[item_unit] [varchar](10) NOT NULL
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[dict_body_sign]') AND name = N'IX_dict_body_sign')
CREATE UNIQUE NONCLUSTERED INDEX [IX_dict_body_sign] ON [dbo].[dict_body_sign] 
(
	[item_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[criticalvalue_info]    Script Date: 09/22/2013 15:08:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[criticalvalue_info]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[criticalvalue_info](
	[criticalvalue_info_id] [int] IDENTITY(1,1) NOT NULL,
	[pat_id] [int] NOT NULL,
	[recive_people] [int] NOT NULL,
	[bed_code] [varchar](20) NOT NULL,
	[in_hosp_no] [int] NOT NULL,
	[send_people] [varchar](20) NOT NULL,
	[send_time] [datetime] NOT NULL,
	[criticalvalue] [varchar](30) NULL,
 CONSTRAINT [PK_criticalvalue_info] PRIMARY KEY CLUSTERED 
(
	[criticalvalue_info_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[bed]    Script Date: 09/22/2013 15:08:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[bed]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[bed](
	[bed_code] [varchar](12) NOT NULL,
	[dept_code] [varchar](10) NULL,
	[ward_code] [varchar](10) NULL,
	[bed_type_code] [char](1) NULL,
	[bed_type_name] [varchar](10) NULL,
	[bed_price] [decimal](10, 0) NULL,
	[use_datetime] [datetime] NULL,
	[room_code] [varchar](10) NULL,
	[ward_name] [varchar](10) NULL,
	[dept_name] [varchar](10) NULL,
	[bed_status] [char](1) NOT NULL CONSTRAINT [DF_bed_bed_status]  DEFAULT ('U'),
	[duty_nurse_id] [varchar](10) NULL,
	[duty_nurse_name] [varchar](10) NULL,
	[nurse_cell_code] [varchar](4) NULL
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[bed]') AND name = N'IX_bed')
CREATE UNIQUE NONCLUSTERED INDEX [IX_bed] ON [dbo].[bed] 
(
	[bed_code] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
IF NOT EXISTS (SELECT * FROM ::fn_listextendedproperty(N'MS_Description' , N'SCHEMA',N'dbo', N'TABLE',N'bed', N'COLUMN',N'bed_status'))
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'U-Unoccupied, O-Occupied' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'bed', @level2type=N'COLUMN',@level2name=N'bed_status'
GO
/****** Object:  Table [dbo].[ass_nurse_record_dept]    Script Date: 09/22/2013 15:08:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ass_nurse_record_dept]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[ass_nurse_record_dept](
	[item_code] [varchar](20) NOT NULL,
	[dept_code] [varchar](10) NOT NULL
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ass_nurse_item_dept]    Script Date: 09/22/2013 15:08:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ass_nurse_item_dept]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[ass_nurse_item_dept](
	[nurse_item_code] [varchar](20) NOT NULL,
	[dept_code] [varchar](10) NOT NULL
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ward_patrol]    Script Date: 09/22/2013 15:08:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ward_patrol]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[ward_patrol](
	[ward_patrol_id] [int] IDENTITY(1,1) NOT NULL,
	[pat_id] [int] NOT NULL,
	[nurse_code] [varchar](6) NOT NULL,
	[dept_code] [varchar](4) NOT NULL,
	[tend_level] [char](1) NOT NULL CONSTRAINT [DF_ward_patrol_tend_level]  DEFAULT ((3)),
	[patrol_date] [datetime] NULL,
	[next_patrol_date] [datetime] NULL,
 CONSTRAINT [PK_ward_patrol] PRIMARY KEY CLUSTERED 
(
	[ward_patrol_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[ward_patrol]') AND name = N'IX_ward_patrol')
CREATE NONCLUSTERED INDEX [IX_ward_patrol] ON [dbo].[ward_patrol] 
(
	[pat_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[vital_sign_info]    Script Date: 09/22/2013 15:08:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[vital_sign_info]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[vital_sign_info](
	[vital_sign_info_id] [int] IDENTITY(0,1) NOT NULL,
	[exec_nurse] [varchar](20) NOT NULL,
	[exec_time] [datetime] NOT NULL,
	[patient_id] [varchar](15) NOT NULL,
	[create_time] [datetime] NOT NULL CONSTRAINT [DF_vital_sign_info_create_time]  DEFAULT (getdate()),
	[temperature] [varchar](20) NULL,
	[temperature_value] [varchar](10) NULL,
	[temperature_flag] [smallint] NULL,
	[temperature_unit] [varchar](10) NOT NULL CONSTRAINT [DF_vital_sign_info_temperature_unit]  DEFAULT ('��'),
	[pluse] [varchar](20) NULL,
	[pluse_value] [varchar](10) NULL,
	[pluse_unit] [varchar](10) NOT NULL CONSTRAINT [DF_vital_sign_info_pluse_unit]  DEFAULT ('��/��'),
	[hr] [varchar](20) NULL,
	[hr_value] [varchar](10) NULL,
	[hr_unit] [varchar](10) NOT NULL CONSTRAINT [DF_vital_sign_info_hr_unit]  DEFAULT ('��/��'),
	[breath] [varchar](20) NULL,
	[breath_value] [varchar](20) NULL,
	[breath_unit] [varchar](10) NOT NULL CONSTRAINT [DF_vital_sign_info_breath_unit]  DEFAULT ('��/��'),
	[bp] [varchar](20) NULL,
	[bp_value] [varchar](20) NULL,
	[bp_unit] [varchar](10) NOT NULL CONSTRAINT [DF_vital_sign_info_bp_unit]  DEFAULT ('mmHg'),
	[in_take] [varchar](20) NULL,
	[in_take_value] [varchar](20) NULL,
	[in_take_unit] [varchar](10) NOT NULL CONSTRAINT [DF_vital_sign_info_in_take_unit]  DEFAULT ('ml'),
	[urine] [varchar](20) NULL,
	[urine_value] [varchar](20) NULL,
	[urine_unit] [varchar](10) NOT NULL CONSTRAINT [DF_vital_sign_info_urine_unit]  DEFAULT ('ml'),
	[other_out_put] [varchar](20) NULL,
	[other_out_put_value] [varchar](20) NULL,
	[other_out_put_unit] [varchar](10) NOT NULL CONSTRAINT [DF_vital_sign_info_other_out_put_unit]  DEFAULT ('ml'),
	[total_out_put] [varchar](20) NULL,
	[total_out_put_value] [varchar](20) NULL,
	[total_out_put_unit] [varchar](10) NOT NULL CONSTRAINT [DF_vital_sign_info_total_out_put_unit]  DEFAULT ('ml'),
	[shit] [varchar](20) NULL,
	[shit_value] [varchar](20) NULL,
	[shit_unit] [varchar](10) NOT NULL CONSTRAINT [DF_vital_sign_info_shit_unit]  DEFAULT ('��'),
	[height] [varchar](20) NULL,
	[height_value] [varchar](20) NULL,
	[height_unit] [varchar](10) NOT NULL CONSTRAINT [DF_vital_sign_info_height_unit]  DEFAULT ('cm'),
	[weight] [varchar](20) NULL,
	[weight_value] [varchar](20) NULL,
	[weight_unit] [varchar](10) NOT NULL CONSTRAINT [DF_vital_sign_info_weight_unit]  DEFAULT ('kg'),
	[skin_test] [varchar](20) NULL,
	[skin_test_value] [varchar](30) NULL,
	[other_value] [varchar](50) NULL,
	[event] [varchar](50) NULL,
	[event_value] [varchar](50) NULL,
	[in_patient_num] [varchar](20) NULL,
	[remark_value] [varchar](100) NULL
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  StoredProcedure [dbo].[uih_sp_bodysign_speedy]    Script Date: 09/22/2013 15:08:32 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[uih_sp_bodysign_speedy]') AND type in (N'P', N'PC'))
BEGIN
EXEC dbo.sp_executesql @statement = N'CREATE   PROCEDURE  [dbo].[uih_sp_bodysign_speedy]
	@dept_code int -- dept id
AS

BEGIN
    select pb.pat_name,ph.bed_code,ph.in_hosp_no,ph.tend_level,ph.admit_datetime,
    CASE 
       
        WHEN datediff(day,ph.admit_datetime,getDate())<=3   
             THEN datediff(day,ph.admit_datetime,getDate())
        ELSE -1     
    END,
    ''days_aftersurgery'' =
    CASE     
        WHEN datediff(day,ph.surgery_datetime,getDate())<=3 
             THEN datediff(day,ph.surgery_datetime,getDate())
        ELSE -1
    END,
    ''last_msmenttime'' =
    CASE
        WHEN 
			(select top 1 bm.rec_datetime from rec_body_sign_mas bm
			left join rec_body_sign_detail bd on(bm.body_sign_mas_id=bd.body_sign_mas_id)
			where bm.pat_id=ph.pat_id
			and bd.item_code=''temperature'' 
			and item_value>=(select temp_rule_value from temp_rule where temp_rule_code=''highTemp'')
			order by bm.rec_datetime desc) is not null
            THEN (select top 1 bm.rec_datetime from rec_body_sign_mas bm
			left join rec_body_sign_detail bd on(bm.body_sign_mas_id=bd.body_sign_mas_id)
			where bm.pat_id=ph.pat_id
			and bd.item_code=''temperature'' 
			and item_value>=(select temp_rule_value from temp_rule where temp_rule_code=''highTemp'')
			order by bm.rec_datetime desc)
    END
    from pat_hosp ph
	left join pat_base pb on(ph.pat_id=pb.pat_id)
	where ph.dept_code=''1005''
	order by ph.tend_level asc
	
END' 
END
GO
/****** Object:  UserDefinedFunction [dbo].[count_unexec_orders]    Script Date: 09/22/2013 15:08:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[count_unexec_orders]') AND type in (N'FN', N'IF', N'TF', N'FS', N'FT'))
BEGIN
execute dbo.sp_executesql @statement = N'-- =============================================
-- Author:		wenhuan.cui
-- Create date: 2013-3-8 14:50
-- Description:	count unexecuted orders for a patient

-- test:
-- exec uih_sp_count_unexec_orders ''122''
-- =============================================
CREATE FUNCTION [dbo].[count_unexec_orders]
(
	@pat_id int -- patient id
)
RETURNS int
AS
BEGIN
	-- Declare the return variable here
	DECLARE @unexec_count int

	-- Add the T-SQL statements to compute the return value here
	SELECT @unexec_count = (SELECT COUNT(e.order_exec_id) AS unexec_order_count
		FROM order_exec e
		LEFT JOIN order_group g
		ON g.order_group_id = e.order_group_id
		LEFT JOIN pat_base pb
		ON pb.pat_id = g.pat_id
		WHERE pb.pat_id = @pat_id
		AND	e.pda_exec_datetime is NULL								-- PDA execute datetime
		AND e.exec_nurse_code IS NOT NULL							-- HIS nurse code
 		AND (g.stop_datetime IS NULL OR getdate()<g.stop_datetime)	 
		 
			
			)

	RETURN @unexec_count

END
' 
END
GO

/** ����С�װ�  **/
/****** Object:  Table [dbo].[task_nurse_attention]    Script Date: 08/02/2014 10:48:32 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[task_nurse_attention](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[nurse_id] [int] NOT NULL,
	[patient_id] [int] NOT NULL,
 CONSTRAINT [PK__task_nur__id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

/****** Object:  Table [dbo].[task_white_record]    Script Date: 08/02/2014 10:52:49 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING OFF
GO

CREATE TABLE [dbo].[task_white_record](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[nurse_id] [int] NOT NULL,
	[nurse_name] [varchar](20) NOT NULL,
	[receive_area] [varchar](20) NOT NULL,
	[receive_id] [varchar](50) NULL,
	[create_time] [datetime] NOT NULL,
	[msg_text] [varchar](1000) NULL,
 CONSTRAINT [PK__task_white__id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

/****** Object:  Table [dbo].[task_white_record_item]    Script Date: 08/02/2014 10:53:10 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING OFF
GO

CREATE TABLE [dbo].[task_white_record_item](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[record_id] [bigint] NOT NULL,
	[send_type] [varchar](3) NOT NULL,
	[data] [image] NOT NULL,
 CONSTRAINT [PK__task_white_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[task_white_record_item]  WITH CHECK ADD  CONSTRAINT [FK_task_white_record_item_task_to_parent] FOREIGN KEY([record_id])
REFERENCES [dbo].[task_white_record] ([id])
GO

ALTER TABLE [dbo].[task_white_record_item] CHECK CONSTRAINT [FK_task_white_record_item_task_to_parent]
GO

/**** ֵ����Ϣ  ****/
/****** Object:  Table [dbo].[hosp_duty]    Script Date: 08/02/2014 13:40:58 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[hosp_duty](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[dept_id] [int] NOT NULL,
	[user_id] [int] NULL,
	[user_name] [varchar](20) NULL,
	[tel] [varchar](30) NOT NULL,
	[type] [varchar](10) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

/****** Object:  Table [dbo].[order_freq_plan_time]    Script Date: 09/03/2014 13:52:49 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING OFF
GO

CREATE TABLE [dbo].[order_freq_plan_time](
	[freq_code]  [varchar](10) NOT NULL,
	[dept_code]  [varchar](10) NOT NULL,
	[dept_name] [varchar](16) NULL,
	[plan_time] [varchar](80) NULL,
	[create_time] [datetime]  NULL,
 CONSTRAINT [PK__order_freq_plan_time] PRIMARY KEY CLUSTERED 
(
	[freq_code] , [dept_code] 
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE dbo.rec_lab_test_mas ADD
	pri_flag int NULL
GO
ALTER TABLE dbo.rec_inspection_mas ADD
    status varchar(10) NULL,
	pri_flag int NULL
GO
ALTER TABLE dbo.criticalvalue_info ADD
	disponer varchar(10) NULL,
	dispone_datetime datetime NULL
GO