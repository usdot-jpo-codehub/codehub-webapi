# codehub-webapi
CodeHub Web API to consume data from CodeHub ElasticSearch storage system.

## Usage
Once the application is running on a configured port it is required to submit a GET request to retrieve a list of repositories or individual repository or a POST request to search for repositories.

Three endpoints were defined to request for data:

### Repositories

To list a set of repositories.

  - Method: GET
  - URL: http://[host:port]/api/v1/repositories?limit=10?rank=popular&order=desc&owner=jpo-ode

To get a set of repositories by it's id.

  - Method: GET
  - URL: http://[host:port]/api/v1/repositories/{Id1, Id2,...Id(n)}


### Metrics

Gets the aggregation of the metrics of all the data.
  - Method: GET
  - URL: http://[host:port]/api/v1/metrics

Gets the aggregation of the metrics by Owners
- Method: GET
  - URL: http://[host:port]/api/v1/metrics/{owner1, owner2,...,owner(n)}

### Search

 - Method: POST
 - URL: http://[host:port]/api/v1/search
 - Content-Type: application/json
 - Payload (sample)
```json
  {
    "term" : "data",
    "limit" : 1000,
    "matchAll" : false
  }
```

#### cURL

```bash
curl 'http://example.com:3000/api/v1/search' -i -X POST \
    -H 'Content-Type: application/json' \
    -d '{
  "term" : "data",
  "limit" : 1000,
  "matchAll" : false
}'
```

### Categories

To list a set of repositories.

  - Method: GET
  - URL: http://[host:port]/api/v1/configurations/categories

To get a set of repositories by it's id.

  - Method: GET
  - URL: http://[host:port]/api/v1/configurations/categories/{Id}


### Response
The response object provides a general response information and the actual data is associated with the "result" property.

