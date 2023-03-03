#language: en
@test-stash @data=$Data
Feature: Stash

  Background:
    * user is on the page "Main"
    * user clicks the button "Contact"
    * user is on the page "Contact"

  @stash @data=$Data{Admin}
  Scenario: Test Stash
    * stores the value "$Data{Admin.first name}" in a variable "TEMPLATE"
    * user checks that the field "first name" is empty
    # For testing stash and non critical in not defined step
    # * ? user filsdsdsdsls the field "first name" with value "#{NOT_FOUND}"
    * user fills the field "first name" with value "#{TEMPLATE}"
    * user checks in the element "first name" value "Alex"
    * user fills the field "first name" with value ""
    * user checks that the field "first name" is empty
    * user fills form
      | first name | #{TEMPLATE} |
    * user checks in the element "first name" value "Alex"
    * user fills the field "first name" with value ""
    * user checks that the field "first name" is empty
    * stores the value "Al" in a variable "PART"
    * user fills the field "first name"
    """
    #{PART}ex
    """
    * user checks that the field "first name" is not empty
    * user checks in the element "first name" value "Alex"

  @stash-formatter @data=$Data{Admin}
  Scenario: Test formatter for stash
    * stores the value "$Data{Admin.first name}" in a variable "TEMPLATE"
    * stores the value "first name" in a variable "NAME"
    * stores the value "first name" in a variable "LONG FIRST NAME"
    * user checks that the field "#{NAME}" is empty
    * user fills the field "#{LONG FIRST NAME}" with value "#{TEMPLATE}"
    * user checks in the element "#{LONG FIRST NAME}" value "Alex"
    * user checks in the element "#{NAME}" value "Alex"
    * user fills the field "#{NAME}" with value ""
    * user fills form
      | #{NAME} | #{TEMPLATE} |

  @stores-from-field
  Scenario: Check stores element value from the field
    * user fills the field "first name" with value "Alex"
    * user stores the value of the field "first name" in the variable "NAME"
    * user fills the field "first name" with value ""
    * user checks that the field "first name" is empty
    * user fills the field "first name" with value "#{NAME}"
