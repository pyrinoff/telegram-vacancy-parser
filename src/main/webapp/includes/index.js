var gChartSalaryData;
var gChartSalaryOptions = {
    title: 'График количества вакансий к зарплате',
    curveType: 'function',
    legend: {position: 'bottom'},
    //colors: ['red', 'blue', 'orange']
};
var gSalaryCountChart;

var gChartTimeData;
var gChartPeriodOptions = {
    title: 'Количество вакансий за промежуток времени',
    //vAxis: {title: 'Количество'},
    //hAxis: {title: 'Даты'},
    seriesType: 'bars',
    series: {5: {type: 'line'}},
    legend: {position: 'bottom'}
};
var gTimePeriodChart;

let gLinesCount;
let gLinesEnabledState = [];
const gDebugRequest = false;
const gDefaultRange = 'lastThreeMonths';

function initializeLinesDefaults() {
    gLinesCount = document.querySelectorAll("fieldset").length;

    //Set state to enabled
    for (i = 0; i < gLinesCount; i++) {
        gLinesEnabledState[i] = i === 0 ? true : false;
        switchEnabledAlert(i, !gLinesEnabledState[i]);
    }


    //setDate to current month
    for (i = 0; i < gLinesCount; i++) {
        setDateRange(gDefaultRange, i);
        let element = document.querySelector('input.dateRange-' + i + '-' + gDefaultRange);
        if (element != null) element.checked = true;
    }
}

function redrawChartsWithNewData(response) {
    gChartSalaryData = google.visualization.arrayToDataTable(response.salaryCountChart);
    gChartTimeData = google.visualization.arrayToDataTable(response.dateCountChart);
    google.charts.setOnLoadCallback(drawCharts);
}


function drawCharts() {
    let viewSalaryChart = new google.visualization.DataView(gChartSalaryData);
    let viewTimeChart = new google.visualization.DataView(gChartTimeData);

    for (i = 0; i < gLinesCount; i++) {
        if (gLinesEnabledState[i] === false) {
            viewSalaryChart.hideColumns([i + 1]);
            viewTimeChart.hideColumns([i + 1]);
        }
    }
    gSalaryCountChart.draw(viewSalaryChart, gChartSalaryOptions);
    gTimePeriodChart.draw(viewTimeChart, gChartPeriodOptions);
}

function switchChartLineVisibility(index, toggleToState) {
    gLinesEnabledState[index] = toggleToState;
    drawCharts();
}

function switchEnabledAlert(index, toggleToState) {
    let element = document.querySelector('fieldset[data-line-index="' + index + '"] div.enabledAlert');
    if (!toggleToState) element.classList.add('d-none');
    else element.classList.remove('d-none');
}


function addTextToInfo(someString) {
    if (typeof someString === 'object' && !Array.isArray(someString) && someString !== null) someString = JSON.stringify(someString, null, 2);
    var contentBlock = document.getElementById("info-block");
    if (contentBlock == null) return;
    var newLine = document.createElement("p");
    newLine.innerHTML = '<pre>' + someString + '</pre>';
    contentBlock.appendChild(newLine);
}

function sendRequestFunc() {
    var requestData = getDataFromForm();
    if (gDebugRequest) addTextToInfo(requestData);

    fetch('./api/dataset', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    }).then(response => response.json())
        .then(response => {
            // do something with the response
            redrawChartsWithNewData(response);
            calculateLinesInfo(response);
            if (gDebugRequest) addTextToInfo(response);
        });
}

function customNameTransformer(nameOfInput) {
    if (nameOfInput.includes("salaryCurrency")) nameOfInput = "salaryCurrency";
    return nameOfInput;
}

function isFieldMustReturnArray(nameOfInput) {
    if (nameOfInput === "salaryCurrency") return false;
    return true;
}

function extractAsArray(nodeListOrDocument, cssSelector) {
    if (nodeListOrDocument === null || typeof nodeListOrDocument !== 'object' || !('querySelectorAll' in nodeListOrDocument)) return [];
    let nodes = nodeListOrDocument.querySelectorAll(cssSelector);
    if (nodes == null) return [];
    return Array.from(nodes).map(function (cb) {
        return cb.value;
    });
}

function extractAsInt(nodeListOrDocument, cssSelector) {
    return parseInt(extractAsString(nodeListOrDocument, cssSelector));
}

