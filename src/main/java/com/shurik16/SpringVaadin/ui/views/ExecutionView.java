package com.shurik16.SpringVaadin.ui.views;

import com.shurik16.SpringVaadin.model.Tman;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.DownloadButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;

@SpringUI(path = "/executions")
@Title("Исполнительное производство")
@Theme("valo")
public class ExecutionView extends UI {

    final int PAGESIZE = 5;

    private Button exit = new MButton("Выход");
    private Button buttonOpen = new MButton("Открыть");
    private Button buttonOpenReport = new MButton("Отчеты");
    private Button add = new MButton("Добавить");
    private Button delete = new ConfirmButton(VaadinIcons.TRASH, "Удалить",
            "Удалить выбранную запись?", this::remove);

    private MTextField name = new MTextField("Компания")
            .withPlaceholder("Поле фильтра");

    private ComboBox form = new ComboBox("Экранная форма");

    private Button report = new DownloadButton();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        UI.getCurrent().setPollInterval(500);

        exit.addClickListener(event -> {
            Page.getCurrent().open("/sb/logout", null);
        });

        buttonOpen.addClickListener(event -> {
            //openSystem();
        });
        add.addClickListener(event -> {
            //Page.getCurrent().open("/sb/man?id=0", null);
        });


        form.setItems("Кадры", "Договора", "Исполнительное");
        form.setSelectedItem("Исполнительное");
        form.addValueChangeListener(valueChangeEvent -> {
            formValueChangeListener(valueChangeEvent);
        });

        setContent(getComponent(null));
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
        HorizontalLayout component = new HorizontalLayout(name);
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
                new HorizontalLayout(buttonOpen, add, delete, buttonOpenReport, report, exit)
                //,list).expand(list);
        );


        return verticalLayout;
    }

    private void formValueChangeListener(HasValue.ValueChangeEvent valueChangeEvent) {
        Object value = valueChangeEvent.getValue();
        if ("Кадры".equals(value)) {
            Page.getCurrent().open("/sb/", null);
        } else if ("Договора".equals(value)) {
            Page.getCurrent().open("/sb/contracts", null);
        }
    }

    private void remove() {
  /*  boolean hasSelection = !list.getSelectedItems().isEmpty();
    if (hasSelection) {
        Tman mandelete = list.asSingleSelect().getValue();
        historyManRepository.deleteAllByMankey(mandelete.getId().intValue());
        ttestRepository.deleteAllByWorkingid(mandelete.getId().intValue());
        repo.delete(mandelete);
    }
    list.setRows(repo.findAll());*/
    }

}
