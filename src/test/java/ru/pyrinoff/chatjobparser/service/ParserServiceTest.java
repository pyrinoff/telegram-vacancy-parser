package ru.pyrinoff.chatjobparser.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pyrinoff.chatjobparser.common.AbstractSpringTest;
import ru.pyrinoff.chatjobparser.enumerated.model.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;
import ru.pyrinoff.chatjobparser.parser.salary.AbstractParser;

import static ru.pyrinoff.chatjobparser.service.ParserService.cleanupText;

@TestMethodOrder(MethodOrderer.MethodName.class)
public final class ParserServiceTest extends AbstractSpringTest {

    @Autowired
    private static @NotNull ParserService parserService;

    @BeforeAll
    public static void setUp() {
        parserService = context.getBean(ParserService.class);
        AbstractParser.DEBUG = true;
    }

    @Test
    public void setUpOk() {}

    @Test
    public void parseSalary() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "ЗП: от 3000$ гросс (обсуждается индивидуально)";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(3000, vacancy.getSalaryFrom());
        Assertions.assertEquals(null, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, vacancy.getCurrency());
    }

    @Test
    public void parseSalary2() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "Вилка: от 2500$";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(2500, vacancy.getSalaryFrom());
        Assertions.assertEquals(null, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, vacancy.getCurrency());
    }

    @Test
    public void parseSalary3() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "ЗП: от 3000$ до 5000$";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(3000, vacancy.getSalaryFrom());
        Assertions.assertEquals(5000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, vacancy.getCurrency());
    }

    @Test
    public void parseSalary4() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "ЗП: от 3000 до 5000$";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(3000, vacancy.getSalaryFrom());
        Assertions.assertEquals(5000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, vacancy.getCurrency());
    }

    @Test
    public void parseSalary5() {
        @NotNull final Vacancy vacancy = new Vacancy();
        AbstractParser.DEBUG = true;
       @NotNull final String text = "ЗП: от 3 000$ до 5 000$";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(3000, vacancy.getSalaryFrom());
        Assertions.assertEquals(5000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, vacancy.getCurrency());
    }

    @Test
    public void parseSalary6() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "ЗП: от 3 000$ до 5 000 $";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(3000, vacancy.getSalaryFrom());
        Assertions.assertEquals(5000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, vacancy.getCurrency());
    }


    @Test
    public void parseSalary7() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "ЗП: 3000-5000$ fdfd";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(3000, vacancy.getSalaryFrom());
        Assertions.assertEquals(5000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, vacancy.getCurrency());
    }

    @Test
    public void parseSalary8() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "З/П: от 100к net на руки";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(100000, vacancy.getSalaryFrom());
        Assertions.assertEquals(null, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, vacancy.getCurrency());
    }

    @Test
    public void parseSalary9() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "Зарплата: 100 000р. - 150 000р. ";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(100000, vacancy.getSalaryFrom());
        Assertions.assertEquals(150000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, vacancy.getCurrency());
    }

    @Test
    public void parseSalary10() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "Вилка 160000-190000 рублей на руки.";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(160000, vacancy.getSalaryFrom());
        Assertions.assertEquals(190000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, vacancy.getCurrency());
    }

    @Test
    public void parseSalary11() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "Зарплата: 250 000 - 350 000 (штат или ИП)";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(250000, vacancy.getSalaryFrom());
        Assertions.assertEquals(350000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, vacancy.getCurrency());
    }

    @Test
    public void parseSalary12() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "зарплатная вилка от 180 до 260 тыс. руб. на руки. требования опыт в нагрузочном тестировании от 1 5 лет. знание ";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(180000, vacancy.getSalaryFrom());
        Assertions.assertEquals(260000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, vacancy.getCurrency());
    }

    @Test
    public void parseSalary13() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "занятость полная \uD83D\uDCB0 уровень з/п от $2000 более подробно по результатам собеседования";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(2000, vacancy.getSalaryFrom());
        Assertions.assertEquals(null, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, vacancy.getCurrency());
    }

    @Test
    public void parseSalary14() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "le ✅ от 3 лет опыта) ✅без частых смен работы ✅локация мир релокейт на кипр) ✅формат работы офис/удаленно кипр) ✅формат оформления штат ✅вилка от 2500$ суть продукта компания разрабатывает мобильные игры и приложе";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(2500, vacancy.getSalaryFrom());
        Assertions.assertEquals(null, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, vacancy.getCurrency());
    }


    @Test
    public void parseSalary15() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "Зарплата: 100 000р. – 150 000р. ";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(100000, vacancy.getSalaryFrom());
        Assertions.assertEquals(150000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, vacancy.getCurrency());
    }

    @Test
    public void parseSalary16() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "   занятость полная зарплатная вилка 250.000 300.000 net оформление по ип проект с";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(250000, vacancy.getSalaryFrom());
        Assertions.assertEquals(300000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, vacancy.getCurrency());
    }

    @Test
    public void parseSalary17() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "командой ❤️ carprice \uD83D\uDCB0з/п 170 000 250 000 руб. \uD83D\uDDA5гибрид/офис алмата \uD83C\uDF1Fсерв\t";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(170000, vacancy.getSalaryFrom());
        Assertions.assertEquals(250000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, vacancy.getCurrency());
    }

    @Test
    public void parseSalary18() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "оты. зп 1500 2300$ net т";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(1500, vacancy.getSalaryFrom());
        Assertions.assertEquals(2300, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, vacancy.getCurrency());
    }

    @Test
    public void parseSalary19() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = " зарплатная вилка от 40 до 80к нетто опыт работы без опыта требовани";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(40000, vacancy.getSalaryFrom());
        Assertions.assertEquals(80000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, vacancy.getCurrency());
    }

    @Test
    public void parseSalary20() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "#job #qa #manual #middle #москва \uD83D\uDCA5 qa manual engineer/ старший ручной тестировщик \uD83D\uDCA5 \uD83D\uDCCD компания selecty it \uD83D\uDCCB формат работы офис в москве гибридный формат \uD83D\uDCB0 уровень дохода 180 000 net \uD83D\uDCBC проект работа над созданием мессенджера \uD83C\uDFAF задачи анализ поступающих бизнес-требований; функциональное тестирование приложений; работа с командой разработчиков; сопровождение проектов анализ рекламаций поступающих от заказчика. ❗️требования опыт работы на позиции инженера-тестировщика от 2 лет; опыт тестирования десктопа; опыт тестирования мобильных приложений; разработка тестовых сценариев планов и стратегии тестирования; планирования и распределения задач внутри команды. опыт работы с логами unix console grep); тестирования rest api; ⚒ условия гибридный график работы 1 неделя в офисе и 3 на удаленке) гибкое начало дня период с 8.00 до 11.00); изучение и использование передовых технологий; профессиональный и карьерный рост; обучение и посещение конференций за счет компании; дмс;. \uD83D\uDCE5 в случае заинтересованности пишите\uD83E\uDD17 @tsnikonova ";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(180000, vacancy.getSalaryFrom());
        Assertions.assertEquals(180000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, vacancy.getCurrency());
    }

    @Test
    public void parseSalary21() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "\uD83D\uDCB0зарплата 200 000 руб. – 250 000 руб. на руки.";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(200000, vacancy.getSalaryFrom());
        Assertions.assertEquals(250000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, vacancy.getCurrency());
    }

    @Test
    public void parseSalary22() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "s.io) вилка 200. 000 300.000 рублей наш прое";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(200000, vacancy.getSalaryFrom());
        Assertions.assertEquals(300000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, vacancy.getCurrency());
    }

    @Test
    public void parseSalary23() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "qa город москва зарплата до 250 тыс. на руки формат удалённо";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(null, vacancy.getSalaryFrom());
        Assertions.assertEquals(250000, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, vacancy.getCurrency());
    }


    @Test
    public void parseSalary24() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "зп от 3к долларов уровень";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(3000, vacancy.getSalaryFrom());
        Assertions.assertEquals(null, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, vacancy.getCurrency());
    }

    @Test
    public void parseSalary25() {
        @NotNull final Vacancy vacancy = new Vacancy();
       @NotNull final String text = "от 4 5k eur чистыми";
        parserService.parseSalary(cleanupText(text), vacancy);
        Assertions.assertEquals(4500, vacancy.getSalaryFrom());
        Assertions.assertEquals(null, vacancy.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, vacancy.getCurrency());
    }

    @Test
    public void parsecleanupText() {
            Assertions.assertEquals("от 3000", cleanupText("от 3.000"));
            Assertions.assertEquals("от 200000", cleanupText("от 200 000"));
            Assertions.assertEquals("200000 300000", cleanupText("200. 000 300.000"));
            Assertions.assertEquals("зп от 3000$ до 5000$", cleanupText("ЗП: от 3 000$ до 5 000$"));
    }

}
