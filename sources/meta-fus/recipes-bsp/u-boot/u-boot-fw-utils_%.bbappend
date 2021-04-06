# create uboot tool fw_printenv
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

SRC_URI = "\
			file://u-boot-2018.03-fus.tar.bz2 \
"

S = "${WORKDIR}/u-boot-2018.03-fus"
