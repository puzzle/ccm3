create table `RepositoryGroup` (
  `id` bigint not null auto_increment primary key,
  `name` varchar(32) not null,
  constraint ux_RepositoryGroup_name unique (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table `Repository` (
  `id` bigint not null auto_increment primary key,
  `name` varchar(128) not null,
  `repositoryGroup_id` bigint not null,
  constraint `ux_repositoryGroup_id-name` unique (`repositoryGroup_id`, `name`),
  constraint `Repository-RepositoryGroup_fk` foreign key (`repositoryGroup_id`) references `RepositoryGroup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table `Branch` (
  `id` bigint not null auto_increment primary key,
  `repository_id` bigint not null,
  `name` varchar(128) not null,
  `created` timestamp not null default current_timestamp on update current_timestamp,
  `creator` varchar(24) not null,
  `status` varchar(24) not null,
  constraint `ux_repository_id-name` unique (`repository_id`, `name`),
  constraint `Branch-Repository_fk` foreign key (`repository_id`) references `Repository` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table `Status` (
  `id` bigint not null auto_increment primary key,
  `branch_id` bigint not null,
  `stage` varchar(24) not null,
  `version` varchar(64) not null,
  `executed` timestamp not null default current_timestamp on update current_timestamp,
  `userId` varchar(24) not null,
  `status` varchar(24) not null,
  `auftragNr` varchar(24) default null,
  constraint `Status-Branch_fk` foreign key (`branch_id`) references `Branch` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table `Log` (
  `id` bigint not null auto_increment primary key,
  `action` varchar(32) not null,
  `stage` varchar(24) not null,
  `repositoryName` varchar(128) not null,
  `repositoryGroupName` varchar(32) not null,
  `repository_id` bigint,
  `branch` varchar(128) not null,
  `version` varchar(64) not null,
  `userId` varchar(24) not null,
  `logdate` timestamp not null default current_timestamp on update current_timestamp,
  `auftragNr` varchar(24) default null,
  constraint `Log-Repository_fk` foreign key (`repository_id`) references `Repository` (id) on delete set null on update cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
