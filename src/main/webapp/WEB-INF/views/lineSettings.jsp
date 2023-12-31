<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8"%>
<!-- LINE SETTING START -->
<div class="card">
    <div class="card-header">
        <ul class="nav nav-tabs nav-tabs">
            <c:set var="uniqueId" value="0"/>
            <c:forEach var="theIndex" begin="0" end="${LINES_COUNT-1}">
                <li class="nav-item">
                    <a style="cursor: pointer;" class="nav-link
                            <c:if test="${theIndex == 0}"><c:out value=" active"/></c:if>
                            <c:if test="${theIndex != 0}"><c:out value=" "/></c:if>
                        " data-toggle="line<c:out value="${theIndex}"/>">Настройки линии <c:out value="${theIndex+1}"/>

                    </a>
                </li>
            </c:forEach>
        </ul>
    </div>
    <!--------------------------------------------------------------->
    <c:forEach var="theIndex" begin="0" end="${LINES_COUNT-1}">
        <fieldset data-line-index="<c:out value="${theIndex}"/>">
            <div class="card-body linesCard <c:if test="${theIndex != 0}"><c:out value=" d-none"/></c:if>"
                 id="line<c:out value="${theIndex}"/>">
                <!--            <div class="card-header"><h3 class="text-center mb-4">Линия 1</h3></div>-->
                <div class="card-body">
                    <div class="form-group">
                        <input
                                name="enabled"
                                type="checkbox"
                                class="btn-check"
                                class="custom-control-input"
                                autocomplete="off"
                                id="btn-check<c:out value="${uniqueId}"/>"
                                <c:if test="${theIndex == 0}"><c:out value=" checked"/></c:if>/>
                        <label
                                class="btn btn-outline-success"
                                for="btn-check<c:out value="${uniqueId}"/>"
                        >Включить линию</label>
                        <c:set var="uniqueId" value="${uniqueId+1}"/>
                        <div class="alert alert-warning enabledAlert d-none">Линия отключена! Переключите кнопку
                            и
                            нажмите кнопку "Обновить таблицу".
                        </div>
                    </div>
                    <div class="card-body form-group">
                        <h5>Маркеры:</h5>
                        <c:forEach var="oneMarker" items="${MARKERS}">
                            <div class="form-check form-check-inline">
                                <input
                                        name="markers"
                                        type="checkbox"
                                        class="form-check-input"
                                    <%--class="btn-check"--%>
                                        autocomplete="off"
                                        id="btn-check<c:out value="${uniqueId}"/>"
                                        value="<c:out value="${oneMarker}"/>">
                                <label
                                        for="btn-check<c:out value="${uniqueId}"/>"
                                        <c:set var="uniqueId" value="${uniqueId+1}"/>
                                    <%--class="btn btn-primary"--%>
                                ><c:out value="${oneMarker}"/></label>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="card-body form-group">
                        <input
                                type="radio"
                                name="markersOperator<c:out value="${theIndex}"/>"
                                id="markersOperatorId<c:out value="${uniqueId+1}"/>"
                                value="AND"
                                checked
                        >
                        <label
                                class="col-form-label"
                                for="markersOperatorId<c:out value="${uniqueId+1}"/>"
                                <c:set var="uniqueId" value="${uniqueId+1}"/>
                        >Связать маркеры через "И" (AND)</label>
                        <input
                                type="radio"
                                name="markersOperator<c:out value="${theIndex}"/>"
                                id="markersOperatorId<c:out value="${uniqueId+1}"/>"
                                value="OR"
                        >
                        <label
                                class="col-form-label"
                                for="markersOperatorId<c:out value="${uniqueId+1}"/>"
                                <c:set var="uniqueId" value="${uniqueId+1}"/>
                        >Связать маркеры через "ИЛИ" (OR)</label>
                    </div>
                    <div class="card-body form-group">
                        <h5>Вакансия содержит слова:</h5>
                        <c:forEach var="oneWord" items="${WORDS}">
                            <div class="form-check form-check-inline">
                                <input
                                        name="words"
                                        type="checkbox"
                                        class="form-check-input"
                                    <%--class="btn-check"--%>
                                        autocomplete="off"
                                        value="<c:out value="${oneWord}"/>"
                                        id="btn-check<c:out value="${uniqueId}"/>"
                                >
                                <label
                                        class="form-check-label"
                                        for="btn-check<c:out value="${uniqueId}"/>" <c:set
                                        var="uniqueId" value="${uniqueId+1}"/>
                                ><c:out value="${oneWord}"/></label>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="card-body form-group">
                        <input
                                type="radio"
                                name="wordsOperator<c:out value="${theIndex}"/>"
                                id="wordsOperatorId<c:out value="${uniqueId+1}"/>"
                                value="AND"
                                checked
                        >
                        <label
                                class="col-form-label"
                                for="wordsOperatorId<c:out value="${uniqueId}"/>"
                                <c:set var="uniqueId" value="${uniqueId+1}"/>
                        >Связать слова через "И" (AND)</label>
                        <input
                                type="radio"
                                name="wordsOperator<c:out value="${theIndex}"/>"
                                id="wordsOperatorId<c:out value="${uniqueId+1}"/>"
                                value="OR"
                        >
                        <label
                                class="col-form-label"
                                for="wordsOperatorId<c:out value="${uniqueId}"/>"
                                <c:set var="uniqueId" value="${uniqueId+1}"/>
                        >Связать слова через "ИЛИ" (OR)</label>
                    </div>
                    <!-- SALARY -->
                    <div class="card-body form-group">
                        <h4>Фильтр по ЗП</h4>
                        <p>Считать вакансии, в которых:</p>
                        <ul style="list-style-type: none;">
                            <li>
                                <div class="form-check form-check-inline">
                                    <input
                                            class="form-check-input"
                                            type="checkbox"
                                            name="fromBorder"
                                            checked
                                            id="salary1-<c:out value="${theIndex}"/>"
                                    >
                                    <label
                                            class="form-check-label"
                                            for="salary1-<c:out value="${theIndex}"/>"
                                    >указана только нижняя граница ("от")</label>
                                </div>
                            </li>
                            <li>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="toBorder" checked
                                           id="salary2-<c:out value="${theIndex}"/>">
                                    <label class="form-check-label" for="salary2-<c:out value="${theIndex}"/>">указана
                                        только верхняя граница ("до")</label>
                                </div>
                            </li>
                            <li>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="bothBorders" checked
                                           id="salary3-<c:out value="${theIndex}"/>">
                                    <label class="form-check-label" for="salary3-<c:out value="${theIndex}"/>"
                                    >указаны обе границы ("от" и "до") (берется среднее)</label>
                                </div>
                            </li>
                            <li>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="allowPredicted"
                                           checked id="salary4-<c:out value="${theIndex}"/>">
                                    <label class="form-check-label" for="salary4-<c:out value="${theIndex}"/>"
                                    >валюта была угадана (в редких случаях может
                                        давать
                                        неверный
                                        результат)</label>
                                </div>
                            </li>
                        </ul>
                        <div class="row">
                            <div class="col-md-2">
                                <label class="col-form-label">От:</label>
                                <input type="number" class="form-control" name="salaryFrom" step="1000"
                                       for="salary1-<c:out value="${theIndex}"/>">
                            </div>
                            <div class="col-md-2">
                                <label class="col-form-label">До:</label>
                                <input type="number" class="form-control" name="salaryTo" step="1000">
                            </div>
                        </div>
                    </div>
                    <!-- SALARY END -->
                    <!-- TIME -->
                    <div class="card-body form-group">
                        <h4>Временной промежуток</h4>
                        <c:forEach var="onePeriod" items="${PERIODS}">
                            <div class="form-check">
                                <input class="form-check-input
                                            dateRange-<c:out value="${theIndex}"/>-<c:out value="${onePeriod.key}"/>
                                            " type="radio"
                                       name="dateRange<c:out value="${theIndex}"/>"
                                       onclick="setDateRange('<c:out value="${onePeriod.key}"/>', <c:out
                                               value="${theIndex}"/>)"
                                    <%--<c:if test="${onePeriod.key == 'lastMonth'}"><c:out value=" checked"/></c:if>--%>
                                       id="period-<c:out value="${theIndex}"/>-<c:out value="${uniqueId+1}"/>"
                                >
                                <label class="form-check-label"
                                       for="period-<c:out value="${theIndex}"/>-<c:out value="${uniqueId+1}"/>"
                                        <c:set var="uniqueId" value="${uniqueId+1}"/>
                                ><c:out value="${onePeriod.value}"/></label>
                            </div>
                        </c:forEach>
                        <div class="card-body form-group customPeriod" style="display: none;">
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
                        <!-- MONTHS YEAR ONE -->
                        <div class="form-check card-body">
                            <h5>Текущий год:</h5>
                            <c:forEach var="oneMonthPeriod" items="${PERIODS_MONTHS}">
                                <input class="btn-check" type="radio"
                                       name="dateRange<c:out value="${theIndex}"/>"
                                       onclick="setDateRange('<c:out value="${oneMonthPeriod.key}"/>', <c:out
                                               value="${theIndex}"/>, 0)"
                                       id="period-<c:out value="${theIndex}"/>-<c:out value="${uniqueId+1}"/>"
                                >
                                <label class="btn btn-outline-primary"
                                       for="period-<c:out value="${theIndex}"/>-<c:out value="${uniqueId+1}"/>"
                                        <c:set var="uniqueId" value="${uniqueId+1}"/>
                                ><c:out value="${oneMonthPeriod.value}"/></label>
                            </c:forEach>
                        </div>
                        <!-- MONTHS YEAR ONE END -->
                        <!-- MONTHS YEAR TWO -->
                        <div class="form-check card-body">
                            <h5>Предыдущий год:</h5>
                            <c:forEach var="oneMonthPeriod" items="${PERIODS_MONTHS}">
                                <input class="btn-check" type="radio"
                                       name="dateRange<c:out value="${theIndex}"/>"
                                       onclick="setDateRange('<c:out value="${oneMonthPeriod.key}"/>', <c:out
                                               value="${theIndex}"/>, 1)"
                                       id="period-<c:out value="${theIndex}"/>-<c:out value="${uniqueId+1}"/>"
                                >
                                <label class="btn btn-outline-primary"
                                       for="period-<c:out value="${theIndex}"/>-<c:out value="${uniqueId+1}"/>"
                                        <c:set var="uniqueId" value="${uniqueId+1}"/>
                                ><c:out value="${oneMonthPeriod.value}"/></label>
                            </c:forEach>
                        </div>
                        <div class="form-check">
                            <div class="alert alert-warning dateAlert d-none"></div>
                        </div>
                        <!-- MONTHS YEAR TWO END -->
                    </div>
                </div>
                <!-- TIME END -->
            </div>
        </fieldset>
    </c:forEach>
    <!--------------------------------------------------------------->
</div>
<!-- LINE SETTING END -->