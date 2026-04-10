// Инициализация при загрузке
function initAsfForm() {
    // Валидация формы
    if (sessionStorage.getItem("openImagesBlock")) {

        const headers = document.querySelectorAll('.collapse-header');

        headers.forEach(header => {
            const text = header.textContent.toLowerCase();

            if (text.includes("прилож")) {
                const content = header.nextElementSibling;
                if (content && !content.classList.contains("expanded")) {
                    toggleCollapse(header);
                }
            }
        });

        sessionStorage.removeItem("openImagesBlock");
    }
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

// Добавление нового подписанта
function addSigner() {
    const container = document.getElementById('signersContainer');

    let newItem = document.createElement('div');
    newItem.className = 'form-grid-3-del asf-signer-item';

    newItem.innerHTML = `
    <input type="hidden" name="signer_id[]" value="">
        <div>
            <label class="form-label" for="signer_name[]">ФИО подписанта</label>
            <input type="text" name="signer_name[]" placeholder="Иванов И.И.">
        </div>
        <div>
            <label class="form-label" for="signer_position[]">Должность</label>
            <input type="text" name="signer_position[]" placeholder="Директор">
        </div>
        <div>
            <button type="button" class="btn-delete" onclick="removeItem(this)">✖</button>
        </div>
    `;
    container.appendChild(newItem);
}

// Добавление нового типа работ
function addWorkType() {
    const container = document.getElementById('workTypesContainer');

    let newItem = document.createElement('div');
    newItem.className = 'form-grid-2-del asf-work-type-item';

    newItem.innerHTML = `
    <input type="hidden" name="work_type_id[]" value="">
        <div>
            <label class="form-label" for="work_type_name[]">Наименование типа работ</label>
            <input type="text" name="work_type_name[]" placeholder="Газоспасательные работы">
        </div>
        <div>
            <button type="button" class="btn-delete" onclick="removeItem(this)">✖</button>
        </div>
    `;
    container.appendChild(newItem);
}

function closeModal() {
    const modal = document.getElementById('imageModal');
    if (modal) modal.style.display = 'none';
}

function uploadAsfImage(button, group, position, imageId) {
    const fileInput = button.nextElementSibling;
    fileInput.setAttribute('data-group', group);
    fileInput.setAttribute('data-position', position);
    fileInput.setAttribute('data-image-id', imageId);
    fileInput.click();
}

function handleAsfImageUpload(input, group, position, targetWidth, targetHeight) {
    if (!input.files || !input.files[0]) return;

    const file = input.files[0];
    const imageId = input.getAttribute('data-image-id');

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

    const formData = new FormData();
    const asfId = document.querySelector('input[name="asfId"]').value;

    formData.append('group', group);
    formData.append('position', position);
    formData.append('asfId', asfId);
    if (imageId && imageId !== '0') {
        formData.append('imageId', imageId);
    }

    const img = new Image();
    const reader = new FileReader();

    reader.onload = function(e) {
        img.src = typeof e.target.result === 'string' ? e.target.result : '';
        img.onload = function() {
            if (img.width === targetWidth && img.height === targetHeight) {
                formData.append('file', file);
                sendAsfImage(formData, group, position, file.name, input);
            } else {
                const canvas = document.createElement('canvas');
                canvas.width = targetWidth;
                canvas.height = targetHeight;
                const ctx = canvas.getContext('2d');
                ctx.drawImage(img, 0, 0, targetWidth, targetHeight);

                canvas.toBlob(function(blob) {
                    const resizedFile = new File([blob], file.name, {
                        type: 'image/png',
                        lastModified: Date.now()
                    });
                    formData.append('file', resizedFile);
                    sendAsfImage(formData, group, position, file.name, input);
                }, 'image/png', 1.0);
            }
        };
    };
    reader.readAsDataURL(file);
}

function sendAsfImage(formData, group, position, fileName, input) {
    fetch('upload-asf-image', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            // Обновляем imageId
            const imageIdInput = document.querySelector(`input[name="image_id_${group}_${position}"]`);
            if (imageIdInput && data['imageId']) {
                imageIdInput.value = data['imageId'];
            }

            // очищаем input
            if (input) {
                input.value = '';
            }

            // перезагрузка
            sessionStorage.setItem("openImagesBlock", "true");
            location.reload();
        })
        .catch(err => {
            alert('❌ Ошибка загрузки изображения');
            console.error(err);
        });
}

function addAsfImageField(group) {
    const container = document.getElementById(`appendix${group}_container`);
    const currentCount = container.querySelectorAll('.asf-image-item').length;
    const newPosition = currentCount + 1;

    const div = document.createElement('div');
    div.className = 'asf-image-item';
    div.style.cssText = 'border: 1px solid #eee; padding: 10px; background-color: #fafafa; border-radius: 4px;';

    div.innerHTML = `
        <input type="hidden" name="image_id_${group}_${newPosition}" value="">
        <input type="hidden" name="image_group_${group}_${newPosition}" value="${group}">
        <input type="hidden" name="image_name_${group}_${newPosition}" value="">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 5px;">
            <span style="font-size: 11px; color: #666;">Изображение ${newPosition}</span>
            <span class="delete-row" onclick="this.closest('.asf-image-item').remove()" style="color:#f44336; cursor:pointer;">✖</span>
        </div>
        <div style="margin-top: 8px; text-align: center;">
            <button type="button" class="btn btn-small" style="font-size: 11px; padding: 3px 8px;"
                    onclick="uploadAsfImage(this, '${group}', ${newPosition}, 0)">
                Загрузить
            </button>
            <input type="file" accept="image/png" style="display: none;"
                   onchange="handleAsfImageUpload(this, '${group}', ${newPosition}, 1047, 1480)">
        </div>
    `;

    container.appendChild(div);
}

function deleteAsfImage(id, element) {
    if (!confirm("Удалить изображение?")) return;

    fetch("/delete-asf-image?id=" + id, { method: "POST" })
        .then(() => {
            const imageDiv = element.closest('.asf-image-item');
            imageDiv.remove();
            // Перезагружаем страницу
            location.reload();
        })
        .catch(err => {
            alert("Error deleting Image");
            console.error(err);
        });
}

document.addEventListener("DOMContentLoaded", function () {
    initAsfForm();
    resizeAllTextareas();
});