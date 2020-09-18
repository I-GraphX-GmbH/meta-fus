# Copyright 2020 F&S Elektronik Systeme GmbH

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI[md5sum] = "634ebdb05616275326d6dfcaa8687a7d"
SRC_URI += "file://0001-Disable-dvfs-for-DDR3L.patch"
SRC_URI[md5sum] = "fc5412a097bf597229beeac0295627ba"
SRC_URI += "file://0002_Disable-Debug-Console.patch"

