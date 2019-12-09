create table data_providers (
    id integer primary key,
    name varchar(255) not null
);

create table segment_types (
    id integer primary key,
    name varchar(255) not null,
    view_name varchar(255) not null
);

create table segments (
    id serial primary key,
    name varchar(255),
    is_active boolean,
    data_provider integer not null references data_providers(id),
    type_id integer not null references segment_types(id)
);

create table country_stats (
    segment_id integer not null references segments(id),
    country_code varchar(2),
    active_profiles_amount bigint,
    sleeping_profiles_amount bigint,
    PRIMARY KEY (segment_id,country_code)
);
