USE meetingroom;

-- -----------------------------------------
-- 0) 変数：今年の下2桁（YY）
-- 例：2026年なら '26'
-- -----------------------------------------
SET @yy := DATE_FORMAT(CURDATE(), '%y');

-- -----------------------------------------
-- 1) マスタ投入（room）
-- -----------------------------------------
INSERT INTO room (id, name) VALUES
('0201', '大会議室'),
('0301', '3A会議室'),
('0302', '3B会議室');

-- -----------------------------------------
-- 2) 採番初期化（user_seq）
-- その年の行が無ければ last_no=0 を作る
-- -----------------------------------------
INSERT INTO user_seq (yy, last_no)
VALUES (@yy, 0)
ON DUPLICATE KEY UPDATE last_no = last_no;

-- -----------------------------------------
-- 3) users 3人を採番で作成
-- 00001開始、年が変われば自動で別系列になる
--
-- password は既存のSHA-256 hexを流用
--  - pass123: 9b8769...
--  - pass456: 1d4598...
-- -----------------------------------------

-- 1人目（管理者）
UPDATE user_seq SET last_no = last_no + 1 WHERE yy = @yy;
INSERT INTO users (id, password, name, address, adminflag, active)
SELECT CONCAT(@yy, LPAD(last_no, 5, '0')),
       '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c',
       '管理者太郎',
       '東京都',
       1,
       1
FROM user_seq WHERE yy = @yy;

-- 2人目（一般）
UPDATE user_seq SET last_no = last_no + 1 WHERE yy = @yy;
INSERT INTO users (id, password, name, address, adminflag, active)
SELECT CONCAT(@yy, LPAD(last_no, 5, '0')),
       '1d4598d1949b47f7f211134b639ec32238ce73086a83c2f745713b3f12f817e5',
       '一般花子',
       '大阪府',
       0,
       1
FROM user_seq WHERE yy = @yy;

-- 3人目（一般）
UPDATE user_seq SET last_no = last_no + 1 WHERE yy = @yy;
INSERT INTO users (id, password, name, address, adminflag, active)
SELECT CONCAT(@yy, LPAD(last_no, 5, '0')),
       '1d4598d1949b47f7f211134b639ec32238ce73086a83c2f745713b3f12f817e5',
       '一般次郎',
       '愛知県',
       0,
       1
FROM user_seq WHERE yy = @yy;

-- 便宜上、作った3人のIDを変数化（以降の予約投入で使う）
SET @u1 := CONCAT(@yy, '00001');
SET @u2 := CONCAT(@yy, '00002');
SET @u3 := CONCAT(@yy, '00003');

-- -----------------------------------------
-- 4) reservation（今日・明日で0201を満室）
-- 時間枠：09:00～17:00（開始09～16の8枠）
-- endはstart+1h固定
-- -----------------------------------------

-- 今日：0201 満室
INSERT INTO reservation (roomId, date, start, end, userId) VALUES
('0201', CURDATE(), '09:00', '10:00', @u1),
('0201', CURDATE(), '10:00', '11:00', @u2),
('0201', CURDATE(), '11:00', '12:00', @u3),
('0201', CURDATE(), '12:00', '13:00', @u1),
('0201', CURDATE(), '13:00', '14:00', @u2),
('0201', CURDATE(), '14:00', '15:00', @u3),
('0201', CURDATE(), '15:00', '16:00', @u1),
('0201', CURDATE(), '16:00', '17:00', @u2);

-- 明日：0201 満室
INSERT INTO reservation (roomId, date, start, end, userId) VALUES
('0201', DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00', '10:00', @u2),
('0201', DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00', '11:00', @u3),
('0201', DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00', '12:00', @u1),
('0201', DATE_ADD(CURDATE(), INTERVAL 1 DAY), '12:00', '13:00', @u2),
('0201', DATE_ADD(CURDATE(), INTERVAL 1 DAY), '13:00', '14:00', @u3),
('0201', DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00', '15:00', @u1),
('0201', DATE_ADD(CURDATE(), INTERVAL 1 DAY), '15:00', '16:00', @u2),
('0201', DATE_ADD(CURDATE(), INTERVAL 1 DAY), '16:00', '17:00', @u3);

-- 他の部屋にも少しだけ予約（画面を“現実寄り”にする）
INSERT INTO reservation (roomId, date, start, end, userId) VALUES
('0301', CURDATE(), '13:00', '14:00', @u1),
('0302', DATE_ADD(CURDATE(), INTERVAL 1 DAY), '11:00', '12:00', @u2);

-- -----------------------------------------
-- 5) 確認
-- -----------------------------------------
SELECT id, name, adminflag, active FROM users ORDER BY id;
SELECT * FROM room ORDER BY id;

SELECT roomId, date, start, end, userId
FROM reservation
ORDER BY date, roomId, start;
