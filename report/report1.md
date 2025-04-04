# Разработка Web-сервиса для кадрового агентства

## Навигация страниц

![pages](/report/page_navigation.png "Навигация страниц")

## Описание страниц
> На каждой странице вверху находятся кнопки перехода на главную страницу, поиска ваканский, поиска резюме, профиля и страницу авторизации

### Главная страница

На главной страницуе находятся:
- Краткое описание сервиса
- Последние новости

### Авторизация

На странице находятся поля для заполнения:
- email
- пароль
А также кнопка для запуска процесса авторизации. При успехе - автоматический переход на страницу профиля.

### Регистрация

На странице находятся поля для заполнения:
- email
- пароль
- ФИО
- дата рождения
- выбор, соискатель или наниматель человек, который хочет зарегистрироваться

Если возраст < 18, то процесс регистрации прерывется, появляется предупреждение о неправильном возрасте, и человек проходит регистрацию заново.
При успешной регистрации - автоматический переход на страницу профиля.

### Профиль

Разделы для соискателя:

    Профиль:
    - Просмотр и редактирование личных данных (поля для ввода новых данных (имя, возраст, город, контакты) и кнопка для сохранения)

    Резюме:
    - Возможность создать, редактировать, удалять и просматривать своё резюме (список составленных резюме, кнопки для добавления, редактирования, удаления и просмотра (переброс на страницу резюме) резюме)
    
    Образование и опыт:
    - Разделы для добавления информации об образовании и опыте работы (поля для ввода информации про образование: институт, специализация и год окончания - и про опыт работы: компания, года начала и конца работы, зарплата и должность). Можно добавлять несколько записей об обраховании и опыте работы

    Отклики:
    - Список откликов со статусом (Pending, Reviewed, Accepted)
    - Показанная информация - дожность, название компании, статус

Разделы для работодателя:

    Профиль компании:
    - Название компании, контакты с возможностью редактирования (соответствующие поля ввода) + отдельный выбор, ищет ли человек работу

    Вакансии:
    - Список созданных вакансий
    - Возможность добавить новую вакансию, редактировать или удалить существующую (кнопки для добавления, редактирования, удаления и просмотра (переброс на страницу вакансии) вакансии)
    - В списке вакансий показана информация: должность, зарплата
    
    Отклики от соискателей:
    - Список откликов по каждой вакансии с возможностью изменения статуса заявки (Pending, Reviewed, Accepted) (выпадающий список статусов для изменения)
    - Показанная информация - должность, имя соискателя, который откликнулся, статус


### Добавить вакансию

На странице добавления вакансии находятся поля для заполнения:

- должность
- предположительная зарплата
- требования в виде обычного текста

Также кнопка для сохранения(размещения) вакансии на сайте

### Добавить резюме

На странице добавления резюме находятся поля для заполнения:

- желаемая должность
- желаемая зарплата
- способности в виде обычного текста

Также кнопка для сохранения(размещения) резюме на сайте

### Поиск вакансий

Для фильтров используются выпадающие списки, в которых варианты - те, которые были указаны работодателями при создании вакансий.

Фильтры есть для: имя компании, должность

Для зарплаты используется боксы с указанием минимальной и максимальной зарплаты

После указания фильтров нажимается кнопка поиска

Далее отображается список вакансий сверху вниз с гиперссылкой, зарплатой и названием должности

### Вакансия

На странице вакансии показаны все критерии: зарплата, должность, а также дополнительные требования, кнопка отклика на вакансию, название компании.
Также соискатели на этой странице могут нажать кнопку, чтобы добавить отклик ("откликнуться") на данную вакансию

### Поиск резюме

Для фильтров используются выпадающие списки, в которых варианты - те, которые были указаны соискателями при создании резюме.

Фильтры есть для: название института, специализация, должность, название компании (по опыту работы)

Для зарплаты используется боксы с указанием минимальной и максимальной зарплаты

После указания фильтров нажимается кнопка поиска (ищутся только те резюме, чьи соискатели отметили, что ищут работу)

### Резюме

На странице резюме показаны все желаемые критерии: должность, зарплата, а также навыки, которые прописал пользователь, краткая сводка об образовании и опыте работы

## Сценарии использования

1. Поиск резюме по образованию, предыдущим местам работы, должностям или зарплатам

        Главная страница -> Поиск резюме
        Выставление фильтров (институт, специализация, должность, зарплата, название компании предыдущей работы)
        Кнопка поиска
        В полученных резюме выбираем нужное -> страница с резюме

2. Поиск вакансий по компаниям, должностям, зарплатам

        Главная страница -> Поиск вакансий.
        Выставление фильтров (название компании, должность, зарплата).
        Кнопка поиска.
        В полученных вакансиях выбираем нужное -> страница с вакансией

3. Получение истории работы для данного человека

        Для соискателя:

        Главная страница -> Профиль
        Найти раздел про опыт
        Просмотреть прошлые добавленные записи

        Для работодателя:

        Главная страница -> Поиск резюме
        Выставление фильтров под конкретного человека
        В полученных резюме выбираем нужное -> страница с резюме
        Посмотреть про опыт работы

4. Поиск подходящих вакансий на резюме и подходящих резюме на вакансию

        Для соискателя:

        Главная страница -> Поиск работы
        Выставление фильтров по резюме
        В полученных вакансиях выбираем нужное -> страница с вакансией
        Промотреть и нажать кнопку отклика

        Для работодателя:

        Главная страница -> Поиск резюме
        Выставление фильтров по вакансии
        В полученных резюме выбираем нужное -> страница с резюме

5. Добавление и удаление данных о человеке, чтение и редактирование данных о нем, добавление данных о новом трудоустройстве

        Главная страница -> Профиль
        - Редактирование: выбор записи, заполнение полей, кнопка сохранить
        - Добавление: выбор нужного раздела, заполнение полей, кнопка добавить
        - Удаление: выбор резюме, нажать кнопку удалить

6. Добавление и удаление компании, чтение и редактирование данных о них, добавление, удаление и редактирование вакансий

        Главная страница -> Профиль
        - Редактирование: заполнение полей профиля, кнопка сохранить; вакансии - выбрать из списка, заполнение полей, кнопка сохранить
        - Добавление: заполнение полей профиля, кнопка сохранить; вакансии - перейти на страницу добавления вакансии, заполнение полей, кнопка добавить
        - Просмотр откликов: найти на странице информацию

7. Отклик на вакансию

        Главная страница -> Поиск вакансий
        Выствление фильтров по резюме (желаемые зарплата, должность, например)
        В полученных вакансиях выбираем нужное -> страница с вакансией
        Нажатие кнопки отклика

## Схема базы данных

![db](/db/web_db.png "Схема базы данных")