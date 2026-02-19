-- -----------------------------
-- 1) room マスタ
-- -----------------------------
INSERT INTO room (id, name) VALUES
('0201', '大会議室'),
('0301', '3A会議室'),
('0302', '3B会議室');

-- -----------------------------
-- 2) users（bcrypt済みパスワード）
-- pass123 / pass456
-- -----------------------------
INSERT INTO users (id, password, name, address, adminflag, deleteflag) VALUES
('2600001', '$2b$10$Mhw7wxUQihvpRH.xGUSvNOsTGjiFKPk7khD2bMIHfRVIYmKJ7gIR.', '管理者太郎', '東京都', 1, 0),
('2600002', '$2b$10$vwWIAmo6AhyjIhQSD2Br7uU1j2pPQRgACNpGoAD1mW2bbP6n1Nlgm', '一般花子', '大阪府', 0, 0),
('2600003', '$2b$10$vwWIAmo6AhyjIhQSD2Br7uU1j2pPQRgACNpGoAD1mW2bbP6n1Nlgm', '一般次郎', '愛知県', 0, 0);

-- -----------------------------
-- 3) reservation
-- -----------------------------
INSERT INTO reservation (roomId, date, start, end, userId) VALUES
('0201', CURDATE(), '09:00', '10:00', '2600001'),
('0201', CURDATE(), '10:00', '11:00', '2600002'),
('0301', CURDATE(), '13:00', '14:00', '2600001');

-- =====================================================
-- 4) 表示確認
-- =====================================================

-- users確認
SELECT id, name, adminflag, deleteflag
FROM users
ORDER BY id;

-- room確認
SELECT *
FROM room
ORDER BY id;

-- reservation確認（JOINして見やすくする）
SELECT
  r.id AS reservation_id,
  r.date,
  r.start,
  r.end,
  rm.name AS room_name,
  u.name AS user_name
FROM reservation r
JOIN room rm ON r.roomId = rm.id
JOIN users u ON r.userId = u.id
ORDER BY r.date, r.start;

