package com.shurik16.SpringVaadin.ui.views;

import com.shurik16.SpringVaadin.model.Tcompany;
import com.shurik16.SpringVaadin.model.Totdel;
import com.shurik16.SpringVaadin.model.Tposit;
import com.shurik16.SpringVaadin.model.Tstanding;
import com.shurik16.SpringVaadin.service.*;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.NumberRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;


@Theme("valo")
@Title("Редактирование справочников")
@SpringUI(path = "/guide")
public class GuideView extends UI {

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
    TmanRepository repo;

    private ComboBox boxTabel = new ComboBox("Выбор таблицы");
    private Button close = new MButton("Закрыть");
    private Button delete = new MButton("Удалить");
    private Button add = new MButton("Добавить");
    private TextField nameEditor = new TextField();
    private TextField name = new TextField("Имя: ");


    final int PAGESIZE = 5;

    private enum Tabels {
        POSIT, OTDEL, COMPANY, STATUS;
    }

    private Tabels indexTabel;

    private Grid grid;

    @Override
    protected void init(VaadinRequest request) {

        UI.getCurrent().setPollInterval(500);

        add.setEnabled(false);
        delete.setEnabled(false);

        close.addClickListener(event -> {
            Page.getCurrent().open("/sb/", null);
        });

        boxTabel.setItems("POSIT", "OTDEL", "COMPANY", "STATUS");

        boxTabel.addValueChangeListener(event -> {
            indexTabel = Tabels.valueOf(event.getValue().toString());
            switch (indexTabel) {
                case POSIT:
                    grid = initGridPosit();
                    setContent(getComponent(grid));
                    break;
                case OTDEL:
                    grid = initGridOtdel();
                    setContent(getComponent(grid));
                    break;
                case COMPANY:
                    grid = initGridCompany();
                    setContent(getComponent(grid));
                    break;
                case STATUS:
                    grid = initGridStatus();
                    setContent(getComponent(grid));
                    break;
            }
        });

        name.addValueChangeListener(event -> {
            if (event.getValue().length() > 0)
                add.setEnabled(true);
            else
                add.setEnabled(false);
        });

        add.addClickListener(event -> {
            addDate();
        });

        boxTabel.setSelectedItem("POSIT");

        delete.addClickListener(event -> {

            if (!grid.getSelectedItems().isEmpty()) {

                Object dataGrid = grid.asSingleSelect().getValue();

                switch (indexTabel) {
                    case POSIT:
                        if (repo.findByPosit((Tposit) dataGrid).size() == 0) {
                            tpositRepository.delete((Tposit) dataGrid);
                            grid.setItems(tpositRepository.findAll());
                        } else
                            notificationMess();
                        break;
                    case OTDEL:
                        if (repo.findByOtdel((Totdel) dataGrid).size() == 0) {
                            totdelRepository.delete((Totdel) dataGrid);
                            grid.setItems(totdelRepository.findAll());
                        } else
                            notificationMess();
                        break;
                    case COMPANY:
                        if (repo.findByCompany((Tcompany) dataGrid).size() == 0) {
                            tcompanyRepository.delete((Tcompany) dataGrid);
                            grid.setItems(tcompanyRepository.findAll());
                        } else
                            notificationMess();
                        break;
                    case STATUS:
                        if (repo.findByStanding((Tstanding) dataGrid).size() == 0) {
                            int key = ((Tstanding) dataGrid).getId();
                            //   System.out.println("@@" + key);
                            //   System.out.println(" ** "+historyManRepository.findByStatus((Tstanding) dataGrid).size());
                        /*    if (historyManRepository.findByStatus(key).size()>0)
                                historyManRepository.deleteAllByStatus(key);
                          */
                            tstandingRepository.delete((Tstanding) dataGrid);
                            grid.setItems(tstandingRepository.findAll());
                        } else
                            notificationMess();
                        break;
                }
            } else {
                new Notification("Ошибка", "Не выбрана запись").show(Page.getCurrent());
            }
        });

    }

    private void notificationMess() {
        new Notification("Ошибка", "Есть связанные записи").show(Page.getCurrent());
    }


    protected Component getComponent(Component grid) {
        MVerticalLayout verticalLayout;
        HorizontalLayout components = new HorizontalLayout(boxTabel, close);
        components.setComponentAlignment(close, Alignment.BOTTOM_CENTER);
        HorizontalLayout components0 = new HorizontalLayout(name, add, delete);
        components0.setComponentAlignment(add, Alignment.BOTTOM_CENTER);
        components0.setComponentAlignment(delete, Alignment.BOTTOM_CENTER);
        verticalLayout = new MVerticalLayout(
                components,
                components0,
                grid).expand(grid);


        return verticalLayout;
    }

