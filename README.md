# vulnerable-petstore
This is an implementation of the PetStore with SQL injection willingly inserted in the code in order to test a security tool. 

## Endpoints
### addPet
This allows to insert a new Pet object into the underlying database

Path: /v2/pet/

Parameters: Pet


### deletePet
This allows to delete an existing Pet from the underlying database

Path: /v2/pet/{petId}

Parameters: id of the pet to delete and optionally api key


### findByStatus
This allows to find pets having the given statuses

Path: /v2/pet/findByStatus

Parameters: one or more statuses


### findByTags
This allows to find pets having the given tags

Path: /v2/pet/findByTags

Parameters: one or more tags

### getPetById
This allows to find a pet having a specific id

Path: /v2/pet/{petId}

Parameters: the id of the pet to be looked up for


### getPets
This allows to retrieve all pets in the database

Path: /v2/pet

Parameters: none


### updatePet
This allows to update an existing Pet

Path: /v2/pet

Parameters: the Pet to be updated

### reset
This allows to reset the database, setting the autoincrement counters for the IDs to 1

Path: /v2/pet/reset

Parameters: none
