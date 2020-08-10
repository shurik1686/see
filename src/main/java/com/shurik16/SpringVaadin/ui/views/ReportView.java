package com.shurik16.SpringVaadin.ui.views;

import com.shurik16.SpringVaadin.model.Tcompany;
import com.shurik16.SpringVaadin.model.Tman;
import com.shurik16.SpringVaadin.model.Tstanding;
import com.shurik16.SpringVaadin.model.Ttest;
import com.shurik16.SpringVaadin.service.TcompanyRepository;
import com.shurik16.SpringVaadin.service.TmanRepository;
import com.shurik16.SpringVaadin.service.TstandingRepository;
import com.shurik16.SpringVaadin.service.TtestRepository;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Theme("valo")
@Title("Отчёт проверки")
@SpringUI(path = "/report")
public class ReportView extends UI {

    @Autowired
    TcompanyRepository tcompanyRepository;

    @Autowired
    TstandingRepository tstandingRepository;

    @Autowired
    TmanRepository repo;

    @Autowired
    TtestRepository repot;

    final int PAGESIZE = 5;

    private MGrid<Tman> list = new MGrid<>(Tman.class)
            .withProperties("firstname", "lastname", "middlename", "town_b")
            .withColumnHeaders("Ф", "И", "О", "Город")
            .withFullWidth();

    private DateTimeField date_start = new DateTimeField("С");
    private DateTimeField date_end = new DateTimeField("По");
    private ComboBox<Tcompany> company = new ComboBox<Tcompany>("Компания");
    private ComboBox<Tstanding> standing = new ComboBox<Tstanding>("Статус");
    private Label count = new Label("Наидено");
    private Button close = new MButton("Закрыть");

    @Override
    protected void init(VaadinRequest request) {

        UI.getCurrent().setPollInterval(500);

        date_end.setResolution(DateTimeResolution.DAY);
        date_end.setDateFormat("dd.MM.yyyy");
        date_start.setResolution(DateTimeResolution.DAY);
        date_start.setDateFormat("dd.MM.yyyy");

        close.addClickListener(event -> {
            Page.getCurrent().open("/sb/", null);
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

        date_start.addValueChangeListener(event -> {
            listEntities();
            System.out.println("00000" + isDate());
        });

        date_end.addValueChangeListener(event -> {
            listEntities();
            System.out.println("11111" + isDate());
        });

        list.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());

        list.setDescriptionGenerator(tman -> {
            Ttest t = getTest(tman.getId().intValue());
            String rez = "Дата проверки: " + t.getData_test() + "\n" +
                    "Результат проверки: " + t.getResult() + "\n" +
                    "Примечание: " + t.getTnote() + "\n";
            return rez;
        });

        listEntities();

        setContent(getComponent(null));
    }

    protected Component getComponent(Tman tman) {
        MVerticalLayout verticalLayout;

        verticalLayout = new MVerticalLayout(
                new HorizontalLayout(company, standing, date_start, date_end),//,lastname,middlename),
                new HorizontalLayout(count, close),
                list).expand(list);

        return verticalLayout;
    }

    private void listEntities() {

        List<Tman> listTman = repo.findAll();
        List<Tman> listTmanRez = new ArrayList<>();
        List<Ttest> listTtest = repot.findAll();

        if (company.getSelectedItem().isPresent() && !standing.getSelectedItem().isPresent()) {
            listTman = repo.findByCompany(company.getValue());
        } else if (company.getSelectedItem().isPresent() && standing.getSelectedItem().isPresent()) {
            listTman = repo.findByCompanyAndStanding(company.getValue(), standing.getValue());
        } else if (!company.getSelectedItem().isPresent() && standing.getSelectedItem().isPresent()) {
            listTman = repo.findByStanding(standing.getValue());
        }

        for (Tman x : listTman) {
            for (Ttest t : listTtest)
                if (t.getWorkingid() == x.getId() && getDateReport()[0].before(t.getData_testt())
                        && getDateReport()[1].after(t.getData_testt()))
                    listTmanRez.add(x);
        }

        list.setRows(listTmanRez);
        count.setValue("Найдено: " + listTmanRez.size());

        adjustActionButtonState();
    }

    protected boolean isDate() {
        if (!date_end.isEmpty() || !date_start.isEmpty())
            return true;
        else
            return false;
    }

    protected Ttest getTest(int id) {
        List<Ttest> listTtest = repot.findAll();
        for (Ttest t : listTtest)
            if (t.getWorkingid() == id)
                return t;

        return null;
    }

    protected Date[] getDateReport() {

        Date[] dates = new Date[2];

        if (!date_start.isEmpty()) {
            LocalDateTime localDateTime = date_start.getValue();
            Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
            dates[0] = Date.from(instant);
        } else
            dates[0] = new Date(0);

        if (!date_end.isEmpty()) {
            LocalDateTime localDateTimeEnd = date_end.getValue();
            Instant instantEnd = localDateTimeEnd.toInstant(ZoneOffset.UTC);
            dates[1] = Date.from(instantEnd);
        } else
            dates[1] = new Date();

        return dates;
    }

    protected void adjustActionButtonState() {

        boolean hasSelection = !list.getSelectedItems().isEmpty();
        if (hasSelection) {

        }
        //edit.setEnabled(hasSelection);
        //delete.setEnabled(hasSelection);
    }
}