function extractAsString(nodeListOrDocument, cssSelector) {
    if (nodeListOrDocument === null || typeof nodeListOrDocument !== 'object') return "";
    let nodeValue = nodeListOrDocument.querySelector(cssSelector).value;
    if (nodeValue === null) return "";
    return nodeValue;
}

/*

function extractAsString(nodeListOrDocument, cssSelector) {
    let theArray = extractAsArray(nodeListOrDocument, cssSelector);
    if(theArray === null || theArray.length === 0 ) return "";
    return theArray.at(0);
}
*/

function hasSuchElements(nodeListOrDocument, cssSelector) {
    return nodeListOrDocument.querySelectorAll(cssSelector).length > 0;
}


function getDataFromForm() {
    var formData = {};
    formData["lines"] = [];
    formData["salaryCurrency"] = extractAsString(document, 'input[name="salaryCurrency"]:checked'); //'input[name^="salaryCurrency"]:checked'
    formData["timePeriod"] = extractAsString(document, 'input[name="timePeriod"]:checked');

    var fieldsets = document.querySelectorAll("fieldset");
    var fieldsetsCount = fieldsets.length;

    for (i = 0; i < fieldsetsCount; i++) {
        //send null if line is disabled
        if (gLinesEnabledState[i] === false) {
            formData["lines"].push(null);
            continue;
        }

        //else form request
        oneFieldset = fieldsets[i];

        var fieldsetData = {
            "markers": extractAsArray(oneFieldset, 'input[name="markers"]:checked'),
            "markersOperator": extractAsString(document, 'input[name="markersOperator' + i + '"]:checked'),
            "words": extractAsArray(oneFieldset, 'input[name="words"]:checked'),
            "wordsOperator": extractAsString(document, 'input[name="wordsOperator' + i + '"]:checked'),
            "salary": {
                "fromBorder": hasSuchElements(oneFieldset, 'input[name="fromBorder"]:checked'),
                "toBorder": hasSuchElements(oneFieldset, 'input[name="toBorder"]:checked'),
                "bothBorders": hasSuchElements(oneFieldset, 'input[name="bothBorders"]:checked'),
                "allowPredicted": hasSuchElements(oneFieldset, 'input[name="allowPredicted"]:checked'),
                "salaryFrom": extractAsInt(oneFieldset, 'input[name="salaryFrom"][type="number"]'),
                "salaryTo": extractAsInt(oneFieldset, 'input[name="salaryTo"][type="number"]')
            },
            "periodFrom": extractAsString(oneFieldset, 'input[name="periodFrom"][type="date"]'),
            "periodTo": extractAsString(oneFieldset, 'input[name="periodTo"][type="date"]')
        };
        //var lineIndex = parseInt(fieldset.getAttribute("data-line-index")); //можно так, но нужно ли?
        formData["lines"].push(fieldsetData);
    }
    console.log(formData);
    return formData;
}


/* LOAD GOOGLE CHARTS ENGINE */
function onReady() {
    //page
    recalculateInflation('one');

    //chart
    google.charts.load('current', {'packages': ['corechart']});
    google.charts.setOnLoadCallback(onReady2);
}

function getDatasetIndex(node) {
    return parseInt(node.closest('fieldset[data-line-index]').dataset.lineIndex);
}

function setupToggleLines() {
    var lineSwitcherCheckboxes = document.querySelectorAll('input[type="checkbox"][name="enabled"]');
    lineSwitcherCheckboxes.forEach(function (oneCheckbox) {
        oneCheckbox.addEventListener('change', function () {
            var columnIndex = getDatasetIndex(oneCheckbox);
            var isChecked = oneCheckbox.checked;
            switchChartLineVisibility(columnIndex, isChecked);
            switchEnabledAlert(columnIndex, !isChecked);
        });
    });
}

function setupToggleCards() {
    //Line cards switch
    var buttons = document.querySelectorAll("a[data-toggle]");
    var cards = document.querySelectorAll(".linesCard");

    for (var i = 0; i < buttons.length; i++) {
        buttons[i].addEventListener("click", function () {
            var cardId = this.getAttribute("data-toggle");
            cards.forEach(card => {
                card.classList.remove('d-none');
                if (cardId !== card.id) card.classList.add('d-none');
            });
            buttons.forEach(oneButton => {
                //oneButton.classList.remove('active', 'btn-light', 'btn-dark');
                //if(oneButton === this) oneButton.classList.add('active', 'btn-dark');
                //else oneButton.classList.add('active', 'btn-light');
                oneButton.classList.remove('active');
                if (oneButton === this) oneButton.classList.add('active');
            });

        });
    }
}

