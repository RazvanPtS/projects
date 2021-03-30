from unittest import TestCase
from service import *
from domain import *


class Tests(TestCase):
    def test_addABook_newInstanceOfBook_updatedListOfBooks(self):
        repoCaller = Repo()
        testList = []
        repoCaller.clearListOfBooks()
        testBook = Book("1", "yo", "jo")
        addABook(["", "1", "yo", "jo"])
        testList = repoCaller.getListOfBooks()
        self.assertTrue(Book.bookToString(testList[-1]) == Book.bookToString(testBook))
        repoCaller.clearListOfBooks()
    def test2_addABook_newInstanceOfBook_Error(self):
        repoCaller = Repo()
        testList = []
        repoCaller.clearListOfBooks()
        testBook = Book("1", "yo", "Jo")
        addABook(["", "1", "yo", "jo"])
        testList = repoCaller.getListOfBooks()
        self.assertFalse(Book.bookToString(testList[-1]) == Book.bookToString(testBook))
        repoCaller.clearListOfBooks()
    def test3_addABook_newInstanceOfBook_Error(self):
        repoCaller = Repo()
        testList = []
        repoCaller.clearListOfBooks()
        testBook = Book("1", "yO", "Jo")
        addABook(["", "1", "yo", "jo"])
        testList = repoCaller.getListOfBooks()
        self.assertFalse(Book.bookToString(testList[-1]) == Book.bookToString(testBook))
        repoCaller.clearListOfBooks()
    def test_addABook_newInstanceOfBook_Error(self):
        repoCaller = Repo()
        testList = []
        repoCaller.clearListOfBooks()
        testBook = Book("2", "yo", "Jo")
        addABook(["", "1", "Yo", "Jo"])
        testList = repoCaller.getListOfBooks()
        self.assertFalse(Book.bookToString(testList[-1]) == Book.bookToString(testBook))
        repoCaller.clearListOfBooks()

    def test_addAClient_newInstanceOfClient_updatedListOfClients(self):
        repoCaller = Repo()
        testList = []
        repoCaller.clearListOfClients()
        testClient = Client("1", "Gigel")
        addAClient(["", "1", "Gigel"])
        testList = repoCaller.getListOfClients()
        self.assertTrue(Client.clientToString(testList[-1]) == Client.clientToString(testClient))
        repoCaller.clearListOfClients()

    def test_removeABook_bookFromListOfBooksById_updatedListOfBooks(self):
        repoCaller = Repo()
        repoCaller.clearListOfBooks()
        addABook(["", "1", "name", "author"])
        testList = repoCaller.getListOfBooks()
        removeABook(["", "1"])
        self.assertTrue(len(testList) == 0)
        repoCaller.clearListOfBooks()

    def test_removeAClient_clientFromListOfClientsById_updatedListOfClients(self):
        repoCaller = Repo()
        repoCaller.clearListOfClients()
        addAClient(["", "1", "name"])
        testList = repoCaller.getListOfClients()
        removeAClient(["", "1"])
        self.assertTrue(len(testList) == 0)
        repoCaller.clearListOfClients()

    def test_updateAClient_idOfclientSelectedById_updatedInstanceOfClient(self):
        repoCaller=Repo()
        repoCaller.clearListOfClients()
        addAClient(["","1","nameBefore"])
        updateAClient(["","1","4","nameAfter"])
        testList=repoCaller.getListOfClients()
        self.assertTrue(Client.getClientId(testList[-1])=="4")
        repoCaller.clearListOfClients()
    def test_updateAClient_nameOfclientSelectedById_updatedInstanceOfClient(self):
        repoCaller=Repo()
        repoCaller.clearListOfClients()
        addAClient(["","1","nameBefore"])
        updateAClient(["","1","4","nameAfter"])
        testList=repoCaller.getListOfClients()
        self.assertTrue(Client.getClientName(testList[-1])=="nameAfter")
        repoCaller.clearListOfClients()

    def test_updateABook_idOfbookSelectedById_updatedInstanceOfBook(self):
        repoCaller=Repo()
        repoCaller.clearListOfBooks()
        addABook(["","1","nameBefore","authorBefore"])
        updateABook(["","1","4","nameAfter","authorAfter"])
        testList=repoCaller.getListOfBooks()
        self.assertTrue(Book.getBookId(testList[-1])=="4")
        repoCaller.clearListOfBooks()
    def test_updateABook_nameOfbookSelectedById_updatedInstanceOfBook(self):
        repoCaller=Repo()
        repoCaller.clearListOfBooks()
        addABook(["","1","nameBefore","authorBefore"])
        updateABook(["","1","4","nameAfter","authorAfter"])
        testList=repoCaller.getListOfBooks()
        self.assertTrue(Book.getBookName(testList[-1])=="nameAfter")
        repoCaller.clearListOfBooks()
    def test_updateABook_authorOfbookSelectedById_updatedInstanceOfBook(self):
        repoCaller=Repo()
        repoCaller.clearListOfBooks()
        addABook(["","1","nameBefore","authorBefore"])
        updateABook(["","1","4","nameAfter","authorAfter"])
        testList=repoCaller.getListOfBooks()
        self.assertTrue(Book.getBookAuthor(testList[-1])=="authorAfter")
        repoCaller.clearListOfBooks()