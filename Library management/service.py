from domain import *
from repository import Repo
from exceptions import *
import datetime

def validateABook(newBook):
    # checks if a book is already registred
    # input - instance of a book
    bookCaller = Book
    repoCaller = Repo()
    listOfBooks = repoCaller.getListOfBooks()
    for iterator in listOfBooks:
        if bookCaller.getBookId(iterator) == bookCaller.getBookId(newBook):
            raise BookAlreadyExistsError()

def validateAClient(newClient):
    # checks if a client is already registred
    # input - instance of a client
    clientCaller = Client
    repoCaller = Repo()
    listOfClients = repoCaller.getListOfClients()
    for iterator in listOfClients:
        if clientCaller.getClientId(iterator) == clientCaller.getClientId(newClient):
            raise ClientAlreadyExistsError


def validateRental(ghostBook, ghostClient):
    inBookList = False
    repoCaller = Repo()
    listOfBooks = repoCaller.getListOfBooks()
    listOfRentedBooks = repoCaller.getListOfRentals()
    listOfClients = repoCaller.getListOfClients()
    for iterator in listOfBooks:
        if Book.getBookId(iterator) == Book.getBookId(ghostBook):
            inBookList = True
            break
    if inBookList == False:
        raise BookAlreadyRentedOrDoesntExistError
    # bookIsRented=True
    for books in listOfRentedBooks:
        if Book.getBookId(ghostBook) == Rental.getBookId(books) and Rental.getReturnDate(books) == "notRented":
            for iterator in listOfRentedBooks:
                if Book.getBookId(iterator) == Book.getBookId(ghostBook):
                    raise BookAlreadyRentedOrDoesntExistError
    clientFound = False
    for iterator in listOfClients:
        if Client.getClientId(iterator) == Client.getClientId(ghostClient):
            clientFound = True
    if clientFound == False:
        raise ClientAlreadyExistsError


def addABook(commandInPieces):
    # adds a book to the repository
    # input - details about the book as a list
    bookId = commandInPieces[1]
    repoCaller = Repo()
    bookTitle = commandInPieces[2]

    bookAuthor = commandInPieces[3]
    newBook = Book(bookId, bookTitle, bookAuthor)
    undoFunction = Undo("removeB", ["undo", bookId, bookTitle,bookAuthor])
    if commandInPieces[0] != "undo":
        repoCaller.addToListOfOperations(undoFunction)
        repoCaller.setRedoFalse()
    try:
        validateABook(newBook)
        repoCaller.addTheBookToTheRepo(newBook)
        repoCaller.loadIntoFiles()
        repoCaller.loadIntoPickle()
    except BookAlreadyExistsError:
        print(str(BookAlreadyExistsError()))
    return False

def addAClient(commandInPieces):
    # adds a client to the repository
    # input - details about the client as a list
    repoCaller = Repo()
    clientId = commandInPieces[1]
    clientName = commandInPieces[2]
    undoFunction=Undo("removeC",["undo",clientId,clientName])
    if commandInPieces[0] != "undo":
        repoCaller.addToListOfOperations(undoFunction)
        repoCaller.setRedoFalse()
    newClient = Client(clientId, clientName)
    try:
        validateAClient(newClient)
        repoCaller.addTheClientToTheRepo(newClient)
        repoCaller.loadIntoFiles()
        repoCaller.loadIntoPickle()
    except ClientAlreadyExistsError:
        print(str(ClientAlreadyExistsError()))
    return False


def listAllClients(commandInPieces):
    # lists clients in repository
    # input - not important / not used
    repoCaller = Repo()
    listOfClients=repoCaller.getListOfClients()
    s=""
    s+="Clients: \n"
    for iterator in listOfClients:
        s+=Client.clientToString(iterator)+"\n"
    return s


def listAllBooks(commandInPieces):
    # lists books in repository
    # input - not important/ not used
    repoCaller = Repo()
    listOfBooks=repoCaller.getListOfBooks()
    s=""
    s+="Books: \n"
    for iterator in listOfBooks:
        s+=Book.bookToString(iterator)+"\n"
    return s


def removeABook(commandInPieces):
    # removes a book from the repository
    # input - details about the book to be removed (id)
    repoCaller = Repo()
    listOfBooks=repoCaller.getListOfBooks()
    idToRemove = commandInPieces[1]
    position = 0
    removed = False
    for iterator in listOfBooks:

        if Book.getBookId(iterator) == idToRemove:
            if commandInPieces[0] != "undo":
                undoFunction=Undo("addB",["undo",Book.getBookId(iterator),Book.getBookName(iterator),Book.getBookAuthor(iterator)])
                repoCaller.addToListOfOperations(undoFunction)
                repoCaller.setRedoFalse()
            repoCaller.removeBookFromPosition(position)
            repoCaller.loadIntoFiles()
            repoCaller.loadIntoPickle()
            removed = True
        position += 1
    if removed == False:
        print(IdOfBookDoesntExist())
    return False
