INSERT INTO media_types( mime_type) VALUES
( 'image/jpeg'),
( 'video/webm');

INSERT INTO measure_units( name) VALUES
('граммов'),
('миллилитров'),
('ч.л.'),
('ст.л.');

INSERT INTO users(username) VALUES
('root');


INSERT INTO measure_units_sets VALUES (0);

INSERT INTO measure_units_distribution(set_id, unit_id)  VALUES
(0,(SELECT id FROM measure_units WHERE name = 'граммов')),
(0,(SELECT id FROM measure_units WHERE name = 'миллилитров'));