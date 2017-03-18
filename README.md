# KnowledgeBase
KnowledgeBase is a RSI-Fishery project (3rd) module which scrapes information from the web about products/services offered by the project. The communication is possible through REST API documented with OpenAPI (Swagger2).

The module is currently hosted on Heroku at: https://fishery-knowledge-base.herokuapp.com  
Current API version 1.2.2 is supposed to be stable. Future versions should be backward compatible.
***
### Communication
The majority of the API is open for use based on licenses provided in project license page (*/license.html*). To use articles API a so called TOTP token is needed. If you're using Java you can use [the totp4j module](https://gitlab.com/Druzyna-A/totp4j) for consistent and easy token generation.

Required private parameters to generate a token are as follows (**DO NOT PUBLISH**):

* **INTERVAL** = 30
* **KEY** = "plotka"
* **TOKEN_LENGTH** = 8
* **HMAC_ALGORITHM** = "HmacSha256"
