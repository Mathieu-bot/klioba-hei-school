# klioba-hei-school

## Running

### On cloud, using JCloudify

This app is hosted on [JCloudify](https://www.jcloudify.com). It's the best way we know to host Spring Boot applications.
Within a few clicks, you have your Spring Boot running with a publicly accessible URL, and with your CI/CD pipelines automatically configured on GitHub.

### Locally

First, set all following environment variables:

```
# Database (PostgreSQL)
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
SPRING_FLYWAY_OUTOFORDER=

# Casdoor authentication (OAuth2)
SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_CASDOOR_ISSUERURI=
SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_CASDOOR_USERNAMEATTRIBUTE=
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_CASDOOR_CLIENTID=
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_CASDOOR_CLIENTSECRET=
SPRING_SECURITY_OAUTH2_CLIENT_REDIRECTURI=
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_CASDOOR_SCOPE_0_=openid
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_CASDOOR_SCOPE_1_=profile
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_CASDOOR_SCOPE_2_=email
CASDOOR_LOGOUT_URL=
APP_LOGOUT_URL=

# AWS
AWS_S3_BUCKET=
AWS_SES_SOURCE=

# Payment provider
VOLA_API_URL=
VOLA_API_KEY=

# Server configuration
SERVER_ERROR_INCLUDEMESSAGE=
```

Then, run Spring Boot as usual,
for example by building an uber jar through `gradle bootJar`,
then by launching `java -jar app.jar`.
As there are a lot of environment variables to set,
you probably want to load them through an `.env` file:
`export $(cat .env | xargs) && java -jar app.jar`.

Last, visit `http://localhost:8080`.
