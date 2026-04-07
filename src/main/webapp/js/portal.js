// Раскрытие/сворачивание блоков
function toggleCollapse(header) {
    const content = header.nextElementSibling;
    if (!content) return;
    const arrow = header.querySelector('span:last-child');
    content.classList.toggle('expanded');
    arrow.textContent = content.classList.contains('expanded') ? '▲' : '▼';
    setTimeout(resizeAllTextareas, 0);
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

// удаление строки для таблиц
function deleteTableRow(el) {
    const row = el.closest('tr');
    const tbody = row.closest('tbody');

    row.remove();

    // перенумерация
    tbody.querySelectorAll('tr').forEach((tr, i) => {
        const num = tr.querySelector('input[type="number"]');
        if (num) num.value = i + 1;
    });
}

// удаление строки
function removeItem(el) {
    const item = el.closest('.asf-signer-item, .asf-work-type-item');
    if (!item) return;

    const idField = item.querySelector('[name$="_id[]"]');

    if (!idField || !idField.value) {
        item.remove();
        return;
    }

    item.style.display = 'none';
}

function autoResize(el) {
    el.style.height = "auto";
    el.style.overflow = "hidden";
    el.style.height = el.scrollHeight + "px";
}

function resizeAllTextareas() {
    document.querySelectorAll('textarea.auto-resize').forEach(el => {
        el.style.height = "auto";
        el.style.height = el.scrollHeight + "px";
    });
}

window.addEventListener('load', function () {
    resizeAllTextareas();
});

document.addEventListener('input', function(e) {
    if (e.target.matches('textarea.auto-resize')) {
        autoResize(e.target);
    }
});

function openLoginModal() {
    document.getElementById("loginModal").style.display = "flex";
}

function closeLoginModal() {
    document.getElementById("loginModal").style.display = "none";
}

let isLogin = true;

function switchMode() {
    isLogin = !isLogin;

    document.getElementById("loginForm").style.display = isLogin ? "block" : "none";
    document.getElementById("registerForm").style.display = isLogin ? "none" : "block";

    document.getElementById("modalTitle").innerText =
        isLogin ? "Вход" : "Регистрация";

    document.getElementById("switchLink").innerText =
        isLogin ? "Нет аккаунта? Зарегистрироваться"
            : "Уже есть аккаунт? Войти";
}