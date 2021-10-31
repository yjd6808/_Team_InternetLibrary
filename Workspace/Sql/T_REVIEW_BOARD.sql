
  CREATE TABLE "NOVELPIA"."T_REVIEW_BOARD" 
   (	"U_ID" NUMBER(20,0) NOT NULL ENABLE, 
	"USER_UID" NUMBER(18,0) NOT NULL ENABLE, 
	"BOOK_UID" NUMBER(20,0) NOT NULL ENABLE, 
	"TITLE" VARCHAR2(100 BYTE) NOT NULL ENABLE, 
	"CONTENT" VARCHAR2(3000 BYTE) NOT NULL ENABLE, 
	"CREATED_DATE" DATE NOT NULL ENABLE, 
	"VISIT_COUNT" NUMBER(10,0) DEFAULT 0, 
	"LIKE_COUNT" NUMBER(10,0) DEFAULT 0, 
	"SCORE" FLOAT(126) DEFAULT 0 NOT NULL ENABLE, 
	 CONSTRAINT "REVIEW_BOARD_PK" PRIMARY KEY ("U_ID", "USER_UID", "BOOK_UID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE, 
	 CONSTRAINT "REVIEW_BOARD_USER_FK" FOREIGN KEY ("USER_UID")
	  REFERENCES "NOVELPIA"."T_USER" ("U_ID") ON DELETE CASCADE ENABLE, 
	 CONSTRAINT "REVIEW_BOARD_BOOK_FK" FOREIGN KEY ("BOOK_UID")
	  REFERENCES "NOVELPIA"."T_BOOK" ("U_ID") ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;