#### Repositories List
Response sample
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "timestamp" : "2020-01-02T23:12:05Z",
  "status" : "OK",
  "code" : 200,
  "path" : "http://localhost",
  "verb" : "GET",
  "traceid" : "20200102231205568",
  "result" : [ {
    "id" : "6ff840548453424392e661e8195aa63c",
    "sourceData" : {
      "commits" : 2604,
      "stars" : 2197,
      "watchers" : 756,
      "description" : "Description of the repository",
      "language" : "Javascript",
      "name" : "repository1",
      "repositoryUrl" : "http://owner1/repository1.url",
      "createdAt" : "2020-01-02T23:12:05.568+0000",
      "lastPush" : "2020-01-02T23:12:05.568+0000",
      "owner" : {
        "avatarUrl" : "http://owner1.avatar.url",
        "name" : "owner1",
        "type" : "Organization",
        "url" : "http://owner1.url"
      },
      "readme" : {
        "content" : "[README Content]",
        "url" : "http://repository1/readme.url"
      },
      "forks" : {
        "forkedRepos" : [ {
          "id" : "1",
          "name" : "repository11",
          "owner" : "fork-owner"
        } ]
      },
      "contributors" : [ {
        "avatarUrl" : "http://contributor1.avatar.url",
        "profileUrl" : "http://contributor1.profile.url",
        "userType" : "User",
        "username" : "contributor1"
      } ],
      "releases" : [ {
        "id" : "11",
        "tagName" : "Tag11",
        "name" : "Rel11",
        "totalDownloads" : 891,
        "assets" : [ {
          "id" : "Asset19",
          "name" : "Asset19",
          "label" : "Label19",
          "size" : 162297288,
          "downloadCount" : 891
        } ]
      } ],
      "languages" : {
        "CSS" : 75,
        "Javascript" : 840,
        "HTML" : 361
      }
    },
    "generatedData" : {
      "rank" : 2159,
      "sonarMetrics" : {
        "bugs" : {
          "frmt_val" : "327",
          "key" : "bugs",
          "val" : 327.0
        },
        "code_smells" : {
          "frmt_val" : "519",
          "key" : "code_smells",
          "val" : 519.0
        },
        "reliability_rating" : {
          "frmt_val" : "C",
          "key" : "reliability_rating",
          "val" : 3.0
        },
        "security_rating" : {
          "frmt_val" : "A",
          "key" : "security_rating",
          "val" : 1.0
        },
        "sqale_index" : {
          "frmt_val" : "B",
          "key" : "sqale_index",
          "val" : 40.0
        },
        "sqale_rating" : {
          "frmt_val" : "A",
          "key" : "sqale_rating",
          "val" : 65.0
        },
        "violations" : {
          "frmt_val" : "423",
          "key" : "violations",
          "val" : 423.0
        },
        "vulnerabilities" : {
          "frmt_val" : "416",
          "key" : "vulnerabilities",
          "val" : 416.0
        },
        "complexity" : {
          "frmt_val" : "8",
          "key" : "complexity",
          "val" : 8.0
        }
      },
      "vscan" : {
        "data_scanned" : "38 MB",
        "infected_files" : 1,
        "lastscan" : "2020-01-02T23:12:05.568+0000",
        "scanned_directories" : 95,
        "scanned_files" : 945,
        "time" : "206 sec",
        "reported_files" : [ {
          "filename" : "filename88",
          "virus" : "WORM594"
        } ]
      }
    },
    "codehubData" : {
      "etag" : "16a2f965a20b483f861dc043088a5783",
      "source" : "Github",
      "lastModified" : "2020-01-02T23:12:05.568+0000",
      "lastIngested" : "2020-01-02T23:12:05.568+0000",
      "badges" : {
        "status" : "Active",
        "isFeatured" : true
      },
      "categories" : [ "a73ea362-da1a-4c15-967f-d247eba50e0a", "1854ba95-021a-4257-8002-e940b6e2b63b" ],
      "isIngested" : true,
      "isIngestionEnabled" : true,
      "isVisible" : false
    }
  } ]
}
```

#### Metrics Response

Response sample where the "result" is an individual document.
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "timestamp" : "2020-01-02T23:12:05Z",
  "status" : "OK",
  "code" : 200,
  "path" : "http://localhost",
  "verb" : "GET",
  "traceid" : "20200102231205604",
  "result" : {
    "numberOfOrganizations" : 2,
    "numberOfProjects" : 10,
    "bugsVulnerabilities" : 7558,
    "technicalDebt" : 3658,
    "organizations" : [ "Organization1", "Organization2" ],
    "languageCountsStat" : {
      "Java" : 5,
      "Javascript" : 3,
      "Python" : 2
    },
    "languagePercentageStat" : {
      "Java" : 50.0,
      "Javascript" : 30.0,
      "Python" : 20.0
    },
    "metricsSummary" : {
      "releasibility" : {
        "values" : {
          "ERROR" : 0,
          "OK" : 8,
          "WARN" : 2
        }
      },
      "reliability" : {
        "values" : {
          "A" : 5,
          "B" : 2,
          "C" : 3,
          "D" : 0,
          "E" : 0
        }
      },
      "security" : {
        "values" : {
          "A" : 8,
          "B" : 2,
          "C" : 0,
          "D" : 0,
          "E" : 0
        }
      },
      "maintainability" : {
        "values" : {
          "A" : 3,
          "B" : 2,
          "C" : 2,
          "D" : 2,
          "E" : 1
        }
      }
    }
  }
}
```

#### Categories Response
Response sample

```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "timestamp" : "2020-02-14T18:18:38Z",
  "status" : "OK",
  "code" : 200,
  "path" : "http://localhost",
  "verb" : "GET",
  "traceid" : "20200214181838089",
  "result" : [ {
    "id" : "3b73cb2f-796e-408e-ad9f-9c2f6f292892",
    "name" : "Category-82",
    "description" : "This is the description",
    "lastModified" : "2020-02-14T18:18:38.089+0000",
    "isEnabled" : true
  }, {
    "id" : "fd84321f-1d33-49ef-aca3-65afc58fcfdc",
    "name" : "Category-42",
    "description" : "This is the description",
    "lastModified" : "2020-02-14T18:18:38.089+0000",
    "isEnabled" : true
  } ]
}
```

#### Search

