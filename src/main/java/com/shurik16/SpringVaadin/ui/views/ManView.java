package com.shurik16.SpringVaadin.ui.views;

import com.shurik16.SpringVaadin.model.*;
import com.shurik16.SpringVaadin.service.*;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.*;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Theme("valo")
@SpringUI(path = "/man")
public class ManView extends UI {

    private static Logger LOG = LoggerFactory.getLogger(ManView.class);

    private Tman tman;

    @Autowired
    TmanRepository repo;

    @Autowired
    TpositRepository tpositRepository;

    @Autowired
    TcompanyRepository tcompanyRepository;

    @Autowired
    TotdelRepository totdelRepository;

    @Autowired
    TstandingRepository tstandingRepository;

    @Autowired
    HistoryManRepository historyManRepository;

    @Autowired
    TtestRepository ttestRepository;

    @Autowired
    TfotoRepository tfotoRepository;

    @Value("${directory.dosie}")
    private String pathDosie;

    @Value("${directory.foto}")
    private String pathFotos;

    public void setTman(Tman tman) {
        this.tman = tman;
    }

    private TextField firstname = new TextField("Фамилия");
    private TextField lastname = new TextField("Имя");
    private TextField middlename = new TextField("Отчество");
    private DateTimeField date_of_birth = new DateTimeField("Дата рождения");
    private TextField town_b = new TextField("Место рождения");
    private TextField reg_address = new TextField("Адресс регистраци");
    private TextField residence_address = new TextField("Адресс проживания");
    private TextField phone = new TextField("Телефон");
    private TextField pathFoto = new TextField("Путь к фото");
    private DateTimeField dateTest = new DateTimeField("Дата проверки");
    private TextField rezultTest = new TextField("Результат проверки");
    private TextArea infoTest = new TextArea("Описание");
    private Image foto;
    private ComboBox<Tcompany> company = new ComboBox<Tcompany>("Компания");
    private ComboBox<Totdel> otdel = new ComboBox<Totdel>("Отдел");
    private ComboBox<Tstanding> status = new ComboBox<Tstanding>("Статус");
    private ComboBox<Tposit> tposit = new ComboBox<Tposit>("Должность");
    private int idfoto;
    private Label label = new Label("АНКЕТА", ContentMode.HTML);//"<h2  style=\"text-align: center;\">Анкета</h2>");
    private Label ifoDir = new Label("Не определено");
    private TabSheet tabSheet = new TabSheet();
    private Button close = new MButton("Закрыть");
    private Button creatDir = new MButton("Создать папку");
    private Button save = new MButton("Сохранить");
    private Button hisstatus = new MButton("История");
    private Button files = new MButton("Открыть папку работника");
    private Button setFoto = new MButton("Установить фото");
    private Button uplodFoto = new MButton("Загрузить фото");
    private Button deleteTest = new ConfirmButton(VaadinIcons.TRASH, "Удалить",
            "Удалить выбранную запись?", this::removeTest);
    private Button addTest = new Button("Добавить проверку");
    private Button editTest = new Button("Редактировать проверку");
    final Window window = new Window();


    private TreeGrid<File> treeGrid = new TreeGrid<>();
    private TreeGrid<File> treeGridFoto = new TreeGrid<>();
    private MGrid<Ttest> gridTest = new MGrid<>(Ttest.class).withProperties("data_test", "result")
            .withColumnHeaders("Дата проверки", "Результат")
            .withFullWidth();

    private File rootFileFofo;

    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private boolean isNew;
    private Binder<Tman> binder;

