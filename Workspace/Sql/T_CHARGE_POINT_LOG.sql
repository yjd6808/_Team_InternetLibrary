
  CREATE TABLE "NOVELPIA"."T_CHARGE_POINT_LOG" 
   (	"USER_UID" NUMBER(18,0) NOT NULL ENABLE, 
	"PRICE" NUMBER(10,0) NOT NULL ENABLE, 
	"CHARGE_POINT" NUMBER(10,0) NOT NULL ENABLE, 
	"CHARGE_DATE" DATE NOT NULL ENABLE, 
	"U_ID" NUMBER(20,0) NOT NULL ENABLE, 
	 PRIMARY KEY ("U_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE, 
	 CONSTRAINT "CHARGE_POINT_LOG_USER_FK" FOREIGN KEY ("USER_UID")
	  REFERENCES "NOVELPIA"."T_USER" ("U_ID") ON DELETE CASCADE ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
