
function openDocument(mode) {
    const select = document.querySelector('select[name="orgId"]');  // ИЗМЕНЕНО: name="orgId"
    if (!select || select.selectedIndex < 0) {
        alert('Пожалуйста, выберите организацию');
        return;
    }

    const selected = select.options[select.selectedIndex];
    const orgId = select.value;  // ИЗМЕНЕНО: теперь orgId в value
    const objId = selected.getAttribute('data-obj-id');  // для возможного использования

    if (!orgId) {
        alert('Пожалуйста, выберите организацию');
        return;
    }

    // ИЗМЕНЕНО: убрали docId, оставили только orgId
    window.location.href = 'portal?mode=' + mode + '&orgId=' + orgId;
}

function developPlan() {
    const select = document.querySelector('select[name="orgId"]');  // ИЗМЕНЕНО: name="orgId"
    if (!select || select.selectedIndex < 0) {
        alert('Пожалуйста, выберите организацию');
        return;
    }

    const orgId = select.value;  // ИЗМЕНЕНО: берем orgId

    if (!orgId) {
        alert('Пожалуйста, выберите организацию');
        return;
    }

    const btn = event.target;
    const originalText = btn.textContent;
    btn.textContent = '⏳ Разработка...';
    btn.disabled = true;

    // ИЗМЕНЕНО: передаем orgId вместо documentId
    fetch('generatePlan', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'orgId=' + orgId  // ИЗМЕНЕНО: documentId → orgId
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            }
            throw new Error('Ошибка сервера');
        })
        .then(data => {
            alert('✅ ' + data);
            location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Ошибка при разработке плана');
            btn.textContent = originalText;
            btn.disabled = false;
        });
}

function copyToClipboard(text) {
    // Создаем временный элемент
    const textarea = document.createElement('textarea');
    textarea.value = text;
    textarea.style.position = 'fixed';
    textarea.style.opacity = '0';
    document.body.appendChild(textarea);

    // Выделяем и копируем
    textarea.select();
    textarea.setSelectionRange(0, 99999);
    document.execCommand('copy');

    // Удаляем временный элемент
    document.body.removeChild(textarea);

    // Показываем уведомление
    alert('Скопировано: ' + text);
}