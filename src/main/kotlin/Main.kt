package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.example.apache_ftp.setUp

fun main() = runBlocking() {

    var server = setUp()
    if (server.checkPermissions()) {
        server.start()
        while (true) { delay(1000) }
    } else server.showPermission()

}

