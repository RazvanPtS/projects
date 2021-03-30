#include "gui.h"
#include "qtlab.h"
#include "popUpWindow.h"
#include "QStringListModel.h"
Gui::Gui(Service* serviceCaller)
{
	this->serviceCaller = serviceCaller;
}

void Gui::printError(QLabel* errorLabel, std::string errorInfo)
{
	QString string(errorInfo.c_str());
	errorLabel->setText(string);
}

void Gui::printPlants(Service* serviceCaller,QListWidget *listOfPlants)
{
	listOfPlants->clear();
	std::vector<Plant>vector = serviceCaller->listPlants();
	for (auto i : vector)
	{
		char plant[200] = "";
		strcat(plant, i.getCodedName());
		strcat(plant, ",");
		strcat(plant, i.getSpecies());
		strcat(plant, ",");
		strcat(plant, i.getAge());
		strcat(plant, ",");
		strcat(plant, i.getScan());
		QString* string = new QString(plant);
		QListWidgetItem* line = new QListWidgetItem{};
		line->setText(*string);
		listOfPlants->addItem(line);
	}
}

void Gui::addToList(Service* serviceCaller, QListWidget* listOfPlants,QLineEdit* addedInfo,QLabel* errorLabel)
{
	//std::vector<Plant>vector = serviceCaller->listPlants();
	QString* string = new QString;
	*string=addedInfo->text();
	std::string plantToString = string->toUtf8();
	std::string name, scan, age, species;
	name = plantToString.substr(0, plantToString.find(","));
	plantToString.erase(0, name.size() + 2);
	species = plantToString.substr(0, plantToString.find(","));
	plantToString.erase(0, species.size() + 2);
	age = plantToString.substr(0, plantToString.find(","));
	plantToString.erase(0, age.size() + 2); 
	scan = plantToString.substr(0, plantToString.find(","));
	plantToString.erase(0, scan.size() + 2);
	try {
		if (this->serviceCaller->repoCaller->isFile())
			RepoValidator::checkExistingNameFile(name.c_str(), (FileRepo*)this->serviceCaller->repoCaller, false);
		else
			RepoValidator::checkExistingNameMemory(name.c_str(), (MemoryRepo*)this->serviceCaller->repoCaller, false);
		Validator::validateNumberOfParams(name.c_str(), species.c_str(), age.c_str(), scan.c_str(),4);
		Validator::validateName(name.c_str());
		Validator::validateSpecies(species.c_str());
		Validator::validateAge(age.c_str());
		Validator::validateScan(scan.c_str());
		this->serviceCaller->addPlantToList(name.c_str(), species.c_str(), age.c_str(), scan.c_str());
		addedInfo->clear();
		errorLabel->clear();
		this->printPlants(this->serviceCaller, listOfPlants);
	}
	catch (ValidateException & exception)
	{
		this->printError(errorLabel,exception.printError());
	}
	catch (RepoException & exception)
	{
		this->printError(errorLabel, exception.printError());
	}
}

void Gui::deleteFromList(Service* serviceCaller, QListWidget* listOfPlants, QLineEdit* plantInfo, QLabel* errorLabel)
{
	std::vector<Plant>vector = serviceCaller->listPlants();
	std::string plant;
	QString plantInfoString = plantInfo->text();
	plant = plantInfoString.toUtf8();
	if (plant.find(",") != std::string::npos)
		plant.erase(plant.find(","), plant.size());
	try {
		Validator::validateName(plant.c_str());
		if (this->serviceCaller->repoCaller->isFile())
			RepoValidator::checkExistingNameFile(plant.c_str(), (FileRepo*)this->serviceCaller->repoCaller, true);
		else
			RepoValidator::checkExistingNameMemory(plant.c_str(), (MemoryRepo*)this->serviceCaller->repoCaller, true);
		this->serviceCaller->deletePlantFromList(plant.c_str());
		plantInfo->clear();
		errorLabel->clear();
		this->printPlants(this->serviceCaller, listOfPlants);
	}
	catch (ValidateException & exception)
	{
		this->printError(errorLabel, exception.printError());
	}
	catch (RepoException & exception)
	{
		this->printError(errorLabel, exception.printError());
	}
}

