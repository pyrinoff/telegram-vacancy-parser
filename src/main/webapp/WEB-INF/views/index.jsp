<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="./includes/bootstrap.min.css" rel="stylesheet">
    <link href="./includes/custom.css" rel="stylesheet">
    <script src="./includes/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <!--<script src="./includes/jquery-3.6.3.min.js"></script>-->
    <script src="./includes/custom.js"></script>
</head>
<body>
<div class="container">
    <div class="card border-0">
        <div class="card-body p-0" id="info-block">
        </div>
    </div>
    <div id="curve_chart" style="width: 900px; height: 500px;"></div>
    <div class="form-group">
        <button class="btn btn-primary" id="submit">Обновить таблицу</button>
        <button class="btn btn-dark" onclick="disableLineTest()">Test</button>
    </div>
    <div class="form-group">
        <h4>Валюта вакансии</h4>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="salaryCurrency" value="RUB" checked>
            <label class="form-check-label">Рубли</label>
        </div>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="salaryCurrency" value="USD">
            <label class="form-check-label">$</label>
        </div>
    </div>
    <div class="">
        <c:forEach var="theIndex" begin="0" end="${LINES_COUNT-1}">
            <button class="btn
            <c:if test="${theIndex == 0}"><c:out value=" btn-dark"/></c:if>
            <c:if test="${theIndex != 0}"><c:out value=" btn-light"/></c:if>
            " data-toggle="line<c:out value="${theIndex}"/>">Настройки линии <c:out value="${theIndex+1}"/></button>
        </c:forEach>
    </div>
    <form>
        <!--------------------------------------------------------------->
        <c:forEach var="theIndex" begin="0" end="${LINES_COUNT-1}">
        <fieldset data-line-index="<c:out value="${theIndex}"/>">
            <div class="linesCard <c:if test="${theIndex != 0}"><c:out value=" d-none"/></c:if>" id="line<c:out value="${theIndex}"/>">
                <!--            <div class="card-header"><h3 class="text-center mb-4">Линия 1</h3></div>-->
                <div class="card-body">
                    <div class="form-group">
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="enabled"
                                <c:if test="${theIndex == 0}"><c:out value=" checked"/></c:if>>
                            <label class="form-check-label">Отображать</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <h4>Маркеры:</h4>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="markers" value="junior">
                            <label class="form-check-label">junior</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="markers" value="middle">
                            <label class="form-check-label">middle</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="markers" value="senior">
                            <label class="form-check-label">senior</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <h4>Вакансия содержит слова:</h4>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="words" value="java">
                            <label class="form-check-label">java</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="words" value="python">
                            <label class="form-check-label">python</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="checkbox" name="words" value="javascript">
                            <label class="form-check-label">javascript</label>
                        </div>
                    </div>
                    <!-- SALARY -->
                    <div class="form-group">
                        <h4>Фильтр по ЗП</h4>
                        <p>Считать вакансии, в которых:</p>
                        <ul style="list-style-type: none;">
                            <li>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="fromBorder" checked>
                                    <label class="form-check-label">указана только нижняя граница ("от")</label>
                                </div>
                            </li>
                            <li>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="toBorder"
                                           checked>
                                    <label class="form-check-label">указана только верхняя граница ("до")</label>
                                </div>
                            </li>
                            <li>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="bothBorders" checked>
                                    <label class="form-check-label">указаны обе границы ("от" и "до") (берется
                                        среднее)</label>
                                </div>
                            </li>
                            <li>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="allowPredicted" checked>
                                    <label class="form-check-label">валюта была угадана (в редких случаях может давать
                                        неверный
                                        результат)</label>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="form-group">
                        <div class="form-group row">
                            <div class="col-md-4">
                                <label class="col-form-label ">От:</label>
                                <input type="number" class="form-control" name="salaryFrom">
                            </div>
                            <div class="col-md-4">
                                <label class="col-form-label">До:</label>
                                <input type="number" class="form-control" name="salaryTo">
                            </div>
                        </div>
                    </div>
                    <!-- SALARY END -->
                    <!-- TIME -->
                    <div class="form-group">
                        <h5>Временной промежуток</h5>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="dateRange<c:out value="${theIndex}"/>"
                                   onclick="setDateRange('currentMonth', <c:out value="${theIndex}"/>)" checked>
                            <label class="form-check-label">Текущий месяц (см. дату обновления данных)</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="dateRange<c:out value="${theIndex}"/>"
                                   onclick="setDateRange('lastMonth', <c:out value="${theIndex}"/>)">
                            <label class="form-check-label">Предыдущий месяц</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="dateRange<c:out value="${theIndex}"/>"
                                   onclick="setDateRange('lastThreeMonths', <c:out value="${theIndex}"/>)">
                            <label class="form-check-label">Последние три месяца (без текущего)</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="dateRange<c:out value="${theIndex}"/>"
                                   onclick="setDateRange('lastSixMonths', <c:out value="${theIndex}"/>)">
                            <label class="form-check-label">Последние 6 месяцев (без текущего)</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="dateRange<c:out value="${theIndex}"/>"
                                   onclick="setDateRange('currentYear', <c:out value="${theIndex}"/>)">
                            <label class="form-check-label">Текущий год</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="dateRange<c:out value="${theIndex}"/>"
                                   onclick="setDateRange('currentYearMinusOne', <c:out value="${theIndex}"/>)">
                            <label class="form-check-label">Предыдущий год</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="dateRange<c:out value="${theIndex}"/>"
                                   onclick="setDateRange('currentYearMinusTwo', <c:out value="${theIndex}"/>)">
                            <label class="form-check-label">Позапрошлый год</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="dateRange<c:out value="${theIndex}"/>"
                                   onclick="setDateRange('all', <c:out value="${theIndex}"/>)">
                            <label class="form-check-label">Весь период</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="dateRange<c:out value="${theIndex}"/>"
                                   onclick="showCustomYearDiv(true, <c:out value="${theIndex}"/>)">
                            <label class="form-check-label">Кастомный период</label>
                        </div>
                        <div class="form-group customPeriod" style="display: none;">
                            <!-- Additional div for custom period -->
                            <div class="form-group row">
                                <div class="col-md-4">
                                    <label class="col-form-label col-md-2">От:</label>
                                    <input type="date" class="form-control" name="periodFrom">
                                </div>
                                <div class="col-md-4">
                                    <label class="col-form-label col-md-2">До:</label>
                                    <input type="date" class="form-control" name="periodTo">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- TIME END -->
            </div>
        </fieldset>
        </c:forEach>
        <!--------------------------------------------------------------->
    </form>
    <div class="form-group">
        <div class="form-check">
            <p>Дата последнего обновления базы данных: <c:out value="${DB_LAST_VACANCY_DATE}"/></p>
            <p>Записей в БД: <c:out value="${DB_ROWS_COUNT}"/></p>
        </div>
    </div>
</div>
</body>
</html>
