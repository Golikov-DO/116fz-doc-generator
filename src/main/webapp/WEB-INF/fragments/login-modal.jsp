<%@ page contentType="text/html;charset=UTF-8"%>
<div id="loginModal" class="gh-modal">
    <div class="gh-modal-content">

        <div class="gh-modal-header">
            <h3 id="modalTitle">Вход</h3>
            <span class="gh-close" onclick="closeLoginModal()">×</span>
        </div>

        <!-- LOGIN -->
        <form method="post" action="login" class="gh-form">
            <label>Логин</label>
            <input type="text" name="login" autocomplete="username" required>
            <label>Пароль</label>
            <input type="password" name="password" autocomplete="current-password" required>
            <button type="submit" class="gh-btn-primary">
                Войти
            </button>

        </form>

        <!-- REGISTER -->
        <form id="registerForm" class="gh-form" style="display:none;">
            <label>Логин</label>
            <input type="text" required>

            <label>Пароль</label>
            <input type="password" required>

            <button type="submit" class="gh-btn-primary">Зарегистрироваться</button>
        </form>

        <div class="gh-switch">
            <a href="#" id="switchLink" onclick="switchMode()">
                Нет аккаунта? Зарегистрироваться
            </a>
        </div>

    </div>
</div>