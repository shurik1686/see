--
-- Создать таблицу "role"
--
CREATE TABLE role (
  id bigint(11)NOT NULL AUTO_INCREMENT,
  authority varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

--
-- Создать таблицу "schema_version"
--
CREATE TABLE schema_version (
  version_rank bigint(11)NOT NULL,
  installed_rank bigint(11)NOT NULL,
  version varchar(50) NOT NULL,
  description varchar(200) NOT NULL,
  type varchar(20) NOT NULL,
  script varchar(1000) NOT NULL,
  checksum bigint(11)DEFAULT NULL,
  installed_by varchar(100) NOT NULL,
  installed_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  execution_time bigint(11)NOT NULL,
  success tinyint(1) NOT NULL,
  PRIMARY KEY (version)
);

--
-- Создать таблицу "tcompany"
--
CREATE TABLE tcompany (
  id int(11) NOT NULL AUTO_INCREMENT,
  name_company varchar(255) DEFAULT NULL,
  contract_number varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

--
-- Создать таблицу "test"
--
CREATE TABLE test (
  id bigint(11)NOT NULL AUTO_INCREMENT,
  FirstName varchar(50) DEFAULT NULL,
  LastName varchar(255) DEFAULT NULL,
  MiddleName varchar(255) DEFAULT NULL,
  Foto varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);



--
-- Создать таблицу "totdel"
--
CREATE TABLE totdel (
  id int(11) NOT NULL AUTO_INCREMENT,
  name_otdel varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

--
-- Создать таблицу "Tposit"
--
CREATE TABLE Tposit (
  id int(11) NOT NULL AUTO_INCREMENT,
  name_position varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

--
-- Создать таблицу "TSTANDING"
--
CREATE TABLE TSTANDING (
  id int(11) NOT NULL AUTO_INCREMENT,
  name_standing varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);
--
-- Создать таблицу "Tfoto"
--
CREATE TABLE TFOTO (
  id int(11) NOT NULL AUTO_INCREMENT,
  foto binary  DEFAULT NULL,
  PRIMARY KEY (id)
);
--
-- Создать таблицу "user"
--
CREATE TABLE user (
  id bigint(11)NOT NULL AUTO_INCREMENT,
  account_non_expired boolean(1) NOT NULL,
  account_non_locked boolean(1) NOT NULL,
  credentials_non_expired boolean(1) NOT NULL,
  enabled boolean(1) NOT NULL,
  full_name varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  username varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);
--
-- Создать таблицу "tstatus_history"
--
CREATE TABLE tstatus_history (
  id bigint(11)NOT NULL AUTO_INCREMENT,
  datestatus timestamp NOT NULL,
  id_status int(11)NOT NULL,
  mankey int(11)NOT NULL,
  id_manager bigint(11)NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_tstatus_history_id_manager FOREIGN KEY (id_manager)
  REFERENCES user (id) ON DELETE NO ACTION ON UPDATE RESTRICT,
  CONSTRAINT FK_tstatus_history_id_status FOREIGN KEY (id_status)
  REFERENCES TSTANDING (id) ON DELETE NO ACTION ON UPDATE RESTRICT
);

--
-- Создать таблицу "user_roles"
--
CREATE TABLE user_roles (
  user_id bigint(11)NOT NULL,
  role_id bigint(11)NOT NULL,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT user_roles_ibfk_1 FOREIGN KEY (user_id)
  REFERENCES user (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT user_roles_ibfk_2 FOREIGN KEY (role_id)
  REFERENCES role (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

--
-- Создать таблицу "tman"
--
CREATE TABLE tman (
  id bigint(11) NOT NULL AUTO_INCREMENT,
  firstname varchar(255) DEFAULT NULL,
  lastname varchar(255) DEFAULT NULL,
  middlename varchar(255) DEFAULT NULL,
  Date_of_birth timestamp DEFAULT NULL,
  town_b varchar(255) DEFAULT NULL,
  Reg_address varchar(255) DEFAULT NULL,
  Residence_address varchar(255) DEFAULT NULL,
  Phone varchar(255) DEFAULT NULL,
  Foto varchar(255) DEFAULT NULL,
  id_posit int(11) NOT NULL DEFAULT 0,
  id_company int(11) NOT NULL DEFAULT 0,
  id_otdel int(11) NOT NULL DEFAULT 0,
  id_standing int(11) NOT NULL DEFAULT 0,
  id_foto int(11) DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE INDEX UK_tman_id (id),
  CONSTRAINT FK_tman_company FOREIGN KEY (id_company)
  REFERENCES tcompany (id) ON DELETE NO ACTION ON UPDATE RESTRICT,
  CONSTRAINT FK_tman_Otdel FOREIGN KEY (id_otdel)
  REFERENCES totdel (id) ON DELETE NO ACTION ON UPDATE RESTRICT,
  CONSTRAINT FK_tman_Posit FOREIGN KEY (id_posit)
  REFERENCES tposit (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FK_tman_Standing FOREIGN KEY (id_standing)
  REFERENCES tstanding (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);
--
-- Создать таблицу "ttest"
--
CREATE TABLE ttest (
  id int(11) NOT NULL AUTO_INCREMENT,
  id_working int(11) NOT NULL,
  data_test timestamp DEFAULT NULL,
  result varchar(255) DEFAULT NULL,
  tnote varchar(1000) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_ttest_id_working FOREIGN KEY (id_working)
  REFERENCES tman (id) ON DELETE NO ACTION ON UPDATE RESTRICT
);


--
-- Вывод данных для таблицы schema_version
--
INSERT INTO schema_version VALUES
(1, 1, '1', '<< Flyway Baseline >>', 'BASELINE', '<< Flyway Baseline >>', NULL, 'root', '2017-12-06 10:52:08', 0, 1),
(2, 2, '1.1', 'Add Colleague Flag', 'SQL', 'V1_1__Add_Colleague_Flag.sql', 0, 'root', '2017-12-06 10:52:08', 4, 1);

--
-- Вывод данных для таблицы tcompany
--
INSERT INTO tcompany VALUES
(10, 'ООО ТЭО', NULL);

--
-- Вывод данных для таблицы totdel
--
INSERT INTO totdel VALUES
(1, 'Общий отдел'),
(2, 'Департамент вагонного хозяйства'),
(3, 'Отдел по работе с томожней'),
(4, 'Отдел по работе с нефтеналивными грузами'),
(5, 'АХО'),
(6, 'Юридический отдел'),
(7, 'Бухгалтерия'),
(8, 'Отдел по экспедированию'),
(9, 'Отдел по работе с грузами на ОПС '),
(10, 'Коммерческий отдел'),
(11, 'Администрация'),
(12, 'Контейнерный отдел'),
(13, 'Отдел по работе с персоналом'),
(15, 'Отдел претензионной работы'),
(16, 'Отдел экономической безопасности'),
(17, 'IT отдел'),
(18, 'Департамент ЭСПС'),
(19, 'Отдел ЭСПС'),
(20, 'Сектор оперативного учёта доходов'),
(21, 'Сектор оперативного учёта расходов'),
(22, 'Отдел инвестиционных проектов (ОИП)'),
(23, 'Отдел тарифов'),
(24, 'Отдел ТЭО'),
(25, 'Пропарочная станция'),
(26, 'Отдел бюджетирования'),
(27, 'ЖД отдел'),
(28, 'Автоперевозка'),
(29, 'Отдел СВХ'),
(30, 'Отдел локомотивного хозяйства');

--
-- Вывод данных для таблицы tstanding
--
INSERT INTO TPOSIT VALUES
(1, 'Адвокат'),
(2, 'АХО - Делопроизводитель'),
(3, 'АХО - Заместитель начальника отдела'),
(4, 'АХО - Начальник отдела'),
(5, 'АХО - Офис-менеджер'),
(6, 'Бухгалтер'),
(9, 'Бухгалтер-экономист'),
(10, 'Ведущий специалист'),
(11, 'Ведущ.юрисконсульт'),
(19, 'Ведущий экономист'),
(20, 'Ведущий юрисконсульт'),
(21, 'Водитель автомобиля'),
(22, 'Временный управляющий'),
(23, 'Генеральный директор'),
(24, 'Главны специалист'),
(25, 'Главный бухгалтер'),
(26, 'Главный инженер'),
(27, 'Главный специалист по договорной работе'),
(28, 'Делопроизводитель'),
(30, 'Директор'),
(31, 'Директор департамента эксплуатации СПС'),
(32, 'Зам. Ген. директора по экономике '),
(33, 'Зам.ген.дир. по таможен.вопросам'),
(34, 'Зам.глав.бухгалтера'),
(35, 'Зам.нач.отдела'),
(36, 'Заместитель генерального директора'),
(37, 'Консультант САП'),
(38, 'Нач. отдела организации перевозок'),
(39, 'Нач. отдела по работе с нефт. Груз.'),
(40, 'Нач. юр. Отдела'),
(41, 'Начальник отдела'),
(42, 'Начальник отдела нефтеналивных грузов'),
(43, 'Начальник отдела организации перевозок'),
(44, 'Начальник отдела по учету выручки'),
(45, 'Начальник Финансово-расчетного отдела'),
(46, 'Оператор перевозок нефтеналивных грузов'),
(47, 'Первый зам. генерального директора'),
(48, 'Руководитель департамента вагонного хозяйства'),
(49, 'Руководитель коммерческого департамента'),
(50, 'Системн.админ.'),
(51, 'Специалист'),
(52, 'Старш.системн.админ.'),
(53, 'Старший специалист'),
(54, 'Старший юрисконсульт'),
(55, 'Уборщик служебных помещений'),
(56, 'Уборщик территорий'),
(57, 'Финансовый директор'),
(58, 'Экономист'),
(59, 'Юрисконсульт'),
(60, 'Офис-менеджер'),
(61, 'Неопределена'),
(62, 'Старший оператор'),
(63, 'Водитель УМПС'),
(64, 'Пропарщик'),
(65, 'Помошник Генерального директора по правовым вопросам'),
(66, 'Директор по экономике и финансам'),
(67, 'Менеджер'),
(68, 'Оператор вагонно-распределительного пункта'),
(70, 'Глава представительства'),
(71, 'Исполнительный директор'),
(72, 'Инженер по охране труда'),
(73, 'Начальник отдела ЭСПС'),
(74, 'Ведущий специалист по в/х'),
(75, 'Экономист по учёту затрат'),
(76, 'Специалист по в/х'),
(77, 'Ведущий специалист по интермодальным перевозкам'),
(78, 'Секретарь-делопроизводитель'),
(79, 'Старший специалист по экспедированию'),
(80, 'Специалист по экспедированию'),
(81, 'Ведущий специалист по экспедированию'),
(82, 'Заместитель генерального директора по общим и правовым вопросам'),
(83, 'Фельдшер'),
(84, 'Составитель-оператор'),
(85, 'Мастер-механик'),
(86, 'Промывальщик-пропарщик цистерн'),
(87, 'Машинист тепловоза'),
(88, 'Начальник УМПС'),
(89, 'Газоэлектросварщик'),
(90, 'Старший смены пропарщиков'),
(91, 'Охранник'),
(92, 'Электрослесарь'),
(93, 'Контрагент'),
(95, 'Стропальщик'),
(96, 'Стропальщик-грузчик'),
(97, 'Начальник ОП'),
(98, 'Кладовщик СВХ'),
(99, 'Контролер'),
(100, 'Начальник складской части'),
(101, 'Электрогазосварщик'),
(102, 'Заместитель генерального директора по инвестициям'),
(103, 'Составитель поездов');

--
-- Вывод данных для таблицы tstatus
--
INSERT INTO TSTANDING VALUES
(1, 'Работник'),
(2, 'Кандидат'),
(4, 'Уволен');
--
INSERT INTO tman VALUES
(55, 'Иванов', 'Иван', 'Иванович', '1980-01-01', 'г.Хабаровск', 'г.Хабаровск, ул. Фургала, д.1, кв.1', 'г.Хабаровск, ул. Фургала, д.1, кв.1', '8-927-027-27-27', 'c:\\sb\\hase.jpg', 51, 10, 9, 4,0);
--
-- Вывод данных для таблицы role
--
INSERT INTO role VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER');

--
-- Вывод данных для таблицы user
--
INSERT INTO user VALUES
(1, 1, 1, 1, 1, 'Попов А.В.', '$2a$10$G0ZwyCtAwlSYahvO9Rklb.qAbBahQLoNzMPcFirK.7Hw4/rjNvo8y', 'popov'),
(5, 1, 1, 1, 1, 'Guru', '$2a$10$y2ROPYDq8jJrCZ/nVQcgRuLd3qbdTZzn3c1JCsB1Z8nbovc4JMr/e', 'admin'),
(6, 1, 1, 1, 1, 'John Doe', '$2a$10$o72nbiWMPF25EHSqAY15j.gk7kBl3x9wvUO089TOPLPt4tl7yusUe', 'user');

--
-- Вывод данных для таблицы user_roles
--
INSERT INTO user_roles VALUES
(1, 1),
(5, 1),
(6, 1),
(5, 2);