void Gui::updateFromList(Service* serviceCaller, QListWidget* listOfPlants, QLineEdit* addedInfo,QLabel* errorLabel)
{
	QString* string = new QString;
	*string = addedInfo->text();
	std::string plantToString = string->toUtf8();
	std::string name, scan, age, species;
	name = plantToString.substr(0, plantToString.find(","));
	plantToString.erase(0, name.size() + 2);
	species = plantToString.substr(0, plantToString.find(","));
	plantToString.erase(0, species.size() + 2);
	age = plantToString.substr(0, plantToString.find(","));
	plantToString.erase(0, age.size() + 2);
	scan = plantToString.substr(0, plantToString.find(","));
	plantToString.erase(0, scan.size() + 2);
	try {
		if (this->serviceCaller->repoCaller->isFile())
			RepoValidator::checkExistingNameFile(name.c_str(), (FileRepo*)this->serviceCaller->repoCaller, true);
		else
			RepoValidator::checkExistingNameMemory(name.c_str(), (MemoryRepo*)this->serviceCaller->repoCaller, true);
		Validator::validateNumberOfParams(name.c_str(), species.c_str(), age.c_str(), scan.c_str(), 4);
		Validator::validateName(name.c_str());
		Validator::validateSpecies(species.c_str());
		Validator::validateAge(age.c_str());
		Validator::validateScan(scan.c_str());
		this->serviceCaller->updatePlantFromList(name.c_str(), species.c_str(), age.c_str(), scan.c_str());
		addedInfo->clear();
		errorLabel->clear();
		this->printPlants(this->serviceCaller, listOfPlants);
	}
	catch (ValidateException & exception)
	{
		this->printError(errorLabel, exception.printError());
	}
	catch (RepoException & exception)
	{
		this->printError(errorLabel, exception.printError());
	}
}

void Gui::engageMode(qtlab* window,bool state,bool notState)
{
	window->ui.plantList->clear();
	window->ui.errorLabel->clear();
	window->ui.nextPlantInfo->clear();
	window->ui.plantInfo->clear();
	window->ui.addButton->setEnabled(state);
	window->ui.deleteButton->setEnabled(state);
	window->ui.updateButton->setEnabled(state);
	window->ui.listButton->setEnabled(state);
	window->ui.filterButton->setEnabled(notState);
	window->ui.myListButton->setEnabled(notState);
	window->ui.nextButton->setEnabled(notState);
	window->ui.saveButton->setEnabled(notState);
}

void Gui::printNextPlant(qtlab* window, Service* serviceCaller)
{
	Plant* nextPlant;
	nextPlant=&(serviceCaller->nextPlant());
	char plant[30]="";
	strcat(plant, nextPlant->getCodedName());
	strcat(plant, ",");
	strcat(plant, nextPlant->getSpecies());
	strcat(plant, ",");
	strcat(plant, nextPlant->getAge());
	strcat(plant, ",");
	strcat(plant, nextPlant->getScan());
	QString string(plant);
	window->ui.nextPlantInfo->setText(string);


}

void Gui::filterMyPlants(qtlab* window, Service* serviceCaller)
{
	window->ui.plantList->clear();
	QLineEdit* plantCriteria = window->ui.plantInfo;
	std::vector<Plant> filteredList;
	std::string criteria;
	criteria = (plantCriteria->text()).toUtf8();
	std::string species;
	std::string age;
	species = criteria.substr(0, criteria.find(","));
	criteria.erase(0, species.size() + 1);
	age = criteria;
	filteredList = serviceCaller->listSpeciesAndAge(species.c_str(),age.c_str());
	for (auto i : filteredList)
	{
		char plant[200] = "";
		strcat(plant, i.getCodedName());
		strcat(plant, ",");
		strcat(plant, i.getSpecies());
		strcat(plant, ",");
		strcat(plant, i.getAge());
		strcat(plant, ",");
		strcat(plant, i.getScan());
		QString* string = new QString(plant);
		QListWidgetItem* line = new QListWidgetItem{};
		line->setText(*string);
		window->ui.plantList->addItem(line);
	}
	window->ui.plantInfo->clear();
	
}

void Gui::saveMyPlant(qtlab* window, Service* serviceCaller)
{
	std::string plantToSave = window->ui.plantInfo->text().toUtf8();
	try {
		if(serviceCaller->repoCaller->isFile())
			RepoValidator::checkExistingNameFile(plantToSave.c_str(),(FileRepo*)serviceCaller->repoCaller, true);
		else
			RepoValidator::checkExistingNameMemory(plantToSave.c_str(),(MemoryRepo*)serviceCaller->repoCaller, true);
		serviceCaller->addToAssistantList(plantToSave.c_str());
		window->ui.plantInfo->clear();
		window->ui.errorLabel->clear();
	}
	catch (RepoException & exception)
	{
		this->printError(window->ui.errorLabel, exception.printError());
	}
}

