document.addEventListener('DOMContentLoaded', function () {
    // сообщения исчезают сами через несколько секунд
    document.querySelectorAll('.alert').forEach(function (el) {
        setTimeout(function () { el.classList.add('hide'); }, 8000);
    });

    // форма с атрибутом data-confirm спрашивает подтверждение перед отправкой
    document.querySelectorAll('form[data-confirm]').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!window.confirm(form.dataset.confirm)) {
                event.preventDefault();
            }
        });
    });

    // защита от двойной отправки формы
    document.querySelectorAll('form').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (event.defaultPrevented) {
                return;
            }
            form.querySelectorAll('button[type=submit]').forEach(function (button) {
                setTimeout(function () { button.disabled = true; }, 0);
            });
        });
    });
});

// при возврате на страницу кнопки снова становятся активными
window.addEventListener('pageshow', function () {
    document.querySelectorAll('button[type=submit]').forEach(function (button) {
        button.disabled = false;
    });
});
