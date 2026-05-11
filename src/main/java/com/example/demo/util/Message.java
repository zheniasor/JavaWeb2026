package com.example.demo.util;

public enum Message {

    WELCOME("Добро пожаловать!", "Welcome!"),
    HELLO("Привет", "Hello"),
    LOGOUT("Выйти", "Logout"),

    LOGIN_TITLE("Вход в систему", "Login"),
    LOGIN_LABEL("Логин", "Login"),
    PASSWORD_LABEL("Пароль", "Password"),
    LOGIN_BUTTON("Войти", "Sign In"),
    REGISTER_LINK("Регистрация нового пользователя", "Register new user"),
    LOGIN_ERROR("Неверный логин или пароль", "Invalid login or password"),
    EMPTY_FIELDS("Заполните все поля", "Please fill all fields"),
    SHOW_PASSWORD("Показать пароль", "Show password"),

    REGISTER_TITLE("Регистрация", "Registration"),
    REGISTER_BUTTON("Зарегистрироваться", "Register"),
    EMAIL_LABEL("Email", "Email"),
    BACK_TO_LOGIN("На главную", "Back to login"),
    REGISTER_SUCCESS("На указанный email отправлено письмо с подтверждением. Пожалуйста, проверьте почту.",
            "Confirmation email has been sent to your email address. Please check your mail."),
    REGISTER_ERROR("Пользователь с таким логином или email уже существует", "User with this login or email already exists"),
    SERVER_REGISTER_ERROR("Регистрация не удалась из-за ошибки сервера", "Registration failed due to server error"),

    LOGIN_REQUIREMENTS("Логин должен содержать 3-20 латинских букв или цифр",
            "Login must contain 3-20 Latin letters or numbers"),
    LOGIN_EXAMPLE("Пример", "Example"),
    PASSWORD_REQUIREMENTS("Минимум 8 символов, хотя бы одна буква и одна цифра",
            "Minimum 8 characters, at least one letter and one number"),
    EMAIL_REQUIREMENTS("Действительный email адрес", "Valid email address"),

    CONFIRM_TITLE("Подтверждение регистрации", "Registration Confirmation"),
    CONFIRM_MESSAGE("После подтверждения вы сможете войти в систему", "You can log in after confirmation"),
    CONFIRM_SUCCESS("Электронная почта успешно подтверждена! Теперь вы можете войти в систему.",
            "Email confirmed successfully! You can now log in."),
    CONFIRM_ERROR("Недействительная или просроченная ссылка для подтверждения", "Invalid or expired confirmation link"),
    CONFIRM_SERVER_ERROR("Ошибка сервера во время подтверждения", "Server error during confirmation"),
    CONFIRM_INVALID_LINK("Неверная ссылка для подтверждения", "Invalid confirmation link"),

    EDIT_TITLE("Редактирование профиля", "Edit Profile"),
    EDIT_NEW_LOGIN("Новый логин", "New Login"),
    EDIT_NEW_EMAIL("Новый Email", "New Email"),
    EDIT_NEW_PASSWORD("Новый пароль (оставьте пустым, чтобы не менять)",
            "New password (leave blank to keep current)"),
    EDIT_AVATAR("Аватар (макс 5 МБ)", "Avatar (max 5MB)"),
    EDIT_UPDATE_BUTTON("Обновить профиль", "Update Profile"),
    EDIT_SUCCESS("Профиль успешно обновлен!", "Profile updated successfully!"),
    EDIT_NO_CHANGES("Изменений не обнаружено", "No changes detected"),
    EDIT_ERROR("Не удалось обновить профиль", "Failed to update profile"),
    EDIT_INVALID_LOGIN("Неверный логин или уже существует", "Invalid login or already exists"),
    EDIT_INVALID_EMAIL("Неверный email или уже существует", "Invalid email or already exists"),
    EDIT_INVALID_PASSWORD("Неверный пароль", "Invalid password"),
    EDIT_UNAUTHORIZED("Пожалуйста, сначала войдите в систему", "Please log in first"),
    EDIT_USER_NOT_FOUND("Пользователь не найден", "User not found"),

