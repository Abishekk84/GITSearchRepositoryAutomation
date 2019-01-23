# GITSearchRepositoryAutomation
GIT Search Repository API Automation

Data Driven API Automation Framework
Programming Language : Java
Build tool: Maven
Libraries Used: httpcore,httpclient --> To build Rest Requests
                TestNG --> Test engine to run the Test scripts
                json --> To parse the JSON
                ApachePOI --> To Read data from Excel file

Steps to invoke the Automation script:
1. Setup TestData for different scenario's using the excel available in the following path:
    "user.dir"+ "\src\main\java\com\qa\data\API_TestData.xlsx"
    
    Sheet: StatusCode  (Verify the different status codes returned for a API request against the data provided in excel)
    URL--> API URL requested
    StatusCode --> The expected status code in Reponse for the URL requested
    
    Sheet: Headers (Verify the different header values returned for a API request against the data provided in excel)
    URL--> API URL requested 
    ex:
    Content-type--> The expected content-type in header response 
    Link---> The expected Link in content response
    
    Sheet: JSONData (Verify the different JSON values returned for a API request against the JSON Path data provided in excel)
    URL--> API URL requested 
    ex:
    /incomplete_results--> The expected incomplete_results in JSON response 
    /items[0]/id---> The /items[0]/id in JSON response 
    
    Sheet: UserName (Verify the different API response for different username data as provided in the excel)
    URL--> API URL requested 
    ex:
    satuscode -->Expected status code for the different userName requested
    Status-->Expected status for the different userName requested
    Message-->Expected Message
    /items[0]/owner/login-->Expected userName to match with API request
    
    Sheet: Sort (Verify the different API response for different sort parameter and sort order chosen)
    URL--> API URL requested 
    ex:
    sortParam -->NULL uses default sort based on score
    SortOrder-->Sort order for the Sort Param defined (Not applicabe for default values)         
    
2. Run the GetTest.Java available in "user.dir"+\src\test\java\com\qa\test\GetTest.java using TestNG run config

3. Verify the TestNG Results
        
