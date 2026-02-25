// Добавление объекта
function addObject() {
    const container = document.getElementById('objectsContainer');
    const objectCount = container.children.length + 1;

    const newObject = document.createElement('div');
    newObject.className = 'object-item';
    newObject.style = 'margin-bottom: 20px; border: 1px solid #ddd; padding: 10px;';
    newObject.innerHTML = getObjectTemplate(objectCount);
    container.appendChild(newObject);
}

// Шаблон нового объекта
function getObjectTemplate(number) {
    const existingSelect = document.querySelector('select[name="object_asf_id[]"]');
    let asfOptions = '';

    if (existingSelect) {
        for (let i = 1; i < existingSelect.options.length - 1; i++) {
            const opt = existingSelect.options[i];
            asfOptions += `<option value="${opt.value}">${opt.text}</option>`;
        }
    } else if (window.asfOptionsList && window.asfOptionsList.length > 0) {
        asfOptions = window.asfOptionsList.map(asf =>
            `<option value="${asf.id}">${asf.name}</option>`
        ).join('');
    }

    return `
        <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
            <span style="font-weight: bold;">Объект #${number}</span>
        </div>
        
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th>Полное наименование</th>
                <th>Краткое наименование</th>
                <th>ID города</th>
                <th>Класс опасности</th>
            </tr>
            <tr>
                <td><input type="text" name="object_full_name[]" style="width: 100%;"></td>
                <td><input type="text" name="object_short_name[]" style="width: 100%;"></td>
                <td><input type="number" name="object_city_id[]" style="width: 80px;"></td>
                <td><input type="number" name="hazard_class[]" style="width: 70px;"></td>
            </tr>
        </table>
        
        <table class="objects-data-table" style="margin-bottom: 10px; width: 100%; table-layout: fixed;">
            <tr>
                <th>АСФ:</th>
                <th>Подписант:</th>
            </tr>
            <tr>
                <td>
                    <select name="object_asf_id[]" class="asf-select" style="width: 100%;">
                        <option value="">Выберите АСФ</option>
                        ${asfOptions}
                        <option value="new_asf">➕ Добавить новое АСФ</option>
                    </select>
                </td>
                <td>
                    <select name="object_signer_id[]" class="signer-select" style="width: 100%;" disabled>
                        <option value="">Сначала выберите АСФ</option>
                    </select>
                </td>
            </tr>
        </table>
        
        <input type="hidden" class="signer-id-hidden" name="object_signer_id_hidden[]" value="">
        
        <!-- Детали объекта -->
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th>Ближайшая ПСЧ</th>
                <th>Департамент ГОЧС</th>
                <th>Наличие КЧС</th>
            </tr>
            <tr>
                <td><input type="text" name="nearest_fire_station[]" style="width: 100%;"></td>
                <td><input type="text" name="department_gochs[]" style="width: 100%;"></td>
                <td>
                    <select name="emergency_commission[]">
                        <option value=""></option>
                        <option value="создана">Создана</option>
                        <option value="не создана">Не создана</option>
                    </select>
                </td>
            </tr>
        </table>
        
        <!-- Адрес объекта -->
        <div style="display: grid; grid-template-columns: 1fr 1fr 2fr; gap: 10px; margin-bottom: 10px;">
            <div>
                <label style="font-size: 11px;">Субъект РФ</label>
                <input type="text" name="object_constituent_entity[]" style="width: 100%;">
            </div>
            <div>
                <label style="font-size: 11px;">Район</label>
                <input type="text" name="object_area[]" style="width: 100%;">
            </div>
            <div>
                <label style="font-size: 11px;">Координаты</label>
                <input type="text" name="object_coordinates[]" style="width: 100%;">
            </div>
        </div>
        
        <!-- Страховка и Приказ -->
        <div style="display: grid; grid-template-columns: 1fr 1fr 1fr 1fr; gap: 10px; margin-bottom: 10px;">
            <div>
                <label style="font-size: 11px;">Номер полиса</label>
                <input type="text" name="insurance_number[]" style="width: 100%;">
            </div>
            <div>
                <label style="font-size: 11px;">Действителен до</label>
                <input type="date" name="insurance_valid_until[]" style="width: 100%;">
            </div>
            <div>
                <label style="font-size: 11px;">Номер приказа</label>
                <input type="number" name="balance_number[]" style="width: 100%;">
            </div>
            <div>
                <label style="font-size: 11px;">Дата приказа</label>
                <input type="date" name="balance_date[]" style="width: 100%;">
            </div>
        </div>
        
        <!-- Тип объекта -->
        <div style="margin-bottom: 10px;">
            <label style="font-size: 11px;">Тип объекта (определение)</label>
            <textarea name="object_type_definition[]" rows="2" style="width: 100%;"></textarea>
        </div>
        
        <!-- КЧС -->
        <div class="object-collapse-block">
            <div class="object-collapse-header" onclick="toggleCollapse(this)">
                <span>👥 Состав КЧС для объекта</span>
                <span>▼</span>
            </div>
            <div class="object-collapse-content">
                <button type="button" class="add-row" onclick="addKchs(this)">Добавить члена КЧС</button>
                <table class="objects-data-table" style="margin-top: 10px;">
                    <thead>
                        <tr>
                            <th>Должность в КЧС</th>
                            <th>ФИО</th>
                            <th>Телефон</th>
                            <th>Домашний адрес</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody class="kchs-body">
                        <tr>
                            <td><input type="text" name="kchs_position[]" style="width: 100%;"></td>
                            <td><input type="text" name="kchs_name[]" style="width: 100%;"></td>
                            <td><input type="text" name="kchs_phone[]" style="width: 100%;"></td>
                            <td><input type="text" name="kchs_address[]" style="width: 100%;"></td>
                            <td class="delete-row" onclick="deleteRow(this)">✖</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        
        <!-- Оборудование -->
        <div class="object-collapse-block">
            <div class="object-collapse-header" onclick="toggleCollapse(this)">
                <span>Оборудование</span>
                <span>▼</span>
            </div>
            <div class="object-collapse-content">
                <button type="button" class="add-row" onclick="addEquipment(this)">Добавить оборудование</button>
                <table class="objects-data-table" style="margin-top: 10px;">
                    <thead>
                        <tr>
                            <th>Наименование</th>
                            <th>Характеристики</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody class="equipment-body">
                        <tr>
                            <td><input type="text" name="techno_name[]" style="width: 100%;"></td>
                            <td><textarea name="techno_characteristics[]" rows="2" style="width: 100%;"></textarea></td>
                            <td class="delete-row" onclick="deleteRow(this)">✖</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    `;
}

