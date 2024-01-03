# semester-work-android

# Приложение для поиска и сохранения рецептов
____________________________________


# Что было сделано:


- мультимодульное приложение с разделением на 3 основных типа:
—app
—core
—feature

- 4 фичи: поиск рецептов по названию, список избранных рецептов, фича для работы с пользователем (авторизация и профиль), просмотр детальной информации о рецепте (фото, ингредиенты и шаги)

- данные берутся из api

- presentation layer:
—compose
—mvi
—coil

- di: dagger+hilt

- db: room

- network: retrofit+okhttp

- cicd: Github actions (но билд заканчивается с непонятной ошибкой, скорее всего что-то не то написала)

- юнит-тесты на use-case

- версионирование зависимостей через toml файл


# Функционал:


Внизу экрана 3 вкладки: поиск рецептов (начальный экран), список избранных и профиль.
При первом входе в приложение избранные рецепты и профиль будут доступны только авторизованным пользователям, войти в аккаунт или зарегистрироваться можно в профиле.

При поиске рецептов есть проверка на наличие интернета перед выполнением запроса в сеть.
После успешного выполнения запроса будет показан список рецептов, можно перейти на экран с детальной информацией о рецепте по клику на него. Там справа свеху будет показан значок сердца, но нажатию на который можно добавить/удалить рецепт из избранного, если пользователь не зарегистрирован - значка не будет.

Во второй вкладке находятся избранные рецепты.

В профиле указан username пользователя и две кнопки: выйти из аккаунта и удалить аккаунт.
Во время входа и регистрации есть все проверки на введенные данные и в зависимости от ошибок будут показаны соответствующие сообщения.

