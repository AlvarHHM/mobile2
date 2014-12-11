BEGIN TRANSACTION;
CREATE TABLE `room` (
	`_id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`room`	TEXT,
	`lat`	INTEGER,
	`lon`	INTEGER
);
INSERT INTO `room` VALUES ('1','A001','52766238','-1228394');
CREATE TABLE "lecture" (
	`_id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`module`	TEXT,
	`room`	TEXT,
	`type`	INTEGER,
	`lecturer`	TEXT,
	`semester`	INTEGER,
	`week`	TEXT,
	`time`	INTEGER
);
INSERT INTO `lecture` VALUES ('1','COC140','A001','0','Dr Ana Salagean','1','1,2,3,4,6,7,8,9,10,11,12,','309600000');
COMMIT;
