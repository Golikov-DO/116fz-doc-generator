// универсальное добавление строки
function addTableRow(button, type) {

    const table = button.closest('.collapse-content').querySelector('tbody');
    if (!table) {
        console.error('tbody not found');
        return;
    }

    const index = table.querySelectorAll('tr').length + 1;

    let row = document.createElement('tr');

    if (type === 'kchs') {
        row.innerHTML = `
            <td>
                <input type="hidden" name="kchs_id[]" value="">
                <input type="number" name="kchs_number[]" value="${index}" min="1">
            </td>
            <td><input type="text" name="kchs_position[]"></td>
            <td><input type="text" name="kchs_name[]"></td>
            <td><input type="text" name="kchs_work_phone[]"></td>
            <td><input type="text" name="kchs_phone[]"></td>
            <td><input type="text" name="kchs_address[]"></td>
            <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
        `;
    }

    if (type === 'equipment') {
        row.innerHTML = `
            <td>
                <input type="hidden" name="techno_id[]" value="">
                <input type="number" name="techno_number[]" value="${index}" min="1">
            </td>
            <td>
                <textarea class="auto-resize" name="techno_name[]" rows="1" oninput="autoResize(this)"></textarea>
            </td>
            <td>
                <textarea class="auto-resize" name="techno_characteristics[]" rows="1" oninput="autoResize(this)"></textarea>
            </td>
            <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
        `;
    }

    if (type === 'structure') {
        row.innerHTML = `
            <td>
                <input type="hidden" name="structurre_id[]" value="">
                <input type="number" name="structurre_number[]" value="${index}" min="1">
            </td>
            <td>
                <textarea class="auto-resize" name="structurre_name[]" rows="1" oninput="autoResize(this)"></textarea>
            </td>
            <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
        `;
    }

    if (type === 'techno-block') {
        row.innerHTML = `
            <td>
                <input type="hidden" name="techno_blocke_id[]" value="">
                <input type="number" name="techno_blocke_number[]" value="${index}" min="1">
            </td>
            <td>
                <textarea class="auto-resize" name="techno_blocke_name[]" rows="1" oninput="autoResize(this)"></textarea>
            </td>
            <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
        `;
    }

    if (type === 'persons_response') {
        row.innerHTML = `
            <td>
                <input type="hidden" name="persons_response_id[]" value="">
                <input type="number" name="persons_response_number[]" value="${index}" min="1">
            </td>
            <td>
                <textarea class="auto-resize" name="persons_response_full_name[]" rows="1" oninput="autoResize(this)"></textarea>
            </td>
            <td>
                <textarea class="auto-resize" name="persons_response_position[]" rows="1" oninput="autoResize(this)"></textarea>
            </td>
            <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
        `;
    }

    table.appendChild(row);
    resizeAllTextareas();
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
    if (!modal) return;
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

// Вспомогательные функции
function getObjectElements(element) {
    let objectItem;
    if (element instanceof HTMLElement) {
        objectItem = element.closest('.card');
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
    const objectItem = select.closest('.card');
    if (!objectItem) return;

    const kchsBlock = [...objectItem.querySelectorAll('.collapse-block')]
        .find(block => block.textContent.includes('КЧС'));
    if (!kchsBlock) return;

    if (!select.value || select.value === 'false') {
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

    document.querySelectorAll('select[name="emergency_commission"]').forEach(select => {
        toggleKchsVisibility(select);

    });
});

document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('asfModal');

    if (!modal) return;

    const observer = new MutationObserver(function (mutations) {
        mutations.forEach(function (mutation) {
            if (mutation.attributeName === 'style' && modal.style.display === 'block') {
                const isViewMode = modal.getAttribute('data-view-mode') === 'true';
                const saveButton = document.getElementById('saveAsfButton');

                if (saveButton) {
                    saveButton.style.display = isViewMode ? 'none' : 'inline-block';
                }
            }
        });
    });

    observer.observe(modal, {attributes: true});
});

// Обработчик изменений
document.addEventListener('change', function(event) {
    if (event.target?.name === 'emergency_commission') {
        toggleKchsVisibility(event.target);
    }
    // Обработка выбора АСФ
    if (event.target?.classList.contains('asf-select')) {
        const asfId = event.target.value;
        const elements = getObjectElements(event.target);

        if (!asfId) {
            resetSignerSelect(elements.signerSelect);
            return;
        }

        if (asfId === 'new_asf') {
            addNewAsf();
            event.target.value = ''; // сбрасываем выбор
        }

        loadSigners(asfId, elements.signerSelect, elements.hiddenField, true);
    }

    // Обработка выбора подписанта
    if (event.target?.classList.contains('signer-select') && event.target.value === 'add_new_signer') {
        const elements = getObjectElements(event.target);
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

function openAsfFullPageFromSelect(objectItem) {
    const asfSelect = objectItem.querySelector('.asf-select');
    const asfId = asfSelect.value;

    // Получаем orgId из URL текущей страницы (objects?mode=edit&orgId=1)
    const urlParams = new URLSearchParams(window.location.search);
    const orgId = urlParams.get('orgId');
    const objectId = urlParams.get('id');

    if (!asfId || asfId === 'new_asf') {
        alert('Выберите АСФ для редактирования');
        return;
    }

    // Передаем returnOrgId, а не returnObjectId!
    window.location.href = 'asf?mode=edit&asfId=' + asfId + '&returnOrgId=' + orgId + '&returnObjectId=' + objectId;
}

function addNewAsf() {
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

// === ЗАГРУЗКА КАРТИНОК ОБЪЕКТА ===

// клик по "загрузить"
document.addEventListener('click', function(e) {
    const upload = e.target.closest('.asf-image-upload-area');
    if (!upload) return;

    const input = upload.querySelector('.file-input');
    if (input) input.click();
});

// выбор файла
document.addEventListener('change', function(e) {
    if (!e.target.classList.contains('file-input')) return;

    const input = e.target;
    const block = input.closest('.image-block');

    const file = input.files[0];
    if (!file) return;

    const group = block.dataset.group;

    const objectId = document.querySelector('[name="objectId"]').value;

    const formData = new FormData();
    formData.append('file', file);
    formData.append('group', group);
    formData.append('objectId', objectId);

    fetch('uploadObjectImage', {
        method: 'POST',
        body: formData
    })
        .then(r => r.json())
        .then(() => {
            previewImage(block, file);
        });
});

function previewImage(block, file) {
    const reader = new FileReader();

    reader.onload = function(e) {
        const preview = block.querySelector('.image-preview');
        preview.innerHTML = `<img src="${e.target.result}" style="max-width:100px;" alt="">`;
    };

    reader.readAsDataURL(file);
}

function generatePlan(form) {

    const objectId = form.querySelector('[name="objectId"]').value;

    // 👇 контейнер с кнопками (td -> div)
    const actionsDiv = form.closest('td').querySelector('div');

    // сохраним старое содержимое
    const originalContent = actionsDiv.innerHTML;

    // 👇 заменяем ВСЁ на текст
    actionsDiv.innerHTML = '⏳ План разрабатывается...';

    fetch('generatePlan', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'objectId=' + objectId
    })
        .then(r => {
            if (!r.ok) throw new Error();
            return r.text();
        })
        .then(data => {
            // 👇 возвращаем кнопки
            actionsDiv.innerHTML = originalContent;

            alert('✅ ' + data);
        })
        .catch(err => {
            console.error(err);

            // вернуть кнопки даже при ошибке
            actionsDiv.innerHTML = originalContent;

            alert('Ошибка при разработке плана');
        });

    return false;
}