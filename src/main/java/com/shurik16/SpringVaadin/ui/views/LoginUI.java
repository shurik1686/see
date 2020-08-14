package com.shurik16.SpringVaadin.ui.views;

import com.shurik16.SpringVaadin.spring.security.SecurityConfiguration;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringUI(path = "/login")

@Title("Вход в систему учета СЭБ")

@Theme("valo")

public class LoginUI extends UI {

    private static Logger LOG = LoggerFactory.getLogger(LoginUI.class);

    TextField user;

    PasswordField password;

    Button loginButton = new Button("Вход", this::loginButtonClick);

    Notification notification = new Notification("Ошибка");


    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Override
    protected void init(VaadinRequest request) {

        setSizeFull();


        user = new TextField("Логин:");

        user.setWidth("300px");

        //user.setRequired(true);

        //user.setInputPrompt("Your username");


        password = new PasswordField("Пароль:");

        password.setWidth("300px");

        //password.setRequired(true);

        password.setValue("");

        //password.setNullRepresentation("");


        VerticalLayout fields = new VerticalLayout(user, password, loginButton);

        fields.setCaption("Введите логин/пароль для дустпа к приложению");

        fields.setSpacing(true);

        fields.setMargin(new MarginInfo(true, true, true, false));

        fields.setSizeUndefined();


        VerticalLayout uiLayout = new VerticalLayout(fields);

        uiLayout.setSizeFull();

        uiLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);

        ///setStyleName(Reindeer.LAYOUT_BLUE);

        setFocusedComponent(user);


        setContent(uiLayout);

    }


    public void loginButtonClick(Button.ClickEvent e) {
   try {
       Authentication auth = new UsernamePasswordAuthenticationToken(user.getValue(), password.getValue());
       Authentication authenticated = daoAuthenticationProvider.authenticate(auth);
       SecurityContextHolder.getContext().setAuthentication(authenticated);
   }catch (BadCredentialsException badCredentialsException){
             notification.show("bgb");
             System.out.print(badCredentialsException.getMessage());
             LOG.error(badCredentialsException.getMessage());
   }
//redirect to main application
        getPage().setLocation("/sb/");

    }


}
