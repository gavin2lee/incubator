ALTER TABLE paasos.sys_user ADD mobile VARCHAR(11);

drop view openbridge.sys_user; 
CREATE VIEW openbridge.`sys_user`  AS (SELECT * FROM paasos.`sys_user`);