from exceptions import BookAlreadyExistsError,ClientAlreadyExistsError,BookAlreadyRentedOrDoesntExistError
from domain import Book,Client,Rental
import pickle
from service import *
import datetime
class Repo():
    listOfBooks = []
    listOfClients = []
    listOfRentedBooks = []
    mostRentedBooks = {}
    mostRentedAuthor = {}
    mostActiveClients = {}
    listOfOperations=[]
    indexOfOperations=[-1]
    idOfRent=1
    canRedo=[False]
    def __init__(self):
        self.mostRentedBooks={}
        self.mostRentedAuthor={}
        self.mostActiveClients={}

    def readInputMethod(self):
        f=open("settings.properties.txt","r")
        inputMethod=""
        inputMethodBooks=""
        inputMethodClients=""
        inputMethodRentals=""

        numberOfLines=f.readlines()
        for line in numberOfLines:
            if "repository" in line:
                list=line.split("=")
                inputMethod+=list[1]
                inputMethod= inputMethod.strip()
            if "books" in line:
                list=line.split("=")
                inputMethodBooks+=list[1]
                inputMethodBooks=inputMethodBooks.strip()
            if "clients" in line:
                list = line.split("=")
                inputMethodClients+= list[1]
                inputMethodClients=inputMethodClients.strip()
            if "rentals" in line:
                list = line.split("=")
                inputMethodRentals += list[1]
                inputMethodRentals=inputMethodRentals.strip()
        if inputMethod == "inmemory":
            self.initFromMemory()
        if inputMethod == "textfile":
            self.initFromText(inputMethodBooks,inputMethodClients,inputMethodRentals)
        if inputMethod == "pickle":
            self.initFromPickle(inputMethodBooks,inputMethodClients,inputMethodRentals)

    def initFromPickle(self,inputMethodBooks,inputMethodClients,inputMethodRentals):
        fileBooks=open(inputMethodBooks,"rb")
        fileClients=open(inputMethodClients,"rb")
        fileRentals=open(inputMethodRentals,"rb")
        booksData=fileBooks.read()
        clientsData=fileClients.read()
        rentalsData=fileRentals.read()
        self.listOfBooks.extend(pickle.loads(booksData))
        self.listOfClients.extend(pickle.loads(clientsData))
        self.listOfRentedBooks.extend(pickle.loads(rentalsData))

    def loadIntoPickle(self):
        fileForBooks = ""
        fileForClients = ""
        fileForRentals = ""
        f = open("settings.properties.txt", "r")
        numberOfLines = f.readlines()
        if "pickle" in numberOfLines[0]:
            for line in numberOfLines:
                if "pbooks" in line:
                    list = line.split("=")
                    fileForBooks += list[1]
                    fileForBooks = fileForBooks.strip()
                if "pclients" in line:
                    list = line.split("=")
                    fileForClients += list[1]
                    fileForClients = fileForClients.strip()
                if "prentals" in line:
                    list = line.split("=")
                    fileForRentals += list[1]
                    fileForRentals = fileForRentals.strip()
            booksHandler=open(fileForBooks,"wb")
            clientsHandler=open(fileForClients,"wb")
            rentalsHandler=open(fileForRentals,"wb")
            pickle.dump(self.listOfBooks,booksHandler)
            pickle.dump(self.listOfClients,clientsHandler)
            pickle.dump(self.listOfRentedBooks,rentalsHandler)

    def initFromMemory(self):
        self.listOfBooks.extend([Book("1", "B", "E"), Book("2", "E", "x"), Book("5", "po", "k"), Book("4", "T", "J"),
                       Book("3", "z", "E"), Book("7", "Iz", "Pzz")])
        self.listOfClients.extend( [Client("1", "jo"), Client("2", "Richy"), Client("3", "Iu"), Client("4", "Jaz"),
                         Client("5", "Qwe")])
        self.listOfRentedBooks.extend([Rental("1", "1", "1", datetime.datetime(2012, 12, 5), datetime.datetime(2012, 12, 10)),
                             Rental("2", "1", "4", datetime.datetime(2012, 12, 22), datetime.datetime(2013, 1, 22)),
                             Rental("3", "1", "2", datetime.datetime(2012, 12, 29), datetime.datetime(2013, 12, 10)),
                             Rental("3", "2", "5", datetime.datetime(2010, 9, 20), datetime.datetime(2011, 1, 1))])
        self.mostRentedBooks = {}
        self.mostRentedAuthor = {}
        self.mostActiveClients = {}

    def stringToDate(self,dateAsString):
        dateAsString=dateAsString.split(" ")
        dateAsString=str(dateAsString[0])
        dateAsString=dateAsString.split("-")
        dateAsString=datetime.datetime(int(dateAsString[0]),int(dateAsString[1]),int(dateAsString[2]))
        return dateAsString

    def initFromText(self,fileForBooks,fileForClients,fileForRentals):
        fBooks=open(fileForBooks,"r")
        fClients=open(fileForClients,"r")
        fRentals=open(fileForRentals,"r")
        linesBook=fBooks.readlines()
        linesClient=fClients.readlines()
        linesRental=fRentals.readlines()
        for lines in linesBook:
            list = lines.split(",")
            newBook=Book(list[0],list[1],list[2])
            self.addTheBookToTheRepo(newBook)
        for lines in linesClient:
            list=lines.split(",")
            newClient=Client(list[0],list[1])
            self.addTheClientToTheRepo(newClient)
        for lines in linesRental:
            list=lines.split(",")
            list[3]=self.stringToDate(list[3])
            if list[4] !="notReturned":
                list[4]=self.stringToDate(list[4])
            newRental=Rental(list[0],list[1],list[2],list[3],list[4])
            self.insertIntoRentals(newRental)

    def loadIntoFiles(self):
        fileForBooks=""
        fileForClients=""
        fileForRentals=""
        f=open("settings.properties.txt","r")
        numberOfLines = f.readlines()
        if "textfile" in numberOfLines[0]:
            for line in numberOfLines:
                if "books" in line:
                    list = line.split("=")
                    fileForBooks += list[1]
                    fileForBooks = fileForBooks.strip()
                if "clients" in line:
                    list = line.split("=")
                    fileForClients += list[1]
                    fileForClients = fileForClients.strip()
                if "rentals" in line:
                    list = line.split("=")
                    fileForRentals += list[1]
                    fileForRentals = fileForRentals.strip()
            fBooks = open(fileForBooks, "w")
            fClients = open(fileForClients, "w")
            fRentals = open(fileForRentals, "w")
            for books in self.listOfBooks:
                fBooks.write(books.loadBook())
            for clients in self.listOfClients:
                fClients.write(clients.loadClient())
            for rentals in self.listOfRentedBooks:
                fRentals.write(rentals.loadRental())



    def getListOfBooks(self):
        return self.listOfBooks
    def clearListOfBooks(self):
        self.listOfBooks.clear()
    def getListOfClients(self):
        return self.listOfClients
    def clearListOfClients(self):
        self.listOfClients.clear()
    def addTheClientToTheRepo(self,newClient):
        self.listOfClients.append(newClient)
    def addTheBookToTheRepo(self,newBook):
        self.listOfBooks.append(newBook)
    def removeBookFromPosition(self,position):
        self.listOfBooks.pop(position)
    def removeClientFromPosition(self,position):
        self.listOfClients.pop(position)
    def insertClientIntoPosition(self,position,newClient):
        self.listOfClients.insert(position,newClient)
    def insertBookIntoPosition(self,position,newBook):
        self.listOfBooks.insert(position,newBook)
    def insertIntoRentals(self,newRental):
        self.listOfRentedBooks.append(newRental)
    def getListOfRentals(self):
        return self.listOfRentedBooks
    def getListOfMostRentedBooks(self):
        return self.mostRentedBooks
    def addToMostRentedBooks(self,newBook):
        self.mostRentedBooks.append([Rental.getBookId(newBook),1])
    def getListOfMostRentedAuthors(self):
        return self.mostRentedAuthor
    def addToMostRentedAuthor(self,idOfBook):
        for books in self.listOfBooks:
            if Book.getBookId(books) == idOfBook:
                self.mostRentedAuthor[Book.getBookName(books)]=1

    def getListOfMostActiveClients(self):
        return self.mostActiveClients
    def addToMostActiveClients(self,client,total):
        self.mostActiveClients[Client.getClientName(client)]=total
    def addToListOfOperations(self,undoOperation):
        self.indexOfOperations[0] += 1
        self.listOfOperations.append(undoOperation)
        self.canRedo[0]=False
    def decrementIndex(self):
        self.indexOfOperations[0]-=1
    def incrementIndex(self):
        self.indexOfOperations[0]+=1
    def getListOfOperations(self):
        return self.listOfOperations
    def clearListOfOperations(self):
        self.listOfOperations.clear()
        self.indexOfOperations[0]=-1
    def getIndexOfOperations(self):
        return self.indexOfOperations[0]
    def getRedoState(self):
        return self.canRedo[0]
    def setRedoFalse(self):
        self.canRedo[0]=False
    def setRedoTrue(self):
        self.canRedo[0]=True