DESCRIPTION = "Add package silex-wlanbt-fs-firmware for F&S boards"
LICENSE = "CLOSED"

inherit module-base

SRC_URI = "file://silex-wlanbt-fs-firmware-${PV}.tar.bz2"

SILEX = "${WORKDIR}/silex-wlanbt-fs-firmware-4.0.11.213V-0"

# recipe fus-silex may installed after wpa-supplicant and hostapd are already
# installed. The reason is fus-silex overwrites some binarys of wpa-supplicant
# and hostapd so this recipes must be installed before fus-silex installed.
RDEPENDS_${PN} += "wpa-supplicant hostapd"

do_install() {

# install firmware
	install -d ${D}/lib/firmware/
    cp -r ${SILEX}/firmware/* ${D}/lib/firmware/
	ln -r -s ${D}/lib/firmware/qca/rampatch_tlv_tf_1.1.tlv ${D}/lib/firmware/qca/tfbtfw11.tlv
	ln -r -s ${D}/lib/firmware/qca/nvm_tlv_tf_1.1.bin ${D}/lib/firmware/qca/tfbtnv11.bin

}

FILES_${PN} += "lib/firmware/"
