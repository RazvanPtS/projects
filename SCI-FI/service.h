#pragma once
#include "domain.h"

struct operationStruct {
	std::string type;
	Plant firstPlant;
	Plant secondPlant;
};

class Operations {
	friend class Service;
	
public:
	virtual void operateOnAdd(Plant*)=0;
	virtual void operateOnRemove(Plant*)=0;
	virtual void operateOnUpdate(Plant*,Plant*)=0;
	virtual void addToList(std::string, Plant, Plant)=0;
	virtual void increaseIndex() = 0;
	virtual void decreaseIndex() = 0;
	virtual int getIndex() = 0;
	virtual std::vector<operationStruct> getList() = 0;
};

class UndoOperations : public Operations {

public:
	std::vector<operationStruct> operationsList;
	int indexOfOperation;
	Service* serviceCaller;
	Operations* redoOperations;
	UndoOperations(Service*);
	void operateOnAdd(Plant*);
	void operateOnRemove(Plant*);
	void operateOnUpdate(Plant*, Plant*);
	void increaseIndex();
	void decreaseIndex();
	int getIndex();
	void addToList(std::string, Plant, Plant);
	std::vector<operationStruct> getList();
};


class RedoOperations : public Operations {
public:
	std::vector<operationStruct> operationsList;
	int indexOfOperation;
	Service* serviceCaller;
	Operations* undoOperations;
	RedoOperations(Service*);
	void operateOnAdd(Plant*);
	void operateOnRemove(Plant*);
	void operateOnUpdate(Plant*, Plant*);
	void increaseIndex();
	void decreaseIndex();
	int getIndex();
	void addToList(std::string, Plant, Plant);
	std::vector<operationStruct> getList();
};

class Service {
	
public:
	BaseRepo* repoCaller;
	UndoOperations* undoCaller;
	RedoOperations* redoCaller;
	Service(BaseRepo*);
	bool addPlantToList(const char*,const char*, const char*,const char*);
	std::vector<Plant> listPlants();
	bool updatePlantFromList(const char*, const char*, const char*, const char*);
	void deletePlantFromList(const char*);
	Plant nextPlant();
	void addToAssistantList(const char* name);
	std::vector<Plant> printAssistantList();
	std::vector<Plant> listSpeciesAndAge(const char*,const char*);
	void performUndoRedo(bool,Operations*);
};

class Validator
{
public:
	static void validateAge(const char*);
	static void validateName(const char*);
	static void	validateSpecies(const char*);
	static void validateScan(const char*);
	static void validateNumberOfParams(const char*, const char*, const char*, const char*, int);
};
class ValidateException {

public:
	ValidateException(const std::string& msg) : msg_(msg) {}
	~ValidateException() {}

	std::string printError() { return(msg_); }
private:
	std::string msg_;
};
