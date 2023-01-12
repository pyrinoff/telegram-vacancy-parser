package ru.pyrinoff.chatjobparser;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.pyrinoff.chatjobparser.component.MainApplication;
import ru.pyrinoff.chatjobparser.configuration.ApplicationConfiguration;

public class EntryPoint {

    public static void main(String[] args) {
        @NotNull final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        @NotNull final MainApplication mainApplication = context.getBean(MainApplication.class);
        mainApplication.start(args);
    }

}
