package com.shurik16.SpringVaadin.ui;

import com.shurik16.SpringVaadin.model.*;
import com.shurik16.SpringVaadin.service.*;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.DownloadButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SpringUI
@Title("Список досье")
@Theme("valo")
public class AdminUI extends UI {

    @Autowired
    TmanRepository repo;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TcompanyRepository tcompanyRepository;

    @Autowired
    HistoryManRepository historyManRepository;

    @Autowired
    TtestRepository ttestRepository;

    @Autowired
    TstandingRepository tstandingRepository;

    final int PAGESIZE = 5;

    private MGrid<Tman> list = new MGrid<>(Tman.class)
            .withProperties("firstname", "lastname", "middlename", "foto")
            .withColumnHeaders("Ф", "И", "О", "Foto")
            .withFullWidth();
    private MGrid<User> listUser;
    private MTextField firstname = new MTextField("Фамилия")
            .withPlaceholder("Поле фильтра");
    private ComboBox<Tcompany> company = new ComboBox<Tcompany>("Компания");
    private ComboBox<Tstanding> standing = new ComboBox<Tstanding>("Статус");
    private ComboBox form = new ComboBox("Экранная форма");


    private Button exit = new MButton("Выход");
    private Button buttonOpen = new MButton("Открыть");
    private Button buttonOpenReport = new MButton("Отчеты");
    private Button openUser = new MButton("Пользователи системы");
    private Button addUser = new MButton("Добавить");
    private Button deleteUser = new ConfirmButton(VaadinIcons.TRASH, "Удалить",
            "Удалить выбранную запись?", this::remove);
    private Button guide = new MButton("Cправочники");

    private Button report = new DownloadButton();


    //private Image image;

    @Override
    protected void init(VaadinRequest request) {
        UI.getCurrent().setPollInterval(500);
        list.setRows(repo.findAll());

        exit.addClickListener(event -> {
            Page.getCurrent().open("/sb/logout", null);
        });
        buttonOpen.addClickListener(event -> {
            adjustActionButtonClick();
        });
        buttonOpenReport.addClickListener(event -> {
            Page.getCurrent().open("/sb/report", null);
        });

        guide.addClickListener(event -> {
            Page.getCurrent().open("/sb/guide", null);
        });

        openUser.addClickListener(event -> {
            openUserSystem();
        });

        addUser.addClickListener(event -> {
            Page.getCurrent().open("/sb/man?id=0", null);
        });

        firstname.addValueChangeListener(e -> {
            listEntities();
        });

        company.setItems(tcompanyRepository.findAll());

        company.setItemCaptionGenerator(item -> {
            return item.getName_company();
        });

        company.addValueChangeListener(event -> {
            listEntities();
        });

        standing.setItems(tstandingRepository.findAll());

        standing.setItemCaptionGenerator(item -> {
            return item.getName_standing();
        });

        standing.addValueChangeListener(event -> {
            listEntities();
        });

        list.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());

        form.setItems("Кадры", "Договора", "Исполнительное");
        form.setSelectedItem("Кадры");
        form.addValueChangeListener(valueChangeEvent -> {
            formValueChangeListener(valueChangeEvent);
        });

