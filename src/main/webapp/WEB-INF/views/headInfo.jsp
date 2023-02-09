<%@ page pageEncoding="UTF-8" %>
<!-- HEADER INFO START -->
<div class="card border-none">
    <div class="card-body">
        <h3>Зарплаты в сфере QA</h3>
        <p>Дата последнего обновления базы данных: <c:out value="${DB_LAST_VACANCY_DATE}"/><br/>
            Записей в БД: <c:out value="${DB_ROWS_COUNT}"/><br/>
            Используются данные вакансий канала: <a href="https://t.me/qa_jobs" target="_blank">QA — вакансии и
                аналитика рынка
                вакансий</a><br/>
            Аналитика вакансий на HH.ru: <a href="https://clingon.pythonanywhere.com/salary"
                                            target="_blank">здесь</a>
        </p>
    </div>
</div>
<!-- HEADER INFO END -->