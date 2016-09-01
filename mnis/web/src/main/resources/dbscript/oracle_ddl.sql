alter table LX_METADATA add constraint UK_LX_METADATA_CODE unique (METADATA_CODE);

commit;