package org.example.ftp

interface FTP {
    fun start()
    fun stop()
    fun checkPermissions(): Boolean
    fun showPermission()
}