package org.example.misc

const val MAC_COMMAND_FOR_LOCAL_IP = "sudo wdutil info"
const val WINDOWS_COMMAND_FOR_LOCAL_IP = "ipconfig /all"
const val LINUX_COMMAND_FOR_LOCAL_IP = "resolvectl status eth0"
val message = """
    
    To find the host IP (DNS Addresses):
    
    Windows:
        1. Through command line:
          Run this command '$WINDOWS_COMMAND_FOR_LOCAL_IP'
            DNS Servers: 192.168.00.00
    
    Mac:
      1. Through the terminal:
        Run this command '$MAC_COMMAND_FOR_LOCAL_IP'
          DNS Addresses:  192.168.00.00
    
      2. Go to System Information >>> Network >>> Wifi 
        DNS:
          Server Addresses: 192.168.00.00
         
    Linux:
        1. Through command line:
          Run this command '$LINUX_COMMAND_FOR_LOCAL_IP' [ Replace 'eth0' if you need to be specific ]
            DNS Servers: 192.168.00.00
            
    
""".trimIndent()