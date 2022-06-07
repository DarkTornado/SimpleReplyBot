package com.darktornado.simplereplier

import org.json.JSONObject

class ReplyData {
    val input: String
    val output: String
    val room: String
    val type: Int

    constructor(input: String, output: String, room: String, type: Int) {
        this.input = input
        this.output = output
        this.room = room
        this.type = type
    }

    constructor(json: JSONObject) {
        input = json["input"] as String
        output = json["output"] as String
        room = json["room"] as String
        type = json["type"] as Int
    }

    constructor() {
        input = ""
        output = ""
        room = ""
        type = 0
    }

    fun toJSON(): String {
        return "{" +
                "\"input\":\"" + input.replace("\"", "\\\"") + "\"," +
                "\"output\":\"" + output.replace("\"", "\\\"") + "\"," +
                "\"room\":\"" + room.replace("\"", "\\\"") + "\"," +
                "\"type\":" + type +
                "}"
    }
}