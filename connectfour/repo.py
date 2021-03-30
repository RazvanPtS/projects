class Repo():
    board=[]
    def __init__(self):
        self.board=[[0,0,0,0,0,0,0],[0,0,0,0,0,0,0],[0,0,0,0,0,0,0],[0,0,0,0,0,0,0],[0,0,0,0,0,0,0],[0,0,0,0,0,0,0]]
    def getBoard(self):
        return self.board
    def setPosition(self,position,iterator,turn):
        self.board[iterator][position]=turn