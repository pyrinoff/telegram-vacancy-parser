function checkStatus() {
    setInfoText('Запустили проверку статуса...');
    fetch('/admin/parser/status', {
        method: 'GET'
        //headers: {'Content-Type': 'application/json'},
        //body: JSON.stringify(requestData)
    }).then(response => response.json())
        .then(response => {
            if (typeof response !== 'object' || response === null || response.processing === null) responseString = 'Запрос вернул неожиданный ответ: ' + response;
            else {
                responseString = 'Статус: обработка ' + (response.processing ? 'в процессе' : 'завершена/не идет');
                if (!response.processing) responseString += ', результат последней обработки: ' + response.result;
            }
            setInfoText(responseString);
        });
}

function clear() {
    setInfoText('Запустили удаление всех записей из БД');
    fetch('/admin/parser/clear', {
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
    fetch('/admin/maintenance', {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(requestData)
    }).then(response => {
        setInfoText((response.status === 200) ? 'Успешно!' : 'Произошла какая-то ошибка, код: ' + response.status);
    });
}

async function uploadFile(event) {
    event.preventDefault();

    const fileInput = document.getElementById('file');
    const formData = new FormData();
    formData.append('file', fileInput.files[0]);

    try {
        const response = await fetch('/admin/parser/upload', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();
        setInfoText(result.message);
        // document.getElementById('message').innerText = result.message;
    } catch (error) {
        setInfoText(error.message);
        // document.getElementById('message').innerText = 'Ошибка: ' + error.message;
    }
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