@VerifyCrudActions
Feature: Verify REST CRUD app functionality
  As a REST CRUD app user
  I want to be able to connect to the API
  And work with the books in the app

  @VerifyGetAll
  Scenario: Read all books - Verify GET all books
    Given I execute GET request to get all books
    Then I assert the response code is 200
    And I assert the response contains a list of books
    And I assert the books count is more than 1

  @VerifyGetParticularBook
  Scenario: Read a book - Verify GET a particular book
    Given I execute GET request to get all books
    Then I assert the response code is 200
    And I assert the response contains a list of books
    And I execute GET request to get the first book from the books response
    Then I assert the response code is 200
    And I assert the response matches "get_particular_book_schema.json" schema

  @VerifyCreateNewBook
  Scenario Outline: Create a book - Verify POST for a new book
    Given I execute POST request to create a new book from the file "<file>"
    Then I assert the response code is 200
    And I assert the book details match the file "<file>"
    Examples:
      | file          |
      | new_book.json |

  @VerifyUpdateBook
  Scenario Outline: Update a book - PUT for an existing book
    Given I execute GET request to get all books
    Then I assert the response code is 200
    And I assert the response contains a list of books
    And I remember the ID of a random book
    And I execute PUT request to update the book by ID with data from the file "<file>"
    Then I assert the response code is 200
    And I assert the book details match the file "<file>"
    Examples:
      | file              |
      | updated_book.json |

  @VerifyDeleteBook
  Scenario: Delete a book - Verify DELETE an existing book
    Given I execute GET request to get all books
    Then I assert the response code is 200
    And I assert the response contains a list of books
    And I remember the ID of a random book
    And I execute DELETE request to remove the book by ID
    Then I assert the response code is 200
    And I execute GET request to get the book by ID
    Then I assert the response code is 404