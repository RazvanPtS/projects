#pragma once
#include <vector>
#include <string>
class Plant {
private:
	char codedName[20];
	char species[20];
	char age[10];
	char scan[20];
public:
	Plant();
	Plant(const char* codedName,const char* species,const char* age,const char* scan);
	char* getCodedName();
	char* getSpecies();
	char* getScan();
	char* getAge();
	void setName(const char* name);
	void setSpecies(const char* species);
	void setAge(const char* age);
	void setScan(const char* scan);
	static bool eq(Plant, Plant);
	friend std::istream& operator>>(std::istream& is, Plant& plant);
	friend std::ostream& operator<<(std::ostream& os, const Plant& plant);
	
};

class BaseRepo {
public:

	virtual void addPlantToRepo(Plant) = 0;
	virtual void updatePlantFromRepo(Plant&) = 0;
	virtual Plant deletePlantFromRepo(const char*) = 0;
	virtual Plant currentPlantToList() = 0;
	virtual void addCurrentToAssistantList(const char* name) = 0;
	virtual std::vector<Plant> getAllFromAssistantList() = 0;
	virtual std::vector<Plant> assistantListBy(char* species,char* age) = 0;
	virtual std::vector<Plant> getListOfPlants() = 0;
	virtual bool isFile()=0;
	
};

class FileRepo:public BaseRepo
{
private:
	int currentToShow;
	std::string filePath;
	std::string filePathAssistantList;
public:
	FileRepo(std::string, std::string);
	~FileRepo();
	void addPlantToRepo(Plant);
	void updatePlantFromRepo(Plant&);
	Plant deletePlantFromRepo(const char*);
	Plant currentPlantToList();
	void addCurrentToAssistantList(const char* name);
	std::vector<Plant> getAllFromAssistantList();
	std::vector<Plant> assistantListBy(char* species, char* age);
	std::vector<Plant> getListOfPlants();
	std::string getFilePath();
	bool isFile();

};


class MemoryRepo:public BaseRepo
{
private:
	std::vector < Plant> listOfPlants;
	std::vector < Plant> assistantList;
	int size;
	int assistantListPosition;
	int currentToShow;
public:
	MemoryRepo();
	~MemoryRepo();
	void addPlantToRepo(Plant);
	void updatePlantFromRepo(Plant&);
	Plant deletePlantFromRepo(const char*);
	Plant currentPlantToList();
	void addCurrentToAssistantList(const char* name);
	std::vector<Plant> getAllFromAssistantList();
	std::vector<Plant> assistantListBy(char* species, char* age);
	std::vector<Plant> getListOfPlants();
	int getSize();
	char* plantsToString();
	bool isFile();


};

class RepoValidator
{
public:
	static void checkExistingNameFile(const char*, FileRepo*,bool);
	static void checkExistingNameMemory(const char*, MemoryRepo*,bool);
};
class RepoException {

public:
	RepoException(const std::string& msg) : msg_(msg) {}
	~RepoException() {}

	std::string printError() { return(msg_); }
private:
	std::string msg_;
};
