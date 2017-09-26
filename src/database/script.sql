DROP TABLE IF EXISTS Reaction_Post;
DROP TABLE IF EXISTS Like_Page;
DROP TABLE IF EXISTS Like_Comment;
DROP TABLE IF EXISTS Comment;
DROP TABLE IF EXISTS Post;
DROP TABLE IF EXISTS Page;

CREATE TABLE Page(
    PAG_ID_FACEBOOK bigint NOT NULL,
    PAG_NAME varchar,        
    PAG_STATE varchar,
    PAG_POLITICAL_PARTY varchar,
    CONSTRAINT pk_PAGE_ID PRIMARY KEY (PAG_ID_FACEBOOK)
);

CREATE TABLE Post(
    POS_ID_FACEBOOK bigint NOT NULL,
    POS_MESSAGE varchar,
    POS_STORY varchar,
    POS_CREATED_TIME varchar,
    POS_PAGE_ID bigint,
    CONSTRAINT pk_POS_ID PRIMARY KEY (POS_ID_FACEBOOK)
);

CREATE TABLE Comment(
    COM_ID_FACEBOOK bigint NOT NULL,
    COM_ID_COMM_REPLIED bigint,
    COM_MESSAGE varchar,
    COM_FROM_ID bigint,
    COM_FROM_NAME varchar,
    COM_CREATED_TIME varchar,
    COM_POST_ID bigint,
    CONSTRAINT pk_COM_ID PRIMARY KEY (COM_ID_FACEBOOK)
);
    

/*CREATE TABLE Like_Page(
    LPG_ID_USER bigint,
    LPG_ID_PAGE bigint,
    CONSTRAINT pk_LPG_ID PRIMARY KEY (LPG_ID_USER, LPG_ID_PAGE)
);*/

CREATE TABLE Reaction_Post(
    RPT_ID_USER bigint,
    RPT_ID_POST bigint,
    RPT_TYPE varchar,
    CONSTRAINT pk_LPT_ID PRIMARY KEY (RPT_ID_USER, RPT_ID_POST),
    CONSTRAINT fk_POST_LPT FOREIGN KEY (RPT_ID_POST) REFERENCES Post(POS_ID_FACEBOOK)
);

CREATE TABLE Like_Comment(
    LCT_ID_USER bigint,
    LCT_ID_COMMENT bigint,
    CONSTRAINT pk_LCT_ID PRIMARY KEY (LCT_ID_USER,LCT_ID_COMMENT)
);


ALTER TABLE Post ADD CONSTRAINT fk_PAGE_POST FOREIGN KEY (POS_PAGE_ID) REFERENCES Page(PAG_ID_FACEBOOK);
ALTER TABLE Comment ADD CONSTRAINT fk_POST_COMMENT FOREIGN KEY (COM_POST_ID) REFERENCES Post(POS_ID_FACEBOOK);
ALTER TABLE Comment ADD CONSTRAINT fk_REP_ID_COMM_REPLIED FOREIGN KEY (COM_ID_COMM_REPLIED) REFERENCES Comment(COM_ID_FACEBOOK);
--ALTER TABLE Like_Page ADD CONSTRAINT fk_PAGE_LPG FOREIGN KEY (LPG_ID_PAGE) REFERENCES Page(PAG_ID_FACEBOOK);
ALTER TABLE Like_Comment ADD CONSTRAINT fk_COMMENT_LCT FOREIGN KEY (LCT_ID_COMMENT) REFERENCES Comment(COM_ID_FACEBOOK);


--INSERT INTO Page VALUES('829813800384226','Haken EJ', 'DF', 'PPS');



--fonte: http://www.senado.leg.br/senadores/senadoresPorPartido.asp

/*
--DEM-Democratas
INSERT INTO Page VALUES('580813708692882', 'Davi Alcolumbre', 'AP', 'DEM');
INSERT INTO Page VALUES('234137580017617', 'José Agripino', 'RN', 'DEM');
INSERT INTO Page VALUES('289650211197144', 'Maria do Carmo Alves', 'SE', 'DEM');
INSERT INTO Page VALUES('398434416879875', 'Ronaldo Caiado', 'GO', 'DEM');

--PCdoB-Partido Comunista do Brasil
INSERT INTO Page VALUES('302901263140722', 'Vanessa Grazziotin', 'AM', 'PCdoB');

--PDT-Partido Democrático Trabalhista
INSERT INTO Page VALUES('400957796603490', 'Acir Gurgacz', 'RO', 'PDT');
*/

