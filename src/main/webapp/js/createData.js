// Глобальные функции для переключения вкладок
function showTab(tabName) {
    document.querySelectorAll('.tab-pane').forEach(tab => {
        tab.classList.remove('active');
    });
    document.querySelectorAll('.tab').forEach(tab => {
        tab.classList.remove('active');
    });

    document.getElementById(tabName).classList.add('active');

    // Активируем нужную кнопку таба
    document.querySelectorAll('.tab').forEach(tab => {
        if (tab.textContent.includes('Документ') && tabName === 'document') {
            tab.classList.add('active');
        }
        if (tab.textContent.includes('Объекты') && tabName === 'objects') {
            tab.classList.add('active');
        }
    });
}

// Удаление строки
function deleteRow(element) {
    if (confirm('Удалить строку?')) {
        const row = element.parentNode.parentNode;
        const tbody = row.parentNode;
        if (tbody.children.length > 1) {
            row.remove();
        } else {
            alert('Должна быть хотя бы одна строка');
        }
    }
}

// Раскрытие/сворачивание блоков
function toggleCollapse(header) {
    const content = header.nextElementSibling;
    const arrow = header.querySelector('span:last-child');
    content.classList.toggle('expanded');
    arrow.textContent = content.classList.contains('expanded') ? '▲' : '▼';
}

// Добавление контакта организации
function addOrgContact(button) {
    const tbody = button.closest('.collapse-content').querySelector('.org-contacts-body');
    const newRow = documateElement('tr');
    newRow.innerHTML = `
        <td><input type="text" name="org_contact_name[]" placeholder="ФИО" style="width: 100%;"></td>
        <td><input type="text" name="org_contact_position[]" placeholder="Должность" style="width: 100%;"></td>
        <td><input type="text" name="org_contact_phone[]" placeholder="Телефон" style="width: 100%;"></td>
        <td><input type="text" name="org_contact_address[]" placeholder="Адрес" style="width: 100%;"></td>
        <td class="delete-row" onclick="deleteRow(this)">✖</td>
    `;
    tbody.appendChild(newRow);
}

// Добавление члена КЧС
function addKchs(button) {
    const tbody = button.closest('.collapse-content').querySelector('.kchs-body');
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
    const tbody = button.closest('.collapse-content').querySelector('.equipment-body');
    const newRow = document.createElement('tr');
    newRow.innerHTML = `
        <td><input type="text" name="techno_name[]" placeholder="Наименование" style="width: 100%;"></td>
        <td><textarea name="techno_characteristics[]" rows="2" placeholder="Характеристики" style="width: 100%;"></textarea></td>
        <td class="delete-row" onclick="deleteRow(this)">✖</td>
    `;
    tbody.appendChild(newRow);
}

// Добавление структуры
function addStructure(button) {
    const tbody = button.closest('.collapse-content').querySelector('.structure-body');
    const newRow = document.createElement('tr');
    newRow.innerHTML = `
        <td><input type="text" name="structure_name[]" placeholder="Наименование участка" style="width: 100%;"></td>
        <td class="delete-row" onclick="deleteRow(this)">✖</td>
    `;
    tbody.appendChild(newRow);
}

// Добавление средства пожаротушения
function addFire(button) {
    const tbody = button.closest('.collapse-content').querySelector('.fire-body');
    const newRow = document.createElement('tr');
    newRow.innerHTML = `
        <td><input type="text" name="fire_name[]" placeholder="Наименование" style="width: 100%;"></td>
        <td><input type="text" name="fire_quantity[]" placeholder="Количество" style="width: 80px;"></td>
        <td><input type="text" name="fire_location[]" placeholder="Местоположение" style="width: 100%;"></td>
        <td class="delete-row" onclick="deleteRow(this)">✖</td>
    `;
    tbody.appendChild(newRow);
}

