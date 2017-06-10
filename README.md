# FisheryKnowledgeBase

[![v1.4.1 build status](https://gitlab.com/Druzyna-A/KnowledgeBase/badges/v1.4.1/build.svg)](https://gitlab.com/Druzyna-A/KnowledgeBase/commits/v1.4.1) - v1.4.1

KnowledgeBase is a RSI-Fishery project (3rd) module which scrapes information from the web about products/services offered by the project. The communication is possible through REST API documented with OpenAPI (Swagger2).

The module is currently hosted on Heroku at: https://fishery-knowledge-base.herokuapp.com  
Current API version 1.2.5 is supposed to be stable. Future versions should be backward compatible (with 1.0.0 version).

**Check out wiki pages for more info.**
***
## Communication
The majority of the API is open for use based on licenses provided in project license page (*/license.html*). To use articles API a so called TOTP token is needed. If you're using Java you can use [the totp4j module](https://gitlab.com/Druzyna-A/totp4j) for consistent and easy token generation.

Required private parameters to generate a token are as follows (**DO NOT PUBLISH**):

* **INTERVAL** = 30
* **KEY** = "plotka"
* **TOKEN_LENGTH** = 8
* **HMAC_ALGORITHM** = "HmacSha256"
