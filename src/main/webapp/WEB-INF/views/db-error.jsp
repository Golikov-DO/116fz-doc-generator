<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Ошибка подключения</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }

        .card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            text-align: center;
        }

        h1 {
            color: #e74c3c;
        }

        p {
            color: #555;
        }
    </style>
</head>
<body>
<div class="card">
    <p>База данных недоступна</p>
    <p>Проверьте, запущен ли PostgreSQL / Docker</p>
    <p><b>После запуска перезапустите приложение</b></p>
</div>
</body>
</html>