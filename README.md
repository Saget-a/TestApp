

username -> создание продукта -> (микросервис продукта (username)) -> (микросервис логина(username -> ID)) 

username = username (не показывать товар)

## Пользователь

- [ ] Добавить к пользователю следующие поля:
  - `email`
  - `phone-number`
  - `name`
- [ ] Добавить возможность входа через `email`

## База данных

- [x] ID добавляются во всех таблицах по умолчанию
- [x] Фото хранятся в папках, в БД — только ссылки
- [x] Названия фото генерировать как UUID

## Микросервисы я досихпор не панимать ( ПАМАГИТИ ) 

- [x] Сервис аутентификации — отвечает за регистрацию и вход пользователей.

  Микросервис может предоставлять API для регистрации нового пользователя, входа, восстановления пароля и так далее.

- [ ] Сервис управления товарами — отвечает за управление товарами в каталоге, добавление новых товаров, редактирование и удаление существующих товаров.

  Микросервис может предоставить API для добавления товара, получения информации о товаре и т. д.

- [ ] Сервис заказов — обрабатывает заказы от пользователей, управляет состоянием заказов (новый, в обработке, отправлен).

  Микросервис может предложить API для создания нового заказа, получения состояния заказа, его обновления и завершения.

### Реализация микросервисов
- Сервис аутентификации:

Вход: /login — POST-запрос с данными пользователя (имя пользователя и пароль).

Регистрация: /register — POST-запрос с данными для нового пользователя.

- Сервис управления товарами:

Получение списка товаров: /products — GET-запрос.

Добавление товара: /products — POST-запрос с деталями нового товара.

- Сервис заказов:

Создание нового заказа: /orders — POST-запрос с данными о заказе (пользователь, товары, адрес доставки).

Получение статуса заказа: /orders/{orderId} — GET-запрос с ID заказа.
### use case

  додавання продукту 
  - 
  підписка

  - товар закреплён за юзером 
  - товар продающийся юзером у него не отображается 
  - 
product ( 
  - ID 
  - seller ID
  - product name
  - amount
  - price
  - product description
)