/* ALL OTHERS */
function onReady2() {
    //make chart object
    gSalaryCountChart = new google.visualization.LineChart(document.getElementById('salaryChart'));
    gTimePeriodChart = new google.visualization.ComboChart(document.getElementById('periodChart'));

    initializeLinesDefaults();

    //make request and draw first chart
    sendRequestFunc();

    //add listener for submit button
    document.getElementById("submit").addEventListener("click", sendRequestFunc);

    //add listener for checkbox to switch lines off/on
    setupToggleLines();
    setupToggleCards();

}

function showCustomYearDiv(show, index) {
    let element = document.querySelector('fieldset[data-line-index="' + index + '"] div.customPeriod');
    if (element == null) {
        addTextToInfo("ERROR: cant find custom year div!");
        return;
    }
    if (show) element.style.display = 'block';
    else element.style.display = 'none';

}

function setDateRange(period, index, minusYear) {
    // showCustomYearDiv(false, index);
    let elementFrom = document.querySelector('fieldset[data-line-index="' + index + '"] div.customPeriod input[name="periodFrom"]');
    if (elementFrom == null) {
        addTextToInfo("ERROR: cant find periodFrom element!");
        return;
    }
    let elementTo = document.querySelector('fieldset[data-line-index="' + index + '"] div.customPeriod input[name="periodTo"]');
    if (elementTo == null) {
        addTextToInfo("ERROR: cant find periodTo element!");
        return;
    }

    switch (period) {
        case 'currentMonth': {
            elementFrom.value = getDateMinusMonths(0, 'start').yyyymmdd();
            elementTo.value = new Date().yyyymmdd();
            return;
        }
        case 'lastMonth': {
            elementFrom.value = getDateMinusMonths(1, 'start').yyyymmdd();
            elementTo.value = getDateMinusMonths(1, 'end').yyyymmdd();
            return;
        }
        case 'lastThreeMonths': {
            elementFrom.value = getDateMinusDays(90).yyyymmdd();
            elementTo.value = (new Date).yyyymmdd();
            return;
        }
        case 'lastSixMonths': {
            elementFrom.value = getDateMinusDays(180).yyyymmdd();
            elementTo.value = (new Date).yyyymmdd();
            return;
        }
        case 'lastYear': {
            elementFrom.value = getDateMinusDays(365).yyyymmdd();
            elementTo.value = (new Date).yyyymmdd();
            return;
        }
        case 'currentYear': {
            elementFrom.value = getDateMinusYears(0, 'start').yyyymmdd();
            elementTo.value = new Date().yyyymmdd();
            return;
        }
        case 'currentYearMinusOne': {
            elementFrom.value = getDateMinusYears(1, 'start').yyyymmdd();
            elementTo.value = getDateMinusYears(1, 'end').yyyymmdd();
            return;
        }
        case 'currentYearMinusTwo': {
            elementFrom.value = getDateMinusYears(2, 'start').yyyymmdd();
            elementTo.value = getDateMinusYears(2, 'end').yyyymmdd();
            return;
        }
        case 'all': {
            elementFrom.value = null;
            elementTo.value = null;
            return;
        }
        case 'custom': {
            showCustomYearDiv(true, index);
            return;
        }
        case 'january': {
            var startDate = getMonthStartOrEnd(1, minusYear, 'start');
            checkStartDate(startDate, index);
            elementFrom.value = startDate.yyyymmdd();
            elementTo.value = getMonthStartOrEnd(1, minusYear, 'end').yyyymmdd();
            return;
        }
        case 'february': {
            var startDate = getMonthStartOrEnd(2, minusYear, 'start');
            checkStartDate(startDate, index);
            elementFrom.value = startDate.yyyymmdd();
            elementTo.value = getMonthStartOrEnd(2, minusYear, 'end').yyyymmdd();
            return;
        }
        case 'march': {
            var startDate = getMonthStartOrEnd(3, minusYear, 'start');
            checkStartDate(startDate, index);
            elementFrom.value = startDate.yyyymmdd();
            elementTo.value = getMonthStartOrEnd(3, minusYear, 'end').yyyymmdd();
            return;
        }
        case 'april': {
            var startDate = getMonthStartOrEnd(4, minusYear, 'start');
            checkStartDate(startDate, index);
            elementFrom.value = startDate.yyyymmdd();
            elementTo.value = getMonthStartOrEnd(4, minusYear, 'end').yyyymmdd();
            return;
        }
        case 'may': {
            var startDate = getMonthStartOrEnd(5, minusYear, 'start');
            checkStartDate(startDate, index);
            elementFrom.value = startDate.yyyymmdd();
            elementTo.value = getMonthStartOrEnd(5, minusYear, 'end').yyyymmdd();
            return;
        }
        case 'june': {
            var startDate = getMonthStartOrEnd(6, minusYear, 'start');
            checkStartDate(startDate, index);
            elementFrom.value = startDate.yyyymmdd();
            elementTo.value = getMonthStartOrEnd(6, minusYear, 'end').yyyymmdd();
            return;
        }
        case 'july': {
            var startDate = getMonthStartOrEnd(7, minusYear, 'start');
            checkStartDate(startDate, index);
            elementFrom.value = startDate.yyyymmdd();
            elementTo.value = getMonthStartOrEnd(7, minusYear, 'end').yyyymmdd();
            return;
        }
        case 'august': {
            var startDate = getMonthStartOrEnd(8, minusYear, 'start');
            checkStartDate(startDate, index);
            elementTo.value = getMonthStartOrEnd(8, minusYear, 'end').yyyymmdd();
            return;
        }
        case 'september': {
            var startDate = getMonthStartOrEnd(9, minusYear, 'start');
            checkStartDate(startDate, index);
            elementFrom.value = startDate.yyyymmdd();
            elementTo.value = getMonthStartOrEnd(9, minusYear, 'end').yyyymmdd();
            return;
        }
        case 'october': {
            var startDate = getMonthStartOrEnd(10, minusYear, 'start');
            checkStartDate(startDate, index);
            elementFrom.value = startDate.yyyymmdd();
            elementTo.value = getMonthStartOrEnd(10, minusYear, 'end').yyyymmdd();
            return;
        }
        case 'november': {
            var startDate = getMonthStartOrEnd(11, minusYear, 'start');
            checkStartDate(startDate, index);
            elementFrom.value = startDate.yyyymmdd();
            elementTo.value = getMonthStartOrEnd(11, minusYear, 'end').yyyymmdd();
            return;
        }
        case 'december': {
            var startDate = getMonthStartOrEnd(12, minusYear, 'start');
            checkStartDate(startDate, index);
            elementFrom.value = startDate.yyyymmdd();
            elementTo.value = getMonthStartOrEnd(12, minusYear, 'end').yyyymmdd();
            return;
        }
    }
}

