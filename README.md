
# Bug Management System

This project was made to showcase the use of a bug management system to aid with the QA  working cycle of documenting and managing of bugs.

## Installing

Clone github project from repo

```bash
  git clone https://github.com/JoaoSeg/bug-management-api.git
```
Import the requests collection included in the project files into Postman

Open project on IDE, install PostgreSQL set datasource credentials, user and pass in application.yml located at the resources dir, name database registration.

Run the application
    
## Testing requirements

Open Postman and register a user by hitting the Register User localhost url

```
POST http://localhost:8080/register
```

In the running client a mock email with a address is generated, ie:

```
http://localhost:8080/verifyRegistration?token=
```
Access it by looking at the console of the application, access it to verify the user token, after accessing the user is enabled.

Create users with different credentials such as different emails and firstnames and one of each the three roles  

Log in with the created TESTER employee by hitting the log in endpoint using the credentials

```
POST http://localhost:8080/login?username=?&password=
```

Now Issue a new bug from the Issue new Bug request

```
POST localhost:8080/api/bug/tester
```
Sending a body containing a JSON with the attributes title and description, the bug object is going to be returned in JSON format


To edit a bug hit the Edit bug request 
```
PUT localhost:8080/api/bug/tester?id=
```
Send a body with a json of the title and description to be edited


To delete a bug hit the Delete Bug request
```
DELETE localhost:8080/api/bug/tester?id=
```

Supplying the bug id, a simple bug deleted string is returned

Log in again with a Developer account and hit the List all bugs request
```
GET localhost:8080/api/bug/developer/listbugs
```
All the bugs are returned inside a array in JSON format

To select a bug to be worked by a developer suppling the bug id in the request param, hit the Select bug request
```
GET localhost:8080/api/bug/developer?id=
```
Now the bug JSON is returned and the developer is now working in the bug, status is set to analyzes

Editing a bug status is done by suppling the id of the bug and a valid status
```
PUT localhost:8080/api/bug/developer?id=&status=
```
The altering of the status is only possible if done by the employee that started to analyze the bug and the employee cannot change the status directly from reported to anything other than analyzes

Now log in with a Manager account and hit the Show manager dash request, suppling the status, begin and end of the desired time interval
```
GET localhost:8080/api/bug/manager?status=reported&begin=2023-07-13 11:59:02.739587&end=2023-07-15 11:59:02.739587
```
A list of bugs is returned in JSON format for the Manager to see.

## API Documentation

### Registration Controller

#### Employee registration

```http
  POST /register
```

| Parameter   | Type       | Description                           |
| :---------- | :--------- | :---------------------------------- |
| `firstname` | `string` | employee first name |
| `lastname`  | `string` | employee last name  |
| `password`  | `string` | account password    |
| `role`      | `string` | employee role       |
| `email`      | `string`| employee email     |

Receives employee json with the required fields for registration, valid roles are as follows: TESTER,DEVELOPER,MANAGER.

#### Returns success and triggers event listener for email verification token

### 

#### Verify Registration

```http
  GET /verifyRegistration
```

| Parameter   | Type       | Description                                   |
| :---------- | :--------- | :------------------------------------------ |
| `token`      | `string` | Generated token for verification |

Receives token generated from registration and generates a new for the now enabled employee account.

#### Returns User verifies successfully or in case of wrong token returns bad user

### Bug Controller

#### Issue Bug

```http
  POST /api/bug/tester
```

| Parameter   | Type       | Description                           |
| :---------- | :--------- | :---------------------------------- |
| `title` | `string` | bug title |
| `description`  | `string` | bug description  |

Receives bug json with the required fields for bug issuing, status is automaticaly set to reported from creation.

#### Returns the now issued bug details

### 

#### Edit Bug

```http
  PUT /api/bug/tester?id=
```

| Parameter   | Type       | Description                           |
| :---------- | :--------- | :---------------------------------- |
| `title` | `string` | bug title |
| `description`  | `string` | bug description  |
| `id`  | `long` | bug id  |

Receives bug json with the new title or description for bug issuing and parameter id. Status is not possible to be updated by tester so the bug fixing flow is respected.
If bug is already being analyzed by a developer, tester is not able to edit bug.

#### Returns Bug edited successfully

### 

#### Delete Bug

```http
  DELETE /api/bug/tester?id=
```

| Parameter   | Type       | Description                           |
| :---------- | :--------- | :---------------------------------- |
| `id` | `long` | bug id |

Deletes bug from the parameter id passed by the request

#### Returns Bug deleted

### 

#### List all bugs

```http
  GET /api/bug/developer/listbugs
```

List all bugs and its details

#### Returns list of issued bugs

### 

#### Select bug to be fixed

```http
  GET /api/bug/developer?id=
```

| Parameter   | Type       | Description                           |
| :---------- | :--------- | :---------------------------------- |
| `id` | `long` | bug id |

Lists selected bug and changes its status to analyzes as it got selected by developer to be worked on.

#### Returns selected bug detais

### 

#### Edit bug status

```http
  PUT /api/bug/developer?id=?status=
```

| Parameter   | Type       | Description                           |
| :---------- | :--------- | :---------------------------------- |
| `id` | `long` | bug id |
| `status` | `string` | bug status |

Edits bug status to what developer sent in request, if bug is already being worked by another developer returns bug is already being fixed by and the employee name
if bugs status is reported, not able to set to fixed or not reproduced as to not violate bug fixing flow returns bug status must follow flow

#### Returns Bug status resolved

### 

#### Edit bug status

```http
  GET /api/bug/manager?status=?begin=?end=
```

| Parameter   | Type       | Description                           |
| :---------- | :--------- | :---------------------------------- |
| `status` | `string` | bug status |
| `begin` | `timestamp` | set period range beggining |
| `end` | `timestamp` | set period range end |

Lists all bugs based on status and time interval sent in the request 

#### Returns List of bugs JSON
