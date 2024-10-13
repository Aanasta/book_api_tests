# Books API Test Framework

Application under test: Book API (http://77.102.250.113:17354/api/v1/books/)

The framework contains four test scenarios covering a CRUD actions flow:
1. Get all books
2. Get one book
3. Create a new book
4. Update a book
5. Delete a book

Use the following command to run the test in console:
`mvn clean test -Dcucumber.options="--tags @VerifyCrudActions" `

Tools used:
- Java 11
- Maven
- Cucumber
- REST Assured
- TestNG
