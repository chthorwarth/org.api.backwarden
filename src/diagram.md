@startuml
hide empty description

title HATEOAS Navigation State Machine

[*] --> start: GET /api

start --> register : POST /users
start --> login : POST /tokens
login --> start
login --> showallvaults : GET users/{userid}/vaults
register --> start

showallvaults --> showuser: GET /users/{userid}
showuser --> showallvaults

showallvaults --> showallcredentials : GET /vaults/{vaultId}/credentials
showallcredentials --> showallvaults
showallvaults --> updateonevault : PUT users/{userid}/vaults/{vaultId}
updateonevault --> showallvaults
showallvaults --> createonevault : POST /users/{userid}/vaults
createonevault --> showallvaults
showallvaults --> deleteonevault : DELETE users/{userid}/vaults/{vaultId}
deleteonevault --> showallvaults

showallcredentials --> createonecredential : POST /vaults/{vaultId}/credentials
createonecredential --> showonecredential
showallcredentials --> showonecredential : GET /vaults/{vaultid}/credentials/{credentialId}
showonecredential --> showallcredentials
showonecredential --> updateonecredential : PUT /vaults/{vaultid}/credentials/{credentialId}
updateonecredential --> showonecredential
showonecredential --> deleteonecredential : DELETE /vaults/{vaultid}/credentials/{credentialId}
deleteonecredential --> showallcredentials

@enduml
