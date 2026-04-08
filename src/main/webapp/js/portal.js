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

    const params = new URLSearchParams(window.location.search);

    if (params.get("login") === "true") {
        openLoginModal();
    }

    if (params.get("error") === "1") {
        const msg = document.getElementById("loginCheck");
        if (msg) {
            msg.innerText = "❌ неверный логин или пароль";
            msg.style.color = "red";
        }
    }
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
let loginAvailable = false;

function switchMode() {
    isLogin = !isLogin;

    const form = document.getElementById("authForm");
    const title = document.getElementById("modalTitle");
    const btn = document.getElementById("submitBtn");
    const link = document.getElementById("switchLink");
    const msg = document.getElementById("loginCheck");
    const sourceInput = document.querySelector('input[name="source"]');

    if (msg) msg.innerText = "";

    const loginInput = document.querySelector('[name="login"]');
    if (loginInput) loginInput.value = "";

    if (isLogin) {
        form.action = "login";
        title.innerText = "Вход";
        btn.innerText = "Войти";
        link.innerText = "Нет аккаунта? Зарегистрироваться";
        sourceInput.value = "login";

        btn.disabled = false;
        loginAvailable = false;
        msg.innerText = "";

    } else {
        form.action = "saveUser";
        title.innerText = "Регистрация";
        btn.innerText = "Зарегистрироваться";
        link.innerText = "Уже есть аккаунт? Войти";
        sourceInput.value = "register";

        btn.disabled = true;
        loginAvailable = false;
    }
}

document.addEventListener('input', function(e) {

    if (e.target.name === 'login' && !isLogin) {

        const login = e.target.value;
        const btn = document.getElementById('submitBtn');
        const msg = document.getElementById('loginCheck');

        if (login.length < 3) {
            loginAvailable = false;
            msg.innerText = "";
            btn.disabled = true;
            return;
        }

        fetch('checkLogin?login=' + encodeURIComponent(login))
            .then(r => {
                if (!r.ok) throw new Error();
                return r.text();
            })
            .then(result => {

                if (result === 'free') {
                    loginAvailable = true;
                    msg.innerText = "✅ свободен";
                    msg.style.color = "green";
                    btn.disabled = false;
                } else {
                    loginAvailable = false;
                    msg.innerText = "❌ занят";
                    msg.style.color = "red";
                    btn.disabled = true;
                }
            })
            .catch(() => {
                loginAvailable = false;
                msg.innerText = "⚠ ошибка";
                msg.style.color = "orange";
                btn.disabled = true;
            });
    }
});

document.getElementById("authForm").addEventListener("submit", function(e) {

    if (!isLogin && !loginAvailable) {
        e.preventDefault();
    }
});