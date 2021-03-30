#include "service.h"
#include <cstring>
#include <qdebug.h>
Service::Service(BaseRepo* repoCaller)
{
	this->repoCaller = repoCaller;
	this->undoCaller = new UndoOperations(this);
	this->redoCaller = new RedoOperations(this);
	this->undoCaller->redoOperations = this->redoCaller;
	this->redoCaller->undoOperations = this->undoCaller;
}

bool Service::addPlantToList(const char* codedName,const char* species,const char* age,const char* scan)
{	//prepares user input (parameters) to be added to the plant list from repo
		Plant newPlant, nullPlant{};
		newPlant.setAge(age);
		newPlant.setName(codedName);
		newPlant.setScan(scan);
		newPlant.setSpecies(species);
		this->repoCaller->addPlantToRepo(newPlant);
		this->undoCaller->addToList("add", newPlant,nullPlant);
		this->redoCaller->operationsList.clear();
		this->redoCaller->indexOfOperation = 0;
		return 1;
		
}

std::vector<Plant> Service::listPlants()
{	//gets the vector of plants to be outputted to the user
	return this->repoCaller->getListOfPlants();
}

bool Service::updatePlantFromList(const char* name,const char* species,const char* age,const char* scan)
{	//prepares the user input in oreder to update the plant with the new information(parameters)
		Plant newPlant,afterUpdatePlant;
		newPlant.setAge(age);
		newPlant.setName(name);
		newPlant.setScan(scan);
		newPlant.setSpecies(species);
		afterUpdatePlant.setAge(age);
		afterUpdatePlant.setName(name);
		afterUpdatePlant.setScan(scan);
		afterUpdatePlant.setSpecies(species);
		this->repoCaller->updatePlantFromRepo(newPlant);
		this->undoCaller->addToList("update", newPlant, afterUpdatePlant);
		this->redoCaller->operationsList.clear();
		this->redoCaller->indexOfOperation = 0;
		return 1;

}

void Service::deletePlantFromList(const char* name)
{	//deletes from repo the plant with the given name(user input - parameter)
	Plant erasedPlant;
	Plant nullPlant{};
	erasedPlant=this->repoCaller->deletePlantFromRepo(name);
	this->undoCaller->addToList("delete", erasedPlant, nullPlant);
	this->redoCaller->operationsList.clear();
	this->redoCaller->indexOfOperation = 0;
}

Plant Service::nextPlant()
{	//transforms the current plant to display to a string and returns it through the out parameter
	Plant newPlant;
	newPlant = this->repoCaller->currentPlantToList();
	return newPlant;
}

void Service::addToAssistantList(const char* name)
{	
	this->repoCaller->addCurrentToAssistantList(name);
}

std::vector<Plant> Service::printAssistantList()
{
	std::vector<Plant> plantsTolist;
	plantsTolist=this->repoCaller->getAllFromAssistantList();
	return plantsTolist;


}

std::vector<Plant> Service::listSpeciesAndAge(const char* species,const char* age)
{
	return this->repoCaller->assistantListBy((char*)species,(char*) age);
}

void Validator::validateNumberOfParams(const char* name, const char* species, const char* age, const char* scan, int numberOfInputs)
{
	int numberOfInputsRead = 0;
	if (strcmp(name, "") != 0)
		numberOfInputsRead++;
	if (strcmp(species, "") != 0)
		numberOfInputsRead++;
	if (strcmp(age, "") != 0)
		numberOfInputsRead++;
	if (strcmp(scan, "") != 0)
		numberOfInputsRead++;
	if (numberOfInputs != numberOfInputsRead)
		throw ValidateException("Invalid number of parameters!\n");
}

void Validator::validateAge(const char* age)
{

	int i=0;
	for (; age[i]; i++)
		if (!(age[i] >= '0' && age[i] <= '9'))
			throw(ValidateException("Invalid age!\n"));
}

void Validator::validateName(const char* name)
{
	if (strlen(name)<1)
		throw(ValidateException("Invalid name!\n"));
}

void Validator::validateSpecies(const char* species)
{
	if (strlen(species) < 1)
		throw(ValidateException("Invalid species!\n"));
}

void Validator::validateScan(const char* scan)
{
	if (strlen(scan) < 1)
		throw(ValidateException("Invalid scan!\n"));
}