// Добавление органа власти
function addAuthority(button) {
    const tbody = button.closest('.collapse-content').querySelector('.authorities-body');
    const newRow = document.createElement('tr');
    newRow.innerHTML = `
        <td><input type="text" name="authority_name[]" placeholder="Наименование органа" style="width: 100%;"></td>
        <td><input type="text" name="authority_department[]" placeholder="Отдел" style="width: 100%;"></td>
        <td><input type="text" name="authority_phone[]" placeholder="Телефон" style="width: 100%;"></td>
        <td><input type="text" name="authority_address[]" placeholder="Адрес" style="width: 100%;"></td>
        <td class="delete-row" onclick="deleteRow(this)">✖</td>
    `;
    tbody.appendChild(newRow);
}

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
    return `
        <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
            <span style="font-weight: bold;">Объект #${number}</span>
            <span class="delete-row" onclick="removeObject(this)">Удалить объект</span>
        </div>
        
        <!-- Основные поля объекта -->
        <table class="data-table" style="margin-bottom: 10px;">
            <tr>
                <th>Полное наименование</th>
                <th>Краткое наименование</th>
                <th>ID города</th>
                <th>Класс опасности</th>
                <th>ID вещества</th>
            </tr>
            <tr>
                <td><input type="text" name="object_full_name[]" style="width: 100%;"></td>
                <td><input type="text" name="object_short_name[]" style="width: 100%;"></td>
                <td><input type="number" name="object_city_id[]" style="width: 80px;"></td>
                <td><input type="number" name="hazard_class[]" style="width: 70px;"></td>
                <td><input type="number" name="hazardous_substance_id[]" style="width: 70px;"></td>
            </tr>
        </table>
        
        <!-- Детали объекта -->
        <table class="data-table" style="margin-bottom: 10px;">
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
        
        <!-- Страховка и остаток -->
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
                <label style="font-size: 11px;">Номер остатка</label>
                <input type="number" name="balance_number[]" style="width: 100%;">
            </div>
            <div>
                <label style="font-size: 11px;">Дата остатка</label>
                <input type="date" name="balance_date[]" style="width: 100%;">
            </div>
        </div>
        
        <!-- Тип объекта -->
        <div style="margin-bottom: 10px;">
            <label style="font-size: 11px;">Тип объекта (определение)</label>
            <textarea name="object_type_definition[]" rows="2" style="width: 100%;"></textarea>
        </div>
        
        <!-- КЧС -->
        <div class="collapse-block">
            <div class="collapse-header" onclick="toggleCollapse(this)">
                <span>👥 Состав КЧС для объекта</span>
                <span>▼</span>
            </div>
            <div class="collapse-content">
                <button type="button" class="add-row" onclick="addKchs(this)">+ Добавить члена КЧС</button>
                <table class="data-table" style="margin-top: 10px;">
                    <thead>
                        <tr>
                            <th>Должность в КЧС</th>
                            <th>ФИО с должностью</th>
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
        <div class="collapse-block">
            <div class="collapse-header" onclick="toggleCollapse(this)">
                <span>🔧 Оборудование</span>
                <span>▼</span>
            </div>
            <div class="collapse-content">
                <button type="button" class="add-row" onclick="addEquipment(this)">+ Добавить оборудование</button>
                <table class="data-table" style="margin-top: 10px;">
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
        
        <!-- Структура -->
        <div class="collapse-block">
            <div class="collapse-header" onclick="toggleCollapse(this)">
                <span>📋 Структура объекта</span>
                <span>▼</span>
            </div>
            <div class="collapse-content">
                <button type="button" class="add-row" onclick="addStructure(this)">+ Добавить участок</button>
                <table class="data-table" style="margin-top: 10px;">
                    <thead>
                        <tr>
                            <th>Наименование участка</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody class="structure-body">
                        <tr>
                            <td><input type="text" name="structure_name[]" style="width: 100%;"></td>
                            <td class="delete-row" onclick="deleteRow(this)">✖</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        
        <!-- Средства пожаротушения -->
        <div class="collapse-block">
            <div class="collapse-header" onclick="toggleCollapse(this)">
                <span>🔥 Средства пожаротушения</span>
                <span>▼</span>
            </div>
            <div class="collapse-content">
                <button type="button" class="add-row" onclick="addFire(this)">+ Добавить средство</button>
                <table class="data-table" style="margin-top: 10px;">
                    <thead>
                        <tr>
                            <th>Наименование</th>
                            <th>Количество</th>
                            <th>Местоположение</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody class="fire-body">
                        <tr>
                            <td><input type="text" name="fire_name[]" style="width: 100%;"></td>
                            <td><input type="text" name="fire_quantity[]" style="width: 80px;"></td>
                            <td><input type="text" name="fire_location[]" style="width: 100%;"></td>
                            <td class="delete-row" onclick="deleteRow(this)">✖</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        
        <!-- Органы власти -->
        <div class="collapse-block">
            <div class="collapse-header" onclick="toggleCollapse(this)">
                <span>🏛️ Органы власти (по месту объекта)</span>
                <span>▼</span>
            </div>
            <div class="collapse-content">
                <button type="button" class="add-row" onclick="addAuthority(this)">+ Добавить орган власти</button>
                <table class="data-table" style="margin-top: 10px;">
                    <thead>
                        <tr>
                            <th>Наименование</th>
                            <th>Отдел</th>
                            <th>Телефон</th>
                            <th>Адрес</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody class="authorities-body">
                        <tr>
                            <td><input type="text" name="authority_name[]" style="width: 100%;"></td>
                            <td><input type="text" name="authority_department[]" style="width: 100%;"></td>
                            <td><input type="text" name="authority_phone[]" style="width: 100%;"></td>
                            <td><input type="text" name="authority_address[]" style="width: 100%;"></td>
                            <td class="delete-row" onclick="deleteRow(this)">✖</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    `;
}

