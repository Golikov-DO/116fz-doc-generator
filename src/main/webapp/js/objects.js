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

    let substanceOptions = '';
    if (window.substanceOptionsList) {
        substanceOptions = window.substanceOptionsList.map(sub =>
            `<option value="${sub.id}">${sub.name}</option>`
        ).join('');
    }

    let presenceOptions = '';
    if (window.presenceAreaList) {
        presenceOptions = window.presenceAreaList.map(area =>
            `<option value="${area.id}">${area.name}</option>`
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
            </tr>
            <tr>
                <td><input type="text" name="object_full_name[]" style="width: 100%;"></td>
                <td><input type="text" name="object_short_name[]" style="width: 100%;"></td>
            </tr>
        </table>
        
        <!-- Класс опасности, опасное вещество, количество -->
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th style="width:15%;">Класс опасности</th>
                <th style="width:50%;">Опасное вещество</th>
                <th style="width:35%;">Количество опасного вещества</th>
            </tr>
            <tr>
                <td>
                    <select name="hazard_class[]" style="width:100%;">
                        <option value="1">I класс</option>
                        <option value="2">II класс</option>
                        <option value="3">III класс</option>
                        <option value="4">IV класс</option>
                    </select>
                </td>
                <td>
                    <select name="hazardous_substance_id[]" style="width:100%;">
                        <option value="">— выберите вещество —</option>
                        ${substanceOptions}
                    </select>
                </td>
                <td>
                    <input type="text" name="amount_of_hazardous_substance[]" style="width:100%;">
                </td>
            </tr>
        </table>
        
        <!-- АСФ и подписант -->
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th style="width:50%;">Аварийно-спасательное формирование</th>
                <th style="width:50%;">Подписант от АСФ</th>
            </tr>
            <tr>
                <td>
                    <div style="display: flex; gap: 5px; align-items: center;">
                        <select name="object_asf_id[]" class="asf-select" style="width:100%;">
                            <option value="">Выберите АСФ</option>
                            ${asfOptions}
                            <option value="new_asf">Добавить новое АСФ</option>
                        </select>
                    </div>
                </td>
                <td>
                    <select name="object_signer_id[]" class="signer-select" style="width:100%;" disabled>
                        <option value="">Сначала выберите АСФ</option>
                    </select>
                    <input type="hidden" class="signer-id-hidden" name="object_signer_id_hidden[]" value="">
                </td>
            </tr>
        </table>
        
        <!-- Ближайшая ПСЧ, Департамент ГОЧС, Наличие КЧС -->
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th>Ближайшая ПСЧ</th>
                <th>Департамент ГОЧС</th>
                <th>Наличие КЧС</th>
            </tr>
            <tr>
                <td><input type="text" name="nearest_fire_station[]" style="width:100%;"></td>
                <td><input type="text" name="department_gochs[]" style="width:100%;"></td>
                <td>
                    <select name="emergency_commission[]" onchange="toggleKchsVisibility(this, ${number})">
                        <option value="true">Создана</option>
                        <option value="false">Не создана</option>
                    </select>
                </td>
            </tr>
        </table>
        
        <!-- Адрес: индекс, субъект, район, город -->
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th style="width:10%;">Индекс</th>
                <th style="width:30%;">Субъект РФ</th>
                <th style="width:25%;">Район</th>
                <th style="width:35%;">Город</th>
            </tr>
            <tr>
                <td><input type="text" name="object_index[]" style="width:100%;" placeholder="356000"></td>
                <td><input type="text" name="object_constituent_entity[]" style="width:100%;"></td>
                <td><input type="text" name="object_area[]" style="width:100%;"></td>
                <td>
                    <input type="text" name="object_city[]" style="width:100%;" placeholder="г. Ставрополь">
                </td>
            </tr>
        </table>
        
        <!-- Улица, дом, координаты, район ОПО -->
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th style="width:25%;">Улица</th>
                <th style="width:15%;">Дом</th>
                <th style="width:30%;">Координаты</th>
                <th style="width:30%;">Район расположения ОПО</th>
            </tr>
            <tr>
                <td><input type="text" name="object_street[]" style="width:100%;"></td>
                <td><input type="text" name="object_house[]" style="width:100%;"></td>
                <td><input type="text" name="object_coordinates[]" style="width:100%;"></td>
                <td>
                    <select name="presence_area_id[]" style="width:100%;">
                        <option value="">— выберите район присутствия —</option>
                        ${presenceOptions}
                    </select>
                </td>
            </tr>
        </table>
        
        <!-- Страховка и приказ -->
        <table class="objects-data-table" style="margin-bottom: 10px;">
            <tr>
                <th>Номер полиса</th>
                <th>Действителен до</th>
                <th>Номер приказа</th>
                <th>Дата приказа</th>
            </tr>
            <tr>
                <td><input type="text" name="insurance_number[]" style="width:100%;"></td>
                <td><input type="date" name="insurance_valid_until[]" style="width:100%;"></td>
                <td><input type="number" name="balance_number[]" style="width:100%;"></td>
                <td><input type="date" name="balance_date[]" style="width:100%;"></td>
            </tr>
        </table>
        
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
                    <tbody class="kchs-body" data-object-index="${number}">
                        <tr>
                            <td>
                                <input type="hidden" name="kchs_object_index[]" value="${number}">
                                <input type="text" name="kchs_position[]" style="width: 100%;">
                            </td>
                            <td><input type="text" name="kchs_name[]" style="width: 100%;"></td>
                            <td><input type="text" name="kchs_phone[]" style="width: 100%;"></td>
                            <td><input type="text" name="kchs_address[]" style="width: 100%;"></td>
                            <td class="delete-row" onclick="deleteRow(this)">✖</td>
                        </tr>
                    </tbody>
                </table>
                <button type="button" class="add-row" onclick="addKchs(this)">Добавить члена КЧС</button>
            </div>
        </div>
        
        <!-- Оборудование -->
        <div class="object-collapse-block">
            <div class="object-collapse-header" onclick="toggleCollapse(this)">
                <span>Оборудование</span>
                <span>▼</span>
            </div>
            <div class="object-collapse-content">
                <table class="objects-data-table" style="margin-top: 10px;">
                    <thead>
                        <tr>
                            <th>№</th>
                            <th>Наименование</th>
                            <th>Характеристики</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody class="equipment-body" data-object-index="${number}">
                        <tr>
                            <td>
                                <input type="number" name="techno_number[]" style="width:60px;">
                            </td>
                            <td>
                                <input type="hidden" name="techno_object_index[]" value="${number}">
                                <input type="text" name="techno_name[]" style="width: 100%;">
                            </td>
                            <td><textarea name="techno_characteristics[]" rows="2" style="width: 100%;"></textarea></td>
                            <td class="delete-row" onclick="deleteRow(this)">✖</td>
                        </tr>
                    </tbody>
                </table>
                <button type="button" class="add-row" onclick="addEquipment(this)">Добавить оборудование</button>
            </div>
        </div>
    `;
}

// Добавление члена КЧС
function addKchs(button) {
    const tbody = button.closest('.object-collapse-content').querySelector('.kchs-body');
    const number = tbody.dataset.objectIndex;
    const newRow = document.createElement('tr');
    newRow.innerHTML = `
        <td>
            <input type="hidden" name="kchs_object_index[]" value="${number}">
            <input type="hidden" name="kchs_id[]" value="">
            <input type="text" name="kchs_position[]" placeholder="Должность в КЧС" style="width: 100%;"></td>
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
    const number = tbody.dataset.objectIndex;
    const newRow = document.createElement('tr');
    newRow.innerHTML = `
        <td>
            <input type="hidden" name="techno_object_index[]" value="${number}">
            <input type="hidden" name="techno_id[]" value="">
            <input type="text" name="techno_name[]" placeholder="Наименование" style="width: 100%;"></td>
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

// objects.js - обновите существующую функцию openAsfModal
function openAsfModal(asfId, objectItem, addSignerMode = false) {
    console.log('openAsfModal called with asfId:', asfId);

    const modal = document.getElementById('asfModal');
    const content = document.getElementById('asfModalContent');
    const title = document.getElementById('asfModalTitle');

    // Сохраняем ссылку на объект для обратного вызова
    modal.setAttribute('data-object-item', objectItem ? objectItem.id || '' : '');
    modal.setAttribute('data-add-signer-mode', addSignerMode ? 'true' : 'false');
    modal.setAttribute('data-view-mode', 'false'); // Это режим редактирования

    // Меняем заголовок в зависимости от режима
    if (addSignerMode) {
        title.textContent = 'Добавление подписанта';
    } else {
        title.textContent = asfId ? 'Редактирование АСФ' : 'Добавление АСФ';
    }

    const url = asfId ? 'asf?mode=edit&asfId=' + asfId : 'asf?mode=create';

    fetch(url, {
        headers: {'X-Requested-With': 'XMLHttpRequest'}
    })
        .then(response => response.text())
        .then(html => {
            content.innerHTML = html;

            // Если это режим добавления подписанта, скрываем ненужные блоки
            if (addSignerMode) {
                // Скрываем все блоки кроме подписантов
                const blocks = content.querySelectorAll('.asf-collapse-block');
                blocks.forEach(block => {
                    const header = block.querySelector('.asf-collapse-header span');
                    if (header && !header.textContent.includes('Подписанты')) {
                        block.style.display = 'none';
                    }
                });
            }

            modal.style.display = 'block';
        })
        .catch(error => {
            console.error('Ошибка загрузки АСФ:', error);
            alert('Ошибка при загрузке данных АСФ');
        });
}

// objects.js - добавьте эту функцию
function viewAsf(asfId, objectItem) {
    console.log('viewAsf called with asfId:', asfId);

    const modal = document.getElementById('asfModal');
    const content = document.getElementById('asfModalContent');
    const title = document.getElementById('asfModalTitle');

    // Сохраняем ссылку на объект для обратного вызова
    modal.setAttribute('data-object-item', objectItem ? objectItem.id || '' : '');
    modal.setAttribute('data-view-mode', 'true'); // Отмечаем, что это режим просмотра

    title.textContent = 'Просмотр АСФ';

    const url = 'asf?mode=view&asfId=' + asfId;

    fetch(url, {
        headers: {'X-Requested-With': 'XMLHttpRequest'}
    })
        .then(response => response.text())
        .then(html => {
            content.innerHTML = html;

            // В режиме просмотра все поля должны быть disabled
            // asf.jsp уже обрабатывает это через параметр mode=view

            modal.style.display = 'block';
        })
        .catch(error => {
            console.error('Ошибка загрузки АСФ:', error);
            alert('Ошибка при загрузке данных АСФ');
        });
}

function closeAsfModal() {
    document.getElementById('asfModal').style.display = 'none';
}

// Сохранение АСФ из модального окна
function saveAsfModal() {
    const modal = document.getElementById('asfModal');
    const modalContent = document.getElementById('asfModalContent');
    const isViewMode = modal.getAttribute('data-view-mode') === 'true';

    // Если это режим просмотра - просто закрываем модальное окно
    if (isViewMode) {
        closeAsfModal();
        return;
    }

    const form = modalContent.querySelector('#asfForm');
    const objectItemId = modal.getAttribute('data-object-item');
    const addSignerMode = modal.getAttribute('data-add-signer-mode') === 'true';

    if (!form) {
        alert('Форма не найдена');
        return;
    }

    // Создаём FormData из формы
    const formData = new FormData(form);

    // Добавляем mode
    const mode = form.querySelector('input[name="mode"]').value;
    formData.append('mode', mode);

    // Отправляем на savePortal
    fetch('savePortal', {
        method: 'POST',
        body: formData
    })
        .then(response => response.text())
        .then(() => {
            closeAsfModal();

            // Получаем ID сохранённого АСФ из ответа
            // Временно используем заглушку - после сохранения нужно получить ID
            // Пока просто перезагрузим страницу
            alert('АСФ успешно сохранён');
            location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Ошибка: ' + error);
        });
}

// Вспомогательные функции
function getObjectElements(element) {
    let objectItem;
    if (element instanceof HTMLElement) {
        objectItem = element.closest('.object-item');
    } else {
        objectItem = element;
    }
    return {
        objectItem,
        signerSelect: objectItem.querySelector('.signer-select'),
        hiddenField: objectItem.querySelector('.signer-id-hidden'),
        asfSelect: objectItem.querySelector('.asf-select')
    };
}

function resetSignerSelect(signerSelect) {
    signerSelect.innerHTML = '<option value="">Сначала выберите АСФ</option>';
    signerSelect.disabled = true;
}

// Переключение видимости блока КЧС в зависимости от выбора
function toggleKchsVisibility(select, objectIndex) {

    const objectItem = select.closest('.object-item');
    // Ищем блок КЧС по ID
    const kchsBlock = document.getElementById('kchs-block-' + objectIndex);

    if (!kchsBlock) {
        return;
    }

    // Если выбрана пустая опция или "Не создана" - скрываем
    if (select.value === '' || select.value === 'false') {
        kchsBlock.style.display = 'none';
    } else if (select.value === 'true') {
        kchsBlock.style.display = 'block';
    }
}

// Инициализация
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.asf-select').forEach(select => {
        const asfId = select.value;
        if (!asfId || asfId === 'new_asf') return;

        const elements = getObjectElements(select);
        loadSigners(asfId, elements.signerSelect, elements.hiddenField, true);
    });

    document.querySelectorAll('select[name="emergency_commission[]"]').forEach((select, index) => {
        // Добавляем обработчик изменения
        select.addEventListener('change', function() {
            toggleKchsVisibility(this, index);
        });

        // Устанавливаем начальное состояние
        toggleKchsVisibility(select, index);
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
            // Открываем модальное окно для создания нового АСФ
            openAsfModal(null, elements.objectItem);
            return;
        }

        loadSigners(asfId, elements.signerSelect, elements.hiddenField, true);
    }

    // Обработка выбора подписанта
    if (e.target?.classList.contains('signer-select') && e.target.value === 'add_new_signer') {
        const elements = getObjectElements(e.target);
        // Открываем модальное окно для создания нового подписанта
        // Но сначала нужно получить выбранное АСФ
        const asfSelect = elements.asfSelect;
        if (asfSelect && asfSelect.value && asfSelect.value !== 'new_asf') {
            openAsfModal(asfSelect.value, elements.objectItem, true); // true = добавляем подписанта
        } else {
            alert('Сначала выберите АСФ');
        }
    }
});

function autoResize(el) {
    el.style.height = "auto";
    el.style.height = el.scrollHeight + "px";
}