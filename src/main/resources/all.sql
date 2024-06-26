CREATE TABLE person
(
    id       bigint generated by default as identity primary key,
    username varchar not null unique,
    email    varchar not null unique,
    password varchar not null,
    role     varchar not null,
    enable   boolean default false
);

CREATE TABLE Post
(
    id             bigint generated by default as identity primary key,
    owner_username varchar references person (username) not null,
    title          varchar                              not null,
    description    varchar                              not null,
    views          int       default 0,
    photo_url      varchar                              not null,
    created_at     timestamp default current_timestamp,
    category       varchar                              not null
);
CREATE TABLE Comment
(
    id              bigint generated by default as identity primary key,
    post_id         bigint references post (id) on delete cascade not null,
    person_username varchar references person (username)          not null,
    description     text                                          not null,
    created_at      timestamp default current_timestamp
);

CREATE TABLE Photo_like
(
    id              bigint generated by default as identity primary key,
    post_id         bigint references post (id) on delete cascade not null,
    person_username varchar references person (username)          not null
);

CREATE TABLE Activation
(
    id       bigint generated by default as identity primary key,
    key      varchar                              not null,
    username varchar references person (username) not null
);

CREATE TABLE Statistic
(
    id             bigint generated by default as identity primary key,
    total_views    bigint not null check ( total_views > -1 ),
    total_likes    bigint not null check ( total_likes > -1 ),
    total_comments bigint not null check ( total_comments > -1),
    created_at     timestamp default current_timestamp
);
CREATE TABLE System_info
(
    id    bigint generated by default as identity primary key,
    title varchar unique not null,
    text  text           not null
);