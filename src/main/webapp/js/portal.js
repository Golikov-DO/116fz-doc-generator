
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
    if (!content) return;
    const arrow = header.querySelector('span:last-child');
    content.classList.toggle('expanded');
    arrow.textContent = content.classList.contains('expanded') ? '▲' : '▼';
}

function goBack() {
    window.history.back();
}

function createNewObject() {

    const urlParams = new URLSearchParams(window.location.search);
    const orgId = urlParams.get("orgId");

    if (!orgId) {
        alert("Сначала выберите организацию");
        return;
    }

    const form = document.createElement("form");

    form.method = "POST";
    form.action = "createEmptyObject";

    const input = document.createElement("input");
    input.type = "hidden";
    input.name = "orgId";
    input.value = orgId;

    form.appendChild(input);
    document.body.appendChild(form);

    form.submit();
}