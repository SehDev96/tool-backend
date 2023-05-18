# MapTool Collection

------

This is a collection of API to use this Map Tool.

This API response is always in the following format:

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
----
### Upload Postcode CSV

This endpoint is used to upload a list of postcode data. There is a limitation on the maximum file size for upload, which is 40MB. The approximate time taken to upload 499,999 data was ~15.5 minutes.

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
    "postcode": "ABC 123",
    "latitude": 57.1374680,
    "longitude": -2.1124550
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
- postcode: `PO1%202HG`

#### Request Body

The request body should be in raw JSON format:

```json
{
    "latitude": 50.792362,
    "longitude": -1.101501
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

- postcode1: `<postcode1`.
- postcode2: `<postcode2>`.


