# FisheryKnowledgeBase

KnowledgeBase is the 3rd module, for distributed Fishery project, which scrapes information from the web about products/services offered by the project. The application was written in **Java**. The communication is possible through REST API, which is documented with **OpenAPI** (Swagger2).

The module was hosted on **Heroku** and was build using **Spring Boot** with mLab **MongoDB** database. FisheryKnowledgeBase is based on **REST architectural style, NoSQL database, web crawling and scraping, concurrency**.

### Gallery [[more]](https://t3r1jj.github.io/DistributedFisheryProject)
![fisheryknowledgebase](https://user-images.githubusercontent.com/20327242/27429022-1cab8542-5744-11e7-9d8e-622a383d8d2a.png)

### API
The API allows fetching information from external resources about:
- articles
- fishes
- fisheries
- weather

Check out wiki for more information.

### Configuration
The configuration has been placed in application.properties file. It contains server properties (port, context, etc.), database connection settings and properties required for authorized communication. 

### Communication
The majority of the API is open for use based on licenses provided in project license page (*/license.html*). To use articles API a so called TOTP token is needed. [The totp4j lib](https://github.com/T3r1jj/totp4j) has been created for consistent and easy token generation in Java.

Exemplary private parameters to generate a token are as follows:

* **INTERVAL** = 30
* **KEY** = "plotka"
* **TOKEN_LENGTH** = 8
* **HMAC_ALGORITHM** = "HmacSha256"
