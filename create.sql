/* 신청하려면 유저정보가 필요*/
/*


*/
CREATE TABLE TBL_USER(
	USER_ID VARCHAR(64) PRIMARY KEY,
	USER_PASSWORD VARCHAR(128),
	USER_NAME VARCHAR(64),
	USER_PHONE VARCHAR(11),
    USER_EMAIL VARCHAR(80),
	USER_CREATED_AT DATETIME NOT NULL,
	USER_CREATED_BY VARCHAR(20) NOT NULL,
	USER_UPDATED_AT DATETIME,
	USER_UPDATED_BY VARCHAR(20),
	USER_STATUS VARCHAR(20) NOT NULL,
	USER_ROLE VARCHAR(20) NOT NULL
);

/* 이용신청서*/
/*
	APP_ID				이용신청서 ID(PK)
	APP_NAME			이용신청서 대표자 이름
	APP_EMAIL			이용신청서 대표자 이메일
	APP_PHONE			이용신청서 대표자 휴대전화
	APP_TITLE			이용신청서 제목
	APP_EXPECTED_COUNT	예상 투표자 수
	APP_START			투표 시작 기간
	APP_END				투표 종료 기간
	APP_CREATED_AT		이용신청서 생성일자
	APP_CREATED_BY		이용신청서 생성방식
	APP_UPDATED_AT		이용신청서 수정일자
	APP_UPDATED_BY		이용신청서 수정방식
	APP_STATUS			투표 상태
	APP_HAS_VOTE		투표등록 여부
	APP_HAS_VOTER		유권자등록 여부
	APP_APPROVAL		투표 승인 여부
	APP_HAS_RESULT		개표 여부
	USER_ID				회원 정보 (FK)
*/
CREATE TABLE TBL_APPLY(
	APP_ID INTEGER PRIMARY KEY AUTO_INCREMENT,
	APP_NAME VARCHAR(64) NOT NULL,
	APP_EMAIL VARCHAR(255) NOT NULL,
	APP_PHONE VARCHAR(11) NOT NULL,
	APP_TITLE VARCHAR(40) NOT NULL,
	APP_EXPECTED_COUNT INTEGER NOT NULL,
	APP_START DATETIME NOT NULL,
	APP_END DATETIME NOT NULL,
	APP_CREATED_AT DATETIME NOT NULL,
	APP_CREATED_BY VARCHAR(20) NOT NULL,
	APP_UPDATED_AT DATETIME,
	APP_UPDATED_BY VARCHAR(20),
	APP_STATUS VARCHAR(20) NOT NULL,
	APP_HAS_VOTE TINYINT NOT NULL DEFAULT 0,
	APP_HAS_VOTER TINYINT NOT NULL DEFAULT 0,
	APP_APPROVAL TINYINT NOT NULL DEFAULT -1,
	APP_HAS_RESULT TINYINT NOT NULL DEFAULT 0,
	APP_IS_VOTING TINYINT NOT NULL DEFAULT 0,
	USER_ID VARCHAR(64),
    FOREIGN KEY(USER_ID) REFERENCES TBL_USER(USER_ID) ON DELETE CASCADE
);

/* 투표 정보*/
/*
	VOTEINFO_ID				투표 정보 ID(PK)
	VOTEINFO_NAME			투표 이름
	VOTEINFO_DESC			투표 설명
	VOTEINFO_COUNT			투표 개수
	VOTEINFO_STATUS			투표 정보 상태
	VOTEINFO_CREATED_AT		투표 생성 일자
	VOTEINFO_CREATED_BY		투표 생성 방식
	VOTEINFO_UPDATED_AT		투표 수정 일자
	VOTEINFO_UPDATED_BY		투표 수정 방식
	VOTEINFO_CURRENT		투표한 인원
	APPLY_ID				이용신청서 ID(FK)
*/
CREATE TABLE TBL_VOTEINFO(
	VOTEINFO_ID INTEGER PRIMARY KEY AUTO_INCREMENT,
    VOTEINFO_NAME VARCHAR(40) NOT NULL,
	VOTEINFO_DESC VARCHAR(255),
	VOTEINFO_COUNT INT NOT NULL,
	VOTEINFO_STATUS VARCHAR(20) NOT NULL,
    VOTEINFO_CREATED_AT DATETIME NOT NULL,
	VOTEINFO_CREATED_BY VARCHAR(20) NOT NULL,
	VOTEINFO_UPDATED_AT DATETIME,
	VOTEINFO_UPDATED_BY VARCHAR(20),
	VOTEINFO_CURRENT INT NOT NULL DEFAULT 0,
	APPLY_ID INT NOT NULL,
    FOREIGN KEY(APPLY_ID) REFERENCES TBL_APPLY(APP_ID) ON DELETE CASCADE
);

