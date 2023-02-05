function checkStatus() {
    setInfoText('Запустили проверку статуса...');
    fetch('./api/parse/status', {
        method: 'GET',
        //headers: {'Content-Type': 'application/json'},
        //body: JSON.stringify(requestData)
    }).then(response => response.json())
        .then(response => {
            if (typeof response !== 'object' || response === null || response.processing === null) responseString = 'Запрос вернул неожиданный ответ: ' + response;
            else {
                responseString = 'Статус: обработка ' + (response.processing ? 'в процессе' : 'завершена/не идет');
                if (!response.processing) responseString += ', результат обработки: ' + (response.result ? 'успешен' : response.result === false ? 'не успешен' : 'не производился');
            }
            setInfoText(responseString);
        });
}

function clearAll() {
    setInfoText('Запустили удаление всех записей из БД');
    fetch('./api/parse/clearAll', {
        method: 'GET',
        //headers: {'Content-Type': 'application/json'},
        //body: JSON.stringify(requestData)
    })
        .then(response => {
            setInfoText((response.status === 200) ? 'Данные из БД удалены!' : 'Произошла какая-то ошибка, код: ' + response.status);
        });
}

function setInfoText(someText) {
    var contentBlock = document.getElementById("status-info");
    if (contentBlock === null) return;
    contentBlock.textContent = someText;
}

function switchMaintenance(boolNewStatus) {
    if (boolNewStatus !== false && boolNewStatus !== true) return;
    setInfoText('Пытаемся изменить режим "Ведутся работы" на ' + boolNewStatus);
    var requestData = {
        enabled: boolNewStatus
    };
    fetch('./api/maintenance', {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(requestData)
    }).then(response => {
        setInfoText((response.status === 200) ? 'Успешно!' : 'Произошла какая-то ошибка, код: ' + response.status);
    });
}

function onReady() {
    checkStatus();
}


function docReady(fn) {
    // see if DOM is already available
    if (document.readyState === "complete" || document.readyState === "interactive") {
        // call on next available tick
        setTimeout(fn, 1);
    } else {
        document.addEventListener("DOMContentLoaded", fn);
    }
}


docReady(onReady)