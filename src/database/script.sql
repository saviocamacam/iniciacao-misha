CREATE TABLE IF NOT EXISTS Page(
    PAG_ID_FACEBOOK bigint NOT NULL,
    PAG_NAME varchar,        
    PAG_STATE varchar,
    PAG_POLITICAL_PARTY varchar,
    CONSTRAINT pk_PAGE_ID PRIMARY KEY (PAG_ID_FACEBOOK)
);

CREATE TABLE IF NOT EXISTS Post(
    POS_ID_FACEBOOK bigint NOT NULL,
    POS_MESSAGE varchar,
    POS_STORY varchar,
    POS_CREATED_TIME varchar,
    POS_PAGE_ID bigint,
    CONSTRAINT pk_POS_ID PRIMARY KEY (POS_ID_FACEBOOK)
);

CREATE TABLE IF NOT EXISTS Comment(
    COM_ID_FACEBOOK bigint NOT NULL,
    COM_ID_COMM_REPLIED bigint,
    COM_MESSAGE varchar,
    COM_FROM_ID bigint,
    COM_FROM_NAME varchar,
    COM_CREATED_TIME varchar,
    COM_POST_ID bigint,
    CONSTRAINT pk_COM_ID PRIMARY KEY (COM_ID_FACEBOOK)
);

CREATE TABLE IF NOT EXISTS Reaction_Post(
    RPT_ID_USER bigint,
    RPT_ID_POST bigint,
    RPT_TYPE varchar,
    CONSTRAINT pk_LPT_ID PRIMARY KEY (RPT_ID_USER, RPT_ID_POST,RPT_TYPE)
);

CREATE TABLE IF NOT EXISTS Reaction_Comment(
    RCT_ID_USER bigint,
    RCT_ID_COMMENT bigint,
    RCT_TYPE varchar,
    CONSTRAINT pk_RCT_ID PRIMARY KEY (RCT_ID_USER,RCT_ID_COMMENT,RCT_TYPE)
);

CREATE TABLE IF NOT EXISTS Reaction_Reply(
    RRP_ID_USER bigint,
    RRP_ID_REPLY bigint,
    RRP_TYPE varchar,
    CONSTRAINT pk_RRP_ID PRIMARY KEY (RRP_ID_USER,RRP_ID_REPLY,RRP_TYPE)
);

CREATE TABLE IF NOT EXISTS Visualizacao(
	POS_ID_FACEBOOK bigint,
	POS_CREATED_TIME varchar,
	POS_COMMENTS bigint,
	POS_REPLIES bigint,
	POS_SHARES bigint,
	
	POS_LIKE bigint,
	POS_LOVE bigint,
	POS_HAHA bigint,
	POS_WOW bigint,
	POS_SAD bigint,
	POS_ANGRY bigint,
	
	POS_MEAN_COMMENT_LIKE real,
	POS_MEAN_COMMENT_LOVE real,
	POS_MEAN_COMMENT_HAHA real,
	POS_MEAN_COMMENT_WOW real,
	POS_MEAN_COMMENT_SAD real,
	POS_MEAN_COMMENT_ANGRy real,
	
	POS_MEAN_REPLY_LIKE real,
	POS_MEAN_REPLY_LOVE real,
	POS_MEAN_REPLY_HAHA real,
	POS_MEAN_REPLY_WOW real,
	POS_MEAN_REPLY_SAD real,
	POS_MEAN_REPLY_ANGRy real,	
	CONSTRAINT pk_POS_ID_VISUALIZATION PRIMARY KEY(POS_ID_FACEBOOK)
);


ALTER TABLE Post ADD CONSTRAINT fk_PAGE_POST FOREIGN KEY (POS_PAGE_ID) REFERENCES Page(PAG_ID_FACEBOOK);
ALTER TABLE Comment ADD CONSTRAINT fk_POST_COMMENT FOREIGN KEY (COM_POST_ID) REFERENCES Post(POS_ID_FACEBOOK);
ALTER TABLE Comment ADD CONSTRAINT fk_REP_ID_COMM_REPLIED FOREIGN KEY (COM_ID_COMM_REPLIED) REFERENCES Comment(COM_ID_FACEBOOK);
--ALTER TABLE Like_Page ADD CONSTRAINT fk_PAGE_LPG FOREIGN KEY (LPG_ID_PAGE) REFERENCES Page(PAG_ID_FACEBOOK);
ALTER TABLE Reaction_Comment ADD CONSTRAINT fk_RCT_COMMENT FOREIGN KEY (RCT_ID_COMMENT) REFERENCES Comment(COM_ID_FACEBOOK);
ALTER TABLE Reaction_Post ADD CONSTRAINT fk_RPT_POST FOREIGN KEY (RPT_ID_POST) REFERENCES Post(POS_ID_FACEBOOK);


--PMDB-Partido do Movimento Democrático Brasileiro
INSERT INTO Page VALUES('435464776514810', 'Michel Temer', 'SP', 'PMDB');
INSERT INTO Page VALUES('267949976607343', 'Lula', 'SP', 'PT');
INSERT INTO Page VALUES('351338968253034', 'Dilma Rousseff', 'RS', 'PT');
