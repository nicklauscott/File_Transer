package org.example.apache_ftp

import org.apache.ftpserver.FtpServer
import org.apache.ftpserver.FtpServerFactory
import org.apache.ftpserver.ftplet.Authority
import org.apache.ftpserver.ftplet.FtpException
import org.apache.ftpserver.listener.ListenerFactory
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory
import org.apache.ftpserver.usermanager.impl.BaseUser
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission
import org.apache.ftpserver.usermanager.impl.TransferRatePermission
import org.apache.ftpserver.usermanager.impl.WritePermission
import org.example.ftp.FTP
import org.slf4j.LoggerFactory
import java.io.File

class ApacheFTP(
    private val port: Int, private val username: String, private val password: String, private val homeDirectory: String): FTP {

    private lateinit var server: FtpServer
    private val logger = LoggerFactory.getLogger(ApacheFTP::class.java)

    override fun start() {
        val serverFactory = FtpServerFactory()

        val listenerFactory = ListenerFactory()
        listenerFactory.apply {
            port = this@ApacheFTP.port
            idleTimeout = 300
            serverAddress = "0.0.0.0"
        }
        serverFactory.addListener("default", listenerFactory.createListener())
        logger.info("Listener configured on port $port")

        val userManagerFactory = PropertiesUserManagerFactory()
        val userManager = userManagerFactory.createUserManager()

        val homeDir = File(homeDirectory).absoluteFile
        homeDir.mkdirs()
        homeDir.setReadable(true, false)
        homeDir.setWritable(true, false)

        val authorities = mutableListOf<Authority>(
            WritePermission(),
            ConcurrentLoginPermission(20, 2),
            TransferRatePermission(0, 0)
        )

        val user = BaseUser()
            user.apply {
                name = username
                password = this@ApacheFTP.password
                homeDirectory = homeDir.absolutePath
                this.authorities = authorities
                maxIdleTime = 300
        }


        try {
            userManager.save(user)
            logger.info("user configured: $username with home: ${homeDir.absolutePath}")
            serverFactory.userManager = userManager

            server = serverFactory.createServer()
            server.start()

            logger.info("""
                FTP Server started successfully:
                - Port: $port
                - Host: 0.0.0.0 (all interfaces)
                - Username: $username
                - Home directory: ${homeDir.absolutePath}
                - Directory exists: ${homeDir.exists()}
                - Directory readable: ${homeDir.canRead()}
                - Directory writable: ${homeDir.canWrite()}
               
                You can now use an FTP client 
                [like FileZilla, WinSCP, or FTP command line]
                to access the server
            """.trimIndent())

        } catch (ex: FtpException) {
            println("Failed to start FTP server: ${ex.message}")
        }
    }

    override fun stop() {
        if (::server.isInitialized) {
            server.stop()
            println("FTP Server stopped")
        }
    }

    override fun checkPermissions(): Boolean {
        val homeDir = File(homeDirectory).absoluteFile
        return homeDir.isDirectory && homeDir.exists() && homeDir.canRead() && homeDir.canWrite()
                && homeDir.parentFile.canRead() && homeDir.parentFile.canWrite()
    }

    override fun showPermission() {
        val homeDir = File(homeDirectory).absoluteFile
        println("""
            Permission Check:
            Directory: ${homeDir.absolutePath}
            Exists: ${homeDir.exists()}
            Can Read: ${homeDir.canRead()}
            Can Write: ${homeDir.canWrite()}
            Is Directory: ${homeDir.isDirectory}
            
            Parent Directory:
            Path: ${homeDir.parent}
            Can Read: ${homeDir.parentFile.canRead()}
            Can Write: ${homeDir.parentFile.canWrite()}
        """.trimIndent())
    }

}