    @Override
    protected void init(VaadinRequest request) {
        //if(tman.getTfoto())
        //LOG.error("tman-"+String.valueOf(tman.getIdfoto()));
        LOG.error("Путь к файлу:"+pathFotos);
        LOG.error(System.getProperty("user.dir")+"/src/main/resources/hase.jpg");

        if (Long.parseLong(request.getParameter("id")) == 0) isNew = true;

            rootFileFofo = new File(pathFotos);

        otdel.setItems(totdelRepository.findAll());
        tposit.setItems(tpositRepository.findAll());
        company.setItems(tcompanyRepository.findAll());
        status.setItems(tstandingRepository.findAll());

        company.setItemCaptionGenerator(item -> {
            return item.getName_company();
        });
        otdel.setItemCaptionGenerator(item -> {
            return item.getName_otdel();
        });
        status.setItemCaptionGenerator(item -> {
            return item.getName_standing();
        });
        tposit.setItemCaptionGenerator(item -> {
            return item.getName_position();
        });

        binder = new Binder<>();

        binder.forField(firstname)
                .asRequired("Фамилия должна быть заполнена!")
                .bind(Tman::getFirstname, Tman::setFirstname);

        binder.forField(lastname)
                .asRequired("Имя должно быть заполнено!")
                .bind(Tman::getLastname, Tman::setLastname);

        binder.forField(middlename)
                .asRequired("Отчество должно быть заполнено!")
                .bind(Tman::getMiddlename, Tman::setMiddlename);

        binder.forField(date_of_birth)
                .asRequired("Дата рождения должно быть заполнено!")
                .bind(Tman::getDate_of_birth, Tman::setDate_of_birth);

        binder.forField(town_b)
                .asRequired("Город рождения не указан!")
                .bind(Tman::getTown_b, Tman::setTown_b);

        binder.forField(reg_address)
                .asRequired("Не указано место проживания!")
                .bind(Tman::getReg_address, Tman::setReg_address);

        binder.forField(residence_address)
                .asRequired("Не указано место регистрации!")
                .bind(Tman::getResidence_address, Tman::setResidence_address);

        binder.forField(phone)
                .bind(Tman::getPhone, Tman::setPhone);

        binder.forField(pathFoto)
                .asRequired("Не указано путь к фото!")
                .bind(Tman::getFoto, Tman::setFoto);

        binder.forField(company)
                .asRequired("Не выбрана компания!")
                .bind(Tman::getCompany, Tman::setCompany);

        binder.forField(otdel)
                .asRequired("Не выбран отдел!")
                .bind(Tman::getOtdel, Tman::setOtdel);

        binder.forField(status)
                .asRequired("Не выбран статус!")
                .bind(Tman::getStanding, Tman::setStanding);

        binder.forField(tposit)
                .asRequired("Не выбран должнасть!")
                .bind(Tman::getPosit, Tman::setPosit);

        tman = repo.findOne(Long.parseLong(request.getParameter("id")));

        Label validationStatus = new Label();
        binder.setStatusLabel(validationStatus);

        if (tman != null)
            binder.setBean(tman);
        else
            binder.setBean(new Tman());

        //String path = "nasprudteo\\Share\\УК ДВ-Альянс\\СЭБ\\СЭБ\\КАДРЫ\\1.ПРОВЕРКА КАНДИДАТОВ\\АНКЕТЫ\\"


        pathDosie = pathDosie +"\\"+ binder.getBean().getFirstname() + " " + binder.getBean().getLastname() + " " + binder.getBean().getMiddlename();

        File rootFileDoc = new File(pathDosie);

        treeGridFoto.setHeight(200, Unit.PIXELS);
        tabSheet.setHeight(100.0f, Unit.PERCENTAGE);
        tabSheet.setWidth(700, Unit.PIXELS);
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        VerticalLayout layoutFIO = new VerticalLayout();
        VerticalLayout layoutWork = new VerticalLayout();
        VerticalLayout layoutAdrres = new VerticalLayout();
        VerticalLayout layoutFile = new VerticalLayout();
        VerticalLayout layoutFofo = new VerticalLayout();
        VerticalLayout layoutTest = new VerticalLayout();

        layoutFIO.setMargin(true);
        layoutWork.setMargin(true);
        layoutAdrres.setMargin(true);
        layoutFile.setMargin(true);
        layoutFofo.setMargin(true);
        layoutTest.setMargin(true);

        reg_address.setWidth(500, Unit.PIXELS);
        residence_address.setWidth(500, Unit.PIXELS);
        otdel.setWidth(400, Unit.PIXELS);
        tposit.setWidth(400, Unit.PIXELS);
        company.setWidth(400, Unit.PIXELS);
        pathFoto.setWidth(600, Unit.PIXELS);
        infoTest.setWidth(400, Unit.PIXELS);
        gridTest.setHeight(150, Unit.PIXELS);

        date_of_birth.setResolution(DateTimeResolution.DAY);
        date_of_birth.setDateFormat("dd.MM.yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        treeGridFoto.setDataProvider(new FileSystemDataProvider(rootFileFofo));

        treeGridFoto.addColumn(file -> {
            String iconHtml;
            if (file.isDirectory()) {
                iconHtml = VaadinIcons.FOLDER_O.getHtml();
            } else {
                iconHtml = VaadinIcons.FILE_O.getHtml();
            }
            return iconHtml + " "
                    + Jsoup.clean(file.getName(), Whitelist.simpleText());
        }, new HtmlRenderer()).setCaption("Имя фаила").setId("file-name");


        treeGridFoto.setHierarchyColumn("file-name");

        setFoto.addClickListener(event -> {
            if (treeGridFoto.asSingleSelect().getValue().getAbsolutePath() != null) {
                pathFoto.setValue(treeGridFoto.asSingleSelect().getValue().getAbsolutePath());
                foto.setSource(new FileResource(new File(treeGridFoto.asSingleSelect().getValue().getAbsolutePath())));
            }
        });

        uplodFoto.addClickListener(event -> {

            if (pathFoto.getValue().length()>0) {
                Tfoto tfoto;
                File file =new File(pathFoto.getValue());
                byte[]bFile = new byte[(int) file.length()];
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    //convert file into array of bytes
                    fileInputStream.read(bFile);
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                tfoto = (tman.getId_foto() > 0)?tfotoRepository.findById(tman.getId_foto()):new Tfoto();

                tfoto.setFoto(bFile);
                tfoto = tfotoRepository.save(tfoto);

                tman.setId_foto(tfoto.getId());
                //LOG.error("tfoto-"+String.valueOf(tfoto.getId()));
                // LOG.error("tman-"+String.valueOf(tman.getTfoto().getId()));

                }

        });

        gridTest.asSingleSelect().addValueChangeListener(event -> {
            if (!gridTest.getSelectedItems().isEmpty()) {
                Ttest b = gridTest.asSingleSelect().getValue();                // setContent(getComponent(b));
                dateTest.setValue(b.getData_test());
                rezultTest.setValue(b.getResult());
                infoTest.setValue(b.getTnote() != null ? b.getTnote() : "");
                // UI.getCurrent().requestRepaint();

            }
        });

        addTest.addClickListener(event -> {
            System.out.println("id " + binder.getBean().getId());
            if (binder.getBean().getId() == null)
                if (binder.isValid()) {
                    binder.setBean(saveWork(binder));
                } else {
                    Notification.show("Не заполнены поля. Сохранить не возможно");
                    return;
                }
            addTest(binder.getBean().getId().intValue(), null);
        });

        editTest.addClickListener(event -> {
            Ttest ttest = gridTest.asSingleSelect().getValue();
            if (ttest != null) {
                addTest(binder.getBean().getId().intValue(), ttest);
            } else {
                Notification.show("Не выбрана проверка.");
            }
        });

        window.addCloseListener(e -> {
            udateTestGrid();
        });

        close.addClickListener(event -> {
            Page.getCurrent().open("/sb/", null);
        });

        save.addClickListener(event -> {
            if (binder.isValid()) {
                saveWork(binder);
                Page.getCurrent().open("/sb/", null);
            } else {
                Notification samNotificationple = new Notification("Ошибка", "Поля заполнены не правильно!", Notification.Type.ERROR_MESSAGE);
                samNotificationple.show(Page.getCurrent());
            }
        });

        creatDir.addClickListener(event -> {
            if (binder.isValid()) {
                File fileDir = new File(pathDosie);
                if (!fileDir.exists()) {
                    if (fileDir.mkdir()) {
                        Notification.show("Папка создана!");
                        creatDir.setEnabled(false);
                        files.setEnabled(true);
                        treeGrid.setDataProvider(new FileSystemDataProvider(rootFileDoc));
                    } else
                        Notification.show("Ошибка создание папки");
                }
            } else
                Notification.show("Не заполнены необходимые поля");
        });


        hisstatus.setEnabled(!isNew);
        hisstatus.addClickListener(event -> {
            openHistoryStatus(Math.toIntExact(tman.getId()));
        });

        File fileFoto = new File(System.getProperty("user.dir")+"/src/main/resources/hase.jpg");

        if (!isNew) {

            File fileDir = new File(pathDosie);

            if (fileDir.exists()) {
                files.setEnabled(true);
                creatDir.setEnabled(false);
                ifoDir.setValue("Директория сушествуе.");
            } else {
                files.setEnabled(false);
                creatDir.setEnabled(true);
                ifoDir.setValue("Директория не сушествуе.");
            }

            File fileFotoMan = new File(tman.getFoto());
            if (fileFotoMan.exists())
                fileFoto = fileFotoMan;

            files.addClickListener(event -> {
                Page.getCurrent().open("file:" + pathDosie, null);
            });


            if (rootFileDoc.exists())
                treeGrid.setDataProvider(new FileSystemDataProvider(rootFileDoc));

            treeGrid.addColumn(file -> {
                String iconHtml;
                if (file.isDirectory()) {
                    iconHtml = VaadinIcons.FOLDER_O.getHtml();
                } else {
                    iconHtml = VaadinIcons.FILE_O.getHtml();
                }
                return iconHtml + " "
                        + Jsoup.clean(file.getName(), Whitelist.simpleText());
            }, new HtmlRenderer()).setCaption("Name").setId("file-name");

            treeGrid.addColumn(
                    file -> file.isDirectory() ? "--" : file.length() + " bytes")
                    .setCaption("Size").setId("file-size");

            treeGrid.addColumn(file -> new Date(file.lastModified()),
                    new DateRenderer()).setCaption("Last Modified")
                    .setId("file-last-modified");

            treeGrid.setHierarchyColumn("file-name");

            udateTestGrid();

        } else {
            files.setEnabled(false);
            ifoDir.setValue("Директория не сушествуе.");
            pathFoto.setValue(fileFoto.getAbsolutePath());
        }

        if(tman.getId_foto()>0) {
            LOG.error(String.valueOf(tman.getId_foto()));

            byte[] imageBytes = tfotoRepository.findById(tman.getId_foto()).getFoto();
            File fileFotos = convertUsingOutputStream(imageBytes);
                  foto = new Image("", new FileResource(fileFotos));
        } else
        foto = new Image("",
                new FileResource(fileFoto));

        foto.setWidth(300, Unit.PIXELS);
        foto.setHeight(400, Unit.PIXELS);


        HorizontalLayout stutusLine = new HorizontalLayout(status,
                hisstatus);

        stutusLine.setComponentAlignment(hisstatus, Alignment.BOTTOM_LEFT);

        layoutFIO.addComponents(firstname,
                lastname,
                middlename,
                date_of_birth,
                town_b);
        layoutWork.addComponents(company,
                otdel,
                tposit,
                stutusLine
        );
        layoutAdrres.addComponents(reg_address,
                residence_address,
                phone);
        layoutFile.addComponents(
                new HorizontalLayout(ifoDir, files, creatDir),
                treeGrid);
        layoutFofo.addComponents(pathFoto, setFoto, uplodFoto, treeGridFoto);

        layoutTest.addComponents(new HorizontalLayout(addTest, editTest, deleteTest),
                gridTest,
                new HorizontalLayout(dateTest, rezultTest)
                , infoTest);

        VerticalLayout head = new VerticalLayout();

        head.addComponent(label);
        head.setComponentAlignment(label, Alignment.TOP_CENTER);

        tabSheet.addTab(layoutFIO, "ФИО ");
        tabSheet.addTab(layoutWork, "Работа ");
        tabSheet.addTab(layoutAdrres, "Адрес ");
        tabSheet.addTab(layoutFile, "Файлы ");
        tabSheet.addTab(layoutFofo, "Фото ");
        tabSheet.addTab(layoutTest, "Проверки ");

        MVerticalLayout verticalLayout = new MVerticalLayout(head,
                new HorizontalLayout(foto, tabSheet),
                new HorizontalLayout(save, close, validationStatus)
        );

        setContent(verticalLayout);
    }
    public static File convertUsingOutputStream(byte[] fileBytes)
    {
        File f = new File("image.jpg");
        try (FileOutputStream fos = new FileOutputStream(f)) {
            fos.write(fileBytes);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return f;
    }

    private void udateTestGrid() {
        if (!isNew && ttestRepository.findByWorkingid(tman.getId().intValue()).size() > 0) {
            gridTest.setRows(ttestRepository.findByWorkingid(tman.getId().intValue()));
            gridTest.select(ttestRepository.findByWorkingid(tman.getId().intValue()).get(0));
        } else {
            gridTest.setRows(ttestRepository.findByWorkingid(binder.getBean().getId().intValue()));
            infoTest.setValue("");
            rezultTest.setValue("");
            dateTest.clear();
        }

    }

    private Tman saveWork(Binder<Tman> binder) {
        Tman rez = binder.getBean();
        Tman outMan = repo.save(rez);
        List<HistoryMan> historyMEN = historyManRepository.findByMankey(binder.getBean().getId().intValue());
        /*System.out.println("Size" + historyMEN.size() );*/
        if (historyMEN.size() == 0 || !historyMEN.get(historyMEN.size() - 1).getStatus().getName_standing()
                .equals(rez.getStanding().getName_standing())) {
            HistoryMan historyMan = new HistoryMan();
            historyMan.setDatestatus(new Date());
            historyMan.setStatus(rez.getStanding());
            historyMan.setMankey(rez.getId().intValue());
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            historyMan.setManagerkey(user);
            historyManRepository.save(historyMan);
        }
        return outMan;
    }


    private void openHistoryStatus(int id) {
        List<HistoryMan> historyManList = historyManRepository.findByMankey(id);
        Window window = new Window("История статуса");
        window.setWidth(500.0f, Unit.PIXELS);
        window.setModal(true);
        window.setResizable(false);
        window.center();
        FormLayout content = new FormLayout();
        for (HistoryMan h : historyManList)
            content.addComponent(new Label(DATE_FORMAT.format(h.getDatestatus()) + " "
                    + h.getStatus().getName_standing() + " "
                    + h.getManagerkey().getFullName()));
        content.setMargin(true);
        window.setContent(content);
        getUI().getUI().addWindow(window);
    }

    private void removeTest() {
        Ttest ttest = gridTest.asSingleSelect().getValue();
        if (ttest != null) {
            ttestRepository.delete(ttest);
            udateTestGrid();
        } else {
            Notification.show("Не выбрана проверка.");
        }
    }

    private void addTest(int idMan, Ttest ttest) {
        Button save = new MButton("Сохранить");
        Button cansel = new MButton("Отмена");

        DateTimeField dateTest = new DateTimeField("Дата проверки");
        TextField resulTest = new TextField("Результат");
        TextArea noteTest = new TextArea("Подробности");

        Binder<Ttest> ttestBinder = new Binder<>();

        ttestBinder.forField(dateTest)
                .asRequired("Укажите дату!")
                .bind(Ttest::getData_test, Ttest::setData_test);
        ttestBinder.forField(resulTest)
                .asRequired("Укажите результат!")
                .bind(Ttest::getResult, Ttest::setResult);
        ttestBinder.forField(noteTest)
                .bind(Ttest::getTnote, Ttest::setTnote);

        Ttest ttestsave = new Ttest();
        ttestsave.setData_test(new Date());
        ttestsave.setWorkingid(idMan);

        HorizontalLayout buutonLayout = new HorizontalLayout(save, cansel);
        buutonLayout.setMargin(true);


        if (ttest != null) {
            window.setCaption("Редактирование записи");
            ttestsave = ttest;
        } else {
            window.setCaption("Добавление проверки");

        }

        ttestBinder.setBean(ttestsave);

        window.setWidth(500.0f, Unit.PIXELS);
        window.setModal(true);
        window.setResizable(false);
        window.center();

        final FormLayout content = new FormLayout();

        content.addComponents(dateTest,
                resulTest,
                noteTest,
                buutonLayout);

        content.setMargin(true);

        window.setContent(content);

        getUI().getUI().addWindow(window);

        cansel.addClickListener(event -> {
            window.close();
        });

        save.addClickListener(event -> {
            if (ttestBinder.isValid()) {
                ttestRepository.save(ttestBinder.getBean());
                window.close();
            }

        });
    }


}
