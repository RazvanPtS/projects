from service import *
class Ui():
    def main(self):
        serviceCaller=Service()
        while True:
            gameEnded=serviceCaller.checkForWinners()
            if gameEnded !=0:
                if gameEnded == 1:
                    print("Player won, congrats!")
                else:
                    print("AI won, really?")
                serviceCaller.restartGame()
            command=input("Select a coloumn to place the piece(1-7)\n"
                          "Press r to restart the game\n"
                          "Press x to exit\n")
            if command == "r":
                serviceCaller.restartGame()
            elif command == "x" :
                exit(0)
            else:
                try:
                    command=int(command)
                    if command >0 and command <8:
                        serviceCaller.placeOnBoard(command-1)
                        serviceCaller.makeAIMove()
                        print(serviceCaller.printBoard())
                    else:
                        print("Invalid position")
                except ValueError:
                    print("Invalid position")
start=Ui()
start.main()



