
  CREATE TABLE "NOVELPIA"."T_USER" 
   (	"U_ID" NUMBER(18,0) NOT NULL ENABLE, 
	"ID" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	"PASS" VARCHAR2(300 BYTE) NOT NULL ENABLE, 
	"NICK" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	"EMAIL" VARCHAR2(50 BYTE) NOT NULL ENABLE, 
	"REGISTRATION_DATE" DATE NOT NULL ENABLE, 
	"POINT" NUMBER(32,0) DEFAULT 0, 
	"ADMIN" NUMBER(1,0) DEFAULT 0, 
	 CONSTRAINT "USER_PK" PRIMARY KEY ("U_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
