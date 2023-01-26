var globalChartData;
var gChartOptions = {
    title: 'Company Performance',
    curveType: 'function',
    legend: {position: 'bottom'},
    //colors: ['red', 'blue', 'orange']
};
var gChartObject;
let gLinesCount;
let gLinesEnabledState = [];
const gDebugRequest = true;

function initializeLinesDefaults() {
    gLinesCount =  document.querySelectorAll("fieldset").length;

    //Set state to enabled
    for(i=0;i<gLinesCount;i++)  gLinesEnabledState[i] = false;
    gLinesEnabledState[0] = true;

    //setDate to current month
    for(i = 0; i< gLinesCount; i++)   setDateRange('currentMonth', i);
}

function redrawWithNewData(arrayData) {
    globalChartData = google.visualization.arrayToDataTable(arrayData);
    google.charts.setOnLoadCallback(drawChart);
}


function drawChart() {
    view = new google.visualization.DataView(globalChartData);

    for(i=0;i<gLinesCount;i++) {
        if (gLinesEnabledState[i] === false) view.hideColumns([i+1]);
    }
    //if (optionalView == null) gChartObject.draw(globalChartData, gChartOptions);
    gChartObject.draw(view, gChartOptions);
}

function switchChartLineVisibility(index, toggleToState) {
    gLinesEnabledState[index] = toggleToState;
    drawChart();
}


function addTextToInfo(someString) {
    if (typeof someString === 'object' && !Array.isArray(someString) && someString !== null) someString = JSON.stringify(someString, null, 2);
    var contentBlock = document.getElementById("info-block");
    if(contentBlock == null) return;
    var newLine = document.createElement("p");
    newLine.innerHTML = '<pre>' + someString + '</pre>';
    contentBlock.appendChild(newLine);
}

function sendRequestFunc() {
    var requestData = getDataFromForm();
    if(gDebugRequest) addTextToInfo(requestData);

    fetch('./api/dataset', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    }).then(response => response.json())
        .then(response => {
            // do something with the response
            redrawWithNewData(response.dataset);
            if(gDebugRequest) addTextToInfo(response);
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

    var fieldsets = document.querySelectorAll("fieldset");
    var fieldsetsCount = fieldsets.length;

    for(i=0;i<fieldsetsCount;i++) {
        //send null if line is disabled
        if (gLinesEnabledState[i] === false) {
            formData["lines"].push(null);
            continue;
        }

        //else form request
        oneFieldset = fieldsets[i];

        var fieldsetData = {
            "markers": extractAsArray(oneFieldset, 'input[name="markers"]:checked'),
            "words": extractAsArray(oneFieldset, 'input[name="words"]:checked'),
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
    google.charts.load('current', {'packages': ['corechart']});
    google.charts.setOnLoadCallback(onReady2);
}

function setupToggleLines() {
    var lineSwitcherCheckboxes = document.querySelectorAll('input[type="checkbox"][name="enabled"]');
    lineSwitcherCheckboxes.forEach(function (oneCheckbox) {
        oneCheckbox.addEventListener('change', function () {
            var columnIndex = parseInt(oneCheckbox.closest('fieldset[data-line-index]').dataset.lineIndex);
            var isChecked = oneCheckbox.checked;
            switchChartLineVisibility(columnIndex, isChecked);
        });
    });
}

function setupToggleCards() {
    //Line cards switch
    var buttons = document.querySelectorAll("button[data-toggle]");
    var cards = document.querySelectorAll(".linesCard");

    for (var i = 0; i < buttons.length; i++) {
        buttons[i].addEventListener("click", function () {
            var cardId = this.getAttribute("data-toggle");
            cards.forEach(card => {
                card.classList.remove('d-none');
                if(cardId !== card.id) card.classList.add('d-none');
            });
            buttons.forEach(oneButton => {
                oneButton.classList.remove('active', 'btn-light', 'btn-dark');
                if(oneButton === this) oneButton.classList.add('active', 'btn-dark');
                else oneButton.classList.add('active', 'btn-light');
            });

        });
    }
}

/* ALL OTHERS */
function onReady2() {
    //make chart object
    gChartObject = new google.visualization.LineChart(document.getElementById('curve_chart'));

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
    if(element == null) {
        addTextToInfo("ERROR: cant find custom year div!");
        return;
    }
    if (show) element.style.display = 'block';
    else element.style.display = 'none';

}

function setDateRange(period, index) {
    showCustomYearDiv(false, index);
    let elementFrom = document.querySelector('fieldset[data-line-index="' + index + '"] div.customPeriod input[name="periodFrom"]');
    if(elementFrom == null) {
        addTextToInfo("ERROR: cant find periodFrom element!");
        return;
    }
    let elementTo = document.querySelector('fieldset[data-line-index="' + index + '"] div.customPeriod input[name="periodTo"]');
    if(elementTo == null) {
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
            elementFrom.value = getDateMinusMonths(3, 'start').yyyymmdd();
            elementTo.value = getDateMinusMonths(1, 'end').yyyymmdd();
            return;
        }
        case 'lastSixMonths': {
            elementFrom.value = getDateMinusMonths(6, 'start').yyyymmdd();
            elementTo.value = getDateMinusMonths(1, 'end').yyyymmdd();
            return;
        }
        case 'lastYear': {
            elementFrom.value = getDateMinusYears(1, 'start').yyyymmdd();
            elementTo.value = getDateMinusMonths(1, 'end').yyyymmdd();
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

Date.prototype.yyyymmdd = function() {
    var mm = this.getMonth() + 1; // getMonth() is zero-based
    var dd = this.getDate();

    return [this.getFullYear(),
        (mm>9 ? '' : '0') + mm,
        (dd>9 ? '' : '0') + dd
    ].join('-');
};

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