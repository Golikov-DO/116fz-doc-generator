// webapp/js/portal.js

// Переключение вкладок
function showTab(tabName) {
    document.querySelectorAll('.tab-pane').forEach(tab => {
        tab.classList.remove('active');
    });
    document.querySelectorAll('.tab').forEach(tab => {
        tab.classList.remove('active');
    });

    document.getElementById(tabName).classList.add('active');
    event.target.classList.add('active');
}

// Удаление строки (общая функция)
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