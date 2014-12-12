BEGIN TRANSACTION;
CREATE TABLE `room` (
	`_id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`room`	TEXT,
	`lat`	INTEGER,
	`lon`	INTEGER
);
INSERT INTO `room` VALUES ('1','A001','52766238','-1228394');
INSERT INTO `room` VALUES ('2','CC014','52765066','-1227197');
INSERT INTO `room` VALUES ('3','N001','52766719','-1229146');
INSERT INTO `room` VALUES ('4','JB021','52767849','-1223621');
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
INSERT INTO `lecture` VALUES ('1','COC140','A001','0','Dr Ana Salagean','1','1,3,5,7,9,11,','309600000');
INSERT INTO `lecture` VALUES ('2','COC101','CC014','0','Dr Syeda Fatima','1','1,2,3,4,5,6,7,8,9,10,11,12,','39600000');
INSERT INTO `lecture` VALUES ('3','COC101','CC014','0','Dr Syeda Fatima','1','1,2,3,4,5,6,7,8,9,10,11,12,','122400000');
INSERT INTO `lecture` VALUES ('4','COC001','N001','2','Dr Qinggang Meng','1','1,2,3,4,5,6,7,8,9,10,11,12,','205200000');
INSERT INTO `lecture` VALUES ('5','COC104','JB021','0','Dr Paul Bell','1','1,2,3,4,5,6,7,8,9,10,11,12,','378000000');
COMMIT;