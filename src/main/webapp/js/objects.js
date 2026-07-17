let formChanged = false;
let currentStructureIndex = null;
let currentScenarioType = null;

// Create and submit hidden POST form
function postRedirect(action, params = {}) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = action;
    form.style.display = 'none';

    Object.entries(params).forEach(([key, value]) => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = key;
        input.value = String(value);
        form.appendChild(input);
    });

    document.body.appendChild(form);
    form.submit();
}

// Build URL with params and redirect
// Simple redirect with params
function navigateTo(path, params) {

    let url = path;
    let query = '';

    for (let key in params) {
        if (params[key]) {
            if (query !== '') query += '&';
            query += key + '=' + encodeURIComponent(params[key]);
        }
    }

    if (query) url += '?' + query;

    window.location.href = url;
}

// universal row append
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
        <input type="hidden" name="structure_id[]" value="">
        <input type="number" name="structure_number[]" value="${index}" min="1">
    </td>
    <td>
        <textarea class="auto-resize" name="structure_name[]" rows="1" oninput="autoResize(this)"></textarea>
    </td>

    <td>
        <button type="button" onclick="openScenarioModal(${index}, 'likely')">Вероятные</button>
        <textarea id="likely_selected_${index}" class="auto-resize" readonly></textarea>
        <input type="hidden" name="likely_${index}" id="likely_input_${index}">
    </td>

    <td>
        <button type="button" onclick="openScenarioModal(${index}, 'dangerous')">Опасные</button>
        <textarea id="dangerous_selected_${index}" class="auto-resize" readonly></textarea>
        <input type="hidden" name="dangerous_${index}" id="dangerous_input_${index}">
    </td>

    <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
`;
    }

    if (type === 'techno-block') {
        row.innerHTML = `
            <td>
                <input type="hidden" name="techno_block_id[]" value="">
                <input type="number" name="techno_block_number[]" value="${index}" min="1">
            </td>
            <td>
                <textarea class="auto-resize" name="techno_block_name[]" rows="1" oninput="autoResize(this)"></textarea>
            </td>
            <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
        `;
    }

    if (type === 'persons-response') {
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

    if (type === 'fire-equipment') {
        row.innerHTML = `
            <td>
                <input type="hidden" name="fire_equipment_id[]" value="">
                <input type="number" name="fire_equipment_number[]" value="${index}" min="1">
            </td>
            <td><input type="text" name="fire_equipment_name_product[]"></td>
            <td><input type="text" name="fire_equipment_quantity[]"></td>
            <td><input type="text" name="fire_equipment_location[]"></td>
            <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
        `;
    }

    table.appendChild(row);
    resizeAllTextareas();
    initStructureRow(index);
}

function initStructureRow(index) {

    ['likely', 'dangerous'].forEach(type => {

        const input = document.getElementById(type + '_input_' + index);
        const textarea = document.getElementById(type + '_selected_' + index);
        const btn = document.getElementById(type + '_btn_' + index);

        if (!input || !textarea) return;

        textarea.value = '';
        textarea.style.height = 'auto';
        textarea.style.height = textarea.scrollHeight + 'px';

        if (btn) {
            btn.innerText = 'Добавить сценарии';
        }
    });
}

// loading signatories
function loadSigners(asfId, signerSelect, hiddenField, allowAddOption = true) {
    if (!asfId) {
        signerSelect.innerHTML = '<option value="">Сначала выберите АСФ</option>';
        signerSelect.disabled = true;
        return;
    }

    fetch('/get-asf-signers?asfId=' + asfId)
        .then(response => response.json())
        .then(signers => {
            let options = '<option value="">— выберите —</option>';
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
                    — добавить —
                </option>`;
            }

            signerSelect.innerHTML = options;

            // unlock if it is NOT viewing mode.
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

    // save a reference to the object for the callback
    modal._objectItem = objectItem;
    modal.setAttribute('data-add-signer-mode', addSignerMode ? 'true' : 'false');
    modal.setAttribute('data-view-mode', window.currentMode === 'view' ? 'true' : 'false');
    modal.setAttribute('data-asf-id', asfId ? asfId : '');

    // changing the title
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

            // initializing the ASF form
            if (typeof initAsfForm === 'function') {
                initAsfForm();
            }

            // if this is the signer addition mode, hide unnecessary blocks
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

function closeAsfModal() {
    const modal = document.getElementById('asfModal');
    if (modal) modal.style.display = 'none';
}

function openAsfFullPageFromSelect(objectItem) {
    const asfSelect = objectItem.querySelector('.asf-select');
    const asfId = asfSelect.value;

    // get orgId from the URL of the current page
    const urlParams = new URLSearchParams(window.location.search);
    const orgId = urlParams.get('orgId');
    const objectId = urlParams.get('id');

    if (!asfId || asfId === 'new_asf') {
        alert('Выберите АСФ для редактирования');
        return;
    }

    window.location.href = 'asf?mode=edit&asfId=' + asfId + '&returnOrgId=' + orgId + '&returnObjectId=' + objectId;
}

function addNewAsf() {
    const orgId = new URLSearchParams(window.location.search).get('orgId');
    postRedirect('create-empty-asf', { returnOrgId: orgId });
}