/* 투표 */
/*
	VOTE_ID			투표 ID(PK)
	VOTE_NAME		투표 이름
	VOTE_DESC		투표 설명
	VOTE_SEQ_NUM	투표 순번
	VOTE_SEL_NUM	투표 선택 갯수
	VOTE_ELEC_COUNT	당선 갯수
	VOTEINFO_ID		투표 정보 ID(FK)
*/
CREATE TABLE TBL_VOTE(
	VOTE_ID INTEGER PRIMARY KEY AUTO_INCREMENT,
	VOTE_NAME VARCHAR(40) NOT NULL,
	VOTE_DESC VARCHAR(255),
	VOTE_SEQ_NUM INTEGER NOT NULL,
	VOTE_SEL_NUM INTEGER NOT NULL DEFAULT 1,
	VOTE_ELEC_COUNT INTEGER NOT NULL,
	VOTEINFO_ID INTEGER NOT NULL,
    FOREIGN KEY(VOTEINFO_ID) REFERENCES TBL_VOTEINFO(VOTEINFO_ID) ON DELETE CASCADE
);

/* 유권자들*/
/*
	VOTER_ID			유권자 ID(PK)
	VOTER_NAME			유권자 이름
	VOTER_PHONE			유권자 휴대전화
	VOTER_SSN			유권자 UUID
	VOTER_STATUS		유권자 상태
	VOTER_CREATED_AT	유권자 생성일자
	VOTER_CREATED_BY	유권자 생성방식
	VOTER_UPDATED_AT	유권자 수정일자
	VOTER_UPDATED_BY	유권자 수정방식
	VOTER_VOTED_DATE
	APPLY_ID 			이용신청서 ID(FK)
*/
CREATE TABLE TBL_VOTER(
	VOTER_ID INTEGER PRIMARY KEY AUTO_INCREMENT,
	VOTER_NAME VARCHAR(40) NOT NULL,
	VOTER_PHONE VARCHAR(11) NOT NULL,
	VOTER_SSN VARCHAR(255) NOT NULL,
	VOTER_STATUS VARCHAR(20) NOT NULL,
	VOTER_CREATED_AT DATETIME NOT NULL,
	VOTER_CREATED_BY VARCHAR(20) NOT NULL,
	VOTER_UPDATED_AT DATETIME,
	VOTER_UPDATED_BY VARCHAR(20),
	VOTER_VOTED_DATE DATETIME,
	APPLY_ID INTEGER NOT NULL,
    FOREIGN KEY(APPLY_ID) REFERENCES TBL_APPLY(APP_ID) ON DELETE CASCADE
);

/* 후보자들 */
/* 
	CAND_ID					후보자 ID(PK)
	CAND_SEQ_NO				후보자 순번
	CAND_NAME				후보자 이름
	CAND_DESC				후보자 설명
	CAND_VALUE				후보자 투표 값
	CAND_SELECTED_VALUE		후보자 선택 수
	CAND_ELECTED			후보자 당선 여부
	CAND_IMAGE				후보자 이미지파일이름
	VOTE_ID					투표ID(FK)
*/
CREATE TABLE TBL_CANDIDATE(
	CAND_ID INTEGER PRIMARY KEY AUTO_INCREMENT,
	CAND_SEQ_NO INTEGER NOT NULL,
	CAND_NAME VARCHAR(64) NOT NULL,
	CAND_DESC VARCHAR(255) NOT NULL,
	CAND_VALUE INTEGER DEFAULT 0,   /*값*/
	CAND_SELECTED_VALUE INTEGER DEFAULT 0,  /*선택수*/
	CAND_ELECTED INTEGER DEFAULT -1,    /*그래서 당선? 안당선? -1 미정*/
	CAND_IMAGE VARCHAR(255),
	VOTE_ID INTEGER,
    FOREIGN KEY(VOTE_ID) REFERENCES TBL_VOTE(VOTE_ID) ON DELETE CASCADE
);

/*투표한 사람의 결과*/
/*
	RESULT_VOTER_ID			투표 유권자 ID(PK, FK)
	RESULT_CANDIDATE_ID		투표 후보자 ID(PK, FK)
	RESULT_VALUE			투표 값
	RESULT_CREATED_AT		투표 결과 생성일자
	RESULT_CREATED_BY		투표 결과 생성방식
*/
CREATE TABLE TBL_RESULT(
	RESULT_VOTER_ID INTEGER,
	RESULT_CANDIDATE_ID INTEGER,
	RESULT_VALUE INTEGER NOT NULL,  /* 투표는 1점씩 증가 */
	RESULT_CREATED_AT DATETIME NOT NULL,
	RESULT_CREATED_BY VARCHAR(20) NOT NULL,
    PRIMARY KEY(RESULT_VOTER_ID, RESULT_CANDIDATE_ID),
    FOREIGN KEY(RESULT_VOTER_ID) REFERENCES TBL_VOTER(VOTER_ID), 
    FOREIGN KEY(RESULT_CANDIDATE_ID) REFERENCES TBL_CANDIDATE(CAND_ID) ON DELETE CASCADE
);