void Gui::listMyList(qtlab* window, popUpWindow* popUp, Service* serviceCaller)
{
		QStringListModel* model = new QStringListModel();
		QStringList* string = new QStringList();
		window->ui.plantList->clear();
		std::vector<Plant> assistantsList=this->serviceCaller->printAssistantList();
		for (auto i : assistantsList)
		{
			char plant[200] = "";
			strcat(plant, i.getCodedName());
			strcat(plant, ",");
			strcat(plant, i.getSpecies());
			strcat(plant, ",");
			strcat(plant, i.getAge());
			strcat(plant, ",");
			strcat(plant, i.getScan());
			string->append(plant);
		}
		model->setStringList(*string);
		popUp->uiPopUp.myListView->setModel(model);
		popUp->show();
		window->ui.plantInfo->clear();
}

void Gui::connectButtons(qtlab* main, popUpWindow* popUp)
{
	QObject::connect(main->ui.addButton, &QPushButton::clicked, [main, this]() {this->addToList(this->serviceCaller, main->ui.plantList, main->ui.plantInfo,main->ui.errorLabel); });
	QObject::connect(main->ui.listButton, &QPushButton::clicked, [main,this]() {this->printPlants(this->serviceCaller, main->ui.plantList); });
	QObject::connect(main->ui.deleteButton, &QPushButton::clicked, [main, this]() {this->deleteFromList(this->serviceCaller, main->ui.plantList, main->ui.plantInfo,main->ui.errorLabel); });
	QObject::connect(main->ui.updateButton, &QPushButton::clicked, [main, this]() {this->updateFromList(this->serviceCaller, main->ui.plantList, main->ui.plantInfo,main->ui.errorLabel); });
	QObject::connect(main->ui.AButton, &QPushButton::clicked, [main, this]() {
		this->engageMode(main,true,false);
		});
	QObject::connect(main->ui.BButton, &QPushButton::clicked, [main, this]() {
		this->engageMode(main,false,true);
		});
	QObject::connect(main->ui.nextButton, &QPushButton::clicked, [main, this]() {
		this->printNextPlant(main, this->serviceCaller);
		});
	QObject::connect(main->ui.filterButton, &QPushButton::clicked, [main, this]() {
		this->filterMyPlants(main, this->serviceCaller);
		});
	QObject::connect(main->ui.saveButton, &QPushButton::clicked, [main, this]() {
		this->saveMyPlant(main, this->serviceCaller);
		});
	QObject::connect(main->ui.myListButton, &QPushButton::clicked, [main,popUp,this]() {
		this->listMyList(main, popUp, this->serviceCaller);
		});
	QObject::connect(main->ui.undoButton, &QPushButton::clicked, [main, this]() {
		this->serviceCaller->performUndoRedo(true,(Operations*)this->serviceCaller->undoCaller);
		this->printPlants(this->serviceCaller, main->ui.plantList);

		});
	QObject::connect(main->ui.redoButton, &QPushButton::clicked, [main, this]() {
		this->serviceCaller->performUndoRedo(false,(Operations*)this->serviceCaller->redoCaller);
		this->printPlants(this->serviceCaller, main->ui.plantList);
		});
	QKeyEvent* event;

}

void Gui::run(int argc,char** argv)
{
	Plant newPlant("ab", "Cd", "21", "aab");
	Plant newPlant1("baz", "aarq", "41", "bbaz");
	Plant newPlant2("gqgq", "abrw", "51", "nhhw");
	Plant newPlant3("yqaaz", "jjqa", "102", "bbaz");
	Plant newPlant4("zzzse", "agazr31", "610", "llaz");
	this->serviceCaller->repoCaller->addPlantToRepo(newPlant);
	this->serviceCaller->repoCaller->addPlantToRepo(newPlant1);
	this->serviceCaller->repoCaller->addPlantToRepo(newPlant2);
	this->serviceCaller->repoCaller->addPlantToRepo(newPlant3);
	this->serviceCaller->repoCaller->addPlantToRepo(newPlant4);
	QApplication a(argc, argv);
	QWidget* parent = new QWidget{};
	qtlab* main=new qtlab;
	popUpWindow *popUp = new popUpWindow;
	this->connectButtons(main,popUp);
	this->engageMode(main, false, false);
	main->show();
	a.exec();
}
