<%@ page pageEncoding="UTF-8"%>
<!-- QUICK CALC START -->
<div class="card card-body d-none">
    <button class="btn btn-secondary" type="button" data-bs-toggle="collapse" data-bs-target="#quickCalcForm"
            aria-expanded="false" aria-controls="collapseExample">Калькулятор инфляции</button>
    <div class="collapse" id="quickCalcForm">
        <div class="card card-body">
            <h5>Уровень инфляции (РФ, офф.):</h5>
            <div class="row">
                <div class="col-md-2">
                    <label class="form-check-label" for="inflationYearOne">Год</label>
                    <select class="form-control" id="inflationYearOne" onchange="recalculateInflation('one');">
                        <option value="2022" selected>2022</option>
                        <option value="2021">2021</option>
                        <option value="2020">2020</option>
                    </select>
                    <label class="form-check-label" for="inflationSalaryOne">Зарплата, руб</label>
                    <input type="number" class="form-control" id="inflationSalaryOne"
                           value="100000" onchange="recalculateInflation('one');" step="1000"/>
                </div>
                <div class="col-md-2">
                    <label class="form-check-label" for="inflationYearTwo">Текущий год</label>
                    <select class="form-control" id="inflationYearTwo">
                        <option value="2023" selected>2023</option>
                    </select>
                    <label class="form-check-label" for="inflationSalaryTwo">Зарплата, руб</label>
                    <input type="number" class="form-control" id="inflationSalaryTwo"
                           step="1000"
                           onchange="recalculateInflation('two');"/>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- QUICK CALC END -->