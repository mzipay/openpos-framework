
# Services

## Example 1 Service

This is a description of service 1

| Name | Path | HTTP | Request Body | Response Body | Description |
|------|------|-------------|-------|--------------|---------------|
| *calculate* | `/example1/calculate` | POST | [CalculateRequest](#CalculateRequest) | [CalculateResponse](#CalculateResponse) | The quick brown fox jumps over the lazy dog |

### Request Example
```
{"menu": {
  "id": "file",
  "value": "File",
  "popup": {
    "menuitem": [
      {"value": "New", "onclick": "CreateNewDoc()"},
      {"value": "Open", "onclick": "OpenDoc()"},
      {"value": "Close", "onclick": "CloseDoc()"}
    ]
  }
}}
```

### Response Example
```
{"menu": {
  "id": "file",
  "value": "File",
  "popup": {
    "menuitem": [
      {"value": "New", "onclick": "CreateNewDoc()"},
      {"value": "Open", "onclick": "OpenDoc()"},
      {"value": "Close", "onclick": "CloseDoc()"}
    ]
  }
}}
```


| Name | Path | HTTP | Request Body | Response Body | Description |
|------|------|-------------|-------|--------------|---------------|
| *find* | /example1/find | GET | [CalculateRequest](#CalculateRequest) | [CalculateResponse](#CalculateResponse) | The quick brown fox jumps over the lazy dog| 

### Request Example
```
{"menu": {
  "id": "file",
  "value": "File",
  "popup": {
    "menuitem": [
      {"value": "New", "onclick": "CreateNewDoc()"},
      {"value": "Open", "onclick": "OpenDoc()"},
      {"value": "Close", "onclick": "CloseDoc()"}
    ]
  }
}}
```

### Response Example
```
{"menu": {
  "id": "file",
  "value": "File",
  "popup": {
    "menuitem": [
      {"value": "New", "onclick": "CreateNewDoc()"},
      {"value": "Open", "onclick": "OpenDoc()"},
      {"value": "Close", "onclick": "CloseDoc()"}
    ]
  }
}}
```
## Example 2 Service

This is a description of service 2

| Name | Path | HTTP | Request Body | Response Body | Description |
|------|------|-------------|-------|--------------|---------------|
| calculate | /example2/calculate | POST | [CalculateRequest](#CalculateRequest) | [CalculateResponse](#CalculateResponse) | The quick brown fox jumps over the lazy dog |

### Request Example
```
{"menu": {
  "id": "file",
  "value": "File",
  "popup": {
    "menuitem": [
      {"value": "New", "onclick": "CreateNewDoc()"},
      {"value": "Open", "onclick": "OpenDoc()"},
      {"value": "Close", "onclick": "CloseDoc()"}
    ]
  }
}}
```

### Response Example
```
{"menu": {
  "id": "file",
  "value": "File",
  "popup": {
    "menuitem": [
      {"value": "New", "onclick": "CreateNewDoc()"},
      {"value": "Open", "onclick": "OpenDoc()"},
      {"value": "Close", "onclick": "CloseDoc()"}
    ]
  }
}}
```

## Example 3 Service

This is a description of service 3

| Name | Path | HTTP | Request Body | Response Body | Description |
|------|------|-------------|-------|--------------|---------------|
| ping | /example3/ping | POST | NONE | NONE | The quick brown fox jumps over the lazy dog |

# Models

## CalculateRequest

This is a description of the calculate request model

| Name | Type | Description |
|------|------|-------------|
| deviceId | string | unique id for a device |
| geoCode | int | id for a location |

## CalculateResponse

This is a description of the calculate response model

| Name | Type | Description |
|------|------|-------------|
| itemTax | [ItemTax](#ItemTax) | unique id for a device |

## ItemTax

This is a description of item tax

| Name | Type | Description |
|------|------|-------------|
| id | int | an id |
| amount | decimal | tax amount |


