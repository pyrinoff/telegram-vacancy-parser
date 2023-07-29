<%@ page pageEncoding="UTF-8" %>
<!-- HEADER INFO START -->
<div class="card border-none">
    <div class="card-body">
        <h3>Зарплаты в сфере QA (по вакансиям)</h3>
        <p>
            Используются данные вакансий канала: <a href="https://t.me/qa_jobs" target="_blank">QA — вакансии и аналитика рынка вакансий</a><br/>
            Зарплаты в сфере QA по вакансиям HH.ru (clingon): <a href="https://clingon.pythonanywhere.com/salary" target="_blank">здесь</a><br/>
            Зарплаты в ИТ по опросам работников (getmatch.ru): <a href="https://getmatch.ru/salaries/qa_manual" target="_blank">здесь</a><br/>
            Записей в БД: <c:out value="${DB_ROWS_COUNT}"/><br/>
            Дата последнего обновления базы данных: <c:out value="${DB_LAST_VACANCY_DATE}"/><br/>
        </p>
    </div>
</div>
<!-- HEADER INFO END -->