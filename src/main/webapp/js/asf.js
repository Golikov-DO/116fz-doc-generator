// Раскрытие/сворачивание блоков
function toggleCollapse(header) {
    const content = header.nextElementSibling;
    const arrow = header.querySelector('span:last-child');
    content.classList.toggle('expanded');
    arrow.textContent = content.classList.contains('expanded') ? '▲' : '▼';
}

// Добавление нового подписанта
function addSigner() {
    const container = document.getElementById('signersContainer');
    const newItem = document.createElement('div');
    newItem.className = 'asf-signer-item';
    newItem.innerHTML = `
    <input type="hidden" name="signer_id[]" value="">
        <div>
            <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">ФИО подписанта</div>
            <input type="text" name="signer_name[]" style="width: 100%; padding: 5px;">
        </div>
        <div>
            <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Должность</div>
            <input type="text" name="signer_position[]" style="width: 100%; padding: 5px;">
        </div>
        <div>
            <span class="delete-row" onclick="removeSigner(this)" style="color: #f44336; cursor: pointer; font-size: 18px;">✖</span>
        </div>
    `;
    container.appendChild(newItem);
}

// Удаление подписанта
function removeSigner(element) {
    if (confirm('Удалить подписанта?')) {
        const signerItem = element.closest('.asf-signer-item');
        const container = document.getElementById('signersContainer');

        if (container.children.length > 1) {
            signerItem.remove();
        } else {
            const inputs = signerItem.querySelectorAll('input');
            inputs.forEach(input => input.value = '');
            alert('Должен быть хотя бы один подписант. Поля очищены.');
        }
    }
}

// Добавление нового типа работ
function addWorkType() {
    const container = document.getElementById('workTypesContainer');
    const newItem = document.createElement('div');
    newItem.className = 'asf-work-type-item';
    newItem.innerHTML = `
    <input type="hidden" name="work_type_index[]" value="0">
    <input type="hidden" name="work_type_id[]" value="">
        <div>
            <div class="asf-form-label" style="width: auto; margin-bottom: 3px;">Наименование типа работ</div>
            <input type="text" name="work_type_name[]" style="width: 100%; padding: 5px;" placeholder="Например: Газоспасательные работы">
        </div>
        <div>
            <span class="delete-row" onclick="removeWorkType(this)" style="color: #f44336; cursor: pointer; font-size: 18px;">✖</span>
        </div>
    `;
    container.appendChild(newItem);
}

// Удаление типа работ
function removeWorkType(element) {
    if (confirm('Удалить тип работ?')) {
        const workTypeItem = element.closest('.asf-work-type-item');
        const container = document.getElementById('workTypesContainer');

        if (container.children.length > 1) {
            workTypeItem.remove();
        } else {
            const input = workTypeItem.querySelector('input');
            if (input) input.value = '';
            alert('Должен быть хотя бы один тип работ. Поле очищено.');
        }
    }
}

// Переменные для счетчиков изображений
let appendix1Counter = 1;
let appendix2Counter = 1;

// Валидация и автоматическое сжатие изображения
function validateImageSize(input, targetWidth, targetHeight, group, position) {
    if (!input.files || !input.files[0]) return;

    const file = input.files[0];

    if (file.type !== 'image/png') {
        alert('❌ Поддерживаются только PNG изображения');
        input.value = '';
        return;
    }

    if (file.size > 8 * 1024 * 1024) {
        alert('❌ Размер файла не должен превышать 8MB');
        input.value = '';
        return;
    }

    const img = new Image();
    const reader = new FileReader();

    reader.onload = function(e) {
        img.src = e.target.result;
        img.onload = function() {
            if (img.width === targetWidth && img.height === targetHeight) {
                showImagePreview(e.target.result, group, position, file.name);
                saveImageData(group, position, file.name, e.target.result.split(',')[1]);
                return;
            }
            resizeImage(img, targetWidth, targetHeight, file.name, group, position);
        };
    };
    reader.readAsDataURL(file);
}

// Функция сжатия изображения
function resizeImage(img, targetWidth, targetHeight, fileName, group, position) {
    const fileInput = document.getElementById(`file_${group}_${position}`);
    if (!fileInput) return;

    const canvas = document.createElement('canvas');
    canvas.width = targetWidth;
    canvas.height = targetHeight;

    const ctx = canvas.getContext('2d');
    ctx.imageSmoothingEnabled = true;
    ctx.imageSmoothingQuality = 'high';
    ctx.drawImage(img, 0, 0, targetWidth, targetHeight);

    canvas.toBlob(function(blob) {
        const resizedFile = new File([blob], fileName, {
            type: 'image/png',
            lastModified: Date.now()
        });

        const dataTransfer = new DataTransfer();
        dataTransfer.items.add(resizedFile);
        fileInput.files = dataTransfer.files;

        const resizedDataUrl = canvas.toDataURL('image/png');
        showImagePreview(resizedDataUrl, group, position, fileName);
        saveImageData(group, position, fileName, resizedDataUrl.split(',')[1]);
    }, 'image/png', 1.0);
}

