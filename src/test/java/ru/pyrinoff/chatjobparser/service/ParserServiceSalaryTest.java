package ru.pyrinoff.chatjobparser.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pyrinoff.chatjobparser.common.AbstractSpringTest;
import ru.pyrinoff.chatjobparser.enumerated.model.dto.CurrencyEnum;
import ru.pyrinoff.chatjobparser.exception.service.parser.VacancyNotCorrect;
import ru.pyrinoff.chatjobparser.model.dto.Vacancy;
import ru.pyrinoff.chatjobparser.model.parser.ParserServiceResult;
import ru.pyrinoff.chatjobparser.parser.salary.AbstractSalaryParser;

import static ru.pyrinoff.chatjobparser.service.ParserService.cleanupText;

@TestMethodOrder(MethodOrderer.MethodName.class)
public final class ParserServiceSalaryTest extends AbstractSpringTest {

    @Autowired
    private static @NotNull ParserService parserService;

    @BeforeAll
    public static void setUp() {
        parserService = context.getBean(ParserService.class);
        AbstractSalaryParser.DEBUG = true;
    }

    @Test
    public void setUpOk() {
    }

    @Test
    public void parseSalary() {
        @NotNull final String text = "ЗП: от 3000$ гросс (обсуждается индивидуально)";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(3000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(null, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary2() {
        @NotNull final String text = "Вилка: от 2500$";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(2500, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(null, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary3() {
        @NotNull final String text = "ЗП: от 3000$ до 5000$";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(3000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(5000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary4() {

        @NotNull final String text = "ЗП: от 3000 до 5000$";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(3000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(5000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary5() {
        AbstractSalaryParser.DEBUG = true;
        @NotNull final String text = "ЗП: от 3 000$ до 5 000$";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(3000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(5000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary6() {

        @NotNull final String text = "ЗП: от 3 000$ до 5 000 $";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(3000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(5000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, parserServiceResult.getCurrency());
    }


    @Test
    public void parseSalary7() {

        @NotNull final String text = "ЗП: 3000-5000$ fdfd";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(3000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(5000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary8() {

        @NotNull final String text = "З/П: от 100к net на руки";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(100000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(null, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary9() {

        @NotNull final String text = "Зарплата: 100 000р. - 150 000р. ";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(100000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(150000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary10() {

        @NotNull final String text = "Вилка 160000-190000 рублей на руки.";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(160000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(190000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary11() {

        @NotNull final String text = "Зарплата: 250 000 - 350 000 (штат или ИП)";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(250000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(350000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary12() {

        @NotNull final String text = "зарплатная вилка от 180 до 260 тыс. руб. на руки. требования опыт в нагрузочном тестировании от 1 5 лет. знание ";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(180000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(260000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary13() {

        @NotNull final String text = "занятость полная \uD83D\uDCB0 уровень з/п от $2000 более подробно по результатам собеседования";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(2000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(null, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary14() {

        @NotNull final String text = "le ✅ от 3 лет опыта) ✅без частых смен работы ✅локация мир релокейт на кипр) ✅формат работы офис/удаленно кипр) ✅формат оформления штат ✅вилка от 2500$ суть продукта компания разрабатывает мобильные игры и приложе";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(2500, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(null, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, parserServiceResult.getCurrency());
    }


    @Test
    public void parseSalary15() {

        @NotNull final String text = "Зарплата: 100 000р. – 150 000р. ";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(100000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(150000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary16() {

        @NotNull final String text = "   занятость полная зарплатная вилка 250.000 300.000 net оформление по ип проект с";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(250000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(300000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary17() {

        @NotNull final String text = "командой ❤️ carprice \uD83D\uDCB0з/п 170 000 250 000 руб. \uD83D\uDDA5гибрид/офис алмата \uD83C\uDF1Fсерв\t";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(170000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(250000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary18() {

        @NotNull final String text = "оты. зп 1500 2300$ net т";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(1500, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(2300, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary19() {

        @NotNull final String text = " зарплатная вилка от 40 до 80к нетто опыт работы без опыта требовани";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(40000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(80000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary20() {

        @NotNull final String text = "#job #qa #manual #middle #москва \uD83D\uDCA5 qa manual engineer/ старший ручной тестировщик \uD83D\uDCA5 \uD83D\uDCCD компания selecty it \uD83D\uDCCB формат работы офис в москве гибридный формат \uD83D\uDCB0 уровень дохода 180 000 net \uD83D\uDCBC проект работа над созданием мессенджера \uD83C\uDFAF задачи анализ поступающих бизнес-требований; функциональное тестирование приложений; работа с командой разработчиков; сопровождение проектов анализ рекламаций поступающих от заказчика. ❗️требования опыт работы на позиции инженера-тестировщика от 2 лет; опыт тестирования десктопа; опыт тестирования мобильных приложений; разработка тестовых сценариев планов и стратегии тестирования; планирования и распределения задач внутри команды. опыт работы с логами unix console grep); тестирования rest api; ⚒ условия гибридный график работы 1 неделя в офисе и 3 на удаленке) гибкое начало дня период с 8.00 до 11.00); изучение и использование передовых технологий; профессиональный и карьерный рост; обучение и посещение конференций за счет компании; дмс;. \uD83D\uDCE5 в случае заинтересованности пишите\uD83E\uDD17 @tsnikonova ";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(180000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(180000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary21() {

        @NotNull final String text = "\uD83D\uDCB0зарплата 200 000 руб. – 250 000 руб. на руки.";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(200000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(250000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary22() {

        @NotNull final String text = "s.io) вилка 200. 000 300.000 рублей наш прое";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(200000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(300000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary23() {

        @NotNull final String text = "qa город москва зарплата до 250 тыс. на руки формат удалённо";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(null, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(250000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, parserServiceResult.getCurrency());
    }


    @Test
    public void parseSalary24() {

        @NotNull final String text = "зп от 3к долларов уровень";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(3000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(null, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary25() {

        @NotNull final String text = "от 4.5k EUR чистыми";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(4500, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(null, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary26() {

        @NotNull final String text = "от 5.5k - 7k+ EUR чистыми";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(5500, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(7000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.USD, parserServiceResult.getCurrency());
    }

    @Test
    public void parseSalary27() {
        @NotNull final String text = " окладная вилка 120000 - 150000 т.р. на руки ";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        Assertions.assertThrows(VacancyNotCorrect.class, () -> parserService.parseSalary(parserServiceResult));
    }

    @Test
    public void parseSalary28() {

        @NotNull final String text = "  engineer \uD83D\uDCB0 оплата 230k / месяц название компании ";
        @NotNull final ParserServiceResult parserServiceResult = new ParserServiceResult();
        parserServiceResult.setText(cleanupText(text));
        parserService.parseSalary(parserServiceResult);
        Assertions.assertEquals(230000, parserServiceResult.getSalaryFrom());
        Assertions.assertEquals(230000, parserServiceResult.getSalaryTo());
        Assertions.assertEquals(CurrencyEnum.RUB, parserServiceResult.getCurrency());
    }

    @Test
    public void parsecleanupText() {
        Assertions.assertEquals("от 3000", cleanupText("от 3.000"));
        Assertions.assertEquals("от 200000", cleanupText("от 200 000"));
        Assertions.assertEquals("200000 300000", cleanupText("200. 000 300.000"));
        Assertions.assertEquals("зп от 3000$ до 5000$", cleanupText("ЗП: от 3 000$ до 5 000$"));
    }

}
