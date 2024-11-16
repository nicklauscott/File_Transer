package org.example.apache_ftp

import org.example.ftp.FTP
import org.example.misc.message
import org.example.misc.validate

class ApacheFTPBuilder {

    var port: Int = 1234
    var username: String = "user"
    var password: String = "password"
    var homeDirectory: String = "ftp-home"

    fun build(): FTP = ApacheFTP(port, username, password, homeDirectory)

}

fun apacheFtpBuilder(block: ApacheFTPBuilder.() -> Unit): FTP {
    return ApacheFTPBuilder().apply { block() }.build()
}

fun setUp(): FTP {
    println("\n $message \n")
    println("Fill the server info below or press enter to use the default values")
    val port = validate("Choose a port", { input -> input.isBlank() || input.toInt() > 999 }) { output ->
        try { output?.toInt() ?: 1234 } catch (_: Exception) { 1234 }
    }
    println("Selected Port: $port\n")
    val username = validate("Choose a username", { input -> input.isBlank() || input.length > 2 }) { output ->
        try { output ?: "user" } catch (_: Exception) { "user" }
    }
    println("Selected Username: $username\n")
    val password = validate("Choose a password", { input -> input.isBlank() || input.length > 2 }) { output ->
        try { output ?: "password" } catch (_: Exception) { "password" }
    }
    println("Selected Password: $password\n")
    val homeDirectory = validate("Choose a home directory", { input -> input.isBlank() || input.length > 2 }) { output ->
        try { output ?: "ftp-home" } catch (_: Exception) { "ftp-home" }
    }
    println("Selected Home Directory: $homeDirectory\n")

    return apacheFtpBuilder {
        this.port = port
        this.username = username
        this.password = password
        this.homeDirectory = homeDirectory
    }
}