The search response will provide a list of repositories like is the case of the "repositories" endpoint.

- - -

The following status codes are possible to have as part of a response.

- 200 : The request was successful and there is data available.
- 204 : The request was successful but there is no data available.
- 400 : The request is bad and does not contains the required information.
- 500 : Internal server error will be provided in case of errors.


## Configuration
The API requires the following environment variables

|Name   |Required   |Default   |Description|
|--|--|--|----|
|codehub.webapi.es.host|mandatory||Sets the host of the target ElasticSearch|
|codehub.webapi.es.port|mandatory||Sets the port that the target ElasticSearch is using.|
|codehub.webapi.es.scheme|mandatory||Sets the protocol scheme used by the target ElasticSearch (http or https)|
|datahub.ui.url.endpoint|mandatory||Sets the DataHub target URL ( http://[host]:[port] )|
|datahub.ui.url.questring|optional|/#/search?t=|DataHub query string for the Search page.|
|codehub.webapi.es.index.repositories|optional|repositories|Set the Index name to be used as main source of the data.|
|codehub.webapi.es.related|optional|related|Set the Index name to be used as source of related DataHub information|
|codehub.webapi.es.limit|optional|1000|Default limit in the number of documents read from ElasticSearch|
|server.servlet.context-path|optional|/api|Set the CodeHub Web API context path|
|server.port|optional|3000|Sets the CodeHub Web API listening port|
|server.tomcat.max-threads|optional|5|Sets the maximum number of threads to be used by Tomcat|
|JAVA_OPTS|optional|Max memory available|Sets the JVM memory limits to be used by the application the recommendation is -Xmx512M -Xms512M|


## Installation
The API is a Java application and can be executed updating the values of the following command template.

```bash
sh -c java -Djava.security.egd=file:/dev/./urandom -jar /codehub-webapi-1.3.0.jar"
```
It is important to setup the environment variables before to execute the application.

## File Manifest
* src/main : Contains the source code
* src/test : Contains the unit testing code.
* Dockerfile: Docker image definition file


## Development setup
> The API was developed using [Spring Tool Suite 4](https://spring.io/tools/) that is base on [Eclipse](https://www.eclipse.org/ide/)

1. Install and open Spring Tool Suit
2. Configure the required environment variables
3. Debug/Run as Spring Boot application, after this step the application will be running and ready to receive request.

## Docker Support
A [Docker](https://www.docker.com/) image can be build with the next command line.
```bash
  docker build -t codehub-webapi:latest .
```

The following command with the correct values for the environment variable will start a Docker container.
```bash
docker run -p 3000:3000 --rm \
-e "server.port=3000" \
-e "codehub.webapi.es.host=[HOST]" \
-e "codehub.webapi.es.port=[PORT]" \
-e "codehub.webapi.es.scheme=[SCHEME]" \
-e "datahub.ui.url.endpoint=[DATAHUB-WEBHOST]" \
-e "JAVA_OPTS=-Xmx512M -Xms512M" \
-t -i codehub-webapi:latest
```


## Release History
* 1.0.0
  * Initial version
* 1.1.0
  * Add related DataHub entries.
* 1.3.0
  * Popular category fields.

## Contact information
Joe Doe : X@Y

Distributed under Apache License Version 2.0, January 2004. See *LICENSE* for more information

## Contributing
1. Fork it (https://github.com/usdot-jpo-codehub/codehub-webapi/fork)
2. Create your feature branch (git checkout -b feature/fooBar)
3. Commit your changes (git commit -am 'Add some fooBar')
4. Push to the branch (git push origin feature/fooBar)
5. Create a new Pull Request

## Known Bugs
*

## Credits and Acknowledgment
Thank you to the Department of Transportation for funding to develop this project.

## CODE.GOV Registration Info
* __Agency:__ DOT
* __Short Description:__ WebAPI to interface ITS CodeHub ElasticSearch.
* __Status:__ Beta
* __Tags:__ CodeHub, DOT, Spring Boot, Java, ElasticSearch
* __Labor Hours:__
* __Contact Name:__
* __Contact Phone:__
