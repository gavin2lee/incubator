-- 删除已完成的文书
delete from DEPARTMENT_TEMPLATE where TEMPLATE_REFID in (select t.REFID from TEMPLATE t where t.TEMPLATE_NAME like '护理%');
-- 使用约束带观察记录表,住院患者健康教育及评估单,住院患者疼痛评估记录单
DELETE FROM DEPARTMENT_TEMPLATE
 WHERE TEMPLATE_REFID IN
          ('4aa1ceed8caa4a5bb626a784e9fb8e86',
           '76c84693cc8148bca6d41d0203e8bccf',
           'c181ed1a1f984e2694b828c76eccd142')
