# kotlex

**kotlex** - это генератор лексических анализаторов на языке Kotlin.


## Формат файла с описанием

Пример файла с описанием: [тык](https://github.com/sttie/kotlex/blob/master/examples/simple_ids.kotlex)

Сгенерированный по этому файлу лексер: [тык](https://github.com/sttie/kotlex/blob/master/examples/SimpleLexer.kt)

\
Весь файл с описанием генерируемого лексического анализатора имеет 4 раздела:

1. Раздел с импортами. Здесь в каждой новой строке необходимо указать те файлы, которые вы хотите импортировать в итоговый лексер. Например:

```
import java.lang.StringBuilder
import java.util.Stack
import my.cool.library
```

2. Раздел с типом токенов. Этот раздел состоит лишь из одного объявления вида ```enumtype is path```, где path - путь к файлу с классом, который вы хотите установить в качестве типа токенов. Наример:

```
import MyLangTokenType

enumtype is MyLangTokenType
```

3. Раздел с данными. Этот раздел, начинающийся с ключевого слова ```data```, будет полностью скопирован в список полей сгенерированного лексера. Например:

```
data:
var counter: Int = 0
var id: String = "MyLexer"
```

4. Раздел с правилами. В этом разделе, начинающемся с ключевого слова ```rules```, определяются регулярные 
   выражения, на основе которых будет работать сгенерированный лексер. Например:

```
rules:
newline    := "\n", MyLangTokenType.NEWLINE
lbracket   := "(", MyLangTokenType.LBRACKET
rbracket   := ")", MyLangTokenType.RBRACKET
while      := "while", MyLangTokenType.WHILE
assign     := "=", MyLangTokenType.ASSIGN
plus       := "+", MyLangTokenType.PLUS
minus      := "-", MyLangTokenType.MINUS
identifier := [a-zA-Z_][0-9a-zA-Z_]*, MyLangTokenType.ID
```

Расширение файла с описанием - **.kotlex**.

За полными примерами kotlex-файлов см. директорию examples.


## Запуск

```java -jar kotlex.jar path/to/kotlex/file path/to/lexer```

Здесь ```path/to/kotlex/file``` - путь к файлу с описанием лексера, ```path/to/lexer``` - путь к файлу,
в котором вы хотите получить сгенерированный лексер.

## Использование сгенерированного лексического анализатора

В публичном API сгенерированного лексера есть две функции - reset() и getToken(). Их назначение легко понять на примере:

```
val text = "..."
val lexer = Kotlex()
lexer.reset(text)

var token = lexer.getToken()
while (token != null) {
    println("Получен новый токен: $token")
    token = lexer.getToken()
}
```