function removeObject(element) {
    const objectItem = element.closest('.object-item');
    if (document.querySelectorAll('.object-item').length > 1) {
        objectItem.remove();
    } else {
        alert('Должен быть хотя бы один объект');
    }
}

// Валидация формы
function validateForm() {
    const errors = [];
    let hasDocumentErrors = false;
    let hasObjectErrors = false;

    // 1. Проверяем основные поля организации
    const asfId = document.querySelector('select[name="asf_id"]').value;
    if (!asfId) {
        errors.push('• Выберите ASF ID организации');
        hasDocumentErrors = true;
    }

    const orgFullName = document.querySelector('textarea[name="organization_full_name"]').value;
    if (!orgFullName.trim()) {
        errors.push('• Заполните полное наименование организации');
        hasDocumentErrors = true;
    }

    const orgShortName = document.querySelector('input[name="organization_short_name"]').value;
    if (!orgShortName.trim()) {
        errors.push('• Заполните краткое наименование организации');
        hasDocumentErrors = true;
    }

    // 2. Проверяем адрес организации
    const orgIndex = document.querySelector('input[name="org_index"]').value;
    if (!orgIndex.trim()) {
        errors.push('• Заполните индекс организации');
        hasDocumentErrors = true;
    }

    const orgCity = document.querySelector('input[name="org_city"]').value;
    if (!orgCity.trim()) {
        errors.push('• Заполните город организации');
        hasDocumentErrors = true;
    }

    // 3. Проверяем подписанта
    const signerPosition = document.querySelector('input[name="signer_position"]').value;
    if (!signerPosition.trim()) {
        errors.push('• Заполните должность подписанта');
        hasDocumentErrors = true;
    }

    const signerName = document.querySelector('input[name="signer_name"]').value;
    if (!signerName.trim()) {
        errors.push('• Заполните ФИО подписанта');
        hasDocumentErrors = true;
    }

    // 4. Проверяем контакты организации
    const orgContactRows = document.querySelectorAll('.org-contacts-body tr');
    let hasOrgContact = false;
    orgContactRows.forEach(row => {
        const inputs = row.querySelectorAll('input');
        let rowFilled = false;
        inputs.forEach(input => {
            if (input.value.trim()) rowFilled = true;
        });
        if (rowFilled) hasOrgContact = true;
    });

    if (!hasOrgContact) {
        errors.push('• Добавьте хотя бы один контакт организации');
        hasDocumentErrors = true;
    }

    // 5. Проверяем объекты
    const objectItems = document.querySelectorAll('.object-item');
    if (objectItems.length === 0) {
        errors.push('• Добавьте хотя бы один объект');
        hasObjectErrors = true;
    } else {
        objectItems.forEach((obj, index) => {
            let objHasErrors = false;

            // Проверяем основные поля объекта
            const objFullName = obj.querySelector('input[name="object_full_name[]"]')?.value;
            if (!objFullName?.trim()) {
                errors.push(`• Объект #${index + 1}: заполните полное наименование`);
                objHasErrors = true;
            }

            const objShortName = obj.querySelector('input[name="object_short_name[]"]')?.value;
            if (!objShortName?.trim()) {
                errors.push(`• Объект #${index + 1}: заполните краткое наименование`);
                objHasErrors = true;
            }

            const objCityId = obj.querySelector('input[name="object_city_id[]"]')?.value;
            if (!objCityId) {
                errors.push(`• Объект #${index + 1}: заполните ID города`);
                objHasErrors = true;
            }

            const hazardClass = obj.querySelector('input[name="hazard_class[]"]')?.value;
            if (!hazardClass) {
                errors.push(`• Объект #${index + 1}: заполните класс опасности`);
                objHasErrors = true;
            }

            // Проверяем КЧС
            const kchsRows = obj.querySelectorAll('.kchs-body tr');
            let hasKchs = false;
            kchsRows.forEach(row => {
                const inputs = row.querySelectorAll('input');
                let rowFilled = false;
                inputs.forEach(input => {
                    if (input.value.trim()) rowFilled = true;
                });
                if (rowFilled) hasKchs = true;
            });
            if (!hasKchs) {
                errors.push(`• Объект #${index + 1}: добавьте хотя бы одного члена КЧС`);
                objHasErrors = true;
            }

            // Проверяем оборудование
            const technoRows = obj.querySelectorAll('.equipment-body tr');
            let hasTechno = false;
            technoRows.forEach(row => {
                const inputs = row.querySelectorAll('input, textarea');
                let rowFilled = false;
                inputs.forEach(input => {
                    if (input.value.trim()) rowFilled = true;
                });
                if (rowFilled) hasTechno = true;
            });
            if (!hasTechno) {
                errors.push(`• Объект #${index + 1}: добавьте хотя бы одно оборудование`);
                objHasErrors = true;
            }

            // Проверяем структуру
            const structureRows = obj.querySelectorAll('.structure-body tr');
            let hasStructure = false;
            structureRows.forEach(row => {
                const input = row.querySelector('input');
                if (input?.value.trim()) hasStructure = true;
            });
            if (!hasStructure) {
                errors.push(`• Объект #${index + 1}: добавьте хотя бы один участок структуры`);
                objHasErrors = true;
            }

            // Проверяем средства пожаротушения
            const fireRows = obj.querySelectorAll('.fire-body tr');
            let hasFire = false;
            fireRows.forEach(row => {
                const inputs = row.querySelectorAll('input');
                let rowFilled = false;
                inputs.forEach(input => {
                    if (input.value.trim()) rowFilled = true;
                });
                if (rowFilled) hasFire = true;
            });
            if (!hasFire) {
                errors.push(`• Объект #${index + 1}: добавьте хотя бы одно средство пожаротушения`);
                objHasErrors = true;
            }

            // Проверяем органы власти
            const authorityRows = obj.querySelectorAll('.authorities-body tr');
            let hasAuthority = false;
            authorityRows.forEach(row => {
                const inputs = row.querySelectorAll('input');
                let rowFilled = false;
                inputs.forEach(input => {
                    if (input.value.trim()) rowFilled = true;
                });
                if (rowFilled) hasAuthority = true;
            });
            if (!hasAuthority) {
                errors.push(`• Объект #${index + 1}: добавьте хотя бы один орган власти`);
                objHasErrors = true;
            }

            if (objHasErrors) hasObjectErrors = true;
        });
    }

    // Если есть ошибки - показываем
    if (errors.length > 0) {
        let errorMessage = '❌ Заполните обязательные поля:\n\n' + errors.join('\n');
        alert(errorMessage);

        // Переключаемся на вкладку с ошибками
        if (hasDocumentErrors) {
            showTab('document');
        } else if (hasObjectErrors) {
            showTab('objects');
        }

        return false;
    }

    return true;
}

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('mainForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();

            if (validateForm()) {
                if (confirm('✅ Все данные заполнены. Отправить в базу данных?')) {
                    const btn = document.querySelector('.btn-primary');
                    btn.textContent = '⏳ Сохранение...';
                    btn.disabled = true;
                    this.submit();
                }
            }
        });
    }
});

document.addEventListener('DOMContentLoaded', function () {

    const modeInput = document.querySelector('input[name="mode"]');

    if (!modeInput) return;

    const mode = modeInput.value;

    // Только при создании создаём пустой объект
    if (mode === 'create') {
        addObject();
    }
});