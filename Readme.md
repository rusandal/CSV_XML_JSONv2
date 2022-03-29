# Конвертер CSV и XML в JSON + парсер JSON
## Описание
Программа выгружает данные из data.csv и data.xml в файл data.json.
Для работы приложения неоходимо подключить 3 библиотеки opencsv, json-simple и gson.

Вход:  
data.csv
```
1,John,Smith,USA,25
2,Inav,Petrov,RU,23
```
data.xml
```
<staff>
    <employee>
        <id>1</id>
        <firstName>John</firstName>
        <lastName>Smith</lastName>
        <country>USA</country>
        <age>25</age>
    </employee>
    <employee>
        <id>2</id>
        <firstName>Inav</firstName>
        <lastName>Petrov</lastName>
        <country>RU</country>
        <age>23</age>
    </employee>
</staff>
```
Выход:
new_data(from_csv).json
new_data(from_xml).json
```
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Smith",
    "country": "USA",
    "age": 25
  },
  {
    "id": 2,
    "firstName": "Inav",
    "lastName": "Petrov",
    "country": "RU",
    "age": 23
  }
]
```  
  
Парсинг JSON:  
После получения файла, он парсится и выводится в консоль
```
Employee{id=1, firstName='John', lastName='Smith', country='USA', age=25}
Employee{id=2, firstName='Inav', lastName='Petrov', country='RU', age=23}
```

Выполнение main():  
Прописываем содержание колонок в файле
```
String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
```
далее 
```
String fileName = "data.csv";
List<Employee> list = parseCSV(columnMapping, fileName);
String json = listToJson(list);
toFile("new_data(from_csv).json", json);
List<Employee> myList = parseXML("data.xml");
String json2 = listToJson(myList);
toFile("new_data(from_xml).json", json2);
String json3 = readString("new_data(from_csv).json");
List<Employee> list1 = jsonToList(json3);
for (Employee employee : list1) {
     System.out.println(employee);
}
```
