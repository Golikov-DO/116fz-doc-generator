<%@ page contentType="text/html;charset=UTF-8"%>
<div id="loginModal" class="gh-modal">
    <div class="gh-modal-content">

        <div class="gh-modal-header">
            <h3 id="modalTitle">Вход</h3>
            <span class="gh-close" onclick="closeLoginModal()">×</span>
        </div>

        <!-- LOGIN -->
        <form id="authForm" method="post" action="login" class="gh-form">

            <label>Логин</label>
            <input type="hidden" name="source" value="register">
            <input type="text" name="login" required>

            <label>Пароль</label>
            <input type="password" name="password" autocomplete="current-password" required>

            <input type="hidden" name="role" value="USER">

            <button type="submit" class="gh-btn-primary" id="submitBtn">
                Войти
            </button>

        </form>

        <div class="gh-switch">
            <a href="#" id="switchLink" onclick="switchMode()">
                Нет аккаунта? Зарегистрироваться
            </a>
        </div>

    </div>
</div>