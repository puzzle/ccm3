insert into RepositoryGroup (name)
values ('Repository Group #1');

insert into Repository (name,repositoryGroup_id)
values ('Repository 1',(select id from RepositoryGroup where name = 'Repository Group #1'));

insert into Log (action,stage,repositoryname,repositorygroupname,repository_id,branch,version,userid,logdate,auftragnr)
values ('deploy','Integration','Repository 1','Repository Group #1',(select id from Repository where name = 'Repository 1'),'master','v1.0.0','u123456',{ts '2017-02-06 13:26:15.'},'auftr-nr-1');

insert into Branch (repository_id,name,created,creator,`status`)
values ((select id from Repository where name = 'Repository 1'),'master',{ts '2017-02-06 13:35:59.'},'s','SUCCESS');

insert into `Status` (branch_id,stage,version,executed,userid,`status`,auftragnr)
values ((select id from Branch where name = 'master'),'Integration','v1.0.0',{ts '2017-02-06 13:34:11.'},'u123456','success','s');
