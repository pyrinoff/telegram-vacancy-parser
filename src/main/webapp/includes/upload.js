function checkStatus() {
    var contentBlock = document.getElementById("status-info");
    contentBlock.textContent = 'Запустили проверку статуса...';

    fetch('./api/parse/status', {
        method: 'GET',
        //headers: {'Content-Type': 'application/json'},
        //body: JSON.stringify(requestData)
    }).then(response => response.json())
        .then(response => {
            if (contentBlock == null) return;
            if (typeof response !== 'object' || response === null || response.processing === null) responseString = 'Запрос вернул неожиданный ответ: ' + response;
            else {
                responseString = 'Статус: обработка ' + (response.processing ? 'в процессе' : 'завершена/не идет');
                if (!response.processing) responseString += ', результат обработки: ' + (response.result ? 'успешен' : response.result === false ? 'не успешен' : 'не производился');
            }
            contentBlock.textContent = responseString;
        });
}

function clearAll() {
    var contentBlock = document.getElementById("status-info");
    contentBlock.textContent = 'Запустили удаление всех записей из БД';
    fetch('./api/parse/clearAll', {
        method: 'GET',
        //headers: {'Content-Type': 'application/json'},
        //body: JSON.stringify(requestData)
    })
        .then(response => {
            if (contentBlock == null) return;
            contentBlock.textContent = (response.status === 200) ? 'Данные из БД удалены!' : 'Произошла какая-то ошибка, код: ' + response.status;
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