function getDateMinusMonths(months, startOrEnd) {
    var date = new Date();
    date.setMonth(date.getMonth() - months);
    if (startOrEnd === "start") {
        date.setDate(1);
    } else if (startOrEnd === "end") {
        date.setMonth(date.getMonth() + 1);
        date.setDate(0);
    }
    return date;
}

function getDateMinusYears(years, startOrEnd) {
    var date = new Date();
    date.setFullYear(date.getFullYear() - years);
    if (startOrEnd === "start") {
        date.setMonth(0);
        date.setDate(1);
    } else if (startOrEnd === "end") {
        date.setMonth(11);
        date.setDate(31);
    }
    return date;
}

function getDateMinusDays(days) {
    var date = new Date();
    date.setDate(date.getDate() - days);
    return date;
}


function getMonthStartOrEnd(month, yearMinus, startOrEnd) {
    var date = new Date();
    date.setFullYear(date.getFullYear() - yearMinus);
    ;
    if (startOrEnd === "start") {
        date.setDate(1);
        date.setMonth(month - 1);
    } else if (startOrEnd === "end") {
        date.setMonth(month);
        date.setDate(0);
    }
    return date;
}

Date.prototype.yyyymmdd = function () {
    var mm = this.getMonth() + 1; // getMonth() is zero-based
    var dd = this.getDate();

    return [this.getFullYear(),
        (mm > 9 ? '' : '0') + mm,
        (dd > 9 ? '' : '0') + dd
    ].join('-');
};

function checkStartDate(date, index) {
    let element = document.querySelector('fieldset[data-line-index="' + index + '"] div.dateAlert');
    if (date <= (new Date())) {
        element.textContent = '';
        element.classList.add('d-none');
    } else {
        element.textContent = 'Стартовая дата превышает текущую!'
        element.classList.remove('d-none');
    }
}

