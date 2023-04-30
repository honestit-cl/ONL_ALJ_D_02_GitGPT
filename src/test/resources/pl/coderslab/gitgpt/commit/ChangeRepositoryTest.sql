insert into authors (id, first_name, last_name, name)
values (1, null, null, 'author');

insert into repositories (id, is_fork, is_public, name, owner_id, url)
values (1, false, false, 'repository-1', 1, 'url-1');

insert into repositories (id, is_fork, is_public, name, owner_id, url)
values (2, false, false, 'repository-2', 1, 'url-2');

insert into commits (id, author_id, branch, created_on, description, name, repository_id, sha,
                     uploaded)
values (1, 1, 'main', null, 'description', 'name', 1, 'sha-1', false);

insert into changes (id, commit_id, path, repository_id, type)
values (1, 1, 'path-1', 1, null);
insert into changes (id, commit_id, path, repository_id, type)
values (2, null, 'path-2', 1, null);
insert into changes (id, commit_id, path, repository_id, type)
values (3, null, 'path-3', 2, null);