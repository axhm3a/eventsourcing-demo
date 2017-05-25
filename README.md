event-sourcing with scala and akka
==================================

`http -jv POST :8080/account accountOwner=="Jon Doe"`
`http -jv GET :8080/account`
`http -jv POST :8080/account/0/deposit amount==19.99`
`http -jv GET :8080/account/0`
`http -jv POST :8080/account/0/withdraw amount==18.99`
`http -jv GET :8080/account/0`
