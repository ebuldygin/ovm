# Swing object-view mapping

Небольшая библиотека для быстрой разработки форм ввода простых параметров в Java Swing.
Проект содержит два модуля, для использования в своих приложениях потребуется модуль ovm-core.

Такая проблема зачастую возникает при создании небольших академических приложений с непродолжитльным
жизненным циклом, например для расчётных или моделирующих программ в рамках курсовых и лабораторных работ.
При реализации таких приложений написание кода для пользовательского интерфейса в Java Swing и решение 
сопутствующих проблем (не связанных с исследовательской задачей) может занять значительное время.
Разработанный фреймворк позволяет быстро установить соответствие полей класса и названий на форме 
в декларативном стиле. По созданному описанию можно с минимальными усилиями создать Swing панель 
с соответствующими полями, и собирать/устанавливать данные с/на созданной панели:
![Пример отображения](https://github.com/ebuldygin/ovm/blob/master/demo.png)

Класс, который необходимо поместить на форму помечается аннотацией ``@Form``, поля для отображения 
на форме (или методы чтения в соответствии с JavaBean) - аннотацией ``@Parameter``.
В аннотациях для параметров можно задавать название, порядок, конвертеры и название поля.
Конвертеры необходимо задавать для преобразование значения параметра в значение в виджете и обратно.
Если в аннотации ``@Parameter`` не прописан ни один конвертер, то используется любой подходящий 
конвертер по умолчанию:
* ``IdentityConverter`` - тождественный конвертер, если тип в виджете совпадает с типом параметра
* ``ValueOfConverter`` - если в виджете нужна строка, а в классе параметра есть метод ``static valueOf(String)``

Логические поля в классе могут объединяться в логические группы с помощью аннотации ``@LogicalGroup``, причём
каждое поле может быть объявлено только в одной группе. Объявление групп носит семантический характер, 
никаких средств для контроля значений в исключающих группах нет.

Поддерживаемые типы для Swing (из коробки):
* все простые типы и их оболочки 
* String
* все классы, где есть статический метод ``valueOf(String)`` и ``o.equals(o.valueOf(o.toString()))``
* классы, помеченные аннотацией ``@Form``

В механизме есть возможность для композиции и для наследования.

### Композиция

Если класс поля не помечен аннотацией ``@Form``, то для него должен быть
конвертер и соответствующий виджет. Это потребует расширения класса ``SwingBuilder``.
Если класс поля помечен аннотацией ``@Form``, то в Swing будет создана соответствующая панель с названием из 
``@Parameter``, а не ``@Form``

### Наследование

При наследовании все параметры (поля/методы доступа, помеченные аннотацией ``@Parameter``), объявленные 
в родительском классе (если помечен аннотацией ``@Form``) переходят к наследнику, за исключением полей, 
объявленных в аннотации ``@ExcludeInherited``.

### Получение виджета для поля.
После создания панели можно получить виджет для конкретного поля модели.
Поиск на форме осуществляется по имени параметра, если параметр вложенный, то необходимо указать путь
к этому параметру (имена параметров через точку).