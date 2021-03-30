from service import *
from tests import Tests
from repository import Repo
def main():
    repo=Repo()
    repo.readInputMethod()
    while True:
        commands={"addbook":addABook,"addclient":addAClient,"listclients":listAllClients,"listbooks":listAllBooks,
                  "removebook":removeABook,"removeclient":removeAClient,"updateclient":updateAClient,"updatebook":updateABook,
                  "rentbook":rentABook,"returnbook":returnABook,"listrented":listRentedBooks,"searchclient":searchAClient,
                  "searchbook":searchABook,"showstatistics":showStatistics,"undo":performUndo,"redo":performRedo}
        print("Available commands: ")
        print("     1. addbook ")
        print("     2. addclient")
        print("     3. listclients")
        print("     4. listbooks")
        print("     5. removeclient")
        print("     6. removebook")
        print("     7. updateclient")
        print("     8. updatebook ")
        print("     9. rentbook ")
        print("     10. returnbook")
        print("     11. listrented")
        print("     12. searchclient [id/name]")
        print("     13. searchbook [id/author/title]")
        print("     14. showstatistics")
        print("     15. undo")
        print("     15. redo")
        command=input(">>>")
        if command=="1":
            command=[]
            id=input(">>[id] ")
            title=input(">>[title] ")
            author=input(">>[author] ")
            command.append("addbook")
            command.append(id)
            command.append(title)
            command.append(author)
        if command == "2":
            command=[]
            id = input(">>[id] ")
            name=input(">>[name] ")
            command.append(id)
            command.append(name)
            command.insert(0,"addclient")
        if command == "3":
            command=["listclients"]
        if command == "4":
            command=["listbooks"]
        if command == "5":
            command=[]
            id = input(">>[id] ")
            command.append(id)
            command.insert(0, "removeclient")
        if command == "6":
            command=[]
            id = input(">>[id] ")
            command.append(id)
            command.insert(0, "removebook")
        if command == "7":
            command=[]
            id = input(">>[old ID] ")
            newId=input(">>[new ID] ")
            newName=input(">>[new Name] ")
            command.append(id)
            command.append(newId)
            command.append(newName)
            command.insert(0, "updateclient")
        if command == "8":
            command=[]
            id = input(">>[old ID] ")
            newId = input(">>[new ID] ")
            newTitle = input(">>[new Title] ")
            newAuthor=input(">>[new Author] ")
            command.append(id)
            command.append(newId)
            command.append(newTitle)
            command.append(newAuthor)
            command.insert(0, "updatebook")
        if command == "9":
            command=[]
            bID = input(">>[book ID] ")
            cID=input(">>[client ID] ")
            rentDate=input(">>[rent date] (rent date format: dd/mm/yyyy)")
            command.append(bID)
            command.append(cID)
            command.append(rentDate)
            command.insert(0, "rentbook")
        if command == "10":
            command=[]
            bID = input(">>[book ID] ")
            cID=input(">>[client ID] ")
            returnDate=input(">>[return date] (return date format: dd/mm/yyyy) ")
            command.append(bID)
            command.append(cID)
            command.append(returnDate)
            command.insert(0, "returnbook")
        if command == "11":
            command=["listrented"]
        if command == "12":
            command = input(">>[id/name]>>")
            command = command.split(" ")
            command.insert(0, "searchclient")
        if command == "13":
            command = input(">>[id/name/author]>>")
            command = command.split(" ")
            command.insert(0, "searchbook")
        if command == "14":
            command=["showstatistics"]
        if command== "15":
            command=["undo"]
        if command=="16":
            command=["redo"]
        try:
            output=commands[command[0]](command)
            if output!=False:
                if len(output)==3:
                    print(output[0])
                    print(output[1])
                    print(output[2])
                else:
                    print(output)
        except KeyError:
            print("Invalid command")
main()