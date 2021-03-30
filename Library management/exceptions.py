class BookAlreadyExistsError(Exception,BaseException):
    def __str__(self):
        return "Book already exists!"
class ClientAlreadyExistsError(Exception,BaseException):
    def __str__(self):
        return "Client already exists"
class BookAlreadyRentedOrDoesntExistError(Exception,BaseException):
    def __str__(self):
        return "Book already rented or doesn't exist"
class ClientToRentDoenstExist(Exception,BaseException):
    def __str__(self):
        return "Client that rents doesnt exist"
class IdOfClientDoesntExist(Exception,BaseException):
    def __str__(self):
        return "Id of client doesnt exist"
class IdOfBookDoesntExist(Exception,BaseException):
    def ___str__(self):
        return "Id of book doesnt exist"