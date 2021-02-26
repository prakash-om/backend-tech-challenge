DROP TABLE IF EXISTS document;
DROP TABLE IF EXISTS query_offset_status;
DROP TABLE IF EXISTS retry_mock_counter;

CREATE TABLE document
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    type          VARCHAR(64) NOT NULL,
    serial_number VARCHAR(64),
    pages         INT         NOT NULL DEFAULT 0
);

INSERT INTO document (id, type, serial_number, pages)
VALUES (1,'pdf', null, 4),
       (2,'pdf', null, 8),
       (3,'pdf', null, 15),
       (4,'jpg', null, 16),
       (5,'png', null, 23),
       (6,'jpg', null, 9),
       (7,'jpg', null, 12),
       (8,'jpg', null, 9),
       (9,'pdf', null, 8),
       (10,'pdf', null, 15),
       (11,'jpg', 'PeXTDHagPYj5mHnn', 16),
       (12,'png', null, 23),
       (13,'jpg', null, 9),
       (14,'jpg', null, 12),
       (15,'jpg', null, 9),
       (16,'jpg', null, 9),
       (17,'jpg', null, 9),
       (18,'jpg', null, 9),
       (19,'jpg', null, 9),
       (20,'jpg', null, 9);

CREATE TABLE query_offset_status
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    start_index         INT         NOT NULL DEFAULT 0
);

INSERT INTO query_offset_status(start_index)
VALUES (0);

CREATE TABLE retry_mock_counter
(
	id 		INT PRIMARY KEY,
	retry_count	INT NOT NULL
)
