#include <QApplication>
#include <QLabel>
#include <QLineEdit>
#include <QPushButton>
#include <QListWidget>
#include <QHBoxLayout>
#include <QFormLayout>
#include <Qbuttongroup.h>
#include "service.h"
#include "domain.h"
#include "gui.h"
#include <iostream>
#include <fstream>
int main(int argc, char* argv[])
{
	BaseRepo* repoCaller;
	std::ifstream fileHandler("properties.txt");
	std::string mode;
	std::getline(fileHandler, mode);
	if(mode.compare("memory")==0)
		repoCaller= new MemoryRepo();
	else
	{
		std::string filePath1, filePath2;
		std::getline(fileHandler, filePath1);
		std::getline(fileHandler, filePath2);
		repoCaller = new FileRepo(filePath1, filePath2);
	}
	Service* serviceCaller = new Service(repoCaller);
	Gui* guiCaller = new Gui(serviceCaller);
	guiCaller->run(argc, argv);
	return 0;
}
