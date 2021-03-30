#include "domain.h"
#include <string.h>
#include <iostream>
#include <stdlib.h>
#include <fstream>
#include <Windows.h>
Plant::Plant(const char* codedName,const char* species,const char* age,const char* scan)
{	//constructor
	strcpy(this->codedName, codedName);
	strcpy(this->species, species);
	strcpy(this->scan, scan);
	strcpy(this->age ,age);
}

Plant::Plant()
{	//constructor - default
	strcpy(this->codedName, "");
	strcpy(this->species, "");
	strcpy(this->scan, "");
	strcpy(this->age ,"0");
}
void Plant::setAge(const char* age)
{	//sets the age of a plant
	strcpy(this->age, age);
}

void Plant::setName(const char* name)
{	//changes the name of a plant to inputted name(parameter)
	strcpy(this->codedName, name);
}

void Plant::setScan(const char* scan)
{	//changes the scan state of a plant to inputted scan(parameter)
	strcpy(this->scan, scan);
}

void Plant::setSpecies(const char* species)
{	//sets the species of a plant to inputted species(paramater)
	strcpy(this->species, species);
}

char* Plant::getCodedName()
{	//returns the name(char*) of a plant
	return this->codedName;
}

char* Plant::getSpecies()
{	//returns the species(char*) of a plant
	return this->species;
}

char* Plant::getAge()
{	//returns age(ont) of a plant
	return this->age;
}

char* Plant::getScan()
{	//returns scan(char*) of a plant
	return this->scan;
}

