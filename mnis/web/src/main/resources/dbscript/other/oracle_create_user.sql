drop user sztest cascade;

create user sztest identified by sztest;
grant dba to sztest with admin option;
grant connect to sztest with admin option;