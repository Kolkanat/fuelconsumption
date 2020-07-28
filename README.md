# fuelconsumption

Author: Kolkanat Bashpayev
Location: Almaty, Kazakhstan
email: kolkanat.bashpayev@gmail.com

#project has two main root end points:
- /consumption/register - is for registering single or bulk entries, for bulk registration it accepts csv (csv file example is on /src/main/resources/csv_file_example.csv)
- /report - is for looking analytics

#project also covered with unit and integration tests
- com.org.controller.FuelControllersTest - tests for end point Controllers, in this test file is uploaded and analytics is checked
- com.org.repository.FuelRepositoryTest.eachMonthStatisticsGroupByFuelTypeTest - unit tests of repository



#Currently project uses inmemory db as a data storage.

# requirements

https://github.com/Kolkanat/fuelconsumption/blob/master/backend-homework-200418-1516-102.pdf
