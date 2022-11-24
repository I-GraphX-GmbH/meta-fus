# Copyright 2020 F&S Elektronik Systeme GmbH

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI[md5sum] = "fc5412a097bf597229beeac0295627ba"
SRC_URI += "file://0001-Add-domain-permission-for-UART2-on-M4.patch"
SRC_URI += "file://0002_Disable-Debug-Console.patch"
SRC_URI += "file://0003-Add-DEBUG_CONSOLE-for-imx8mn.patch"
SRC_URI += "file://0004-Add-possibility-to-deactivate-debug-UART-for-fsimx8mp.patch"
SRC_URI += "file://0005-Add-domain-permission-for-UART2-on-M7.patch"

