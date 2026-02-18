DROP DATABASE IF EXISTS meetingroom;

CREATE DATABASE meetingroom
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE meetingroom;

CREATE TABLE users (
  id         VARCHAR(7)  NOT NULL COMMENT 'YY + 5桁連番',
  password   VARCHAR(64) NOT NULL COMMENT 'SHA-256 hex 64文字',
  name       VARCHAR(10) NOT NULL,
  address    VARCHAR(30),
  adminflag  TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '1=管理者,0=一般',
  deleteflag TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '0=有効,1=削除',
  created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at DATETIME    NULL,
  PRIMARY KEY (id),
  CHECK (adminflag IN (0,1)),
  CHECK (deleteflag IN (0,1))
) ENGINE=InnoDB;


CREATE TABLE user_seq (
  yy      CHAR(2) NOT NULL,
  last_no INT     NOT NULL,
  PRIMARY KEY (yy)
) ENGINE=InnoDB;

CREATE TABLE room (
  id   CHAR(4)     NOT NULL,
  name VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE reservation (
  id     INT        NOT NULL AUTO_INCREMENT,
  roomId CHAR(4)    NOT NULL,
  date   DATE       NOT NULL,
  start  TIME       NOT NULL,
  end    TIME       NOT NULL,
  userId VARCHAR(7) NOT NULL,
  PRIMARY KEY (id),

  CONSTRAINT uq_res_slot UNIQUE (roomId, date, start),

  CONSTRAINT fk_res_room  FOREIGN KEY (roomId) REFERENCES room(id),
  CONSTRAINT fk_res_users FOREIGN KEY (userId) REFERENCES users(id),

  CHECK (end = ADDTIME(start, '01:00:00'))
) ENGINE=InnoDB;
