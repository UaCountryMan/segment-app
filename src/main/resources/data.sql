insert into data_providers (id, name) values (1, 'Spring');
insert into data_providers (id, name) values (2, 'Looe');
insert into data_providers (id, name) values (3, 'Gbl');

insert into segment_types (id, name, view_name) values (0, 'none', 'None');
insert into segment_types (id, name, view_name) values (1, 'brand', 'Brand');
insert into segment_types (id, name, view_name) values (2, 'test', 'Test');
insert into segment_types (id, name, view_name) values (3, 'audience', 'Audience');

insert into segments (id, name, data_provider, type_id) values (1, 'Segment1', 2, 3);
insert into segments (id, name, data_provider, type_id) values (2, 'Segment2', 1, 1);
insert into segments (id, name, data_provider, type_id) values (3, 'Segment3', 2, 2);


insert into country_stats (segment_id, country_code) values (1, '');
insert into country_stats (segment_id, country_code) values (1, 'US');
insert into country_stats (segment_id, country_code) values (2, '');
insert into country_stats (segment_id, country_code) values (2, 'US');
insert into country_stats (segment_id, country_code) values (3, '');
insert into country_stats (segment_id, country_code) values (3, 'US');
