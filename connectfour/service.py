from repo import *
import random
class Service():
    repoCaller=Repo()
    turn =1
    def placeOnBoard(self,position):
        board=self.repoCaller.getBoard()
        for iterator in range(0,6):
            if board[iterator][position]==0:
                self.repoCaller.setPosition(position,iterator,self.turn)
                self.turn = 2
                break

    def restartGame(self):
        self.repoCaller=Repo()
    def printBoard(self):
        boardAsString=""
        board=self.repoCaller.getBoard()
        for line in range(5,-1,-1):
            boardAsString+='\n'
            for coloumn in range(0,7):
                if board[line][coloumn] == 0:
                    boardAsString+="- "
                elif board[line][coloumn] == 1:
                    boardAsString+="x "
                else:
                    boardAsString+="o "
        return boardAsString
    def checkLine(self,line,coloumn):
        board=self.repoCaller.getBoard()
        for iterator in range(0,4):
            if board[line+iterator][coloumn]!=board[line][coloumn]:
                return False
        return True
    def checkColoumn(self,line,coloumn):
        board=self.repoCaller.getBoard()
        for iterator in range(0,4):
            if board[line][coloumn] != board[line][coloumn+iterator]:
                return False
        return True
    def checkDiagonalPositive(self,line,coloumn):
        board=self.repoCaller.getBoard()
        for iterator in range(0,4):
            if board[line][coloumn] != board[line+iterator][coloumn+iterator]:
                return False
        return True
    def checkDiagonalNegative(self,line,coloumn):
        board=self.repoCaller.getBoard()
        for iterator in range(0,4):
            if board[line][coloumn] != board[line+iterator][coloumn-iterator]:
                return False
        return True
    def checkForWinners(self):
        board=self.repoCaller.getBoard()
        line =0
        coloumn = 0
        for line in range(0,3):
            for coloumn in range(0,4):
                if board[line][coloumn] != 0:
                    if self.checkDiagonalPositive(line,coloumn):
                        return board[line][coloumn]
        #for line in range(0,3):
            for coloumn in range(6,2,-1):
                if board[line][coloumn] != 0:
                    if self.checkDiagonalNegative(line, coloumn):
                        return board[line][coloumn]
        for line in range(0,6):
            for coloumn in range(0,4):
                if board[line][coloumn] != 0:
                    if self.checkColoumn(line,coloumn):
                        return board[line][coloumn]
        for line in range(0,3):
            for coloumn in range(0,7):
                if board[line][coloumn] != 0:
                    if self.checkLine(line,coloumn):
                        return board[line][coloumn]
        return 0
    def makeAIMove(self):
        position=random.randint(0,6)
        self.placeOnBoard(position)
        self.turn =1