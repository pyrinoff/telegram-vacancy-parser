package ru.pyrinoff.chatjobparser;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.pyrinoff.chatjobparser.component.MainApplication;
import ru.pyrinoff.chatjobparser.component.WordStat;
import ru.pyrinoff.chatjobparser.configuration.ApplicationConfiguration;

public class EntryPointStat {

    public static void main(String[] args) {
        @NotNull final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        @NotNull final WordStat wordStat = context.getBean(WordStat.class);
        long before = System.currentTimeMillis();
        wordStat.start(args);
        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before) + " ms");
    }

}
