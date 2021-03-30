#pragma once
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
#include "qtlab.h"
#include "popUpWindow.h"
class Gui {

public:
	Service* serviceCaller;
	Gui(Service*);
	void run(int, char**);
	void printPlants(Service*,QListWidget*);
	void connectButtons(qtlab*,popUpWindow*);
	void addToList(Service*, QListWidget*,QLineEdit*,QLabel*);
	void deleteFromList(Service*, QListWidget*, QLineEdit*,QLabel*);
	void updateFromList(Service*, QListWidget*, QLineEdit*,QLabel*);
	void engageMode(qtlab*,bool,bool);
	void printError(QLabel*, std::string);
	void printNextPlant(qtlab*, Service*);
	void saveMyPlant(qtlab*, Service*);
	void filterMyPlants(qtlab*, Service*);
	void listMyList(qtlab*,popUpWindow*,Service*);
};
