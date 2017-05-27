package nl.ictrek.ananas

/**
 * Created by Koen Bolhuis on 11-Apr-17.
 *
 * Chat class, contains all info for a chat view on the main activity
 *
 * @param title The chat title/name
 * @param summary A summary of the last message
 * @param time The time at which the last message was sent/received
 * @param type The chat type (personal or group)
 */
class Chat(val title: String, val summary: String, val time: String, val type: Chat.Type) {
    enum class Type {
        PERSONAL,
        GROUP
    }
}