// Добавление члена КЧС
function addKchs(button) {
    const tbody = button.closest('.object-collapse-content').querySelector('.kchs-body');
    const newRow = document.createElement('tr');
    newRow.innerHTML = `
        <td><input type="text" name="kchs_position[]" placeholder="Должность в КЧС" style="width: 100%;"></td>
        <td><input type="text" name="kchs_name[]" placeholder="ФИО с должностью" style="width: 100%;"></td>
        <td><input type="text" name="kchs_phone[]" placeholder="Телефон" style="width: 100%;"></td>
        <td><input type="text" name="kchs_address[]" placeholder="Домашний адрес" style="width: 100%;"></td>
        <td class="delete-row" onclick="deleteRow(this)">✖</td>
    `;
    tbody.appendChild(newRow);
}

// Добавление оборудования
function addEquipment(button) {
    const tbody = button.closest('.object-collapse-content').querySelector('.equipment-body');
    const newRow = document.createElement('tr');
    newRow.innerHTML = `
        <td><input type="text" name="techno_name[]" placeholder="Наименование" style="width: 100%;"></td>
        <td><textarea name="techno_characteristics[]" rows="2" placeholder="Характеристики" style="width: 100%;"></textarea></td>
        <td class="delete-row" onclick="deleteRow(this)">✖</td>
    `;
    tbody.appendChild(newRow);
}