bool Plant::eq(Plant firstPlant, Plant secondPlant)
{
	return !(strcmp(firstPlant.getCodedName(),secondPlant.getCodedName()) || strcmp(firstPlant.getSpecies(),secondPlant.getSpecies()) || strcmp(firstPlant.getAge(),secondPlant.getAge())
		|| strcmp(firstPlant.getScan(), secondPlant.getScan()));
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


std::ostream& operator<<(std::ostream& os, const Plant& plant)
{

	os << plant.codedName << "," << plant.species << "," << plant.age<<","<<plant.scan << "\n";
	return os;
}

std::istream& operator>>(std::istream& is,Plant& plant)
{
	char line[200];
	char detailsOfPlant[5][20],*p;
	is.getline(line, 200);
	if (strcmp(line, "") != 0)
	{
		p = strtok(line, ",");
		int i = 0;
		while (p)
		{
			strcpy(detailsOfPlant[i++], p);
			p = strtok(NULL, ",");
		}
		strcpy(plant.codedName, detailsOfPlant[0]);
		strcpy(plant.species, detailsOfPlant[1]);
		strcpy(plant.age, detailsOfPlant[2]);
		strcpy(plant.scan, detailsOfPlant[3]);
	}
	return is;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void RepoValidator::checkExistingNameFile(const char* name,FileRepo* repo,bool foundState)
{
		bool found = false;
		std::ifstream fileHandlerIn(repo->getFilePath());
		std::vector<Plant> listOfPlants;
		Plant readPlant;
		fileHandlerIn >> readPlant;
		while (fileHandlerIn)
		{

			listOfPlants.push_back(readPlant);
			fileHandlerIn >> readPlant;
		}
		fileHandlerIn.close();
		for (auto i : listOfPlants)
			if (strcmp(i.getCodedName(), name) == 0)
				found = true;
		if (found == true && foundState == false)
			throw RepoException("Name found already!\n");
		else if (found == false && foundState == true)
			throw RepoException("Name not found!\n");
}

void RepoValidator::checkExistingNameMemory(const char* name, MemoryRepo* repo, bool foundState)
{
	bool found = false;
	std::vector<Plant> listOfPlants = repo->getListOfPlants();
	for (auto i : listOfPlants)
		if (strcmp(i.getCodedName(), name) == 0)
			found = true;
	if (found == true && foundState == false)
		throw RepoException("Name found already!\n");
	else if (found == false && foundState == true)
		throw RepoException("Name not found!\n");
}

///File Repo

FileRepo::FileRepo(std::string filePath1,std::string filePath2)
{	//constructor
	this->filePath = filePath1;
	this->filePathAssistantList = filePath2;
	this->currentToShow = 0;
}

bool FileRepo::isFile()
{
	return true;
}

void FileRepo::addPlantToRepo(Plant newPlant)
{	//adds the newPlant (parameter - Plant type) to the fileRepo
	int sizeOfFile=0;
	std::ifstream fileHandlerIn(this->filePath);
	std::vector<Plant> listOfPlants;
	Plant readPlant;
	fileHandlerIn >> readPlant;
	while (fileHandlerIn)
	{
		
		listOfPlants.push_back(readPlant);
		sizeOfFile++;
		fileHandlerIn >> readPlant;
	}

	listOfPlants.push_back(newPlant);
	sizeOfFile++;
	fileHandlerIn.close();

	std::ofstream fileHandlerOut(this->filePath);
	for (int i = 0; i < sizeOfFile; i++)
		fileHandlerOut << listOfPlants[i];
	fileHandlerOut.close();

	
}

void FileRepo::updatePlantFromRepo(Plant &plantToUpdate)
{	//update the plant in fileRepo that matches the newPlant's id (newPlant - update for old plant)
	int sizeOfFile = 0;
	std::ifstream fileHandlerIn(this->filePath);
	std::vector<Plant> listOfPlants;
	Plant readPlant;
	fileHandlerIn >> readPlant;
	while (fileHandlerIn)
	{

		listOfPlants.push_back(readPlant);
		sizeOfFile++;
		fileHandlerIn >> readPlant;
	}
	fileHandlerIn.close();

	for(auto i=listOfPlants.begin();i!=listOfPlants.end();i++)
		if (strcmp((*i).getCodedName(), plantToUpdate.getCodedName()) == 0)
		{
			Plant newPlant;
			(*i).setName(plantToUpdate.getCodedName());
			newPlant.setSpecies((*i).getSpecies());
			(*i).setSpecies(plantToUpdate.getSpecies());
			newPlant.setAge((*i).getAge());
			(*i).setAge(plantToUpdate.getAge());
			newPlant.setScan((*i).getScan());
			(*i).setScan(plantToUpdate.getScan());
			plantToUpdate = newPlant;
		}

	std::ofstream fileHandlerOut(this->filePath);
	for (int i = 0; i < sizeOfFile; i++)
		fileHandlerOut << listOfPlants[i];
	fileHandlerOut.close();
}

Plant FileRepo::deletePlantFromRepo(const char* name)
{	
	//deletes from the file the plant with the given name
	int sizeOfFile = 0;
	Plant newPlant;
	std::ifstream fileHandlerIn(this->filePath);
	std::vector<Plant> listOfPlants;
	Plant readPlant;
	fileHandlerIn >> readPlant;
	while (fileHandlerIn)
	{

		listOfPlants.push_back(readPlant);
		sizeOfFile++;
		fileHandlerIn >> readPlant;
	}
	fileHandlerIn.close();

	for (auto i = listOfPlants.begin(); i != listOfPlants.end(); i++)
		if (strcmp((*i).getCodedName(),name) == 0)
		{
			newPlant.setName((*i).getCodedName());
			newPlant.setAge((*i).getAge());
			newPlant.setScan((*i).getScan());
			newPlant.setSpecies((*i).getSpecies());
			listOfPlants.erase(i);
			sizeOfFile--;
			break;
		}

	std::ofstream fileHandlerOut(this->filePath);
	for (int i = 0; i < sizeOfFile; i++)
		fileHandlerOut << listOfPlants[i];
	fileHandlerOut.close();
	return newPlant;
}

FileRepo::~FileRepo()
{	//deconstructor
}

std::vector<Plant> FileRepo::getListOfPlants()
{
	// returns the plants from the file as a vector
	std::ifstream fileHandlerIn(this->filePath);
	std::vector<Plant> listOfPlants;
	Plant readPlant;
	fileHandlerIn >> readPlant;
	while (fileHandlerIn)
	{

		listOfPlants.push_back(readPlant);
		fileHandlerIn >> readPlant;
	}
	fileHandlerIn.close();
	return listOfPlants;
}

Plant FileRepo::currentPlantToList()
{	//returns the next plant to be displayed to the assistant	
	int sizeOfFile = 0;
	std::ifstream fileHandlerIn(this->filePath);
	std::vector<Plant> listOfPlants;
	Plant readPlant;
	fileHandlerIn >> readPlant;
	while (fileHandlerIn)
	{

		listOfPlants.push_back(readPlant);
		sizeOfFile++;
		fileHandlerIn >> readPlant;
	}
	fileHandlerIn.close();
	if (this->currentToShow == sizeOfFile)
		this->currentToShow = 0;
	return listOfPlants[this->currentToShow++];
}

std::vector<Plant> FileRepo::getAllFromAssistantList()
{	//returns all the plants available in the assistant's list( as a vector ) 
		std::vector<Plant> bla;
		system(this->filePathAssistantList.c_str());
		return bla;
}

void FileRepo::addCurrentToAssistantList(const char* name)
{	//adds the plant with the given name as a parameter to the assistant's list
	
	std::vector<Plant> listOfPlants;
	Plant readPlant;
	
	std::ifstream fileHandler(this->filePath);
	fileHandler >> readPlant;
	while (fileHandler)
	{

		listOfPlants.push_back(readPlant);
		fileHandler >> readPlant;
	}
	fileHandler.close();

	if (this->filePathAssistantList[this->filePathAssistantList.size() - 1] == 'v')
	{

		std::vector<Plant> assistantList;
		std::ifstream fileHandlerIn(this->filePathAssistantList);
		Plant readPlant;
		fileHandlerIn >> readPlant;
		while (fileHandlerIn)
		{

			assistantList.push_back(readPlant);
			fileHandlerIn >> readPlant;
		}
		fileHandlerIn.close();

		std::ofstream fileHandlerOut(this->filePathAssistantList);
		for (auto i : assistantList)
			fileHandlerOut << i;

		int i = 0;
		for (auto i : listOfPlants)
			if (strcmp(name, i.getCodedName()) == 0)
			{
				fileHandlerOut << i;
			}
		fileHandlerOut.close();
	}
	else
	{

		std::ifstream fileHandlerIn(this->filePathAssistantList);
		std::string lineInHtml;
		std::string nameOfPlant;
		std::string species;
		std::string age;
		std::string scan;
		std::vector<Plant> assistantList;
		std::getline(fileHandlerIn, lineInHtml);
		while (lineInHtml.compare("</table>") != 0 && lineInHtml.size()>0)
		{
			while (lineInHtml.compare("<tr>") != 0)
				std::getline(fileHandlerIn, lineInHtml);
			std::getline(fileHandlerIn, nameOfPlant);
			std::getline(fileHandlerIn, species);
			std::getline(fileHandlerIn, age);
			std::getline(fileHandlerIn, scan);
			nameOfPlant.erase(0, 4);
			species.erase(0, 4);
			age.erase(0, 4);
			scan.erase(0, 4);
			nameOfPlant.erase(nameOfPlant.size() - 5, nameOfPlant.size() - 1);
			species.erase(species.size() - 5, species.size() - 1);
			age.erase(age.size() - 5, age.size() - 1);
			scan.erase(scan.size() - 5, scan.size() - 1);
			Plant newPlant{ nameOfPlant.c_str(),species.c_str(),age.c_str(),scan.c_str() };
			assistantList.push_back(newPlant);
			std::getline(fileHandlerIn, lineInHtml);
			std::getline(fileHandlerIn, lineInHtml);
		}
		fileHandlerIn.close();
		std::ofstream fileHandlerOut(this->filePathAssistantList);
		std::string startHtmlFile = "<!DOCTYPE html>\n<html>\n<head>\n<title>Plants</title>\n</head>\<body>\n<table border = \"1\">\n<tr>\<td>Name</td>\n<td>Species</td>\n<td>Age</td>\n<td>Scan</td>\n</tr>\n";
		std::string endHtmlFile = "</table>\n</body>\n</html>\n";
		fileHandlerOut << startHtmlFile;


		for (auto i : assistantList)
		{
				fileHandlerOut << "<tr>\n";
				fileHandlerOut << "<td>";
				fileHandlerOut << i.getCodedName();
				fileHandlerOut << "</td>\n";
				fileHandlerOut << "<td>";
				fileHandlerOut << i.getSpecies();
				fileHandlerOut << "</td>\n";
				fileHandlerOut << "<td>";
				fileHandlerOut << i.getAge();
				fileHandlerOut << "</td>\n";
				fileHandlerOut << "<td>";
				fileHandlerOut << i.getScan();
				fileHandlerOut << "</td>\n";
				fileHandlerOut << "</tr>\n";
		}
		for (auto i : listOfPlants)
		{	
			if (strcmp(name, i.getCodedName()) == 0)
			{
				fileHandlerOut << "<tr>\n";
				fileHandlerOut << "<td>";
				fileHandlerOut << i.getCodedName();
				fileHandlerOut << "</td>\n";
				fileHandlerOut << "<td>";
				fileHandlerOut << i.getSpecies();
				fileHandlerOut << "</td>\n";
				fileHandlerOut << "<td>";
				fileHandlerOut << i.getAge();
				fileHandlerOut << "</td>\n";
				fileHandlerOut << "<td>";
				fileHandlerOut << i.getScan();
				fileHandlerOut << "</td>\n";
				fileHandlerOut << "</tr>\n";
			}
		}
		fileHandlerOut << endHtmlFile;
		fileHandlerOut.close();
	}
}

std::vector<Plant> FileRepo::assistantListBy(char* species,char* age)
{	//returns the plants as a vector that match the filtering rules given by species and age parameters
	if (strcmp(age, "") == 0)
	{
		strcpy(age, species);
		strcpy(species, "");
	}
	int i = 0;
	int sizeOfFile = 0;
	std::ifstream fileHandlerIn(this->filePath);
	std::vector<Plant> plantList;
	std::vector<Plant> listToReturn;
	Plant readPlant;
	fileHandlerIn >> readPlant;
	while (fileHandlerIn)
	{
		plantList.push_back(readPlant);
		sizeOfFile++;
		fileHandlerIn >> readPlant;
	}
	fileHandlerIn.close();

	for (auto i : plantList)
	{
		if ((strcmp(species, "") == 0 || strcmp(i.getSpecies(), species) == 0) && atoi(i.getAge()) < atoi(age))
		{
			listToReturn.push_back(i);
		}
	}
	return listToReturn;

}

std::string FileRepo::getFilePath()
{
	return this->filePath;
}

///////// Memory MemoryRepo

MemoryRepo::MemoryRepo()
{	//constructor
	this->assistantListPosition = 0;
	this->size = 0;
	this->currentToShow = 0;
}

bool MemoryRepo::isFile()
{
	return false;
}

void MemoryRepo::addPlantToRepo(Plant newPlant)
{	//adds the newPlant (parameter - Plant type) to the repo
	this->listOfPlants.push_back(newPlant);
	this->size++;
}

char* MemoryRepo::plantsToString()
{	// returns the plants from the repository as a string of chars;
	std::vector<Plant> plantList;
	plantList = this->listOfPlants;
	char returningString[200] = "";
	for (auto i : plantList)
	{
		strcat(returningString, i.getCodedName());
		strcat(returningString, ",");
		strcat(returningString, i.getSpecies());
		strcat(returningString, ",");
		strcat(returningString, i.getAge());
		strcat(returningString, ",");
		strcat(returningString, i.getScan());
		strcat(returningString, "\n");
	}
	return returningString;
}

void MemoryRepo::updatePlantFromRepo(Plant &plantToUpdate)
{	//update the plant in repo that matches the newPlant's id (newPlant - update for old plant)
	for (auto i = this->listOfPlants.begin(); i != this->listOfPlants.end(); i++)
	{
		if (strcmp((*i).getCodedName(), plantToUpdate.getCodedName()) == 0)
		{
			Plant newPlant;
			newPlant.setName((*i).getCodedName());
			(*i).setName(plantToUpdate.getCodedName());
			newPlant.setSpecies((*i).getSpecies());
			(*i).setSpecies(plantToUpdate.getSpecies());
			newPlant.setAge((*i).getAge());
			(*i).setAge(plantToUpdate.getAge());
			newPlant.setScan((*i).getScan());
			(*i).setScan(plantToUpdate.getScan());
			plantToUpdate = newPlant;
			break;
		}
	}
}

Plant MemoryRepo::deletePlantFromRepo(const char* name)
{	// deletes from repo the plant that matches the user input name
	Plant newPlant;
	for (auto i = this->listOfPlants.begin(); i != this->listOfPlants.end(); i++)
	{

		if (strcmp((*i).getCodedName(), name) == 0)
		{
			newPlant.setName((*i).getCodedName());
			newPlant.setAge((*i).getAge());
			newPlant.setScan((*i).getScan());
			newPlant.setSpecies((*i).getSpecies());
			this->listOfPlants.erase(i);
			this->size--;
			break;
		}
	}
	return newPlant;
}

MemoryRepo::~MemoryRepo()
{	//deconstructor
}

Plant MemoryRepo::currentPlantToList()
{	//returns the next plant to be displayed to the assistant	
	std::vector<Plant> newList = this->listOfPlants;
	if (this->currentToShow == this->size)
		this->currentToShow = 0;
	return newList[this->currentToShow++];
}

void MemoryRepo::addCurrentToAssistantList(const char* name)
{	//adds the plant with the given name as a parameter to the assistant's list
	std::vector<Plant> newList = this->listOfPlants;
	int i = 0;
	for (auto i : newList)
		if (strcmp(name, i.getCodedName()) == 0)
		{
			this->assistantList.push_back(i);
			this->assistantListPosition++;
		}
}

std::vector<Plant> MemoryRepo::getAllFromAssistantList()
{	//returns all the plants available in the assistant's list( as a string ) through the out parameter
	return this->assistantList;
}

int MemoryRepo::getSize()
{
	return this->size;
}

std::vector<Plant> MemoryRepo::assistantListBy(char* species,char* age)
{	//adds to the out parameter as a string only the plants that match the filtering rules given by species and age parameters
	if (strcmp(age, "") == 0)
	{
		strcpy(age, species);
		strcpy(species, "");
	}
	int i = 0;
	std::vector<Plant> plantList = this->listOfPlants;
	std::vector<Plant> listToReturn;
	for (auto i : plantList)
	{
		if ((strcmp(species, "") == 0 || strcmp(i.getSpecies(), species) == 0) && atoi(i.getAge()) < atoi(age))
		{
			listToReturn.push_back(i);
		}
	}
	return listToReturn;
}

std::vector<Plant> MemoryRepo::getListOfPlants()
{
	return this->listOfPlants;
}