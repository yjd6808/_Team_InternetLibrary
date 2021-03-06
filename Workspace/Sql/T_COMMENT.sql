
  CREATE TABLE "NOVELPIA"."T_COMMENT" 
   (	"BOARD_UID" NUMBER(20,0) NOT NULL ENABLE, 
	"USER_UID" NUMBER(18,0) NOT NULL ENABLE, 
	"BOARD_TYPE" NUMBER(2,0) NOT NULL ENABLE, 
	"CONTENT" VARCHAR2(1000 BYTE) NOT NULL ENABLE, 
	"CREATED_DATE" DATE NOT NULL ENABLE, 
	"U_ID" NUMBER(10,0), 
	 PRIMARY KEY ("U_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE, 
	 CONSTRAINT "COMMENT_USER_FK" FOREIGN KEY ("USER_UID")
	  REFERENCES "NOVELPIA"."T_USER" ("U_ID") ON DELETE CASCADE ENABLE 
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