function viewAsf(asfId, objectItem) {
    const modal = document.getElementById('asfModal');
    const content = document.getElementById('asfModalContent');
    const title = document.getElementById('asfModalTitle');

    // save a reference to the object for the callback
    modal._objectItem = objectItem;
    modal.setAttribute('data-view-mode', 'true'); // note that this is a viewing mode.

    title.textContent = 'Просмотр АСФ';

    const url = 'asf?mode=view&asfId=' + asfId;

    fetch(url, {
        headers: {'X-Requested-With': 'XMLHttpRequest'}
    })
        .then(response => response.text())
        .then(html => {
            content.innerHTML = html;
            modal.style.display = 'block';
        })
        .catch(error => {
            console.error('Error loading ASF data:', error);
            alert('Error loading ASF data');
        });
}

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

// Switching the visibility of the KChS block depending on the selection
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

// Update cancel button label depending on form state
function updateCancelButton() {
    const btn = document.getElementById('cancelBtn');
    if (!btn) return;

    btn.innerText = formChanged ? 'Отменить' : 'Назад к списку';
}

// Cancel editing:
// - if no changes → go back
// - if changed → reload original data
function cancelEdit() {

    if (formChanged) {
        window.location.reload();
        return;
    }

    const params = new URLSearchParams(window.location.search);

    // 1. главный сценарий — есть backUrl
    const backUrl = params.get('backUrl');
    if (backUrl) {
        window.location.href = backUrl;
        return;
    }

    // 2. fallback — возврат к списку объектов по orgId
    const orgId = params.get('orgId');
    if (orgId) {
        window.location.href = '/objects?orgId=' + orgId;
        return;
    }

    // 3. крайний fallback
    window.location.href = '/objects';
}

// Centralized handler for form change detection
function handleFormChange(event) {
    if (!event.target.closest('form')) return;

    // mark form as changed only once
    if (!formChanged) {
        formChanged = true;
        updateCancelButton();
    }
}

function previewImage(block, file) {
    const reader = new FileReader();

    reader.onload = function (e) {
        const preview = block.querySelector('.image-preview');
        preview.innerHTML = `<img src="${e.target.result}" style="max-width:100px;" alt="">`;
    };

    reader.readAsDataURL(file);
}

// Plan generate
function generatePlan(form) {
    const objectId = form.querySelector('[name="objectId"]').value;

    // Container with buttons (td -> div)
    const actionsDiv = form.closest('td').querySelector('div');

    // Save original content
    const originalContent = actionsDiv.innerHTML;

    // Replace everything with status text
    actionsDiv.innerHTML = '⏳ План разрабатывается...';

    fetch('generate-plan', {
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
            // Restore buttons
            actionsDiv.innerHTML = originalContent;

            alert('✅ ' + data);
        })
        .catch(err => {
            console.error(err);

            // Restore buttons even if an error occurs
            actionsDiv.innerHTML = originalContent;

            alert('Ошибка при разработке плана');
        });

    return false;
}

function openScenarioModal(index, type) {
    currentStructureIndex = index;
    currentScenarioType = type;

    const input = document.getElementById(type + '_input_' + index);
    const selectedIds = input.value ? input.value.split(',') : [];

    // RESET ALL CHECKBOXES
    document.querySelectorAll('.scenario-checkbox').forEach(cb => {
        cb.checked = false;
    });

    // RESTORATION OF CHOICE
    document.querySelectorAll('.scenario-checkbox').forEach(cb => {
        if (selectedIds.includes(cb.value)) {
            cb.checked = true;
        }
    });

    document.getElementById('scenarioModal').style.display = 'block';
}

function applyScenario() {

    const selectedIds = [];
    const selectedNames = [];

    document.querySelectorAll('.scenario-checkbox:checked').forEach(cb => {
        selectedIds.push(cb.value);
        selectedNames.push(cb.parentElement.innerText.trim());
    });

    const index = currentStructureIndex;
    const type = currentScenarioType;

    // hidden input
    document.getElementById(type + '_input_' + index).value = selectedIds.join(',');
    // textarea
    const textarea = document.getElementById(type + '_selected_' + index);

    textarea.value = selectedNames.join('\n');

    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';

    // кнопка
    const btn = document.getElementById(type + '_btn_' + index);
    if (btn) {
        btn.innerText = selectedIds.length > 0 ? 'Редактировать' : 'Добавить сценарии';
    }

    closeScenarioModal();
}

function closeScenarioModal() {
    document.getElementById('scenarioModal').style.display = 'none';
}

// Upload images
// click "upload"
document.addEventListener('click', function (e) {
    const upload = e.target.closest('.image-upload-area');
    if (!upload) return;

    const input = upload.querySelector('.file-input');
    if (input) input.click();
});