    private void addDate() {
        switch (indexTabel) {
            case POSIT:
                Tposit tposit = new Tposit();
                tposit.setName_position(name.getValue());
                tpositRepository.save(tposit);
                break;
            case OTDEL:
                Totdel totdel = new Totdel();
                totdel.setName_otdel(name.getValue());
                totdelRepository.save(totdel);
                break;
            case COMPANY:
                Tcompany tcompany = new Tcompany();
                tcompany.setName_company(name.getValue());
                tcompanyRepository.save(tcompany);
                break;
            case STATUS:
                Tstanding tstanding = new Tstanding();
                tstanding.setName_standing(name.getValue());
                tstandingRepository.save(tstanding);
                break;
        }
    }


    private Grid initGridPosit() {
        Grid<Tposit> grid = new Grid<>();
        grid.setCaption("Дважды кликните по строчке для редактирования");
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addColumn(Tposit::getId, new NumberRenderer("%02d"))
                .setCaption("##")
                .setExpandRatio(0);

        grid.addColumn(Tposit::getName_position)
                .setEditorComponent(nameEditor, Tposit::setName_position)
                .setCaption("Name")
                .setExpandRatio(2);

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.getEditor().setEnabled(true);

        grid.setItems(tpositRepository.findAll());

        grid.getEditor().addSaveListener(event -> {
            Tposit tposit = event.getBean();
            tpositRepository.save(tposit);
        });

        // Видемость кнопки удалить
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (grid.getSelectedItems().isEmpty())
                delete.setEnabled(false);
            else
                delete.setEnabled(true);
        });

        return grid;
    }


    private Grid initGridOtdel() {
        Grid<Totdel> grid = new Grid<>();
        grid.setCaption("Дважды кликните по строчке для редактирования");
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addColumn(Totdel::getId, new NumberRenderer("%02d"))
                .setCaption("##")
                .setExpandRatio(0);

        grid.addColumn(Totdel::getName_otdel)
                .setEditorComponent(nameEditor, Totdel::setName_otdel)
                .setCaption("Name")
                .setExpandRatio(2);

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.getEditor().setEnabled(true);

        grid.setItems(totdelRepository.findAll());

        grid.getEditor().addSaveListener(event -> {
            Totdel totdel = event.getBean();
            totdelRepository.save(totdel);
        });

        // Видемость кнопки удалить
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (grid.getSelectedItems().isEmpty())
                delete.setEnabled(false);
            else
                delete.setEnabled(true);
        });

        return grid;
    }

    private Grid initGridCompany() {
        Grid<Tcompany> grid = new Grid<>();
        grid.setCaption("Дважды кликните по строчке для редактирования");
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addColumn(Tcompany::getId, new NumberRenderer("%02d"))
                .setCaption("##")
                .setExpandRatio(0);

        grid.addColumn(Tcompany::getName_company)
                .setEditorComponent(nameEditor, Tcompany::setName_company)
                .setCaption("Name")
                .setExpandRatio(2);

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.getEditor().setEnabled(true);

        grid.setItems(tcompanyRepository.findAll());

        grid.getEditor().addSaveListener(event -> {
            Tcompany tcompany = event.getBean();
            tcompanyRepository.save(tcompany);
        });

        // Видемость кнопки удалить
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (grid.getSelectedItems().isEmpty())
                delete.setEnabled(false);
            else
                delete.setEnabled(true);
        });

        return grid;
    }

    private Grid initGridStatus() {
        Grid<Tstanding> grid = new Grid<>();
        grid.setCaption("Дважды кликните по строчке для редактирования");
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addColumn(Tstanding::getId, new NumberRenderer("%02d"))
                .setCaption("##")
                .setExpandRatio(0);

        grid.addColumn(Tstanding::getName_standing)
                .setEditorComponent(nameEditor, Tstanding::setName_standing)
                .setCaption("Name")
                .setExpandRatio(2);

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.getEditor().setEnabled(true);

        grid.setItems(tstandingRepository.findAll());

        grid.getEditor().addSaveListener(event -> {
            Tstanding tstanding = event.getBean();
            tstandingRepository.save(tstanding);
        });

        // Видемость кнопки удалить
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (grid.getSelectedItems().isEmpty())
                delete.setEnabled(false);
            else
                delete.setEnabled(true);
        });

        return grid;
    }

}
