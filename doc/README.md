# MapTool Collection

------

This is a collection of API to use this Map Tool.

API Response format:

```json
{
    "success": "<true/false>",
    "response_code":"<status_code>",
    "message":"<message>",
    "timestamp":"<timestamp",
    "payload": "<data>"
}
```
------

### Authenticate

To authenticate user. It will return `access_token` which will be needed as Bearer Token for other authenticated requests.

- Method: `POST`
- URL: `http://localhost:8080/app/authenticate`
- Content-type: `application/json`

#### Request Body

```json
{
    "username": "username",
    "password": "username"
}
```

#### Response Body

```json
{
    "success": true,
    "response_code": 200,
    "message": "User Authentication Successful!",
    "timestamp": "2023-05-19 01:26:12.735",
    "payload": {
        "access_token": <access_token>
    }
}
```
----

### Register

To register a user. The `role` field is compulsory when sending this request.

- Method: `POST`
- URL: `http://localhost:8080/app/register`
- Content-type: `application/json`

#### Request Body

```json
{
    "username": "username",
    "password": "username",
    "role": "USER"
}
```

#### Response Body

```json
{
    "success": true,
    "response_code": 200,
    "message": "Successfully created user",
    "timestamp": "2023-05-19 01:23:08.06",
    "payload": {
        "id": "6aad6166-8cc7-4397-8d3a-537642afc122",
        "username": "username1",
        "role": "USER"
    }
}
```

----
### Upload Postcode CSV

This endpoint is used to upload a list of postcode data. There is a limitation on the maximum file size for upload, which is 40MB. The approximate time taken to upload 499,999 data was ~15.5 minutes. The payload field in the response shows the number of data that has been uploaded. 

- Method: `POST`
- URL: `http://localhost:8080/app/postcode/uploadcsv`
- Authentication: Bearer Token

#### Authentication

Include the following authentication token in the request headers:

- Type: Bearer Token
- Token: `<token>`

#### Request Body

The request body should be in `formdata` format:

- `file1`: CSV file to upload (max file size: 40MB)

#### Response Body

```json
{
    "success": true,
    "response_code": 200,
    "message": "Successfully uploaded postcodes",
    "timestamp": "2023-05-19 01:31:53.844",
    "payload": 99
}
```


---

### Insert Postcode Data

This endpoint is used to add one postcode data.

- Method: `POST`
- URL: `http://localhost:8080/app/postcode/add`
- Authorization: Bearer Token

#### Authorization

Include the following Bearer Token in the request headers:

- Token: `<token>`

#### Request Body

The request body should be in raw JSON format:

```json
{
    "postcode": "ABC 12345",
    "latitude": 58.1374680,
    "longitude": -1.1124550
}
```

#### Response Body

```json
{
    "success": true,
    "response_code": 200,
    "message": "Successfully added postcode data",
    "timestamp": "2023-05-19 01:34:12.005",
    "payload": {
        "id": 151,
        "postcode": "ABC 12345",
        "latitude": 58.137468,
        "longitude": -1.112455
    }
}
```


---

### Update Postcode Data

This endpoint is used to update the latitude or longitude of a particular postcode. If the postcode contains space, it should be encoded with percentage-encoding.

- Method: `PUT`
- URL: `http://localhost:8080/app/postcode/update`
- Authorization: Bearer Token

#### Authorization

Include the following Bearer Token in the request headers:

- Token: `<token>`

#### Query Parameter
- postcode: `ABC%2012345`

#### Request Body

The request body should be in raw JSON format:

```json
{
    "latitude":57.777777,
    "longitude": -2.22222
}
```

#### Response Body

```json
{
    "success": true,
    "response_code": 200,
    "message": "Successfully updated postcode data",
    "timestamp": "2023-05-19 01:36:12.914",
    "payload": {
        "id": 151,
        "postcode": "ABC 12345",
        "latitude": 57.777777,
        "longitude": -2.22222
    }
}
```

---

### Get Postcode Data

This endpoint is used to retrieve data for a specific postcode. If the postcode contains space, it should be encoded with percentage-encoding.

- Method: `GET`
- URL: `http://localhost:8080/app/postcode/get/<postcode>`
- Authorization: Bearer Token

#### Authorization

Include the following Bearer Token in the request headers:

- Token: `<token>`

#### Response Body

```json
{
    "success": true,
    "response_code": 200,
    "message": "Successfully retrieved data",
    "timestamp": "2023-05-19 01:37:34.077",
    "payload": {
        "id": 151,
        "postcode": "ABC 12345",
        "latitude": 57.777777,
        "longitude": -2.22222
    }
}
```

---
### Distance Calculator

This endpoint calculates the distance between two postcodes.

- Method: `GET`
- URL: `http://localhost:8080/app/postcode/distance`
- Authorization: Bearer Token

#### Authorization

Include the following Bearer Token in the request headers:

- Token: `<token>`

#### Query Parameters

- postcode1: `<postcode1>`.
- postcode2: `<postcode2>`.

#### Response Body

```json
{
    "success": true,
    "response_code": 200,
    "message": "Calculation successful",
    "timestamp": "2023-05-19 01:39:07.297",
    "payload": {
        "unit": "km",
        "distance": 2.3328491546001495,
        "location1": {
            "id": 52,
            "postcode": "AB10 1XG",
            "latitude": 57.144156,
            "longitude": -2.114864
        },
        "location2": {
            "id": 54,
            "postcode": "AB10 7JB",
            "latitude": 57.124274,
            "longitude": -2.127206
        }
    }
}
```