// select file
document.addEventListener('change', function (event) {
    if (!event.target.classList.contains('file-input')) return;

    const input = event.target;
    const block = input.closest('.image-block');

    const file = input.files[0];
    if (!file) return;

    const group = block.dataset.group;

    const objectId = document.querySelector('[name="id"]').value;

    const formData = new FormData();
    formData.append('file', file);
    formData.append('group', group);
    formData.append('objectId', objectId);

    fetch('/api/object-images', {
        method: 'POST',
        body: formData
    })
        .then(r => r.json())
        .then(() => {
            previewImage(block, file);

            const group = block.dataset.group;
            const template = imageTemplates[group];

            if (!template) return;

            const captionField = block.querySelector(`[name="caption_${group}"]`);
            const linkField = block.querySelector(`[name="link_${group}"]`);

            if (captionField && !captionField.value.trim()) {
                captionField.value = template.caption;
            }

            if (linkField && !linkField.value.trim()) {
                linkField.value = template.link;
            }
        });
});

// === INIT ===
document.addEventListener('DOMContentLoaded', function () {

    // restore scenarios
    document.querySelectorAll('[id^="likely_input_"], [id^="dangerous_input_"]')
        .forEach(input => {

            if (!input.value) return;

            const ids = input.value.split(',');
            const parts = input.id.split('_');

            const type = parts[0];
            const index = parts[2];

            const names = [];

            document.querySelectorAll('.scenario-checkbox').forEach(cb => {
                if (ids.includes(cb.value)) names.push(cb.parentElement.innerText.trim());
            });

            const field = document.getElementById(type + '_selected_' + index);
            if (field) field.value = names.join('\n');

            const btn = document.getElementById(type + '_btn_' + index);
            if (btn)  btn.innerText = names.length > 0 ? 'Редактировать' : 'Добавить сценарии';
        });

    // init ASF selects
    document.querySelectorAll('.asf-select').forEach(select => {
        const asfId = select.value;
        if (!asfId || asfId === 'new_asf') return;

        const elements = getObjectElements(select);
        loadSigners(asfId, elements.signerSelect, elements.hiddenField, true);
    });

    // init KCHS visibility
    document.querySelectorAll('select[name="emergency_commission"]').forEach(select => {
        toggleKchsVisibility(select);
    });

    // modal observer
    const modal = document.getElementById('asfModal');
    if (modal) {
        const observer = new MutationObserver(function (mutations) {
            mutations.forEach(function (mutation) {
                if (mutation.attributeName === 'style' && modal.style.display === 'block') {
                    const isViewMode = modal.getAttribute('data-view-mode') === 'true';
                    const saveButton = document.getElementById('saveAsfButton');

                    if (saveButton) saveButton.style.display = isViewMode ? 'none' : 'inline-block';
                }
            });
        });

        observer.observe(modal, {attributes: true});
    }

});

// Handle "add new" option in select
function handleAddNewOption(select, triggerValue, action) {
    if (select.value !== triggerValue) return false;

    action();
    select.value = '';

    return true;
}

// === GLOBAL FORM CHANGE TRACKING ===
// Detect any change inside form to switch Cancel -> Reset mode
document.addEventListener('input', function (event) {
    handleFormChange(event);
});

const imageTemplates = {
    1: {
        caption: 'План схема ОПО',
        link: 'Расположение ОПО приведено на рисунке'
    },
    2: {
        caption: 'Схема размещения оборудования на объекте',
        link: 'Размещение оборудования показано на рисунке'
    },
    3: {
        caption: 'Схема сценариев развития аварий на ОПО с указанием основных причин их возникновения при разгерметизации оборудования',
        link: 'Схемы сценариев развития аварий с указанием основных причин их возникновения применительно к технологическому оборудованию ОПО приведены на рисунке'
    },
    4: {
        caption: 'Схема взаимодействия и оповещения при возникновении ЧС',
        link: 'Схема взаимодействия и оповещения ПАСФ при возникновении ЧС приведена на рисунке'
    }
};

document.addEventListener('change', function (event) {

    // === existing logic ===
    if (event.target?.name === 'emergency_commission') {
        toggleKchsVisibility(event.target);
    }

    // ASF select
    if (event.target?.classList.contains('asf-select')) {
        const asfId = event.target.value;
        const elements = getObjectElements(event.target);

        if (!asfId) {
            resetSignerSelect(elements.signerSelect);
            return;
        }

        if (handleAddNewOption(event.target, 'new_asf', addNewAsf)) return;

        loadSigners(asfId, elements.signerSelect, elements.hiddenField, true);
    }

    // signer select
    if (event.target?.classList.contains('signer-select') && event.target.value === 'add_new_signer') {
        const elements = getObjectElements(event.target);
        const asfSelect = elements.asfSelect;

        if (asfSelect && asfSelect.value && asfSelect.value !== 'new_asf')
            openAsfModal(asfSelect.value, elements.objectItem, true);
        else alert('Сначала выберите АСФ');
    }

    // hazard select
    if (event.target?.classList.contains('hazard-select'))
        handleAddNewOption(event.target, 'new_hazard', addNewHazardous);

    // type select
    if (event.target?.classList.contains('type-select'))
        handleAddNewOption(event.target, 'new_type', addNewObjectType);

    // region select
    if (event.target?.classList.contains('region-select'))
        handleAddNewOption(event.target, 'new_region', addNewRegion);

    // === IMPORTANT: track form changes LAST ===
    handleFormChange(event);
});