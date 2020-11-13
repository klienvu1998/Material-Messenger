package com.styl.materialmessenger.entities

class PeopleEntity {

    var id: String? = ""
    var name: String? = ""
    var email: String? = ""
    var phoneNumber: String? = ""
    var image: String? = ""

    constructor()

    constructor(id: String, name: String, email: String, phoneNumber: String, image: String) {
        this.id = id
        this.name = name
        this.email = email
        this.phoneNumber = phoneNumber
        this.image = image
    }
}