def removeAClient(commandInPieces):
    # removes a client from the repository
    # input - details about the client (id)
    repoCaller = Repo()
    idToRemove = commandInPieces[1]
    position = 0
    removed = False

    for iterator in Repo.listOfClients:
        if Client.getClientId(iterator) == idToRemove:
            if commandInPieces[0] != "undo":
                undoFunction=Undo("addC",["undo",Client.getClientId(iterator),Client.getClientName(iterator)])
                repoCaller.addToListOfOperations(undoFunction)
                repoCaller.setRedoFalse()
            repoCaller.removeClientFromPosition(position)
            repoCaller.loadIntoFiles()
            repoCaller.loadIntoPickle()
            removed = True
        position += 1
    if removed == False:
         print(IdOfClientDoesntExist())
    return False
def updateAClient(commandInPieces):
    # updates a client that is already in the repository
    # input - details about the client to be updated (id) and the values that must be edited
    repoCaller = Repo()
    listOfClients=repoCaller.getListOfClients()
    idToUpdate = commandInPieces[1]
    newId = commandInPieces[2]
    newName = commandInPieces[3]
    updatedClient = Client(newId, newName)
    position = 0
    try:
        validateAClient(updatedClient)
        for iterator in listOfClients:
            if Client.getClientId(iterator) == idToUpdate:
                if commandInPieces[0]!="undo":
                    undoFunction=Undo("updateC",["undo",newId,Client.getClientId(iterator),Client.getClientName(iterator),"undo",Client.getClientId(iterator),newId,newName])
                    repoCaller.addToListOfOperations(undoFunction)
                    repoCaller.setRedoFalse()
                repoCaller.removeClientFromPosition(position)
                repoCaller.insertClientIntoPosition(position, updatedClient)
                repoCaller.loadIntoFiles()
                repoCaller.loadIntoPickle()

                break
            position += 1
    except ClientAlreadyExistsError:
        print(ClientAlreadyExistsError())
    return False

def updateABook(commandInPieces):
    # updates a book from the repository
    # input - details about the book that must be updated(id) and the values that must be edited
    repoCaller = Repo()
    listOfBooks=repoCaller.getListOfBooks()
    idToUpdate = commandInPieces[1]
    newBookId = commandInPieces[2]
    newTitle = commandInPieces[3]
    newAuthor = commandInPieces[4]
    updatedBook = Book(newBookId, newTitle, newAuthor)
    position = 0
    try:
        validateABook(updatedBook)
        for iterator in listOfBooks:
            if Book.getBookId(iterator) == idToUpdate:
                if commandInPieces[0]!="undo":
                    undoFunction=Undo("updateB",["undo",newBookId,Book.getBookId(iterator),Book.getBookName(iterator),Book.getBookAuthor(iterator),"undo",idToUpdate,newBookId,newTitle,newAuthor])
                    repoCaller.addToListOfOperations(undoFunction)
                    repoCaller.setRedoFalse()
                repoCaller.removeBookFromPosition(position)
                repoCaller.insertBookIntoPosition(position, updatedBook)
                repoCaller.loadIntoFiles()
                repoCaller.loadIntoPickle()
                break
            position += 1
    except BookAlreadyExistsError:
        print(BookAlreadyExistsError())
    return False

def rentABook(commandInPieces):
    repoCaller = Repo()
    ghostBook = Book(commandInPieces[1], " ", " ")
    ghostClient = Client(commandInPieces[2], " ")
    rentDateString = commandInPieces[3].split('/')
    rentDate=datetime.datetime(int(rentDateString[0]),int(rentDateString[1]),int(rentDateString[2]))
    try:
        validateRental(ghostBook, ghostClient)
        newRental = Rental("1", Book.getBookId(ghostBook), Client.getClientId(ghostClient), rentDate, "notReturned")
        repoCaller.insertIntoRentals(newRental)
        repoCaller.loadIntoFiles()
        repoCaller.loadIntoPickle()
    except BookAlreadyRentedOrDoesntExistError:
        print(BookAlreadyRentedOrDoesntExistError())
    except ClientAlreadyExistsError:
        print(ClientAlreadyExistsError())
    return False

def returnABook(commandInPieces):
    repoCaller = Repo()
    idBook = commandInPieces[1]
    idClient = commandInPieces[2]
    returnDateString = commandInPieces[3].split('/')
    returnDate=datetime.datetime(int(returnDateString[0]),int(returnDateString[1]),int(returnDateString[2]))
    listOfRentedBooks = repoCaller.getListOfRentals()
    for iterator in listOfRentedBooks:
        if Rental.getBookId(iterator) == idBook and Rental.getClientId(iterator) == idClient:
            Rental.setReturnDate(iterator, returnDate)
            repoCaller.loadIntoFiles()
            repoCaller.loadIntoPickle()
    return False

def listRentedBooks(commandInPieces):
    repoCaller = Repo()
    listOfRentedBooks = repoCaller.getListOfRentals()
    s="Rented: \n"
    for iterator in listOfRentedBooks:
        s+=Rental.printRental(iterator)
    return s