void Service::performUndoRedo(bool undo,Operations* operation)
{
	Operations* performUndoOrRedo;
	if (undo)
		performUndoOrRedo = (UndoOperations*)operation;
	else
		performUndoOrRedo = (RedoOperations*)operation;

	std::vector<operationStruct> operationList = operation->getList();
	int indexOfOperation = operation->getIndex();
	Plant *firstPlant=new Plant{ operationList[indexOfOperation - 1].firstPlant.getCodedName(),
		operationList[indexOfOperation - 1].firstPlant.getSpecies(),
		operationList[indexOfOperation - 1].firstPlant.getAge(), operationList[indexOfOperation - 1].firstPlant.getScan() };
	Plant *secondPlant=new Plant{ operationList[indexOfOperation - 1].secondPlant.getCodedName(),
		operationList[indexOfOperation - 1].secondPlant.getSpecies(),
		operationList[indexOfOperation - 1].secondPlant.getAge(), operationList[indexOfOperation - 1].secondPlant.getScan() };
	
	if (operationList[indexOfOperation-1].type.compare("add") == 0)
	{
		
		performUndoOrRedo->operateOnAdd(firstPlant);
	}
	else if (operationList[indexOfOperation - 1].type.compare("delete") == 0)
	{
		performUndoOrRedo->operateOnRemove(firstPlant);
		
	}
	else if (operationList[indexOfOperation - 1].type.compare("update") == 0)
	{
		performUndoOrRedo->operateOnUpdate(firstPlant, secondPlant);
	}
	delete firstPlant;
	delete secondPlant;
}

UndoOperations::UndoOperations(Service* serviceCaller)
{
	this->serviceCaller = serviceCaller;
	//this->redoOperations = this->serviceCaller->redoCaller;
	this->indexOfOperation = 0;
}

RedoOperations::RedoOperations(Service* serviceCaller)
{
	this->serviceCaller = serviceCaller;
	//this->undoOperations = this->serviceCaller->undoCaller;
	this->indexOfOperation = 0;
}

int UndoOperations::getIndex()
{
	return this->indexOfOperation;
}

int RedoOperations::getIndex()
{
	return this->indexOfOperation;
}

void UndoOperations::increaseIndex()
{
	this->indexOfOperation++;
}

void UndoOperations::decreaseIndex()
{
	this->indexOfOperation--;
}

void RedoOperations::increaseIndex()
{
	this->indexOfOperation++;
}

void RedoOperations::decreaseIndex()
{
	this->indexOfOperation--;
}

std::vector<operationStruct> UndoOperations::getList()
{
	return this->operationsList;
}

std::vector<operationStruct> RedoOperations::getList()
{
	return this->operationsList;
}

void UndoOperations::addToList(std::string type, Plant firstPlant, Plant secondPlant)
{
	operationStruct newOperation{ type,firstPlant,secondPlant };
	this->operationsList.push_back(newOperation);
	this->increaseIndex();
}

void RedoOperations::addToList(std::string type, Plant firstPlant, Plant secondPlant)
{
	operationStruct newOperation{ type,firstPlant,secondPlant };
	this->operationsList.push_back(newOperation);
	this->increaseIndex();
}

void UndoOperations::operateOnAdd(Plant* firstPlant)
{
	Plant nullPlant{};
	this->serviceCaller->repoCaller->deletePlantFromRepo(firstPlant->getCodedName());
	this->decreaseIndex();
	this->redoOperations->addToList("add", *firstPlant, nullPlant);
	
}

void UndoOperations::operateOnRemove(Plant* firstPlant)
{
	Plant nullPlant{};
	this->serviceCaller->repoCaller->addPlantToRepo(*firstPlant);
	this->decreaseIndex();
	this->redoOperations->addToList("delete", *firstPlant, nullPlant);
}

void UndoOperations::operateOnUpdate(Plant* firstPlant, Plant* secondPlant)
{
	this->serviceCaller->repoCaller->updatePlantFromRepo(*firstPlant);
	this->decreaseIndex();
	this->redoOperations->addToList("update", *secondPlant, *firstPlant);

}

void RedoOperations::operateOnAdd(Plant* firstPlant)
{
	Plant nullPlant{};
	this->serviceCaller->repoCaller->addPlantToRepo(*firstPlant);
	this->serviceCaller->undoCaller->increaseIndex();
	this->decreaseIndex();
}

void RedoOperations::operateOnRemove(Plant* firstPlant)
{	
	Plant nullPlant{};
	this->serviceCaller->repoCaller->deletePlantFromRepo(firstPlant->getCodedName());
	this->serviceCaller->undoCaller->increaseIndex();
	this->decreaseIndex();
}

void RedoOperations::operateOnUpdate(Plant* firstPlant, Plant* secondPlant)
{
	this->serviceCaller->repoCaller->updatePlantFromRepo(*secondPlant);
	this->serviceCaller->undoCaller->increaseIndex();
	this->decreaseIndex();
}
