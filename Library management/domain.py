class Book():
    def __init__(self,bookId,title,author):
        self.author = author
        self.title = title
        self.bookId = bookId
        self.isRented=False
    def getBookId(self):
        return self.bookId
    def getBookName(self):
        return self.title
    def getBookAuthor(self):
        return self.author
    def bookToString(self):
        output=""
        output+="ID: "+str(self.bookId)+"\nTitle: "+str(self.title)+"\nAuthor: "+str(self.author)+"\n"
        return output
    def loadBook(self):
        return str(self.getBookId())+","+str(self.getBookName())+","+str(self.getBookAuthor())
class Client():
    def __init__(self,cliendId,name):
        self.clientId=cliendId
        self.name=name
    def getClientId(self):
        return self.clientId
    def getClientName(self):
        return self.name
    def clientToString(self):
        output=""
        output+= "ID: "+str(self.clientId)+"\nName:"+str(self.name)+"\n"
        return output
    def loadClient(self):
        return str(self.getClientId())+","+str(self.getClientName())

class Rental():
    def __init__(self,rentalId,bookId,clientId,rentedDate,returnDate):
        self.returnDate = returnDate
        self.rentedDate = rentedDate
        self.clientId = clientId
        self.bookId = bookId
        self.rentalId = rentalId
    def getClientId(self):
        return self.clientId
    def getBookId(self):
        return self.bookId
    def setReturnDate(self,date):
        self.returnDate=date
    def printRental(self):
        return "ID: "+str(self.bookId)+"\nClientID: "+str(self.clientId)+"\nRentalID: "+str(self.rentalId)+"\nRent Date: "+\
               str(self.rentedDate)+"\nReturn Date: "+str(self.returnDate)+"\n"

    def getReturnDate(self):
        return self.returnDate
    def getRentDate(self):
        return self.rentedDate
    def returnStateOfRental(self):
        return self.returnDate
    def getIdOfRentedBook(self):
        return self.bookId
    def loadRental(self):
        return str(self.getIdOfRentedBook())+","+str(self.getBookId())+","+str(self.getClientId())+","+str(self.getRentDate())+","+str(self.getReturnDate())

class Undo():
    def __init__(self,function,extraParameters):
        self.function=function
        self.extraParameters=extraParameters
    def getFuntion(self):
        return self.function
    def getParameters(self):
        return self.extraParameters
    def getParametersUpdate(self):
        for iterator in range(1,len(self.extraParameters)):
            if self.extraParameters[iterator] == "undo":
                break
        return self.extraParameters[iterator:]