def searchAClient(commandInPieces):
    repoCaller = Repo()
    keyToSearch = commandInPieces[1]
    listOfClients = repoCaller.getListOfClients()
    s=""
    s+="Clients: \n"
    for client in listOfClients:
        if keyToSearch.lower() == Client.getClientId(client).lower():
            s+=Client.clientToString(client)+"\n"
        elif keyToSearch.lower() == Client.getClientName(client).lower():
            s+=Client.clientToString(client)+"\n"
    return s

def searchABook(commandInPieces):
    repoCaller = Repo()
    keyToSearch = commandInPieces[1]
    listOfBooks = repoCaller.getListOfBooks()
    s=""
    s="Books: \n"
    for book in listOfBooks:
        if keyToSearch.lower() in Book.getBookId(book).lower():
            s+=Book.bookToString(book)+"\n"
        elif keyToSearch.lower() in Book.getBookAuthor(book).lower():
            s+=Book.bookToString(book)+"\n"
        elif keyToSearch.lower() in Book.getBookName(book).lower():
            s+=Book.bookToString(book)+"\n"
    return s


def showStatistics(null):
    repoCaller = Repo()
    listOfRentedBooks = repoCaller.getListOfRentals()
    mostRentedBooks = repoCaller.getListOfMostRentedBooks()
    mostRentedAuthors = repoCaller.getListOfMostRentedAuthors()
    mostActiveClients=repoCaller.getListOfMostActiveClients()
    listOfBooks=repoCaller.getListOfBooks()
    listOfClients=repoCaller.getListOfClients()
    booksStatistics="Books statistic:\n"
    authorStatistics="Author statistics:\n"
    clientsStatistics="Clients statistics:\n"
    for rentedBooks in listOfRentedBooks:
        try:
            mostRentedAuthors[Rental.getBookId(rentedBooks)] += 1
        except KeyError:
            mostRentedAuthors[Rental.getBookId(rentedBooks)] = 1

    for rentedBooks in listOfRentedBooks:
        try:
            mostRentedBooks[Rental.getBookId(rentedBooks)] += 1
        except KeyError:
            mostRentedBooks[Rental.getBookId(rentedBooks)] =1

    mostRentedAuthorsList=sorted(mostRentedAuthors.items(),key=lambda x:x[1],reverse=True)
    mostRentedBooksList=sorted(mostRentedBooks.items(),key=lambda x:x[1],reverse=True)

    for rentedBooks in mostRentedBooksList:
            booksStatistics+="Id of book: "+str(rentedBooks[0])+" Rented: "+str(rentedBooks[1])+" times"+"\n"

    for rentedBooks in mostRentedAuthorsList:
        for books in listOfBooks:
            if(Book.getBookId(books) == rentedBooks[0]):
                authorStatistics+="Author: "+str(Book.getBookAuthor(books))+" Rented: "+str(rentedBooks[1])+" times"+"\n"
    for client in listOfClients:
        total=0
        for rent in listOfRentedBooks:
            if Client.getClientId(client) == Rental.getClientId(rent)  and rent.getReturnDate()!="notReturned":
                difference=Rental.getReturnDate(rent) - Rental.getRentDate(rent)
                total+=difference.total_seconds()/86400
        try:
            mostActiveClients[Client.getClientName(client)]+=total
        except KeyError:
            repoCaller.addToMostActiveClients(client,total)
    mostActiveClientsList=sorted(mostActiveClients.items(),key=lambda x:x[1],reverse=True)

    for client in mostActiveClientsList:
        clientsStatistics+="Name: "+str(client[0])+" Total rental days: "+str(client[1])+"\n"
    return [booksStatistics,authorStatistics,clientsStatistics]
def performUndo(null):
    repoCaller=Repo()
    indexOfOperations=repoCaller.getIndexOfOperations()
    listOfOperations=repoCaller.getListOfOperations()
    commands = {"removeB": removeABook,"removeC":removeAClient,"addB":addABook,"addC":addAClient,"updateB":updateABook,"updateC":updateAClient}
    if indexOfOperations >=0 :
        commands[Undo.getFuntion(listOfOperations[indexOfOperations])](Undo.getParameters(listOfOperations[indexOfOperations]))
        repoCaller.decrementIndex()
        repoCaller.setRedoTrue()
    else :
        return "Cannot undo anymore"
    return False

def performRedo(null):
    repoCaller=Repo()
    listOfOperations=repoCaller.getListOfOperations()
    canRedo=repoCaller.getRedoState()
    if canRedo==True:
        repoCaller.incrementIndex()
        index = repoCaller.getIndexOfOperations()
        commands={"removeB":addABook,"removeC":addAClient,"addB":removeABook,"addC":removeAClient,"updateB":updateABook,"updateC":updateAClient}

        if "update" in Undo.getFuntion(listOfOperations[index]):
            commands[Undo.getFuntion(listOfOperations[index])](Undo.getParametersUpdate(listOfOperations[index]))
        else :
            commands[Undo.getFuntion(listOfOperations[index])](Undo.getParameters(listOfOperations[index]))
    return False