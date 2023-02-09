<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" %>
<!-- LINE SETTING STATIC START -->
<div class="card border-none">
    <div class="card-body">
        <h4>Общие настройки для всех линий</h4>
        <!-- CURRENCY START -->

        <div class="row">

            <div class="col-md-2">

                <h5>Валюта вакансии</h5>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="salaryCurrency" value="RUB" checked
                           id="currencyRub">
                    <label class="form-check-label" for="currencyRub">Рубли</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="salaryCurrency" value="USD" id="currencyUsd">
                    <label class="form-check-label" for="currencyUsd">$/EUR</label>
                </div>
            </div>
            <!-- CURRENCY END -->
            <!-- PERIOD START -->
            <div class="col-md-4">
                <h5>Делить график промежутка времени по</h5>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="timePeriod" value="month" checked
                           id="timePeriod1">
                    <label class="form-check-label" for="timePeriod1">Месяцам</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="timePeriod" value="week" id="timePeriod2">
                    <label class="form-check-label" for="timePeriod2">Неделям</label>
                </div>
            </div>
            <div class="col-md-4">
                <%@ include file="refreshButton.jsp" %>
            </div>
            <!-- PERIOD END -->
        </div>
    </div>
</div>
<!-- LINE SETTING STATIC END -->