--PMDB-Partido do Movimento Democrático Brasileiro
INSERT INTO Page VALUES('435464776514810', 'Michel Temer', 'SP', 'PMDB');

/*
INSERT INTO Page VALUES('1869898693283538', 'Airton Sandoval', 'SP', 'PMDB');
INSERT INTO Page VALUES('422287131246397', 'Dário Berger', 'SC', 'PMDB');
INSERT INTO Page VALUES('1564075570518204', 'Edison Lobão', 'MA', 'PMDB');
INSERT INTO Page VALUES('263563620333111', 'Eduardo Braga', 'AM', 'PMDB');
INSERT INTO Page VALUES('601991053164565', 'Elmano Férrer', 'PI', 'PMDB');
INSERT INTO Page VALUES('147474322001734', 'Eunício Oliveira', 'CE', 'PMDB');
INSERT INTO Page VALUES('351982408331236', 'Garibaldi Alves', 'RN', 'PMDB');
INSERT INTO Page VALUES('646113045490159', 'Hélio José', 'DF', 'PMDB');
INSERT INTO Page VALUES('133602516654912', 'Jader Barbalho', 'PA', 'PMDB');
INSERT INTO Page VALUES('1672949919632847', 'João Alberto de Souza', 'MA', 'PMDB');
INSERT INTO Page VALUES('279862745523348', 'José Maranhão', 'PB', 'PMDB');
INSERT INTO Page VALUES('175986319185315', 'Kátia Abreu', 'TO', 'PMDB');
INSERT INTO Page VALUES('125980744110410', 'Marta Suplicy', 'SP', 'PMDB');
INSERT INTO Page VALUES('349925051862391', 'Raimundo Lira', 'PB', 'PMDB');
INSERT INTO Page VALUES('576313645847016', 'Renan Calheiros', 'AL', 'PMDB');
INSERT INTO Page VALUES('339606729398017','Roberto Requião', 'PR', 'PMDB');
INSERT INTO Page VALUES('353898054715969', 'Romero Jucá', 'RR', 'PMDB');
INSERT INTO Page VALUES('266710610020532', 'Rose de Freitas', 'ES', 'PMDB');
INSERT INTO Page VALUES('250792278286894', 'Simone Tebet', 'MS', 'PMDB');
INSERT INTO Page VALUES('1375884822664126', 'Valdir Raupp', 'RO', 'PMDB');
INSERT INTO Page VALUES('375349375824563', 'Waldemir Moka', 'MS', 'PMDB');
INSERT INTO Page VALUES('182149918520953', 'Zeze Perrella', 'MG', 'PMDB');

--PP-Partido Progressista
INSERT INTO Page VALUES('404767669542740', 'Ana Amélia Lemos', 'RS', 'PP');
INSERT INTO Page VALUES('215225455193052', 'Benedito de Lira', 'AL', 'PP');
INSERT INTO Page VALUES('274527822566791', 'Ciro Nogueira', 'PI', 'PP');
INSERT INTO Page VALUES('346692845385821', 'Gladson Cameli', 'AC', 'PP');
INSERT INTO Page VALUES('1481362108788369', 'Ivo Cassol', 'RO', 'PP');
--INSERT INTO Page VALUES('', 'Roberto de Oliveira Muniz', 'BA', 'PP');-- rede social não encontrada
INSERT INTO Page VALUES('452935231455081', 'Wilder Morais', 'GO', 'PP');

--PPS-Partido Popular Socialista
INSERT INTO Page VALUES('166860353356903','Cristovam Buarque', 'DF', 'PPS');

--PR-Partido da República
INSERT INTO Page VALUES('345537648863136','Cidinho Santos', 'MT', 'PR');
INSERT INTO Page VALUES('604028822974212','Magno Malta', 'ES', 'PR');
INSERT INTO Page VALUES('128657703838969','Vicentinho Alves', 'TO', 'PR');
INSERT INTO Page VALUES('1501236886772782','Wellington Fagundes', 'MT', 'PR');

--PRB-Partido Republicano Brasileiro
INSERT INTO Page VALUES('173791542755774','Eduardo Lopes', 'RJ', 'PRB');

--PSB-Partido Socialista Brasileiro
INSERT INTO Page VALUES('441992419255336','Antonio Carlos Valadares', 'SE', 'PSB');
INSERT INTO Page VALUES('214703445397159','Fernando Bezerra Coelho', 'PE', 'PSB');
INSERT INTO Page VALUES('363977866998324','João Capiberibe', 'AP', 'PSB');
INSERT INTO Page VALUES('203206869718110','Lídice da Mata', 'BA', 'PSB');
INSERT INTO Page VALUES('127980693947760','Lúcia Vânia', 'GO', 'PSB');
INSERT INTO Page VALUES('575997129087857','Roberto Rocha', 'MA', 'PSB');
INSERT INTO Page VALUES('111949165566730','Romário Faria', 'RJ', 'PSB');

--PSC-Partido Social Cristão
INSERT INTO Page VALUES('1158198227564050','Pedro Chaves', 'MS', 'PSC');

--PSD-Partido Social Democrático
INSERT INTO Page VALUES('312680748928001','José Medeiros', 'MT', 'PSD');
INSERT INTO Page VALUES('740133689339031', 'Lasier Martins', 'RS', 'PSD');
INSERT INTO Page VALUES('632161500194120','Omar Aziz', 'AM', 'PSD');
INSERT INTO Page VALUES('281229865369097','Otto Alencar', 'BA', 'PSD');
INSERT INTO Page VALUES('245784392128312','Sérgio Petecão', 'AC', 'PSD');

--PSDB-Partido da Social Democracia Brasileira
INSERT INTO Page VALUES('411754008869486','Aécio Neves', 'MG', 'PSDB');
INSERT INTO Page VALUES('779987178702881','Antonio Anastasia', 'MG', 'PSDB');
INSERT INTO Page VALUES('398426556931775','Ataídes Oliveira', 'TO', 'PSDB');
INSERT INTO Page VALUES('260758727306325','Cássio Cunha Lima', 'PB', 'PSDB');
INSERT INTO Page VALUES('1679491075655810','Dalirio Beber', 'SC', 'PSDB');
INSERT INTO Page VALUES('384807388247573','Eduardo Amorim', 'SE', 'PSDB');
INSERT INTO Page VALUES('156522971109335','Flexa Ribeiro', 'PA', 'PSDB');
INSERT INTO Page VALUES('124815588195','José Serra', 'SP', 'PSDB');
INSERT INTO Page VALUES('219938924796414','Paulo Bauer', 'SC', 'PSDB');
INSERT INTO Page VALUES('490371597647024','Ricardo Ferraço', 'ES', 'PSDB');
INSERT INTO Page VALUES('176729425863637','Tasso Jereissati', 'CE', 'PSDB');

--PT-Partido dos Trabalhadores
INSERT INTO Page VALUES('324709780996711','Ângela Portela', 'RR', 'PT');
INSERT INTO Page VALUES('317759544988081','Fátima Bezerra', 'RN', 'PT');
INSERT INTO Page VALUES('136344939876101','Gleisi Hoffmann', 'PR', 'PT');
INSERT INTO Page VALUES('174217422747805','Humberto Costa', 'PE', 'PT');
INSERT INTO Page VALUES('339953416062764','Jorge Viana', 'AC', 'PT');
INSERT INTO Page VALUES('206986182684706','José Pimentel', 'CE', 'PT');
INSERT INTO Page VALUES('223382044339765','Lindbergh Farias', 'RJ', 'PT');
INSERT INTO Page VALUES('364042537032048','Paulo Paim', 'RS', 'PT');
INSERT INTO Page VALUES('1425830751012706','Paulo Rocha', 'PA', 'PT');
INSERT INTO Page VALUES('1552091645060509','Regina Sousa', 'PI', 'PT');

--PTB-Partido Trabalhista Brasileiro
INSERT INTO Page VALUES('197054863777543','Armando Monteiro', 'PE', 'PTB');
--INSERT INTO Page VALUES('','Thieres Pinto', 'RR', 'PTB');--não encontrado

--PTC-Partido Trabalhista Cristão
INSERT INTO Page VALUES('609306612447116','Fernando Collor', 'AL', 'PTC');

--PV-Partido Verde
INSERT INTO Page VALUES('199599520097304','Alvaro Dias', 'PR', 'PV');

--REDE-Rede Sustentabilidade
INSERT INTO Page VALUES('687008454694990','Randolfe Rodrigues', 'AP', 'REDE');

--S/Partido - Sem Partido
INSERT INTO Page VALUES('161039194060036','Reguffe', 'DF', 'Sem Partido');

--INSERT INTO Page VALUES('261925857331811', 'Telmário Mota', 'RR', 'PDT');--não esta no site do senado.

*/