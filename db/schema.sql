-- DB作成
DROP DATABASE IF EXISTS meetingroom;
CREATE DATABASE meetingroom;
USE meetingroom;

-- userテーブル
CREATE TABLE users (
id         VARCHAR(7)   NOT NULL,
password   VARCHAR(255) NOT NULL, -- bcrypt等のハッシュを保存
name       VARCHAR(50)  NOT NULL,
address    VARCHAR(255),
adminflag  TINYINT(1)   NOT NULL DEFAULT 0, -- 0=一般,1=管理者
deleteflag TINYINT(1)   NOT NULL DEFAULT 0, -- 0=有効,1=削除
PRIMARY KEY (id),
CHECK (adminflag IN (0,1)), -- flagに0,1以外が入っていないか確認
CHECK (deleteflag IN (0,1)) -- flagに0,1以外が入っていないか確認
);

-- roomテーブル
CREATE TABLE room (
id	 CHAR(4)	 NOT NULL, -- 階数 + 部屋番号
name VARCHAR(20) NOT NULL,
PRIMARY KEY (id)
);

-- reservationテーブル
CREATE TABLE reservation (
id     INT        NOT NULL AUTO_INCREMENT, -- 予約ID自動
roomId CHAR(4)    NOT NULL, -- room.id
date   DATE       NOT NULL, -- 日付
start TIME      NOT NULL, -- 開始
end   TIME      NOT NULL, -- 終了
userId VARCHAR(7) NOT NULL, -- users.id
PRIMARY KEY (id),
FOREIGN KEY (roomId) REFERENCES room(id),
FOREIGN KEY (userId) REFERENCES users(id),
UNIQUE (roomId,date,start)
);

GRANT SELECT ON meetingroom.room TO 'user'@'localhost';
GRANT SELECT ON meetingroom.users TO 'user'@'localhost';
GRANT SELECT,INSERT,DELETE ON meetingroom.reservation TO 'user'@'localhost';