// Загрузка подписантов
function loadSigners(asfId, signerSelect, hiddenField, allowAddOption = true) {
    if (!asfId) {
        signerSelect.innerHTML = '<option value="">Сначала выберите АСФ</option>';
        signerSelect.disabled = true;
        return;
    }

    fetch('getAsfSigners?asfId=' + asfId)
        .then(response => response.json())
        .then(signers => {
            let options = '<option value="">Выберите подписанта</option>';
            let selectedValue = '';

            signers.forEach(signer => {
                options += `<option value="${signer.id}">
                    ${signer.name} (${signer.position})
                </option>`;

                if (hiddenField && hiddenField.value && signer.id === Number(hiddenField.value)) {
                    selectedValue = signer.id;
                }
            });

            if (allowAddOption) {
                options += `<option value="add_new_signer">
                    Добавить нового подписанта
                </option>`;
            }

            signerSelect.innerHTML = options;

            // !!! ИЗМЕНЕНИЕ ЗДЕСЬ !!!
            // Разблокируем только если НЕ режим просмотра
            signerSelect.disabled = (window.currentMode === 'view');

            if (selectedValue) {
                signerSelect.value = selectedValue;
            }
        })
        .catch(error => {
            console.error('Ошибка загрузки подписантов:', error);
            signerSelect.innerHTML = '<option value="">Ошибка загрузки</option>';
            signerSelect.disabled = true;
        });
}

function openAsfModal(asfId, objectIndex) {
    const modal = document.getElementById('asfModal');
    const content = document.getElementById('asfModalContent');

    // Сохраняем индекс объекта для обновления после сохранения
    modal.setAttribute('data-object-index', objectIndex);

    // Загружаем форму АСФ
    const url = asfId ? 'asf?mode=edit&asfId=' + asfId : 'asf?mode=create';

    fetch(url, {
        headers: {'X-Requested-With': 'XMLHttpRequest'}
    })
        .then(response => response.text())
        .then(html => {
            content.innerHTML = html;
            modal.style.display = 'block';
        });
}

function closeAsfModal() {
    document.getElementById('asfModal').style.display = 'none';
}

// Инициализация для объектов
// Вспомогательные функции
function getObjectElements(element) {
    const objectItem = element.closest('.object-item');
    return {
        objectItem,
        signerSelect: objectItem.querySelector('.signer-select'),
        hiddenField: objectItem.querySelector('.signer-id-hidden'),
        asfSelect: objectItem.querySelector('.asf-select')
    };
}

function handleNewAsf() {
    const urlParams = new URLSearchParams(window.location.search);
    const mode = urlParams.get('mode') || 'create';
    const docId = urlParams.get('docId') || '';
    window.location.href = "addNewAsf?mode=" + mode + "&docId=" + docId;
}

function resetSignerSelect(signerSelect) {
    signerSelect.innerHTML = '<option value="">Сначала выберите АСФ</option>';
    signerSelect.disabled = true;
}

// Инициализация
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.asf-select').forEach(select => {
        const asfId = select.value;
        if (!asfId || asfId === 'new_asf') return;

        const elements = getObjectElements(select);
        loadSigners(asfId, elements.signerSelect, elements.hiddenField, true);
    });
});

// Обработчик изменений
document.addEventListener('change', function(e) {
    // Обработка выбора АСФ
    if (e.target?.classList.contains('asf-select')) {
        const asfId = e.target.value;
        const elements = getObjectElements(e.target);

        if (!asfId) {
            resetSignerSelect(elements.signerSelect);
            return;
        }

        if (asfId === 'new_asf') {
            handleNewAsf();
            return;
        }

        loadSigners(asfId, elements.signerSelect, elements.hiddenField, true);
    }

    // Обработка выбора подписанта
    if (e.target?.classList.contains('signer-select') && e.target.value === 'add_new_signer') {
        const elements = getObjectElements(e.target);
        handleNewAsf(elements.asfSelect.value);
    }
});