        setContent(getComponent(null));
    }

    private void remove() {
        boolean hasSelection = !list.getSelectedItems().isEmpty();
        if (hasSelection) {
            Tman mandelete = list.asSingleSelect().getValue();
            historyManRepository.deleteAllByMankey(mandelete.getId().intValue());
            ttestRepository.deleteAllByWorkingid(mandelete.getId().intValue());
            repo.delete(mandelete);
        }
        list.setRows(repo.findAll());
    }

    private void formValueChangeListener(HasValue.ValueChangeEvent valueChangeEvent) {
        Object value = valueChangeEvent.getValue();
        if ("Договора".equals(value)) {
            Page.getCurrent().open("/sb/contracts", null);
        } else if ("Исполнительное".equals(value)) {
            Page.getCurrent().open("/sb/executions", null);
        }

    }

    private void listEntities() {
        if (firstname.getValue().length() > 0 && !company.getSelectedItem().isPresent() && !standing.getSelectedItem().isPresent()) {
            String likeFilter = "%" + firstname.getValue() + "%";
            list.setRows(repo.findByFirstnameLikeIgnoreCase(likeFilter));
        } else if (firstname.getValue().length() == 0 && company.getSelectedItem().isPresent() && !standing.getSelectedItem().isPresent()) {
            list.setRows(repo.findByCompany(company.getValue()));
        } else if (firstname.getValue().length() == 0 && !company.getSelectedItem().isPresent() && standing.getSelectedItem().isPresent()) {
            list.setRows(repo.findByStanding(standing.getValue()));
        } else if (firstname.getValue().length() > 0 && company.getSelectedItem().isPresent() && !standing.getSelectedItem().isPresent()) {
            String likeFilter = "%" + firstname.getValue() + "%";
            list.setRows(repo.findByFirstnameLikeIgnoreCaseAndCompany(likeFilter, company.getValue()));
        } else if (firstname.getValue().length() > 0 && !company.getSelectedItem().isPresent() && standing.getSelectedItem().isPresent()) {
            String likeFilter = "%" + firstname.getValue() + "%";
            list.setRows(repo.findByFirstnameLikeIgnoreCaseAndStanding(likeFilter, standing.getValue()));
        } else if (firstname.getValue().length() == 0 && company.getSelectedItem().isPresent() && standing.getSelectedItem().isPresent()) {
            list.setRows(repo.findByCompanyAndStanding(company.getValue(), standing.getValue()));
        } else if (firstname.getValue().length() > 0 && company.getSelectedItem().isPresent() && standing.getSelectedItem().isPresent()) {
            String likeFilter = "%" + firstname.getValue() + "%";
            list.setRows(repo.findByFirstnameLikeIgnoreCaseAndCompanyAndStanding(likeFilter, company.getValue(), standing.getValue()));
        } else {
            list.setRows(repo.findAll());
        }

        adjustActionButtonState();
    }

    private void adjustActionButtonClick() {
        boolean hasSelection = !list.getSelectedItems().isEmpty();
        if (hasSelection) {
            Tman tman = list.asSingleSelect().getValue();
            Page.getCurrent().open("/sb/man" + "?id=" + tman.getId(), null);
        }
    }

    protected Component getComponent(Tman tman) {
        MVerticalLayout verticalLayout;
       /* if (tman != null) {
            image = new Image("",
                    new FileResource(new File(tman.getFoto())));
            image.setWidth(200, Unit.PIXELS);
            image.setHeight(267, Unit.PIXELS);
        }
*/
        // firstname.set
        HorizontalLayout component = new HorizontalLayout(firstname, company, standing);
        HorizontalLayout components = new HorizontalLayout(component, form);
        components.setWidth(100, Unit.PERCENTAGE);
        /*
        components.setComponentAlignment(firstname,Alignment.TOP_LEFT);
        components.setComponentAlignment(company,Alignment.TOP_LEFT);
        components.setComponentAlignment(standing,Alignment.TOP_LEFT);
        */
        components.setComponentAlignment(form, Alignment.TOP_RIGHT);
        //components.setCaption("Hzz");


        verticalLayout = new MVerticalLayout(
                components,//,lastname,middlename),
                new HorizontalLayout(buttonOpen, addUser, deleteUser, openUser, guide, buttonOpenReport, report, exit),
                list).expand(list);


        return verticalLayout;
    }

    protected void adjustActionButtonState() {

        boolean hasSelection = !list.getSelectedItems().isEmpty();
        if (hasSelection) {
            genireteDoc();
        }
        //edit.setEnabled(hasSelection);
        //delete.setEnabled(hasSelection);
    }

    private void openUserSystem() {

        listUser = new MGrid<>(User.class)
                .withProperties("fullName", "username", "enabled")
                .withColumnHeaders("Имя", "Логин", "Активный")
                .withFullWidth();
        listUser.setRows((List<User>) userRepository.findAll());

        Button add = new MButton("Добавить");
        Button edit = new MButton("Изменить");
        Button delete = new ConfirmButton(VaadinIcons.TRASH, "Удалить",
                "Удалить выбранную запись?", this::removeuser);
        //new MButton("Удалить");

        final Window windowAdd = new Window("Добавление пользователя");

        windowAdd.addCloseListener(e -> {
            listUser.setRows((List<User>) userRepository.findAll());
        });

        add.addClickListener(event -> {
            openUserAdd(null, windowAdd);
        });

        edit.addClickListener(event -> {
            if (!listUser.getSelectedItems().isEmpty())
                openUserAdd(listUser.asSingleSelect().getValue(), windowAdd);
        });

        delete.addClickListener(event -> {

        });

        final Window window = new Window("Пользователи системы");
        window.setWidth(900.0f, Unit.PIXELS);
        window.setModal(true);
        window.setResizable(false);
        window.center();
        final FormLayout content = new FormLayout();
        content.addComponents(new HorizontalLayout(add, edit, delete), listUser/*,close*/);
        content.setMargin(true);
        window.setContent(content);
        getUI().getUI().addWindow(window);
    }

    private void removeuser() {
        boolean hasSelection = !listUser.getSelectedItems().isEmpty();
        if (hasSelection) {
            User user = listUser.asSingleSelect().getValue();
            user.setEnabled(false);
            userRepository.save(user);
            listUser.setRows((List<User>) userRepository.findAll());
        }
    }

    private void genireteDoc() {
      /*  boolean hasSelection = !list.getSelectedItems().isEmpty();
        if (hasSelection) {
           Tman user = list.asSingleSelect().getValue();
*/
        /**--------------------------------*/
        Tman tman = list.asSingleSelect().getValue();
        try {
            // создаем модель docx документа,
            // к которой будем прикручивать наполнение (колонтитулы, текст)
            XWPFDocument docxModel = new XWPFDocument();
            CTSectPr ctSectPr = docxModel.getDocument().getBody().addNewSectPr();
            // получаем экземпляр XWPFHeaderFooterPolicy для работы с колонтитулами
            XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(docxModel, ctSectPr);

            // создаем верхний колонтитул Word файла
            CTP ctpHeaderModel = createHeaderModel(
                    "Конфиденциально"
            );

            // устанавливаем сформированный верхний
            // колонтитул в модель документа Word
            XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeaderModel, docxModel);
            headerParagraph.setAlignment(ParagraphAlignment.RIGHT);
            headerFooterPolicy.createHeader(
                    XWPFHeaderFooterPolicy.DEFAULT,
                    new XWPFParagraph[]{headerParagraph}
            );

            // создаем обычный параграф, который будет расположен слева,
            // будет синим курсивом со шрифтом 25 размера
            XWPFParagraph bodyParagraph = docxModel.createParagraph();
            bodyParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun paragraphConfig = bodyParagraph.createRun();
            paragraphConfig.setFontSize(14);
            // HEX цвет без решетки #
            paragraphConfig.setColor("000000");
            paragraphConfig.setText(
                    "ФИО: " + tman.getFirstname() + " " + tman.getLastname() + " " + tman.getMiddlename()
            );
            paragraphConfig.setText("\n");
            InputStream pic = new FileInputStream(tman.getFoto());
            paragraphConfig.addPicture(pic, XWPFDocument.PICTURE_TYPE_JPEG, "logo.JPG", 600, 800);
            paragraphConfig.setText("Дата рождения: " + tman.getDate_of_birth());

            // сохраняем модель docx документа в файл
            FileOutputStream outputStream = new FileOutputStream("/anketa.docx");
            docxModel.write(outputStream);
            Resource res = new FileResource(new File("/anketa.docx"));
            FileDownloader fd = new FileDownloader(res);
            fd.extend(report);


            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**--------------------------------*/

//        }

    }

    private static CTP createHeaderModel(String headerContent) {
        // создаем хедер или верхний колонтитул
        CTP ctpHeaderModel = CTP.Factory.newInstance();
        CTR ctrHeaderModel = ctpHeaderModel.addNewR();
        CTText cttHeader = ctrHeaderModel.addNewT();

        cttHeader.setStringValue(headerContent);
        return ctpHeaderModel;
    }

    private void openUserAdd(User user, Window window) {

        Button save = new MButton("Сохранить");
        Button cansel = new MButton("Отмена");

        TextField fullName = new TextField("Имя");
        TextField userName = new TextField("Логин");
        PasswordField pass = new PasswordField("Пароль");
        CheckBox activ = new CheckBox("Активный");

        List<String> roleList = new ArrayList<>();
        for (Role t : roleRepository.findAll())
            roleList.add(t.getAuthority());

        CheckBoxGroup role = new CheckBoxGroup<>("Выбор ролей", roleList);

        HorizontalLayout buutonLayout = new HorizontalLayout(save, cansel);
        buutonLayout.setMargin(true);

        window.setWidth(500.0f, Unit.PIXELS);
        window.setModal(true);
        window.setResizable(false);
        window.center();
        final FormLayout content = new FormLayout();
        content.addComponents(fullName,
                userName,
                pass,
                activ,
                role,
                buutonLayout);
        content.setMargin(true);
        window.setContent(content);
        getUI().getUI().addWindow(window);

        cansel.addClickListener(event -> {
            window.close();
        });
        User finalUser;
        if (user != null)
            finalUser = user;
        else
            finalUser = new User();
        save.addClickListener(event -> {

            List<Role> roleSelektList = new ArrayList<>();
            for (Role t : roleRepository.findAll())
                if (role.isSelected(t.getAuthority()))
                    roleSelektList.add(t);
            if (fullName.getValue().length() < 3 &&
                    userName.getValue().length() < 3 &&
                    pass.getValue().length() < 3 &&
                    roleSelektList.size() == 0) {
                Notification samNotificationple = new Notification("Ошибка", "Поля заполнены не правильно!", Notification.Type.ERROR_MESSAGE);
                samNotificationple.show(Page.getCurrent());
                return;
            }

            finalUser.setFullName(fullName.getValue());
            finalUser.setUsername(userName.getValue());

            if ((finalUser.getPassword() != null &&
                    !finalUser.getPassword().equals(pass.getValue())) ||
                    finalUser.getPassword() == null)
                finalUser.setUnencryptedPassword(pass.getValue());

            finalUser.setEnabled(activ.isEnabled());
            finalUser.setAuthorities(roleSelektList);
            userRepository.save(finalUser);
            window.close();
        });

        if (user != null) {
            window.setCaption("Изменение " + user.getFullName());
            userName.setValue(user.getUsername());
            fullName.setValue(user.getFullName());
            pass.setValue(user.getPassword());
            activ.setValue(user.isEnabled());
            for (Role t : user.getAuthorities())
                role.select(t.getAuthority());
        }

    }
}