// Функция показа превью
function showImagePreview(dataUrl, group, position, fileName) {
    const previewDiv = document.getElementById(`preview_${group}_${position}`);
    if (!previewDiv) return;

    previewDiv.innerHTML = '';

    const container = document.createElement('div');
    container.style = 'display: flex; align-items: center; gap: 10px; margin-top: 5px;';

    const img = document.createElement('img');
    img.src = dataUrl;
    img.style = 'max-width: 60px; max-height: 60px; border: 1px solid #ddd; border-radius: 4px;';

    const info = document.createElement('div');
    info.style = 'font-size: 11px;';
    info.innerHTML = `<div><b>${fileName}</b></div>`;

    container.appendChild(img);
    container.appendChild(info);
    previewDiv.appendChild(container);
}

// Сохранение данных изображения
function saveImageData(group, position, fileName, base64Data) {
    const container = document.getElementById('imageDataContainer');

    const oldName = document.querySelector(`input[name="image_name_${group}_${position}"]`);
    if (oldName) oldName.remove();

    const nameInput = document.createElement('input');
    nameInput.type = 'hidden';
    nameInput.name = `image_name_${group}_${position}`;
    nameInput.value = fileName;
    container.appendChild(nameInput);

    const groupInput = document.createElement('input');
    groupInput.type = 'hidden';
    groupInput.name = `image_group_${group}_${position}`;
    groupInput.value = group;
    container.appendChild(groupInput);

    if (base64Data) {
        const dataInput = document.createElement('input');
        dataInput.type = 'hidden';
        dataInput.name = `image_data_${group}_${position}`;
        dataInput.value = base64Data;
        container.appendChild(dataInput);
    }
}

// Добавление поля изображения
function addImageField(group) {
    const container = document.getElementById(`appendix${group}_container`);
    const counter = group === '1' ? appendix1Counter++ : appendix2Counter++;
    const position = counter;

    const div = document.createElement('div');
    div.className = 'asf-image-item';
    div.innerHTML = `
    <input type="hidden" name="image_index[]" value="0">
    <input type="hidden" name="image_id[]" value="">
    <input type="hidden" name="image_group[]" value="${group}">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
            <span style="font-size: 11px; color: #666;">Изображение ${counter} (1047x1480px)</span>
            <span class="delete-row" onclick="removeImageField(this, '${group}', ${position})" style="color: #f44336; cursor: pointer;">✖</span>
        </div>
        <div style="display: flex; gap: 10px; align-items: center;">
            <div class="asf-image-upload-area" onclick="document.getElementById('file_${group}_${position}').click()">
                📁 Загрузить
            </div>
            <span style="font-size: 11px; color: #999;">PNG, 8MB макс</span>
        </div>
        <input type="file" id="file_${group}_${position}" name="image_file_${group}_${position}" accept="image/png" style="display: none;" onchange="validateImageSize(this, 1047, 1480, '${group}', ${position})">
        <div id="preview_${group}_${position}" style="margin-top: 10px;"></div>
    `;
    container.appendChild(div);
}

// Удаление поля изображения
function removeImageField(element) {
    if (confirm('Удалить изображение?')) {
        const imageDiv = element.closest('.asf-image-item');
        if (imageDiv) {
            imageDiv.remove();
        }
    }
}

// Удаление div с изображением (не из БД, только из DOM)
function removeImageDiv(element) {
    if (confirm('Удалить изображение?')) {
        const imageDiv = element.closest('.asf-image-item');
        if (imageDiv) {
            imageDiv.remove();
        }
    }
}

// Инициализация при загрузке
function initAsfForm()  {
    // Валидация формы
    const form = document.getElementById('asfForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            const shortName = document.querySelector('input[name="short_name"]')?.value.trim();
            const fullName = document.querySelector('textarea[name="full_name"]')?.value.trim();

            if (!shortName || !fullName) {
                e.preventDefault();
                alert('❌ Заполните обязательные поля: Краткое и Полное наименование');
            }
        });
    }
}