    AVATAR_UPLOAD_SUCCESS("Фото профиля успешно обновлено!", "Avatar uploaded successfully!"),
    AVATAR_UPLOAD_ERROR("Не удалось обновить фото профиля", "Failed to upload avatar"),
    AVATAR_ONLY_IMAGES("Разрешены только файлы изображений", "Only image files are allowed"),
    AVATAR_SIZE_ERROR("Размер файла не должен превышать 5 МБ", "File size must be less than 5MB"),
    AVATAR_NO_FILE("Файл не выбран", "No file selected"),

     ADMIN_TITLE("Панель администратора", "Admin Panel"),
    ADMIN_USER_MANAGEMENT("Управление пользователями", "User Management"),
    ADMIN_USER_LIST("Список пользователей", "User List"),
    ADMIN_ID("ID", "ID"),
    ADMIN_LOGIN("Логин", "Login"),
    ADMIN_EMAIL("Email", "Email"),
    ADMIN_CONFIRMED("Подтвержден", "Confirmed"),
    ADMIN_ROLE("Роль", "Role"),
    ADMIN_ACTIVE("Активен", "Active"),
    ADMIN_ACTIONS("Действия", "Actions"),
    ADMIN_DELETE("Удалить", "Delete"),
    ADMIN_DELETE_CONFIRM("Вы уверены, что хотите удалить этого пользователя?",
            "Are you sure you want to delete this user?"),
    ADMIN_DELETE_SUCCESS("Пользователь успешно удален", "User deleted successfully"),
    ADMIN_DELETE_FAILED("Не удалось удалить пользователя", "Failed to delete user"),
    ADMIN_ACCESS_DENIED("Доступ запрещен. Требуется учетная запись администратора.",
            "Access denied. Admin rights required."),
    ADMIN_UNAUTHORIZED("Пожалуйста, сначала войдите в систему", "Please log in first"),
    ADMIN_USER_ID_REQUIRED("Требуется идентификатор пользователя", "User ID required"),
    ADMIN_INVALID_USER_ID("Неверный идентификатор пользователя", "Invalid user ID"),
    ADMIN_CANNOT_DELETE_SELF("Не удается удалить вашу собственную учетную запись",
            "Cannot delete your own account"),
    ADMIN_BACK_TO_MAIN("Вернуться на главную", "Back to Main"),

    SERVER_ERROR("Ошибка сервера", "Server error"),
    UNAUTHORIZED("Пожалуйста, сначала войдите в систему", "Please log in first"),
    PAGE_NOT_FOUND("Страница не найдена", "Page not found"),
    ACCESS_DENIED("Доступ запрещен", "Access denied"),

    LANG_RUSSIAN("Русский", "Russian"),
    LANG_ENGLISH("Английский", "English"),

    ERROR_404_TITLE("404 - Страница не найдена", "404 - Page Not Found"),
    ERROR_404_MESSAGE("Запрошенная страница не существует", "The requested page does not exist"),
    ERROR_500_TITLE("500 - Внутренняя ошибка сервера", "500 - Internal Server Error"),
    ERROR_500_MESSAGE("Извините, на сервере произошла ошибка", "Sorry, something went wrong on the server"),
    ERROR_TYPE("Тип ошибки", "Error type"),
    ERROR_MESSAGE("Сообщение", "Message"),
    ERROR_DETAILS("Информация об ошибке", "Error details"),
    ERROR_CODE("Код ошибки", "Error code"),
    ERROR_TIME("Время", "Time"),
    UNKNOWN_ERROR("Неизвестная ошибка", "Unknown error"),

    BUTTON_BACK("← На главную", "← Back to Main"),
    BUTTON_GO_HOME("Вернуться на главную", "Go to Home"),
    BUTTON_CONFIRM("Подтвердить", "Confirm");

    private final String ru;
    private final String en;

    Message(String ru, String en) {
        this.ru = ru;
        this.en = en;
    }

    public String getRu() {
        return ru;
    }

    public String getEn() {
        return en;
    }

    public String get(String language) {
        if ("en".equals(language)) {
            return en;
        }
        return ru;
    }
}