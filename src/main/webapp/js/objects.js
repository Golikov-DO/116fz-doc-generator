function copySelectOptions(selector, className, objectElement) {

    const existing = document.querySelector(selector);
    if (!existing) return;

    const target = objectElement.querySelector(className);
    if (!target) return;

    target.innerHTML = existing.innerHTML;
    //target.selectedIndex = 0;
}

// Добавление члена КЧС
function addKchs(button) {

    const tbody = button
        .closest(".object-collapse-content")
        .querySelector(".kchs-body");

    const row = document.createElement("tr");

    row.innerHTML = `
<td>
<input type="hidden" name="kchs_id[]" value="">
<input type="hidden" name="kchs_object_index[]" value="0">
<input type="number" name="kchs_number[]" style="width:60px;">
</td>

<td><input type="text" name="kchs_position[]" style="width:100%;"></td>

<td><input type="text" name="kchs_name[]" style="width:100%;"></td>

<td><input type="text" name="kchs_phone[]" style="width:100%;"></td>

<td><input type="text" name="kchs_work_phone[]" style="width:100%;"></td>

<td><input type="text" name="kchs_address[]" style="width:100%;"></td>

<td class="delete-row" onclick="deleteRow(this)">✖</td>
`;

    tbody.appendChild(row);
}

// Добавление оборудования
function addEquipment(button) {

    const tbody = button
        .closest(".object-collapse-content")
        .querySelector(".equipment-body");

    const row = document.createElement("tr");

    row.innerHTML = `
<td>
<input type="hidden" name="techno_id[]" value="">
<input type="hidden" name="techno_object_index[]" value="0">
<input type="number" name="techno_number[]" style="width:60px;">
</td>

<td>
<input type="text" name="techno_name[]" style="width:100%;">
</td>

<td>
<textarea name="techno_characteristics[]" rows="2"
oninput="autoResize(this)"
style="width:100%;resize:none;overflow:hidden;"></textarea>
</td>

<td class="delete-row" onclick="deleteRow(this)">✖</td>
`;

    tbody.appendChild(row);
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
            let selectedValue = hiddenField && hiddenField.value && hiddenField.value !== '0'
                ? Number(hiddenField.value)
                : '';

            signers.forEach(signer => {
                options += `<option value="${signer.id}">
                ${signer.name} (${signer.position})
                </option>`;
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

function openAsfModal(asfId, objectItem, addSignerMode = false) {
    const modal = document.getElementById('asfModal');
    const content = document.getElementById('asfModalContent');
    const title = document.getElementById('asfModalTitle');

    // Сохраняем ссылку на объект для обратного вызова
    modal._objectItem = objectItem;
    modal.setAttribute('data-add-signer-mode', addSignerMode ? 'true' : 'false');
    modal.setAttribute('data-view-mode', window.currentMode === 'view' ? 'true' : 'false');
    modal.setAttribute('data-asf-id', asfId ? asfId : '');

    // Меняем заголовок
    if (window.currentMode === 'view') {
        title.textContent = 'Просмотр АСФ';
    } else if (addSignerMode) {
        title.textContent = 'Добавление подписанта';
    } else {
        title.textContent = asfId ? 'Редактирование АСФ' : 'Добавление АСФ';
    }

    const url = asfId ? 'asf?mode=' + window.currentMode + '&asfId=' + asfId : 'asf?mode=create';

    fetch(url, {
        headers: {'X-Requested-With': 'XMLHttpRequest'}
    })
        .then(response => response.text())
        .then(html => {
            content.innerHTML = html;

            // Инициализируем форму АСФ
            if (typeof initAsfForm === 'function') {
                initAsfForm();
            }

            // Если это режим добавления подписанта, скрываем ненужные блоки
            if (addSignerMode) {
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

function viewAsf(asfId, objectItem) {
    console.log('viewAsf called with asfId:', asfId);

    const modal = document.getElementById('asfModal');
    const content = document.getElementById('asfModalContent');
    const title = document.getElementById('asfModalTitle');

    // Сохраняем ссылку на объект для обратного вызова
    modal._objectItem = objectItem;
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
function toggleKchsVisibility(select) {

    const objectItem = select.closest('.object-item');
    const tbody = objectItem.querySelector('.kchs-body');
    const objectIndex = tbody.dataset.objectIndex;
    const kchsBlock = document.getElementById('kchs-block-' + objectIndex);

    if (!kchsBlock) return;

    if (select.value === '' || select.value === 'false') {
        kchsBlock.style.display = 'none';
    } else {
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

    document.querySelectorAll('select[name="emergency_commission[]"]').forEach(select => {
        toggleKchsVisibility(select);
    });
});

// Обработчик изменений
document.addEventListener('change', function(e) {
    if (e.target?.name === 'emergency_commission[]') {
        toggleKchsVisibility(e.target);
    }
    // Обработка выбора АСФ
    if (e.target?.classList.contains('asf-select')) {
        const asfId = e.target.value;
        const elements = getObjectElements(e.target);

        if (!asfId) {
            resetSignerSelect(elements.signerSelect);
            return;
        }

        if (asfId === 'new_asf') {
            addNewAsf(e.target.closest('.object-item'));
            e.target.value = ''; // сбрасываем выбор
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

// открытие АСФ
function openAsfFullPage(asfId) {
    // Получаем orgId из URL текущей страницы (objects?mode=edit&orgId=1)
    const urlParams = new URLSearchParams(window.location.search);
    const orgId = urlParams.get('orgId');

    if (asfId) {
        window.location.href = 'asf?mode=edit&asfId=' + asfId + '&returnOrgId=' + orgId;
    } else {
        window.location.href = 'asf?mode=edit&asfId=0&returnOrgId=' + orgId;
    }
}

function openAsfFullPageFromSelect(objectItem) {
    const asfSelect = objectItem.querySelector('.asf-select');
    const asfId = asfSelect.value;

    // Получаем orgId из URL текущей страницы (objects?mode=edit&orgId=1)
    const urlParams = new URLSearchParams(window.location.search);
    const orgId = urlParams.get('orgId');

    if (!asfId || asfId === 'new_asf') {
        alert('Выберите АСФ для редактирования');
        return;
    }

    // Передаем returnOrgId, а не returnObjectId!
    window.location.href = 'asf?mode=edit&asfId=' + asfId + '&returnOrgId=' + orgId;
}

function addNewAsf(objectItem) {
    const urlParams = new URLSearchParams(window.location.search);
    const orgId = urlParams.get('orgId');

    // Создаем скрытую форму для POST запроса
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = 'createEmptyAsf';
    form.style.display = 'none';

    const returnOrgIdInput = document.createElement('input');
    returnOrgIdInput.type = 'hidden';
    returnOrgIdInput.name = 'returnOrgId';
    returnOrgIdInput.value = orgId;

    form.appendChild(returnOrgIdInput);
    document.body.appendChild(form);
    form.submit();
}