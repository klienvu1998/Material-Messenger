package com.styl.materialmessenger.entities

class MessageEntity {
    var senderUserId: String = ""
    var receiverUserId: String = ""
    var message: String = ""

    constructor()

    constructor(senderUserId: String, receiverUserId: String, message: String) {
        this.senderUserId = senderUserId
        this.receiverUserId = receiverUserId
        this.message = message
    }
}