function calculateLinesInfo(response) {
    var dataset = response.salaryCountChart;
    var currency = response.request.salaryCurrency;

    //Get initial data
    var linesNames = dataset[0];
    var linesCount = dataset[0].length - 1; //0 for salary value
    var rowCount = dataset.length; //0 is header
    var content = '<ul>';
    var roundMultiplier = currency === 'RUB' ? 1000 : 100;

    //Foreach and collect data by line
    for (i = 1; i <= linesCount; i++) {
        var maxSalary = null;
        var minSalary = null;
        var sum = 0;
        var vacancyCount = 0;
        var dateFrom = null;
        var dateTo = null;
        var midSalary = 0;

        for (j = 1; j < rowCount; j++) {
            var countOfVacanciesInRow = dataset[j][i];
            if (countOfVacanciesInRow === 0) continue;
            vacancyCount += countOfVacanciesInRow;
            var vacancySalary = dataset[j][0];
            sum += vacancySalary * countOfVacanciesInRow;
            maxSalary = vacancySalary > maxSalary ? vacancySalary : maxSalary;
            minSalary = vacancySalary < minSalary || minSalary == null ? vacancySalary : minSalary;
        }
        if (sum === 0) continue; //null lines
        midSalary = Math.round(sum / vacancyCount / roundMultiplier) * roundMultiplier;

        //additional info
        dateFrom = response.request.lines[i - 1].periodFrom;
        dateTo = response.request.lines[i - 1].periodTo;
        const dateDiff = monthsAndDaysBetweenRemain(dateFrom, dateTo);
        content +=
            '<li>Линия '
            + i
            + ' (' + dateFrom + ' - ' + dateTo
            + (dateDiff.months > 0 ?  ', '+ dateDiff.months + ' мес.' : '')
            + (dateDiff.days > 0 ?  ', '+ dateDiff.days + ' дн.' : '')
            +')'
            + ':  вакансий всего - '
            + vacancyCount
            + ' шт, средняя ЗП - '
            + midSalary
            + ', минимальная - '
            + minSalary
            + ', максимальная - '
            + maxSalary
            + '</li>';
    }
    content += '</ul>'

    //Show line
    var element = document.querySelector('#linesInfo');
    element.innerHTML = content;

}

function daysAndMonthsBetweenFull(date1, date2) {
    const d1 = new Date(date1);
    const d2 = new Date(date2);
    const diffTime = Math.abs(d2 - d1);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    const months = (d2.getFullYear() - d1.getFullYear()) * 12 + (d2.getMonth() - d1.getMonth());

    return {
        days: diffDays,
        months: months
    };
}

function monthsAndDaysBetweenRemain(date1, date2) {
    const d1 = new Date(date1);
    const d2 = new Date(date2);
    const diffTime = Math.abs(d2 - d1);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    const months = Math.floor((d2.getFullYear() - d1.getFullYear()) * 12 + (d2.getMonth() - d1.getMonth()));
    const remainingDays = diffDays - months * 30;

    return {
        months: months,
        days: remainingDays
    };
}

var inflationRates = {
    2022: 11.92,
    2021: 4.91,
    2020: 8.39
}

function recalculateInflation(border) {
    const fromBorder = border === 'one';

    let yearOne = document.getElementById('inflationYearOne').value;
    let yearTwo = document.getElementById('inflationYearTwo').value;

    let inflationFieldOne = document.getElementById('inflationSalaryOne');
    let inflationFieldTwo = document.getElementById('inflationSalaryTwo');

    let inflationValueOne = inflationFieldOne.value;
    let inflationValueTwo = inflationFieldTwo.value;

    let coefficient = 1;

    if(yearOne <= 2020) coefficient = coefficient * (1 + inflationRates["2020"] / 100);
    if(yearOne <= 2021) coefficient = coefficient * (1 + inflationRates["2021"] / 100);
    if(yearOne <= 2022) coefficient = coefficient * (1 + inflationRates["2022"] / 100);

    if(fromBorder) {
        inflationFieldTwo.value = Math.round(inflationValueOne * coefficient);
        console.log("Will set inflationFieldTwo to: ", inflationValueTwo);
    }
    else {
        inflationFieldOne.value = Math.round(inflationValueTwo / (1+coefficient/100));
        console.log("Will set inflationFieldOne to: ", inflationFieldOne);
    }


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


//$(document).ready(onReady);
docReady(onReady);