// webapp/js/index.js

function openDocument(mode) {
    const select = document.querySelector('select[name="documentId"]');
    const selected = select.options[select.selectedIndex];

    const docId = select.value;
    const orgId = selected.getAttribute('data-org-id');

    if (!docId) {
        alert('Пожалуйста, выберите документ');
        return;
    }

    window.location.href = 'portal?mode=' + mode +
        '&docId=' + (docId || '') +
        '&orgId=' + (orgId || '');
}

function developPlan() {
    const select = document.querySelector('select[name="documentId"]');
    const selected = select.options[select.selectedIndex];

    const docId = select.value;

    if (!docId) {
        alert('Пожалуйста, выберите документ');
        return;
    }

    const btn = event.target;
    const originalText = btn.textContent;
    btn.textContent = '⏳ Разработка...';
    btn.disabled = true;

    fetch('generatePlan', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'documentId=' + docId
    })
        .then(response => {
            if (response.ok) {
                alert('✅ План успешно разработан!');
                location.reload();
            } else {
                alert('❌ Ошибка при разработке плана');
                btn.textContent = originalText;
                btn.disabled = false;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('❌ Ошибка при разработке плана');
            btn.textContent = originalText;
            btn.disabled = false;
        });
}

// Добавьте эту функцию в конец файла js/index.js

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